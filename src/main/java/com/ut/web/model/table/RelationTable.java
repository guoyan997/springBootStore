package com.ut.web.model.table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity 
@Table(name = "relation") 
public class RelationTable {
	/**
	 * `androidKey` varchar(100) NOT NULL,
	  `iosKey` varchar(100) NOT NULL
	 */
	@Id
	@Column(length = 100, name = "ANDROIDKEY", nullable = false)
	private String androidKey;
	
	@Column(length = 100, name = "IOSKEY", nullable = false)
	private String iosKey;

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
