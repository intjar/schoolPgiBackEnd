package com.np.schoolpgi.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultDet {
	private long domainID;
	private String domainName;
	private long subDomainID;
	private String subDomainName;
	private long questionID;
	private long subQuestionID;
	private long dataSourceID;
	private String dataSourceName;
	private String dataSourceURL;
	private long isThirdParty;
	private long isMandatory;
	private long code;
	private String question;
	private String type;
	private String ansOptions;
	private String answers;
	private Object qOnlyNumeric;
	private Object sqOnlyNumeric;
	private long totalQuestion;
	private long attemptedQuestion;
	private long pendingQuestion;
	private long typeId;
	private long instanceId;
	private String sno;
	private long instanceCode;

	@JsonProperty("sno")
	public String getsno() {
		return sno;
	}

	@JsonProperty("sno")
	public void setSno(String value) {
		this.sno = value;
	}

	@JsonProperty("domain_id")
	public long getDomainID() {
		return domainID;
	}

	@JsonProperty("domain_id")
	public void setDomainID(long value) {
		this.domainID = value;
	}

	@JsonProperty("type_id")
	public long getTypeiD() {
		return typeId;
	}

	@JsonProperty("type_id")
	public void setTypeId(long value) {
		this.typeId = value;
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
	public Object getSubDomainID() {
		return subDomainID;
	}

	@JsonProperty("sub_domain_id")
	public void setSubDomainID(long value) {
		this.subDomainID = value;
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
	public long getQuestionID() {
		return questionID;
	}

	@JsonProperty("question_id")
	public void setQuestionID(long value) {
		this.questionID = value;
	}

	@JsonProperty("sub_question_id")
	public long getSubQuestionID() {
		return subQuestionID;
	}

	@JsonProperty("sub_question_id")
	public void setSubQuestionID(long value) {
		this.subQuestionID = value;
	}

	@JsonProperty("data_source_id")
	public long getDataSourceID() {
		return dataSourceID;
	}

	@JsonProperty("data_source_id")
	public void setDataSourceID(long value) {
		this.dataSourceID = value;
	}

	@JsonProperty("data_source_name")
	public String getDataSourceName() {
		return dataSourceName;
	}

	@JsonProperty("data_source_name")
	public void setDataSourceName(String value) {
		this.dataSourceName = value;
	}

	@JsonProperty("data_source_url")
	public String getDataSourceURL() {
		return dataSourceURL;
	}

	@JsonProperty("data_source_url")
	public void setDataSourceURL(String value) {
		this.dataSourceURL = value;
	}

	@JsonProperty("is_third_party")
	public long getIsThirdParty() {
		return isThirdParty;
	}

	@JsonProperty("is_third_party")
	public void setIsThirdParty(long value) {
		this.isThirdParty = value;
	}

	@JsonProperty("is_mandatory")
	public long getIsMandatory() {
		return isMandatory;
	}

	@JsonProperty("is_mandatory")
	public void setIsMandatory(long value) {
		this.isMandatory = value;
	}

	@JsonProperty("code")
	public long getCode() {
		return code;
	}

	@JsonProperty("code")
	public void setCode(long value) {
		this.code = value;
	}

	@JsonProperty("question")
	public String getQuestion() {
		return question;
	}

	@JsonProperty("question")
	public void setQuestion(String value) {
		this.question = value;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String value) {
		this.type = value;
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

	@JsonProperty("q_only_numeric")
	public Object getQOnlyNumeric() {
		return qOnlyNumeric;
	}

	@JsonProperty("q_only_numeric")
	public void setQOnlyNumeric(Object value) {
		this.qOnlyNumeric = value;
	}

	@JsonProperty("sq_only_numeric")
	public Object getSqOnlyNumeric() {
		return sqOnlyNumeric;
	}

	@JsonProperty("sq_only_numeric")
	public void setSqOnlyNumeric(Object value) {
		this.sqOnlyNumeric = value;
	}

	@JsonProperty("total_question")
	public long getTotalQuestion() {
		return totalQuestion;
	}

	@JsonProperty("total_question")
	public void setTotalQuestion(long value) {
		this.totalQuestion = value;
	}

	@JsonProperty("attempted_question")
	public long getAttemptedQuestion() {
		return attemptedQuestion;
	}

	@JsonProperty("attempted_question")
	public void setAttemptedQuestion(long value) {
		this.attemptedQuestion = value;
	}

	@JsonProperty("pending_question")
	public long getPendingQuestion() {
		return pendingQuestion;
	}

	@JsonProperty("pending_question")
	public void setPendingQuestion(long value) {
		this.pendingQuestion = value;
	}

	@JsonProperty("instance_id")
	public long getInstanceId() {
		return instanceId;
	}

	@JsonProperty("instance_id")
	public void setInstanceId(long value) {
		this.instanceId = value;
	}

	@JsonProperty("instance_code")
	public long getInstanceCode() {
		return instanceCode;
	}

	@JsonProperty("instance_code")
	public void setInstanceCode(long value) {
		this.instanceCode = value;
	}

	@Override
	public String toString() {
		return "ResultDet [domainID=" + domainID + ", domainName=" + domainName + ", subDomainID=" + subDomainID
				+ ", subDomainName=" + subDomainName + ", questionID=" + questionID + ", subQuestionID=" + subQuestionID
				+ ", dataSourceID=" + dataSourceID + ", dataSourceName=" + dataSourceName + ", dataSourceURL="
				+ dataSourceURL + ", isThirdParty=" + isThirdParty + ", isMandatory=" + isMandatory + ", code=" + code
				+ ", question=" + question + ", type=" + type + ", ansOptions=" + ansOptions + ", answers=" + answers
				+ ", qOnlyNumeric=" + qOnlyNumeric + ", sqOnlyNumeric=" + sqOnlyNumeric + ", totalQuestion="
				+ totalQuestion + ", attemptedQuestion=" + attemptedQuestion + ", pendingQuestion=" + pendingQuestion
				+ ", typeId=" + typeId + ", instanceId=" + instanceId + ", sno=" + sno + ", instanceCode="
				+ instanceCode + "]";
	}

}