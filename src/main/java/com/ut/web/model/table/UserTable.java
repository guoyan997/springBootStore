package com.ut.web.model.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity //声明一个实体，用的是Java规范下的注解
@Table(name = "user") //映射的表名称
public class UserTable {
	
	/**
	 `ID` bigint(20) NOT NULL AUTO_INCREMENT,
	 `USERNAME` varchar(100) NOT NULL,
	 `PASSWORD` varchar(100) NOT NULL,
	 `USERTYPE` varchar(100) NOT NULL,
	 `CREATORID` bigint(20) NOT NULL,
	 `CREATETIME` varchar(100) NOT NULL,
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(length = 20,name = "ID")
	private long id;
	@Column(length = 200, name = "USERNAME", nullable = false)
	private String username;
	@Column(length = 100, name = "PASSWORD", nullable = false)
	private String password;
	@Column(length = 100, name = "USERTYPE", nullable = false)
	private String userType;
	@Column(length = 20, name = "CREATORID", nullable = false)
	private long creatorId;
	@Column(length = 100, name = "CREATETIME", nullable = false)
	private String createTime;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
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
