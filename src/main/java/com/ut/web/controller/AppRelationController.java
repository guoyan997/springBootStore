package com.ut.web.controller;

import java.util.List;

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

import com.ut.web.model.OptionMessage;
import com.ut.web.model.relation.Relation;
import com.ut.web.service.AppRelationService;

@Controller
@CrossOrigin(origins = "*", maxAge = 36000)
@RequestMapping("/appRelation")
public class AppRelationController {
	
	private Logger log = LogManager.getLogger(AppRelationController.class);
	@Autowired
	private AppRelationService appRelationService;
	
	
	@RequestMapping(value = "/createRelation", method = RequestMethod.GET)
	@ResponseBody
	public OptionMessage createRelation(HttpServletRequest request, HttpServletResponse response){
	    log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
	    OptionMessage msg = new OptionMessage();
	    String androidKey = request.getParameter("androidKey");
	    String iosKey = request.getParameter("iosKey");
	    Object[] args = new Object[]{androidKey, iosKey};
	    int result  = appRelationService.createRelation(args);
	    if(result>0){
			 msg.setType("success");
			 msg.setMessage("创建关联成功！");
		}else {
			 msg.setType("failed"); 
			 msg.setMessage("创建关联失败");
		}
	    return msg;
	}
	
	@RequestMapping(value = "/updateRelation", method = RequestMethod.GET)
	@ResponseBody
	public OptionMessage updateRelation(HttpServletRequest request, HttpServletResponse response){
	    log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
	    OptionMessage msg = new OptionMessage();
	    String androidKey = request.getParameter("androidKey");
	    String iosKey = request.getParameter("iosKey");
	    String turn = request.getParameter("turn");
	    String sql = "";
	    int result = 0;
	    if(turn.equals("0")) {
	    	sql = "UPDATE RELATION SET IOSKEY = ? WHERE ANDROIDKEY = ?";
	        Object[] args = new Object[]{iosKey,androidKey};
	    	result	= appRelationService.updateRelationByAndroid(sql, args);
	    }else if (turn.equals("1")) {
	    	sql = "UPDATE RELATION SET ANDROIDKEY = ? WHERE IOSKEY = ?";
	        Object[] args = new Object[]{androidKey,iosKey};
	    	result	= appRelationService.updateRelationByIos(sql, args);
	    }
	    if(result>0){
			 msg.setType("success");
			 msg.setMessage("修改关联成功！");
		}else {
			 msg.setType("failed"); 
			 msg.setMessage("修改关联失败");
		}
	    return msg;
	}
	
	
	@RequestMapping(value = "/deleteRelation", method = RequestMethod.GET)
	@ResponseBody
	public OptionMessage deleteRelation(HttpServletRequest request, HttpServletResponse response){
	    log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
	    OptionMessage msg = new OptionMessage();
	    String androidKey = request.getParameter("androidKey");
	    String iosKey = request.getParameter("iosKey");
	    Object[] args = new Object[]{androidKey, iosKey};
	    int result = appRelationService.deleteRelation(args);
	    if(result>0){
			 msg.setType("success");
			 msg.setMessage("刪除关联成功！");
		}else {
			 msg.setType("failed"); 
			 msg.setMessage("删除关联失败");
		}
	    return msg;
	}
	
	@RequestMapping(value = "/findRelationByKey", method = RequestMethod.GET)
	@ResponseBody
	public Relation findRelationByKey(HttpServletRequest request, HttpServletResponse response){
	    log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
	    String versionKey = request.getParameter("versionKey");
	    String platform = request.getParameter("platform");
	    Relation relation  = appRelationService.findRelationByKey(versionKey, platform);
	    return relation;
	}
	
	
	@RequestMapping(value = "/findRelationListForSelect", method = RequestMethod.GET)
	@ResponseBody
	public List<Relation> findRelationListForSelect(HttpServletRequest request, HttpServletResponse response){
	    log.info("****执行类："+Thread.currentThread().getStackTrace()[1].getClassName()+"方法："+Thread.currentThread().getStackTrace()[1].getMethodName());
	    String versionKey = request.getParameter("versionKey");
	    String platform = request.getParameter("platform");
	    String creatorId = request.getParameter("creatorId");
	    List<Relation> relationList  = appRelationService.findRelationListForSelect(versionKey, platform,creatorId);
	    return relationList;
	}

}
