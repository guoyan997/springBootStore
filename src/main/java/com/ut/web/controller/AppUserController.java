package com.ut.web.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ut.web.model.OptionMessage;
import com.ut.web.model.User;
import com.ut.web.service.AppUserService;
import com.ut.web.util.AESUtil;
import com.ut.web.util.CommonUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path="/appUser")
public class AppUserController {
	
	// public static Logger log = LogManager.getLogger(AppUserController.class);
	 private Logger log = LogManager.getLogger(AppUserController.class);
	 
	 
	@Autowired
	private AppUserService appUserService;
	
     @ApiOperation(value="登陆检测", notes="判断用户是否可以登陆")
	 @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Integer", paramType = "path")
	 @RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
	 @ResponseBody
	public User checkLogin(HttpServletRequest request, HttpServletResponse response){
		 log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
	     log.error("这里出错了");  
		 String username = request.getParameter("username");
		 String password = request.getParameter("password");
		 User result = new 	User();
		 User user = appUserService.findUserByName(username);
		 if(null !=user && AESUtil.deCodeData(user.getPassword()).equals(password)){
			 result.setId(user.getId());
			 result.setUsername(user.getUsername());
			 result.setUserType(user.getUserType());
		 }
		return result;
	}
	 
	 @RequestMapping(value = "/changeUserPsw", method = RequestMethod.POST)
	 @ResponseBody
	 public OptionMessage changeUserPsw(@RequestBody User user){
		 log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		 OptionMessage om = new OptionMessage(); 
		 //首先看是否用户名密码正确
		 User userOne = appUserService.findUserByName(user.getUsername());
		 if(userOne!=null && AESUtil.deCodeData(userOne.getPassword()).equals(user.getPassword())){
			//然后修改密码
			 Object[] args = new Object[]{AESUtil.enCodeData(user.getNewPassword()), user.getUsername()};
			 int change =  appUserService.changeUserPsw(args);
			 if(change>0){
				 om.setType("success"); 
				 om.setMessage("密码修改成功！");
			 }else{
				 om.setType("failed"); 
				 om.setMessage("修改密码失败，请重试！");
			 }
			 
		 }else{
			 om.setType("failed"); 
			 om.setMessage("用户名或密码不正确！");
		 }
		 return om;
	 }
	 
	 @RequestMapping(value = "/findUserListByCreatorId", method = RequestMethod.GET)
	 @ResponseBody
	 public List<User> findUserListByCreatorId(HttpServletRequest request, HttpServletResponse response) {
		 log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		 String creatorId = request.getParameter("creatorId");
		 List<User> list = appUserService.findUserListByCreatorId(creatorId);
		 return list;
	 } 
	 
	 @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	 @ResponseBody
	public OptionMessage updateUser(@RequestBody User user){
		 log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		 OptionMessage om = new OptionMessage();
		 
		 String sql = "UPDATE USER SET USERNAME = ?, PASSWORD = ? WHERE ID= ?";
		 Object[] args = new Object[]{user.getUsername(),AESUtil.enCodeData(user.getPassword()), user.getId()};
		 int result =  appUserService.updateUser(args);
		 if(result>0){
			 om.setType("success"); 
			 om.setMessage("用户修改成功！");
		 }else{
			 om.setType("failed"); 
			 om.setMessage("修改用户失败，请重试！");
		 }
		 
		return om;
	} 
	 
    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    @ResponseBody
	public OptionMessage createUser(@RequestBody User user){
		 log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		 OptionMessage om = new OptionMessage();
		 long createTime = System.currentTimeMillis();
		 Object[] args = new Object[]{user.getUsername(),AESUtil.enCodeData(user.getPassword()),"user", user.getCreatorId(),createTime};
		 int result =  appUserService.createUser(args);
		 if(result>0){
			 om.setType("success"); 
			 om.setMessage("创建成功！");
		 }else{
			 om.setType("failed"); 
			 om.setMessage("创建用户失败，请重试！");
		 }
		return om;
	} 
    
	 @RequestMapping(value = "/deleteUserById", method = RequestMethod.GET)
	 @ResponseBody
	 public OptionMessage deleteUserById(HttpServletRequest request, HttpServletResponse response) {
		 log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
		 OptionMessage om = new OptionMessage();
		 String id = request.getParameter("id");
		 int result =  appUserService.deleteUserById(id);
		 //删除该用户的所有上传的静态文件
		  String filePath = request.getServletContext().getRealPath("/output/upload")+"/"+id;
		 if(result>0){
			//删除保存在服务器端的文件
			 CommonUtil.deleteAll(new File(filePath));
			 om.setType("success"); 
			 om.setMessage("删除用户成功！");
		 }else{
			 om.setType("failed"); 
			 om.setMessage("删除用户失败，请重试！");
		 }
		return om;
	 }  

}
