package com.ut.web.model.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity 
@Table(name = "project") 
public class ProjectTable {
	
	
	/**
	  `PROJECTID` bigint(20) NOT NULL AUTO_INCREMENT,
	  `PROJECTNAME` varchar(80) NOT NULL,
	  `PACKAGENAME` varchar(100) DEFAULT NULL,
	  `DESCRIPTION` varchar(500) DEFAULT NULL,
	  `CREATORID` bigint(20) NOT NULL,
	  `CREATETIME` varchar(50) NOT NULL,
	  **/
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 20,name = "PROJECTID")
	private long projectId;
	
	@Column(length = 80, name = "PROJECTNAME", nullable = false)
	private String projectName;
	
	@Column(length = 100, name = "PACKAGENAME", nullable = true)
	private String packageName;
	
	@Column(length = 500, name = "DESCRIPTION", nullable = true)
	private String description;
	
	@Column(length = 20, name = "CREATORID", nullable = false)
	private long creatorId;
	
	@Column(length = 50, name = "CREATETIME", nullable = false)
	private String createTime;

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
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
