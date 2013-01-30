package com.sharproute.common.object;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="FIX_ENGINE")
public class FixEngine extends AbstractCommonObject {
	
	private static final long serialVersionUID = 4861066097319265392L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FIX_ENGINE_UID")
	private Integer uid;
	
	@Column(name="NAME")
	private String name;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
