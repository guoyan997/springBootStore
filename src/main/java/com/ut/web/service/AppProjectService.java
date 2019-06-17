package com.ut.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ut.web.mapper.AppProjectMapper;
import com.ut.web.model.project.Project;

@Service
public class AppProjectService {
	
	@Autowired
	private AppProjectMapper appProjectMapper;
	
	public int createProject(Object[] args){
		return appProjectMapper.createProject(args);
	}
	
	public List<Project> findAllProjects(Object[] args){
		return appProjectMapper.findAllProjects(args);
	}
	
	public int deleteProject(Object[] args){
		return appProjectMapper.deleteProject(args);
	}
	
	// 修改工程的基本信息
	public int updateProject(String sql, Object[] args){
		return appProjectMapper.updateProject(sql, args);
	}
	// 修改工程的包名字段
	public int updateProjectPackageName(String sql, Object[] args){
		return appProjectMapper.updateProjectPackageName(sql, args);
	}
	
	
	public Project findProjectById(String projectId){
		return appProjectMapper.findProjectById(projectId);
	}
	
	public List<Project> findProjectsByName(String projectName){
		return appProjectMapper.findProjectsByName(projectName);
	}
		
	

}
