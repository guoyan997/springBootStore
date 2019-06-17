package com.ut.web.model.branch;

import com.ut.web.util.CommonUtil;

public class BranchNewst {
	
	private long branchId;
	private String branchName;
	private long projectId;
	private String versionCode;
	private String platform;
	private String downloadNum;
	private String createTime;
	private String androidKey;
	private String iosKey;
	
	public long getBranchId() {
		return branchId;
	}
	public void setBranchId(long branchId) {
		this.branchId = branchId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public long getProjectId() {
		return projectId;
	}
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getDownloadNum() {
		return downloadNum;
	}
	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
	}
	public String getCreateTime() {
		return  CommonUtil.convertDateToString(createTime);
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getAndroidKey() {
		return androidKey;
	}
	public void setAndroidKey(String androidKey) {
		this.androidKey = androidKey;
	}
	public String getIosKey() {
		return iosKey;
	}
	public void setIosKey(String iosKey) {
		this.iosKey = iosKey;
	}
	
	
	
}
