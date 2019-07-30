package com.enterprise.base.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

public class OaMsgEntity implements Serializable {

	private Integer id;

	private Integer errCode;

	private String errMsg;

	private String invalidUser;

	private String invalidParty;

	private String messageId;

	private Integer companyId;

	private Integer arenaId;

	private Integer libraryId;

	private Integer userId;

	private Date createdTime;

	private String content;

	private String codeFlag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getErrCode() {
		return errCode;
	}

	public void setErrCode(Integer errCode) {
		this.errCode = errCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getInvalidUser() {
		return invalidUser;
	}

	public void setInvalidUser(String invalidUser) {
		this.invalidUser = invalidUser;
	}

	public String getInvalidParty() {
		return invalidParty;
	}

	public void setInvalidParty(String invalidParty) {
		this.invalidParty = invalidParty;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(Integer libraryId) {
		this.libraryId = libraryId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCodeFlag() {
		return codeFlag;
	}

	public void setCodeFlag(String codeFlag) {
		this.codeFlag = codeFlag;
	}

	public Integer getArenaId() {
		return arenaId;
	}

	public void setArenaId(Integer arenaId) {
		this.arenaId = arenaId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
