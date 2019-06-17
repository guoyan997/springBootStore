package com.ut.web.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ut.web.model.OptionMessage;
import com.ut.web.model.branch.Branch;
import com.ut.web.model.project.Project;
import com.ut.web.model.relation.Relation;
import com.ut.web.model.version.Version;
import com.ut.web.service.AppBranchService;
import com.ut.web.service.AppProjectService;
import com.ut.web.service.AppRelationService;
import com.ut.web.service.AppVersionService;
import com.ut.web.util.CommonUtil;
import com.ut.web.util.MatrixToImageWriter;
import com.ut.web.util.ReadUtil;
import com.ut.web.util.UpdateFileUtil;
import com.ut.web.util.iospng.ConvertHandler;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/appVersion")
public class AppVersionController {
	
	private Logger logger = LogManager.getLogger(AppVersionController.class);
	
	@Autowired
	private AppVersionService appVersionService;
	@Autowired
	private AppProjectService appProjectService;
	@Autowired
	private AppBranchService appBranchService;
	@Autowired
	private AppRelationService appRelationService;
	
	
	
	@RequestMapping(value = "/uploadVersion", method = RequestMethod.POST)
	@ResponseBody
	public OptionMessage uploadAppVersion(HttpServletRequest request,HttpServletResponse response) {
		logger.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		 OptionMessage om = new OptionMessage();
		 //一个App对应一个appId
		 String projectId = request.getParameter("projectId");
		 String branchId = request.getParameter("branchId");
		 String creatorId = request.getParameter("creatorId");
		 String androidKey = request.getParameter("androidKey");
		 String iosKey = request.getParameter("iosKey");
		 String pathStart = androidKey == null?iosKey:androidKey;
		 //String description = request.getParameter("description");
		 String platform = "";
		 //设置文件临时存放路径
		 String tempPath = request.getServletContext().getRealPath("/output/uploadTemp");
		
		 String tempFilePath = tempPath + "/"+pathStart;
		 // 创建AppInfoModel，用于保存从上传的文件中读取的app信息
		 Version appInfo = new Version();
		 HashMap<String, String> uploadData = UpdateFileUtil.saveUploadFile(request,response,tempFilePath);
		 if(uploadData.get("uploadOk").equals("0")) {
			  //说明上传失败，让用户重新上传
				  om.setType("failed");
				  om.setMessage("上传文件失败，请重新上传！");
				  //需要删除创建的上传路径
				  CommonUtil.deleteAll(new File(tempFilePath));
				  return om;
		  }else {
			  //文件上传成功后，需要读取文件的信息
			  String  fileName = uploadData.get("fileName");  //获得上传的apk的文件名
			  String  extName =fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase(); //拿到文件的后缀名
			  String fileSize = uploadData.get("fileSize");
			  long releaseTime = System.currentTimeMillis();    //用于记录上传的时间，是个几乎唯一的数字
			  //保存app文件中读取的信息
			  Map<String, Object> apkInfoMap = new HashMap<String, Object>();
			// 如果是android平台，多判断一步，看看上传的文件是否是apk包
			  if(extName.equals("apk")){
				  platform = "Android";
				 String apkUrl = tempFilePath + "/"+fileName;
				 apkInfoMap = ReadUtil.readAPK(apkUrl,tempFilePath);
			  }else if(extName.equals("ipa")) {
				  platform = "iOS";
				// 如果是ios平台，多判断一步，看看上传的文件是否是ipa包
			     String ipaUrl = tempFilePath + "/"+fileName;
				 apkInfoMap = ReadUtil.readIPA(ipaUrl,tempFilePath);
			  } 
			  
			  //这里要对比versionCode，如果上传文件的versionCode没有比数据库中最大的版本大，那么就不上传，提示要提高版本号
			   //首先根据appId去获取apprelease表中最大的版本号
			  //先去查找该工程的包名，如果工程包名不存在，说明是第一次上传，如果工程中的包名存在，那么就要先判断包名相同，再判断版本号
			//  Project project = appProjectService.findProjectById(projectId);
			  Branch branch = appBranchService.findBranchById(branchId);
			  if(branch.getPackageName() !=null && !branch.getPackageName().trim().equals("")) {
				  if(!branch.getPackageName().toLowerCase().equals(apkInfoMap.get("packageName").toString().toLowerCase())){
					//包名不同，不是同一个App,不允许上传
					  om.setType("failed");
					  om.setMessage("app包名不同，不允许上传！");
					  //需要删除创建的上传路径
					  CommonUtil.deleteAll(new File(tempFilePath));
					  return om;
				  }else {
					  //如果包名相同，就可以进一步判断版本号
					  Version maxVersionObj =  appVersionService.findNewstVersion(branchId, platform);
					  if (maxVersionObj!= null) {
						  // 说明有最新的版本，要进行对比
						  //说明不是第一次上传，需要判断版本号
						  if(Long.parseLong(maxVersionObj.getVersionCode())>= Long.parseLong(apkInfoMap.get("versionCode").toString())){
							  //说明版本号没有提升不能上传
							  om.setType("failed");
							  om.setMessage("请选择高版本号安装包进行上传！");
							  //需要删除创建的上传路径
							  CommonUtil.deleteAll(new File(tempFilePath));
							  return om;
						  }
					  }
				  }
			  }else {
				  // 说明他是这个android 或者ios是第一次上传，不用进行判定，
				 //  是第一次上传，修改自身的包名，
				  String updateBranchSql = " UPDATE BRANCH SET PACKAGENAME = ? WHERE BRANCHID = ?";
				  Object[] args = new Object[]{apkInfoMap.get("packageName").toString(), branchId};
				  appBranchService.updateBranchPackageName(updateBranchSql, args);
				  // 如果是default分支，而且还要需要去修改工程的包名
				  if (branch.getBranchName().equals("Default")) {
					  // 修改project表，把包名保存进去
					  String updateProjectSql = " UPDATE PROJECT SET PACKAGENAME = ? WHERE PROJECTID = ?";
					  Object[] argsProject = new Object[]{apkInfoMap.get("packageName").toString(), projectId};
					  appProjectService.updateProjectPackageName(updateProjectSql, argsProject);
				  }
			  }
			  
			  appInfo.setBranchId(Long.parseLong(branchId));
			  appInfo.setVersionCode(apkInfoMap.get("versionCode").toString());
			  appInfo.setVersionName(apkInfoMap.get("versionName").toString());
			  appInfo.setPackageName(apkInfoMap.get("packageName").toString());
			  appInfo.setCreateTime(releaseTime+ "");
			  appInfo.setFileSize(fileSize);
			  
			  //上传完成后需要把文件转存到最终的文件夹
			  String lastPath = request.getServletContext().getRealPath("/output/upload");
			  //按照不同的版本号进行分包
			  String lastFilePath = lastPath+ "/"+creatorId + "/"+projectId + "/"+branchId+ "/"+pathStart + "/"+apkInfoMap.get("versionCode").toString();
			  File tempFile = new File(tempFilePath);
			  File uploadFile = new File(lastFilePath);
		      //判断上传文件的保存目录是否存在
		      if (!uploadFile.exists() && !uploadFile.isDirectory()) {
		            System.out.println(lastFilePath+"目录不存在，需要创建");
		            //创建目录
		            uploadFile.mkdirs();
		        }
		      
		     //需要 把临时文件夹下的内容拷贝到最终文件夹下
		      try {
				CommonUtil.copyDir(tempFile, uploadFile);
				} catch (Exception e) {
					logger.error(e.getMessage());
					//一旦出异常，就要提示上传不成功，重新上传
					  om.setType("failed");
					  om.setMessage("上传文件失败，请重新上传！");
					  //需要删除创建的上传路径
					  CommonUtil.deleteAll(new File(tempFilePath));
					  CommonUtil.deleteAll(new File(lastFilePath));
					  return om;
				}finally{
					 // 一切操作结束后，删去临时文件夹的内容，节省存储空间
					CommonUtil.deleteAll(new File(tempFilePath)); 
				}
		      
		      //获取工程路径： http://127.0.0.1:8080/xxx/
		      /**这里是自动获取服务器路径地址， http协议下   **/
//		      String httpsUrl = CommonUtil.getProjectPath(request);
//			  String projectPath = httpsUrl  + "/upload"+ "/"+appId + "/"+apkInfoMap.get("versionCode").toString();
		      /**这里是从配置文件中获取服务器路径地址， https协议下   **/
		      String httpsUrl = CommonUtil.getHttpsUrlAddress();
		     // String projectPath = httpsUrl + "/upload"+ "/"+appId + "/"+apkInfoMap.get("versionCode").toString();
		      
		      //文件保存的路径，修改为存放在服务器的位置路径，最后从数据库中读取的时候，转化为文件在服务器上的请求路径，方便更换 
		      String projectPath = lastFilePath;  
			  //文件存放的路径（最终路径）
			  // appInfo.setFilePath(lastFilePath + "/"+ fileName);
		      appInfo.setFilePath(projectPath + "/"+ fileName);
			  //获取最终的图标的路径
		      String iconName = apkInfoMap.get("iconName").toString();
			  
			  //如果是ios则生成plist文件，放到最终目录下
			  if(extName.equals("ipa")) {
				  
				  //如果是ios，需要对图标进行特殊处理
			      File srcFile = new File(lastFilePath + "/"+ iconName);
			      File destFile = new File(lastFilePath + "/icon.png");
				  File upFile = new File(lastFilePath + "/icon_up.png");
				  File downFile = new File(lastFilePath + "/icon_down.png");
			      //转码图标  
			      ConvertHandler handler = new ConvertHandler();
				  handler.convertPNGFile(srcFile, destFile, upFile, downFile);
				  upFile.delete();
				  downFile.delete();
				  appInfo.setIconPath(projectPath + "/icon.png");
				  
				  //et.plist文件保存路径
				  //首先把et.plist模板文件拷贝到最终路径文件夹下
				  String plistTempPath = request.getServletContext().getRealPath("/templet");
				  String plistName = "et.plist";
				  String plistLastPath = lastFilePath + "/"+plistName;
				  
				  String downloadUrlStr = appInfo.getFilePath();
				  String downloadUrl = "";
				  try {
					 downloadUrl = URLEncoder.encode(downloadUrlStr.substring(downloadUrlStr.lastIndexOf('/')+1),"UTF-8");
					 downloadUrl = downloadUrl.replaceAll("\\+","%20");  
					} catch (UnsupportedEncodingException e1) {
						logger.error(e1.getMessage()+"plist地址转码失败");
					}
				  
				  // 拿到文件名称，要把里面的特殊字符转码后使用
				  String downloadUrlEncode = downloadUrlStr.substring(0, downloadUrlStr.lastIndexOf('/')+1) + downloadUrl;
				  
				  //拼接成文件下载路径
				  Map<String, String> root = new HashMap<String, String>();  
		             root.put("downloadUrl", downloadUrlEncode);  
		             root.put("iconPathSmall", appInfo.getIconPath());  
		             root.put("iconPathBig", appInfo.getIconPath());  
		             root.put("packageName", appInfo.getPackageName()); 
		             root.put("versionCode", appInfo.getVersionCode());  
		             root.put("appName", apkInfoMap.get("appDisplayName").toString()); 
				  // 然后使用freemark，插入内容到plist文件
	             try {
					CommonUtil.putDataIntoTemplet(plistTempPath,plistName,root,plistLastPath);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	             //设置plist文件路径
	             appInfo.setPlistFilePath(projectPath+"/et.plist");
			  }else{
				  //如果是android图标，不用转换
				  appInfo.setIconPath(projectPath + "/"+ iconName);
			  }
			  
			  //要更新branch表中的iconpath字段
			  String updateBranchSql = " UPDATE BRANCH SET ICONPATH = ? WHERE BRANCHID = ?";
			  Object[] args = new Object[]{appInfo.getIconPath(), branchId};
			  appBranchService.updateBranchIconPath(updateBranchSql, args);
			  
			  String contentStr = "";
			  if( androidKey == null) {
				 // contentStr = httpsUrl+"/downloadPage/erwmPage.html?iosKey="+iosKey+"&&versionCode="+appInfo.getVersionCode();
				  contentStr = httpsUrl+"/appVersion/downloadPage/"+iosKey+"/"+appInfo.getVersionCode()+ "/iOS"; 
			  }else if(iosKey == null)  {
				 // contentStr = httpsUrl+"/downloadPage/erwmPage.html?androidKey="+androidKey+"&&versionCode="+appInfo.getVersionCode();
				  contentStr = httpsUrl+"/appVersion/downloadPage/"+androidKey+"/"+appInfo.getVersionCode()+ "/Android";
			  }
			  File ewmFile = new File(lastFilePath+"/ewm.png");
			  // 生成二维码图片，放到最终文件夹中
			  MatrixToImageWriter.makeQrPng(contentStr, 180, 180, ewmFile);
			  appInfo.setEwmImgPath(projectPath+"/ewm.png");
			  
			 //向版本表插入一条数据
			 Object[] args_version = new Object[]{branchId,appInfo.getVersionCode(),appInfo.getVersionName(),platform,0,appInfo.getPackageName(),
					 fileSize,appInfo.getFilePath(),appInfo.getPlistFilePath(),appInfo.getEwmImgPath(),appInfo.getIconPath(),"",creatorId,releaseTime};
			 int result_version = appVersionService.createVersion(args_version);
			 om.setType("success");
			 om.setMessage("创建成功！");
			 return om;
		  }
	}
	
	@RequestMapping(value = "/findVersionListByBranchId", method = RequestMethod.GET)
	@ResponseBody
	public List<Version> findVersionListByBranchId(String branchId){
		logger.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
    	return appVersionService.findReleaseListByBranchId(branchId);
    }
	
	@RequestMapping(value = "/updateVersion", method = RequestMethod.POST)
	@ResponseBody	
	public OptionMessage updateVersion(@RequestBody Version version){
		logger.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		OptionMessage msg = new OptionMessage();
		String sql= "UPDATE VERSION SET DESCRIPTION =? WHERE VERSIONID=?"; 
		Object[] args = new Object[]{version.getVersionDescription(),version.getVersionId()};
		int result = appVersionService.updateVersion(sql, args);
		if(result>0){
			 msg.setType("success");
			 msg.setMessage("修改version成功");
		 }else{
			 msg.setType("failed"); 
			 msg.setMessage("修改version失败");
		 }
		return msg;
	}
	
	@RequestMapping(value = "/deleteVersion", method = RequestMethod.GET)
	@ResponseBody
	public OptionMessage deleteVersion(HttpServletRequest request, HttpServletResponse response){
		logger.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		OptionMessage msg = new OptionMessage();
		String creatorId = request.getParameter("creatorId");
		String projectId = request.getParameter("projectId");
		String branchId = request.getParameter("branchId");
		String versionKey = request.getParameter("versionKey");
		String versionCode = request.getParameter("versionCode");
		String versionId = request.getParameter("versionId");
		int result = appVersionService.deleteVersion(versionId);
		 //同时要删除文件夹下的文件, 找到文件夹路径
	    String filePath = request.getServletContext().getRealPath("/output/upload")+"/"+creatorId+"/"+projectId+"/"+branchId+"/"+versionKey+"/" + versionCode;
	    //同时判断删除的是否为该分支下的唯一一个版本，是的话，需要修改该分支的iconPath字段为空
	    List<Version> list =   findVersionListByBranchId(branchId);
	    if(list.size() ==0){
	    	//说明删除的是该分支的最后一个版本
	    	String sql = "UPDATE BRANCH SET ICONPATH = ?, PACKAGENAME =? WHERE BRANCHID = ?";
	    	Object[] args = new Object[]{"","", branchId};
			appBranchService.updateBranchPackageNameAndIcon(sql, args);
			Branch branch = appBranchService.findOneBranchByVersionKey(versionKey);
			if(null != branch && branch.getBranchName().equals("Default")){
				// 如果是Default分支没有版本了，那么还要修改工程的的包名
				String updateProjectsql = "UPDATE PROJECT SET PACKAGENAME = ? WHERE PROJECTID = ?";
		    	Object[] argsProject = new Object[]{"", projectId};
		    	appProjectService.updateProjectPackageName(updateProjectsql, argsProject);
			}
	    }else {
	    	//说明删除后还有版本，将最新的版本图标给分支
	    	String sql = "UPDATE BRANCH SET ICONPATH = ? WHERE BRANCHID = ?";
	    	Object[] args = new Object[]{list.get(0).getIconPath(), branchId};
			appBranchService.updateBranchIconPath(sql, args);
	    }
	    
		if(result>0){
			//删除保存在服务器端的文件
			 CommonUtil.deleteAll(new File(filePath));
			 msg.setType("success");
			 msg.setMessage("删除成功！");
		}else {
			 msg.setType("failed"); 
			 msg.setMessage("删除失败");
		}
		return msg;
	}
	
	
	@RequestMapping(value = "/findNewstVersion", method = RequestMethod.GET)
	@ResponseBody
	public Version findNewstVersion(HttpServletRequest request, HttpServletResponse response){
		logger.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		Version version = null;
		String branchId = request.getParameter("branchId");
		String platform = request.getParameter("platform");
		version = appVersionService.findNewstVersion(branchId, platform);
		return version;
	}
	
	@RequestMapping(value = "/findOneVersion", method = RequestMethod.GET)
	@ResponseBody
	public Version findOneVersion(HttpServletRequest request, HttpServletResponse response){
		logger.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		Version version = null;
		String versionId = request.getParameter("versionId");
//		String versionCode = request.getParameter("versionCode");
//		String platform = request.getParameter("platform");
		version = appVersionService.findOneRelease(versionId);
		return version;
	}
	
	@RequestMapping(value = "/updateDownLoadNum", method = RequestMethod.GET)
	@ResponseBody	
	public OptionMessage updateDownLoadNum(HttpServletRequest request, HttpServletResponse response){
		logger.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		OptionMessage msg = new OptionMessage();
		String versionId = request.getParameter("versionId");
		
		int result = appVersionService.updateDownLoadNum(versionId);
		if(result>0){
			 msg.setType("success");
			 msg.setMessage("统计下载成功");
		 }else{
			 msg.setType("failed"); 
			 msg.setMessage("统计下载失败");
		 }
		return msg;
	}
	
	

	// 通过扫描二维码，打开这个链接，跳转到对用的下载页
	@RequestMapping(value="/downloadPage/{versionKey}/{versionCode}/{platform}")
	public String  showNewstDownLoadPage(@PathVariable String versionKey,@PathVariable String versionCode,@PathVariable String platform,ModelMap map){
		logger.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		    map.addAttribute("versionKey",versionKey);
		    map.addAttribute("versionCode",versionCode);
		    map.addAttribute("platform",platform);
		    //跳转到downloadPage.jsp这个页面下
	        return "downloadPage";
	}
	
	@RequestMapping(value = "/getDownloadVersion", method = RequestMethod.GET)
	@ResponseBody
	public Version  getDownLoadVersion(HttpServletRequest request, HttpServletResponse response){
		logger.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		Version version = new Version();   
		String versionKey = request.getParameter("versionKey");
		String versionCode = request.getParameter("versionCode");
		String platform = request.getParameter("platform");
		String phonePlat = request.getParameter("phonePlat");
		if(phonePlat.equals("weiXin")) {
			//就直接查询该版本的信息，只能用于展示，因为不知道手机是Android还是iOS，所以无法下载
			version = appVersionService.findOneVersionByKey(versionKey, versionCode, platform);
			version.setCanInstall("yes");
		}else {
			if(platform.equals(phonePlat)) {
				// 如果手机平台和下载的版本系统一致，就直接返回该版本的信息
				version = appVersionService.findOneVersionByKey(versionKey, versionCode, platform);
				version.setCanInstall("yes");
			}else {
				// 如果手机平台和下载的版本系统不一致，就查询该版本的关联App，如果有则返回，如果没有返回空对象
				//先去查询其关联的版本
				Relation relation = appRelationService.findRelationByKey(versionKey, platform);
				if(relation!=null) {
					// 说明存在关联版本，那么就要获取关联版本的信息，返回到界面
					String relationPlatform = platform.equals("Android")? "iOS" : "Android";
					String relationKey = platform.equals("Android")? relation.getIosKey(): relation.getAndroidKey();
					version = appVersionService.findOneNewstVersionByKey(relationKey, relationPlatform);
					if(version == null){
						//如果关联的渠道下没有版本
						version = appVersionService.findOneVersionByKey(versionKey, versionCode, platform);
						version.setCanInstall("no");
					} else {
						version.setCanInstall("yes");
					}
				}else {
					// 说明找不到关联版本，手机的系统和要下载的版本系统不一致，无法安装
					// 返回一个版本，用于展示，但是标记提示不支持安装
					version = appVersionService.findOneVersionByKey(versionKey, versionCode, platform);
					version.setCanInstall("no");
				}
			}
		}
	        return version;
	}
	
	
	

}
