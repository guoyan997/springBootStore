package com.ut.web.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ut.web.model.AppCheckModel;
import com.ut.web.model.branch.Branch;
import com.ut.web.service.AppBranchService;
import com.ut.web.service.AppUpdateService;
import com.ut.web.util.CommonUtil;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/appUpdate")
public class AppUpdateController {
	
	private Logger log = LogManager.getLogger(AppUpdateController.class);

	@Autowired
	private AppUpdateService appUpdateService;
	
	@Autowired
	private AppBranchService appBranchService;
	
	/**
	 @CrossOrigin(origins = "*", maxAge = 3600)
	 @RequestMapping(value = "/checkUpdateApp", method = RequestMethod.GET)
	 @ResponseBody
	 public AppCheckModel checkUpdateApp(HttpServletRequest request, HttpServletResponse response){   
		 log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		 String appId = request.getParameter("appId");
		 //System.out.println("******************appId: " + appId);
		 log.info("要检测更新的appId = " + appId);
		 AppCheckModel om = null;
		 ArrayList<AppCheckModel> list = (ArrayList<AppCheckModel>) appUpdateService.checkUpVersionCode(appId);
		 if(list.size()>0){
			 om = new AppCheckModel();
			 AppCheckModel maxObj = list.get(0);
			 om.setVersionName(maxObj.getVersionName());
			 om.setVersionCode(maxObj.getVersionCode());
			 
			 if(maxObj.getPlatform().equals("iOS")){
				 // 那么把下载页的url地址发送到ios端，用于webView使用
				  String httpsUrl = CommonUtil.getHttpsUrlAddress() + "/downloadPage/downloadPage.html?appId="+appId+"&&versionCode="+om.getVersionCode();
				  maxObj.setDownUrl(httpsUrl);
			 }else{
				 maxObj.setDownUrl(CommonUtil.getHttpsResourceUrl(maxObj.getDownUrl()));
			 }
			 om.setDownUrl(maxObj.getDownUrl());
			
		 } 
//		om.setVersionCode("2");
//		om.setVersionName("1.0.1");
//		om.setDownUrl("https://raw.githubusercontent.com/feicien/android-auto-update/develop/extras/android-auto-update-v1.2.apk");
		response.setHeader("Access-Control-Allow-Origin", "*");
		 return om;
	 }
	 **/
/**	
	 @CrossOrigin(origins = "*", maxAge = 3600)
	 @RequestMapping(value = "/checkUpdateApp", method = RequestMethod.GET)
	 @ResponseBody
	 public AppCheckModel checkUpdateApp(HttpServletRequest request, HttpServletResponse response){   
		 log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		 String versionKey = request.getParameter("versionKey");
		 String platform = request.getParameter("platform");
		 //System.out.println("******************appId: " + appId);
		 log.info("要检测更新的versionKey = " + versionKey);
		 AppCheckModel om = null;
		 ArrayList<AppCheckModel> list = (ArrayList<AppCheckModel>) appUpdateService.checkUpVersionCode(versionKey, platform);
		 if(list.size()>0){
			 om = new AppCheckModel();
			 AppCheckModel maxObj = list.get(0);
			 if (maxObj == null || maxObj.getVersionCode() == null) {
				 return null;
			 }
			 om.setVersionName(maxObj.getVersionName());
			 om.setVersionCode(maxObj.getVersionCode());
			 
			 if(maxObj.getPlatform().equals("iOS")){
				 // 那么把下载页的url地址发送到ios端，用于webView使用
				  String httpsUrl = CommonUtil.getHttpsUrlAddress() +"/appVersion/downloadPage/"+versionKey+"/"+om.getVersionCode()+"/iOS";
				//  String httpsUrl = CommonUtil.getHttpsUrlAddress() + "/downloadPage/downloadPage.html?appId="+appId+"&&versionCode="+om.getVersionCode();
				  maxObj.setDownUrl(httpsUrl);
			 }else{
				 maxObj.setDownUrl(CommonUtil.getHttpsResourceUrl(maxObj.getDownUrl()));
			 }
			 om.setDownUrl(maxObj.getDownUrl());
			
		 } 
		response.setHeader("Access-Control-Allow-Origin", "*");
		 return om;
	 }
**/
	
	 @CrossOrigin(origins = "*", maxAge = 3600)
	 @RequestMapping(value = "/checkUpdateApp", method = RequestMethod.GET)
	 @ResponseBody
	 public AppCheckModel checkUpdateApp(HttpServletRequest request, HttpServletResponse response){   
		 log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		 String versionKey = request.getParameter("versionKey");
		 String platform = request.getParameter("platform");
		 String appId = request.getParameter("appId");
		 log.info("要检测更新的versionKey = " + versionKey);
		 AppCheckModel om = null;
		 if(null != appId){
			 //说明是兼容之前的打包版本
			 System.out.println("******************appId: " + appId);
			 // 通过appId去查询分支的信息，判断是android还是ios
			 Branch branch = appBranchService.findOneBranchByVersionKey(appId);
			 if(null != branch){
				 if(branch.getAndroidKey().equals(appId)) {
					 platform = "Android";
				 }else if(branch.getIosKey().equals(appId)) {
					 platform = "iOS";
				 }
			 }
			 versionKey = appId;
		 }
		
		 ArrayList<AppCheckModel> list = (ArrayList<AppCheckModel>) appUpdateService.checkUpVersionCode(versionKey, platform);
		 if(list.size()>0){
			 om = new AppCheckModel();
			 AppCheckModel maxObj = list.get(0);
			 if (maxObj == null || maxObj.getVersionCode() == null) {
				 return null;
			 }
			 om.setVersionName(maxObj.getVersionName());
			 om.setVersionCode(maxObj.getVersionCode());
			 
			 if(maxObj.getPlatform().equals("iOS")){
				 // 那么把下载页的url地址发送到ios端，用于webView使用
				  String httpsUrl = CommonUtil.getHttpsUrlAddress() +"/appVersion/downloadPage/"+versionKey+"/"+om.getVersionCode()+"/iOS";
				//  String httpsUrl = CommonUtil.getHttpsUrlAddress() + "/downloadPage/downloadPage.html?appId="+appId+"&&versionCode="+om.getVersionCode();
				  maxObj.setDownUrl(httpsUrl);
			 }else{
				 maxObj.setDownUrl(CommonUtil.getHttpsResourceUrl(maxObj.getDownUrl()));
			 }
			 om.setDownUrl(maxObj.getDownUrl());
		 } 
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		 return om;
	 }
	
}
