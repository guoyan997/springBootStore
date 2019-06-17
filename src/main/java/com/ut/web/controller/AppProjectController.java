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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ut.web.model.OptionMessage;
import com.ut.web.model.project.Project;
import com.ut.web.service.AppBranchService;
import com.ut.web.service.AppProjectService;
import com.ut.web.service.AppRelationService;
import com.ut.web.util.CommonUtil;

@RestController
@CrossOrigin(origins = "*", maxAge = 36000)
@RequestMapping("/appProject")
public class AppProjectController {
	
	private Logger log = LogManager.getLogger(AppProjectController.class);
	
	@Autowired
	private AppProjectService appProjectService;
	@Autowired
	private AppBranchService appBranchService;
	@Autowired
	private AppRelationService appRelationService;
	
	
    @RequestMapping(value = "/createProject", method = RequestMethod.POST)
    @ResponseBody
    public OptionMessage createProject(@RequestBody Project project){
    	log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
    	OptionMessage msg = new OptionMessage();
    	long createTime = System.currentTimeMillis();
    	Object[] args = new Object[]{project.getProjectName(),"",project.getDescription(), project.getCreatorId(), createTime};
    	List<Project> plist = appProjectService.findProjectsByName(project.getProjectName());
    	if(plist.size()>0){
    		//说明名称已经存在，需要修改名称
    		 msg.setType("existName");
			 msg.setMessage("该名称已经存在");
    	}else{
    		int result =  appProjectService.createProject(args);
        	if(result>0){
        		// 获得工程id
        		String projectId= "";
        		List<Project> plist2 = appProjectService.findProjectsByName(project.getProjectName());
        		if(plist2.size()>0) {
        			projectId = plist2.get(0).getProjectId() + "";
        			// 当工程创建成功时，要随之创建一个默认的渠道，作为通用版本
            		String androidKey = UUID.randomUUID().toString();
                	String iosKey = UUID.randomUUID().toString();
                	Object[] args2 = new Object[]{"Default",projectId,androidKey,iosKey,"","这个是工程通用渠道，不可删除！", project.getCreatorId(), createTime};
                	appBranchService.createBranch(args2);
        		}
    			 msg.setType("success");
    			 msg.setMessage("创建Project成功");
    		 }else{
    			 msg.setType("failed"); 
    			 msg.setMessage("创建Project失败");
    		 }
    	}
    	return msg;
    }
    
    @RequestMapping(value = "/findAllProjects", method = RequestMethod.GET)
    @ResponseBody
	public List<Project> findAllProjects(HttpServletRequest request, HttpServletResponse response){
    	 log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
    	 String creatorId = request.getParameter("creatorId");
    	 Object[] args = new Object[]{creatorId};
    	 return appProjectService.findAllProjects(args);
	}
    
    
    @RequestMapping(value = "/deleteProject", method = RequestMethod.GET)
    @ResponseBody
	public OptionMessage deleteProject(HttpServletRequest request, HttpServletResponse response){
    	log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		OptionMessage msg = new OptionMessage();
		String projectId = request.getParameter("projectId");
		String creatorId = request.getParameter("creatorId");
		// 删除工程前要删除工程下各个分支的关联关系
		Object[] args2 = new Object[]{projectId, projectId};
		appRelationService.deleteRelationByProjectId(args2);
		Object[] args = new Object[]{projectId};
		// 删除数据库中其下的分支记录
		appBranchService.deleteBranchByProjectId(args);
		// 1,将这条记录从表中删除，但是还要级联删除其下的分支，和每个分支的版本，以及保存的静态文件
		int result = appProjectService.deleteProject(args);
		 //同时要删除文件夹下的文件, 找到文件夹路径
	    String filePath = request.getServletContext().getRealPath("/output/upload")+"/"+creatorId+"/"+projectId;
		 if(result>0){
			//删除保存在服务器端的文件
			 CommonUtil.deleteAll(new File(filePath));
			 msg.setType("success");
			 msg.setMessage("删除Project成功");
		 }else{
			 msg.setType("failed"); 
			 msg.setMessage("删除Project失败");
		 }
	
		return msg;
	}
    
    @RequestMapping(value = "/updateProject", method = RequestMethod.POST)
    @ResponseBody
	public OptionMessage updateProject(@RequestBody Project project){
    	log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
    	OptionMessage msg = new OptionMessage();
	    long projectId = project.getProjectId();
	    String projectName = project.getProjectName();
	    String description =project.getDescription();
	    String packageName = project.getPackageName();
	    String sql= "";
	    Object[] args = null;
		int result= 0;
	    if(packageName == null){
	    	// 说明没有提交包名的数据，这里只是修改工程信息
	    	List<Project> plist = appProjectService.findProjectsByName(project.getProjectName());
	    	if(plist.size()>0){
	    		long findid = plist.get(0).getProjectId();
	    		if(plist.size()==1&&findid == projectId){
	    			// 说明修改的是自身，可以修改
	    			sql = "UPDATE PROJECT SET PROJECTNAME = ?, description = ? WHERE PROJECTID = ?  ";
			    	args = new Object[]{projectName,description, projectId};
	    		}else {
	    			//说明是重复的名称
		    		 msg.setType("existName");
					 msg.setMessage("该名称已经存在");
					 return msg;
	    		}
	    	}else{
		    	 sql = "UPDATE PROJECT SET PROJECTNAME = ?, description = ? WHERE PROJECTID = ?  ";
		    	 args = new Object[]{projectName,description, projectId};
	    	}
	    	result = appProjectService.updateProject(sql, args);
	    }else {
	    	// 说明修改的是包名
	    	sql = "UPDATE PROJECT SET PACKAGENAME = ? WHERE PROJECTID = ?  ";
	    	args = new Object[]{packageName, projectId};
	    	result = appProjectService.updateProjectPackageName(sql, args);
	    }
    	if(result>0){
			 msg.setType("success");
			 msg.setMessage("修改Project成功");
		 }else{
			 msg.setType("failed"); 
			 msg.setMessage("修改Project失败");
		 }
    	return msg;
	}
    
    @RequestMapping(value = "/findProjectById", method = RequestMethod.GET)
    @ResponseBody
	public Project findProjectById(HttpServletRequest request, HttpServletResponse response){
    	log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
    	String projectId = request.getParameter("projectId");
		Project p = appProjectService.findProjectById(projectId);
		return p;
	}
    
    

}
