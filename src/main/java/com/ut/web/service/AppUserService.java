package com.ut.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ut.web.mapper.AppUserMapper;
import com.ut.web.model.User;

@Service
public class AppUserService {
	
	@Autowired
	private AppUserMapper appUserMapper;
	
	public User findUserByName(String userName){
		return appUserMapper.findUserByName(userName);
	}
	public int changeUserPsw(Object[] args){
		return appUserMapper.changeUserPsw(args);
	}
	public List<User> findUserListByCreatorId(String creatorId){
		return appUserMapper.findUserListByCreatorId(creatorId);
	}
	
	public int updateUser(Object[] args){
		return appUserMapper.updateUser(args);
	}
	@Transactional//添加事务
	public int createUser(Object[] args){
		return appUserMapper.createUser(args);
	}
	public int deleteUserById(String id){
		return appUserMapper.deleteUserById(id);
	}

}
