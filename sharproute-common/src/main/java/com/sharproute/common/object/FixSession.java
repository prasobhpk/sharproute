package com.sharproute.common.object;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="FIX_SESSION")
public class FixSession implements Serializable {
	
	private static final long serialVersionUID = 4861066097319265392L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FIX_SESSION_UID")
	private Integer uid;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="FIX_SERVER_UID", nullable=false, updatable=true)
	private FixServer fixServer;
	
	@Column(name="CONNECTION_TYPE")
	@Enumerated(EnumType.STRING)
	private ConnectionType connectionType;
	
	@Column(name="BEGIN_STRING")
	private String beginString;
	
	@Column(name="SENDER_COMP_ID")
	private String senderCompId;
	
	@Column(name="TARGET_COMP_ID")
	private String targetCompId;
	
	@Column(name="HOST")
	private String host;
	
	@Column(name="PORT")
	private int port;
	
	@Column(name="LOGIN")
	private String login;
	
	@Column(name="PASSWORD")
	private String password;
	
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public FixServer getFixServer() {
		return fixServer;
	}
	public void setFixServer(FixServer fixServer) {
		this.fixServer = fixServer;
	}
	public ConnectionType getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(ConnectionType connectionType) {
		this.connectionType = connectionType;
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
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
