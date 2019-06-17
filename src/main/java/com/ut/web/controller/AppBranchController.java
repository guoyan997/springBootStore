package com.ut.web.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

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
import com.ut.web.model.branch.BranchInfo;
import com.ut.web.model.branch.BranchNewst;
import com.ut.web.model.version.Version;
import com.ut.web.service.AppBranchService;
import com.ut.web.service.AppProjectService;
import com.ut.web.service.AppRelationService;
import com.ut.web.service.AppVersionService;
import com.ut.web.util.CommonUtil;

@Controller
@CrossOrigin(origins = "*", maxAge = 36000)
@RequestMapping("/appBranch")
public class AppBranchController {
	
	private Logger log = LogManager.getLogger(AppBranchController.class);
	@Autowired
	private AppBranchService appBranchService;
	@Autowired
	private AppVersionService appVersionService;
	
	@Autowired
	private AppProjectService appProjectService;
	@Autowired
	private AppRelationService appRelationService;
	
	@Autowired
	private HttpServletRequest request;
	
	
	@RequestMapping(value = "/createBranch", method = RequestMethod.POST)
	@ResponseBody
	public OptionMessage createBranch(@RequestBody Branch branch){
		log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
    	OptionMessage msg = new OptionMessage();
    	long createTime = System.currentTimeMillis();
    	String androidKey = UUID.randomUUID().toString();
    	String iosKey = UUID.randomUUID().toString();
    	Object[] args = new Object[]{branch.getBranchName(),branch.getProjectId(),androidKey,iosKey,"",branch.getDescription(), branch.getCreatorId(), createTime};
    	List<Branch> plist = appBranchService.findBranchsByName(branch.getBranchName(),branch.getProjectId()+"");
    	if(plist.size()>0){
    		//说明名称已经存在，需要修改名称
    		 msg.setType("existName");
			 msg.setMessage("该名称已经存在");
    	}else{
    		int result =  appBranchService.createBranch(args);
        	if(result>0){
    			 msg.setType("success");
    			 msg.setMessage("创建branch成功");
    		 }else{
    			 msg.setType("failed"); 
    			 msg.setMessage("创建branch失败");
    		 }
    	}
    
    	return msg;
	}
	@RequestMapping(value = "/findBranchsByProjectId", method = RequestMethod.GET)
	@ResponseBody
	public List<Branch> findAllBranchs(HttpServletRequest request, HttpServletResponse response){
		 log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		 String projectId = request.getParameter("projectId");
		 return appBranchService.findAllBranchs(projectId);
	}
	
	@RequestMapping(value = "/findNewstBranchsByProjectId", method = RequestMethod.GET)
	@ResponseBody
	public List<BranchNewst> findNewstBranchs(HttpServletRequest request, HttpServletResponse response){
		 log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		 String projectId = request.getParameter("projectId");
		 return appBranchService.findNewstBranchs(projectId);
	}
	
	@RequestMapping(value = "/deleteBranch", method = RequestMethod.GET)
	@ResponseBody
	public OptionMessage deleteBranch(HttpServletRequest request, HttpServletResponse response){
		log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		OptionMessage msg = new OptionMessage();
		String creatorId = request.getParameter("creatorId");
		String projectId = request.getParameter("projectId");
		String branchId = request.getParameter("branchId");
		
		 // 删除分支时，要删除掉该分支下的关联关系
		Object[] args3 = new Object[]{branchId, branchId};
		appRelationService.deleteRelationByBranchId(args3);
		// 每个分支的版本，以及保存的静态文件
		Object[] args = new Object[]{branchId};
		int result = appBranchService.deleteBranch(args);
		// 删除数据库中这个分支下的所有版本
		appVersionService.deleteAllVersions(branchId);
		 //同时要删除文件夹下的文件, 找到文件夹路径
	    String filePath = request.getServletContext().getRealPath("/output/upload")+"/"+creatorId+"/"+projectId+"/"+branchId;
//	    //  删除分支后，判断该工程下是否还有版本，没有版本的话，需要清空工程表中的包名字段
//	    List<Version> list2 = appVersionService.findReleaseListByProjectId(projectId);
//	    if(list2.size() ==0){
//	    	//说明删除的是该分支的最后一个版本
//	    	String sql = "UPDATE PROJECT SET PACKAGENAME = ? WHERE PROJECTID = ?";
//	    	Object[] args2 = new Object[]{"", projectId};
//	    	appProjectService.updateProjectPackageName(sql, args2);
//	    }
		if(result>0){
			//删除保存在服务器端的文件
			 CommonUtil.deleteAll(new File(filePath));
			 msg.setType("success");
			 msg.setMessage("删除branch成功！");
		}else {
			 msg.setType("failed"); 
			 msg.setMessage("删除branch失败");
		}

		return msg;
	}
	@RequestMapping(value = "/updateBranch", method = RequestMethod.POST)
	@ResponseBody	
	public OptionMessage updateBranch(@RequestBody Branch branch){
		OptionMessage msg = new OptionMessage();
		long branchId = branch.getBranchId();
	    String branchName = branch.getBranchName();
	    String description = branch.getDescription();
	    String projectId= branch.getProjectId()+"";
	    String sql= "";
	    Object[] args = null;
    	// 说明没有提交包名的数据，这里只是修改工程信息
    	List<Branch> plist = appBranchService.findBranchsByName(branchName,projectId);
    	if(plist.size()>0){
    		long findid = plist.get(0).getBranchId();
    		if(plist.size()==1&&findid == branchId){
    			// 说明修改的是自身，可以修改
    		}else {
    			//说明是重复的名称
	    		 msg.setType("existName");
				 msg.setMessage("该名称已经存在");
				 return msg;
    		}
    	}
    	sql = "UPDATE BRANCH SET BRANCHNAME = ?, DESCRIPTION = ? WHERE BRANCHID = ?  ";
    	args = new Object[]{branchName,description, branchId};
    	int result = appBranchService.updateBranch(sql, args);
    	if(result>0){
			 msg.setType("success");
			 msg.setMessage("修改Project成功");
		 }else{
			 msg.setType("failed"); 
			 msg.setMessage("修改Project失败");
		 }
		return msg;
	}
	
