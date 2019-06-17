package com.ut.web.model.branch;

import com.ut.web.util.CommonUtil;

public class Branch {
	
	private long branchId;
	private String branchName;
	private long projectId;
	private String packageName;
	private String androidKey;
	private String iosKey;
	private String iconPath;
	private String description;
	private long creatorId;
	private String creator;
	private String createTime;
	private String downloadCount;    //这个是统计的每个分支的下载量
	
	
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
	public String getIconPath() {
		return CommonUtil.getHttpsResourceUrl(iconPath);
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	public String getCreateTime() {
		return  CommonUtil.convertDateToString(createTime);
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(String downloadCount) {
		this.downloadCount = downloadCount;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	
}
