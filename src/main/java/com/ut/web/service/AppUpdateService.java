package com.ut.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ut.web.mapper.AppUpdateMapper;
import com.ut.web.model.AppCheckModel;

@Service
public class AppUpdateService {
	
	@Autowired
	private AppUpdateMapper appUpdateMapper;
	
	// public List<AppCheckModel> checkUpVersionCode(String appId);
	
	public List<AppCheckModel> checkUpVersionCode(String versionkey, String platform){
		return appUpdateMapper.checkUpVersionCode(versionkey, platform);
	}

}
