package com.ut.web.model.version;

import com.ut.web.util.CommonUtil;

public class Version {
	
	
	private long versionId;
	private String versionKey;
	private long branchId;
	private String branchName;
	private String projectName;
	private String platform;
	private String createTime;
	private String packageName;
	private String branchDescription;
	private String creator; //创建人名称	
	private long   newVersionId;   //最新的版本id
	private String versionCode;
	private String versionName;
	private String iconPath;
	private String fileSize;
	private String versionDescription;
	private String filePath;
	private long downloadNum;    // 存放下载次数
	private String plistFilePath; // 存放plist文件的路径
	private String ewmImgPath;    //这个版本下载页中二维码图片的地址
	
	private String canInstall = "no";   //no标示扫描的手机与要下载的版本系统不一致且找不到关联版本，不能下载
	                                    // yes 标示扫描的手机与要下载的版本系统一致，可以下载
	
	
	public long getVersionId() {
		return versionId;
	}
	public void setVersionId(long versionId) {
		this.versionId = versionId;
	}
	
	public String getVersionKey() {
		return versionKey;
	}
	public void setVersionKey(String versionKey) {
		this.versionKey = versionKey;
	}
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
	
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getCreateTime() {
		return CommonUtil.convertDateToString(createTime);
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getBranchDescription() {
		return branchDescription;
	}
	public void setBranchDescription(String branchDescription) {
		this.branchDescription = branchDescription;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public long getNewVersionId() {
		return newVersionId;
	}
	public void setNewVersionId(long newVersionId) {
		this.newVersionId = newVersionId;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getIconPath() {
		return CommonUtil.getHttpsResourceUrl(iconPath);
	}
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	public String getFileSize() {
		return CommonUtil.convertFileSize(fileSize);
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getVersionDescription() {
		return versionDescription;
	}
	public void setVersionDescription(String versionDescription) {
		this.versionDescription = versionDescription;
	}
	public String getFilePath() {
		return CommonUtil.getHttpsResourceUrl(filePath);
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getPlistFilePath() {
		return CommonUtil.getHttpsResourceUrl(plistFilePath);
	}
	public long getDownloadNum() {
		return downloadNum;
	}
	public void setDownloadNum(long downloadNum) {
		this.downloadNum = downloadNum;
	}
	public void setPlistFilePath(String plistFilePath) {
		this.plistFilePath = plistFilePath;
	}
	public String getEwmImgPath() {
		return CommonUtil.getHttpsResourceUrl(ewmImgPath);
	}
	public void setEwmImgPath(String ewmImgPath) {
		this.ewmImgPath = ewmImgPath;
	}
	public String getCanInstall() {
		return canInstall;
	}
	public void setCanInstall(String canInstall) {
		this.canInstall = canInstall;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	

}
