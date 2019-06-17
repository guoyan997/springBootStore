package com.ut.web.model.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity 
@Table(name = "version") 
public class VersionTable {
	
	/**
	 * `VERSIONID` bigint(20) NOT NULL AUTO_INCREMENT,
  `BRANCHID` bigint(20) NOT NULL,
  `VERSIONCODE` varchar(40) NOT NULL,
  `VERSIONNAME` varchar(40) NOT NULL,
  `PLATFORM` varchar(40) NOT NULL,
  `DOWNLOADNUM` bigint(20) DEFAULT '0',
  `PACKAGENAME` varchar(80) NOT NULL,
  `FILESIZE` varchar(40) NOT NULL,
  `FILEPATH` varchar(200) NOT NULL,
  `PLISTFILEPATH` varchar(200) DEFAULT NULL,
  `EWMIMGPATH` varchar(200) DEFAULT NULL,
  `ICONPATH` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(500) DEFAULT NULL,
  `CREATORID` bigint(20) NOT NULL,
  `CREATETIME` varchar(50) NOT NULL,
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 20,name = "VERSIONID")
	private long versionId;
	
	@Column(length = 20, name = "BRANCHID", nullable = false)
	private String branchId;
	
	@Column(length = 40, name = "VERSIONCODE", nullable = false)
	private String versionCode;
	
	@Column(length = 40, name = "VERSIONNAME", nullable = false)
	private String versionName;
	
	@Column(length = 40, name = "PLATFORM", nullable = false)
	private String platform;
	
	@Column(length = 20, name = "DOWNLOADNUM", nullable = true)
	private long downloadNum;
	
	@Column(length = 80, name = "PACKAGENAME", nullable = false)
	private String packageName;
	
	@Column(length = 40, name = "FILESIZE", nullable = false)
	private String fileSize;
	
	@Column(length = 200, name = "FILEPATH", nullable = false)
	private String filePath;
	
	@Column(length = 200, name = "PLISTFILEPATH", nullable = true)
	private String plistFilePath;
	
	@Column(length = 200, name = "EWMIMGPATH", nullable = true)
	private String ewmImgPath;
	
	@Column(length = 200, name = "ICONPATH", nullable = false)
	private String iconPath;
	
	@Column(length = 500, name = "DESCRIPTION", nullable = true)
	private String description;
	
	@Column(length = 20, name = "CREATORID", nullable = false)
	private String creatorId;
	
	@Column(length = 50, name = "CREATETIME", nullable = false)
	private String createTime;

	public long getVersionId() {
		return versionId;
	}

	public void setVersionId(long versionId) {
		this.versionId = versionId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public long getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(long downloadNum) {
		this.downloadNum = downloadNum;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getPlistFilePath() {
		return plistFilePath;
	}

	public void setPlistFilePath(String plistFilePath) {
		this.plistFilePath = plistFilePath;
	}

	public String getEwmImgPath() {
		return ewmImgPath;
	}

	public void setEwmImgPath(String ewmImgPath) {
		this.ewmImgPath = ewmImgPath;
	}

	public String getIconPath() {
		return iconPath;
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

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
}
