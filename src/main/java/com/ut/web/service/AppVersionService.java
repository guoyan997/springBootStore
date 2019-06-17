package com.ut.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ut.web.mapper.AppVersionMapper;
import com.ut.web.model.version.Version;

@Service
public class AppVersionService {
	
   @Autowired
   private AppVersionMapper appVersionMapper;
	
    public int createVersion(Object[] args){
    	return appVersionMapper.createVersion(args);
    }
	
    // 查找摸个分支下的所有
	public List<Version> findReleaseListByBranchId(String branchId){
		return appVersionMapper.findReleaseListByBranchId(branchId);
	}
	
	public int updateVersion(String sql, Object[] args){
    	return appVersionMapper.updateVersion(sql, args);
    }
	
	public int deleteVersion(String versionId){
    	return appVersionMapper.deleteVersion(versionId);
    }
	
	public int deleteAllVersions(String branchId){
    	return appVersionMapper.deleteAllVersions(branchId);
    }
	
	//查找最新的版本  String branchId, String platForm
	public Version findNewstVersion(String branchId, String platForm){
		return appVersionMapper.findNewstVersion(branchId, platForm);
	}
	
	//查找某一个版本
	public Version findOneRelease(String versionId){
		return appVersionMapper.findOneRelease(versionId);
	}
	
	//更新某个版本的下载量
	public int updateDownLoadNum(String versionId){
    	return appVersionMapper.updateDownLoadNum(versionId);
    }
	
	//查询版本列表通过工程id
	public List<Version> findReleaseListByProjectId(String projectId){
		return appVersionMapper.findReleaseListByProjectId(projectId);
	}
	
	// 通过versionKey和versionCode去查找一个版本的信息
	public Version findOneVersionByKey(String versionKey, String versionCode,String platform){
		return appVersionMapper.findOneVersionByKey(versionKey, versionCode, platform);
	}
//	public Version findOneVersionByKey_Ios(String versionKey, String versionCode,String platform){
//		return appVersionMapper.findOneVersionByKey_Ios(versionKey, versionCode, platform);
//	}
	
	// 通过versionKey和PLATFORM去查找一个最新的版本的信息
	public Version findOneNewstVersionByKey(String versionKey, String platform){
		return appVersionMapper.findOneNewstVersionByKey(versionKey, platform);
	}
	
//	public Version findOneNewstVersionByKey_Ios(String versionKey, String platform){
//		return appVersionMapper.findOneNewstVersionByKey_Ios(versionKey, platform);
//	}
	
}
