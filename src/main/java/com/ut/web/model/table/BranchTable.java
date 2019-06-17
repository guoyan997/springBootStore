package com.ut.web.model.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity 
@Table(name = "branch") 
public class BranchTable {
	/**
	 `BRANCHID` bigint(20) NOT NULL AUTO_INCREMENT,
	  `BRANCHNAME` varchar(80) NOT NULL,
	  `PROJECTID` bigint(20) NOT NULL,
	  `ANDROIDKEY` varchar(100) NOT NULL,
	  `IOSKEY` varchar(100) NOT NULL,
	  `ICONPATH` varchar(200) DEFAULT NULL,
	  `DESCRIPTION` varchar(500) DEFAULT NULL,
	  `CREATORID` bigint(20) NOT NULL,
	  `CREATETIME` varchar(50) NOT NULL,
	  **/
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 20,name = "BRANCHID")
	private long branchId;
	
	@Column(length = 80, name = "BRANCHNAME", nullable = false)
	private String branchName;
	
	@Column(length = 20, name = "PROJECTID", nullable = false)
	private long projectId;
	
	@Column(length = 80, name = "PACKAGENAME", nullable = false)
	private String packageName;
	
	@Column(length = 100, name = "ANDROIDKEY", nullable = false)
	private String androidKey;
	
	@Column(length = 100, name = "IOSKEY", nullable = false)
	private String iosKey;
	
	@Column(length = 200, name = "ICONPATH", nullable = true)
	private String iconPath;
	
	@Column(length = 500, name = "DESCRIPTION", nullable = true)
	private String description;
	
	@Column(length = 20, name = "CREATORID", nullable = false)
	private long creatorId;
	
	@Column(length = 50, name = "CREATETIME", nullable = false)
	private String createTime;

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

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	

}
