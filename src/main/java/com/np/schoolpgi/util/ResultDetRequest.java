package com.np.schoolpgi.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultDetRequest {
	private long domainid;
	private String domainName;
	private Object subDomainid;
	private String subDomainName;
	private long questionid;
	private long subQuestionid;
	private long typeid;
	private String ansOptions;
	private String answers;
	private long thirdParty;
	private long instanceId;

	@JsonProperty("domain_id")
	public long getDomainid() {
		return domainid;
	}

	@JsonProperty("domain_id")
	public void setDomainid(long value) {
		this.domainid = value;
	}

	@JsonProperty("domain_name")
	public String getDomainName() {
		return domainName;
	}

	@JsonProperty("domain_name")
	public void setDomainName(String value) {
		this.domainName = value;
	}

	@JsonProperty("sub_domain_id")
	public Object getSubDomainid() {
		return subDomainid;
	}

	@JsonProperty("sub_domain_id")
	public void setSubDomainid(Object object) {
		this.subDomainid = object;
	}

	@JsonProperty("sub_domain_name")
	public String getSubDomainName() {
		return subDomainName;
	}

	@JsonProperty("sub_domain_name")
	public void setSubDomainName(String value) {
		this.subDomainName = value;
	}

	@JsonProperty("question_id")
	public long getQuestionid() {
		return questionid;
	}

	@JsonProperty("question_id")
	public void setQuestionid(long value) {
		this.questionid = value;
	}

	@JsonProperty("sub_question_id")
	public long getSubQuestionid() {
		return subQuestionid;
	}

	@JsonProperty("sub_question_id")
	public void setSubQuestionid(long value) {
		this.subQuestionid = value;
	}

	@JsonProperty("type_id")
	public long getTypeid() {
		return typeid;
	}

	@JsonProperty("type_id")
	public void setTypeid(long value) {
		this.typeid = value;
	}

	@JsonProperty("ans_options")
	public String getAnsOptions() {
		return ansOptions;
	}

	@JsonProperty("ans_options")
	public void setAnsOptions(String value) {
		this.ansOptions = value;
	}

	@JsonProperty("answers")
	public String getAnswers() {
		return answers;
	}

	@JsonProperty("answers")
	public void setAnswers(String value) {
		this.answers = value;
	}

	@JsonProperty("is_third_party")
	public long getThirdParty() {
		return thirdParty;
	}

	@JsonProperty("is_third_party")
	public void setThirdParty(long value) {
		this.thirdParty = value;
	}

	@JsonProperty("instance_id")
	public long getInstanceId() {
		return instanceId;
	}

	@JsonProperty("instance_id")
	public void setInstanceId(long value) {
		this.instanceId = value;
	}

	@Override
	public String toString() {
		return "ResultDetRequest [domainid=" + domainid + ", domainName=" + domainName + ", subDomainid=" + subDomainid
				+ ", subDomainName=" + subDomainName + ", questionid=" + questionid + ", subQuestionid=" + subQuestionid
				+ ", typeid=" + typeid + ", ansOptions=" + ansOptions + ", answers=" + answers + ", thirdParty="
				+ thirdParty + ", instanceId=" + instanceId + "]";
	}

}