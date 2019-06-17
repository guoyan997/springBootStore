package com.ut.web.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ut.web.mapper.AppBranchMapper;
import com.ut.web.model.branch.Branch;
import com.ut.web.model.branch.BranchInfo;
import com.ut.web.model.branch.BranchNewst;

@Service
public class AppBranchService {
	
	@Autowired
	private AppBranchMapper appBranchMapper;
	
	public int createBranch(Object[] args){
		return appBranchMapper.createBranch(args);
	}
	
	public List<Branch> findAllBranchs(String projectId){
		return appBranchMapper.findAllBranchs(projectId);
	}
	
	public int deleteBranch(Object[] args){
		return appBranchMapper.deleteBranch(args);
	}
	public int deleteBranchByProjectId(Object[] args){
		return appBranchMapper.deleteBranchByProjectId(args);
	}
	
	public int updateBranch(String sql, Object[] args){
		return appBranchMapper.updateBranch(sql, args);
	}
	
	public int updateBranchIconPath(String sql, Object[] args){
		return appBranchMapper.updateBranchIconPath(sql, args);
	}
	
    public int updateBranchPackageName(String sql, Object[] args){
    	return appBranchMapper.updateBranchPackageName(sql, args);
    }
	    
    public int updateBranchPackageNameAndIcon(String sql, Object[] args){
    	return appBranchMapper.updateBranchPackageNameAndIcon(sql, args);
    }
	
	public Branch findBranchById(String branchId){
		return appBranchMapper.findBranchById(branchId);
	}
	// 这个是为了修改名称时，查找是否重复
	public List<Branch> findBranchsByName(String branchName, String projectId){
		return appBranchMapper.findBranchsByName(branchName, projectId);
	}
	
	//通过branchId获取工程信息
	public BranchInfo findProjectByBranchId(String branchId){
		return appBranchMapper.findProjectByBranchId(branchId);
	}
	
	//通过工程id查询最新的分支版本信息列表
	public List<BranchNewst> findNewstBranchs(String projectId){
		return appBranchMapper.findNewstBranchs(projectId);
	}
	

	// 通过versionKey查找分支的信息
	public Branch findOneBranchByVersionKey(String versionKey){
		return appBranchMapper.findOneBranchByVersionKey(versionKey);
		
	}
	

}
