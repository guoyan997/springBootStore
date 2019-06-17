package com.ut.web.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ut.web.mapper.AppRelationMapper;
import com.ut.web.model.relation.Relation;
@Service
public class AppRelationService {
	
	@Autowired
	private AppRelationMapper appRelationMapper;
	// 创建关联
	public int createRelation(Object[] args){
		return appRelationMapper.createRelation(args);
	}
	// 删除关联
	public int deleteRelation(Object[] args){
		return appRelationMapper.deleteRelation(args);
	}
	// 修改关联
	public int updateRelationByAndroid(String sql, Object[] args){
		return appRelationMapper.updateRelationByAndroid(sql, args);
	}
	public int updateRelationByIos(String sql, Object[] args){
		return appRelationMapper.updateRelationByIos(sql, args);
	}
	// 查找关联
	public Relation findRelationByKey(String versionKey, String platform){
		return appRelationMapper.findRelationByKey(versionKey, platform);
	}
   // 查找可以关联的列表
	public List<Relation> findRelationListForSelect(String versionKey, String platform, String creatorId){
		return appRelationMapper.findRelationListForSelect(versionKey, platform, creatorId);
	}
	// 删除关联通过分支id
	public int deleteRelationByBranchId(Object[] args){
		return appRelationMapper.deleteRelationByBranchId(args);
	}
	//删除关联，通过工程id
	public int deleteRelationByProjectId(Object[] args){
		return appRelationMapper.deleteRelationByProjectId(args);
	}
}
