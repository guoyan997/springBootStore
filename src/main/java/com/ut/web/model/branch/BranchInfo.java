package com.ut.web.model.branch;

import com.ut.web.model.version.Version;

public class BranchInfo {
	
	private String projectName;
	private String branchName;
	
	
	private Version androidVersion;
	private Version iosVersion;
	
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
	
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public Version getAndroidVersion() {
		return androidVersion;
	}
	public void setAndroidVersion(Version androidVersion) {
		this.androidVersion = androidVersion;
	}
	public Version getIosVersion() {
		return iosVersion;
	}
	public void setIosVersion(Version iosVersion) {
		this.iosVersion = iosVersion;
	}
	
	
	
	

}
