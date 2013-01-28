package com.sharproute.common.object;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="FIX_SESSION")
public class FixSession implements Serializable {
	
	private static final long serialVersionUID = 4861066097319265392L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FIX_SESSION_UID")
	private Integer uid;
	
	@Column(name="BEGIN_STRING")
	private String beginString;
	
	@Column(name="SENDER_COMP_ID")
	private String senderCompId;
	
	@Column(name="TARGET_COMP_ID")
	private String targetCompId;
	
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getBeginString() {
		return beginString;
	}
	public void setBeginString(String beginString) {
		this.beginString = beginString;
	}
	public String getSenderCompId() {
		return senderCompId;
	}
	public void setSenderCompId(String senderCompId) {
		this.senderCompId = senderCompId;
	}
	public String getTargetCompId() {
		return targetCompId;
	}
	public void setTargetCompId(String targetCompId) {
		this.targetCompId = targetCompId;
	}

}