	@RequestMapping(value = "/findBranchById", method = RequestMethod.GET)
	@ResponseBody
	public Branch findBranchById(HttpServletRequest request, HttpServletResponse response){
	    log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
	    String branchId = request.getParameter("branchId");
	    Branch branch  = appBranchService.findBranchById(branchId);
	    return branch;
	    
	}
	
	// 打开某个分支的最新版本下载页
	@RequestMapping(value="/downloadPage/{versionKey}",method = RequestMethod.GET)
	public String  showNewstDownLoadPage(@PathVariable String versionKey,ModelMap map){
		Branch branch = appBranchService.findOneBranchByVersionKey(versionKey);
		if(null != branch){
			 request.setAttribute("branchId",branch.getBranchId());
		}else {
			 request.setAttribute("branchId","");
		}
	        return "newstDownloadPage";
	}
	
	// 获取某个分支的最新版本下载页详细数据
	@RequestMapping(value = "/findNewestBranchInfo", method = RequestMethod.GET)
	@ResponseBody
	public BranchInfo findNewestBranchInfo(HttpServletRequest request, HttpServletResponse response){
	    log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
	    String branchId = request.getParameter("branchId");
	    BranchInfo branch  = appBranchService.findProjectByBranchId(branchId);
	    if (null != branch) {
	    	 // 先获取android的最新版本
		    Version androidVersion = appVersionService.findNewstVersion(branchId, "Android");
		 // 在获取ios的最新版本
		    Version iosVersion = appVersionService.findNewstVersion(branchId, "iOS");
	    	branch.setAndroidVersion(androidVersion);
	 	    branch.setIosVersion(iosVersion);
	    }
	    return branch;
	    
	}
	
	
	// 用户旧版本下载页连接，转化为新版本下载页
		@RequestMapping(value = "/getNewestDownloadPage", method = RequestMethod.GET)
		@ResponseBody
		public BranchInfo getNewestDownloadPage(HttpServletRequest request, HttpServletResponse response){
		    String versionKey = request.getParameter("versionKey");
		    BranchInfo branchInfo = null;
		    Branch branch = appBranchService.findOneBranchByVersionKey(versionKey);
			 if(null != branch ) {
				 // 先通过branchId获取工程名称和分支名称
				 branchInfo  = appBranchService.findProjectByBranchId(branch.getBranchId()+"");
				 if(null != branchInfo){
					 // 然后更具传上来的Key判断展示android版本还是ios版本
					 if(versionKey.equals(branch.getAndroidKey())) {
						 Version androidVersion = appVersionService.findNewstVersion(branch.getBranchId()+"", "Android");
						 branchInfo.setAndroidVersion(androidVersion);
					 }else if (versionKey.equals(branch.getIosKey())){
						 Version iosVersion = appVersionService.findNewstVersion(branch.getBranchId()+"", "iOS"); 
						 branchInfo.setIosVersion(iosVersion);
					 }
				 }
			 }
			 return branchInfo;  
		}
	

}
