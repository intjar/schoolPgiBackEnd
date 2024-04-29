//package com.np.schoolpgi.dto.request;
//
//import javax.validation.Valid;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//
////import io.swagger.v3.oas.annotations.Hidden;
////import io.swagger.v3.oas.annotations.media.Schema;
//
//@Valid
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class DataDtoReq2
//{
//    @NotEmpty
////    @Schema(title = "Source Originator", allowableValues =
////    { "enc" }, example = "enc")
//    private String  source          = null;
//    //@Hidden
//    private String  clientIPAddress = null;
//    @NotEmpty
////    @Schema(title = "Unique Txn Id for each request")
//    private String  transactionId   = null;
//    @NotNull
////    @Schema(title = "Logged user's login Id, passed 0 for pre-login APIs")
//    private Integer loginUsrId;
//    private String  paramValue      = null;
//    //@Hidden
//    private String  token           = null;
//    //@Hidden
//    private String  usrNamet        = null;
//    //@Hidden
//    private String  usrTypet        = null;
//    //@Hidden
//    private String  usrLevelt       = null;
//    //@Hidden
//    private String  reqOrigin       = null;
//    private Integer recordId;
//    //@Hidden
//    private Integer usrParentIdt    = null;
//    public String getTransactionId()
//    {
//        return transactionId;
//    }
//
//    public void setTransactionId( String transactionId )
//    {
//        this.transactionId = transactionId;
//    }
//
//    public Integer getLoginUsrId()
//    {
//        return loginUsrId;
//    }
//
//    public void setLoginUsrId( Integer loginUsrId )
//    {
//        this.loginUsrId = loginUsrId;
//    }
//
//    public String getParamValue()
//    {
//        return paramValue;
//    }
//
//    public void setParamValue( String paramValue )
//    {
//        this.paramValue = paramValue;
//    }
//
//    public String getToken()
//    {
//        return token;
//    }
//
//    public void setToken( String token )
//    {
//        this.token = token;
//    }
//
//    public String getSource()
//    {
//        return source;
//    }
//
//    public void setSource( String source )
//    {
//        this.source = source;
//    }
//
//    public String getClientIPAddress()
//    {
//        return clientIPAddress;
//    }
//
//    public void setClientIPAddress( String clientIPAddress )
//    {
//        this.clientIPAddress = clientIPAddress;
//    }
//
//    public String getUsrNamet()
//    {
//        return usrNamet;
//    }
//
//    public void setUsrNamet( String usrNamet )
//    {
//        this.usrNamet = usrNamet;
//    }
//
//    public String getUsrTypet()
//    {
//        return usrTypet;
//    }
//
//    public void setUsrTypet( String usrTypet )
//    {
//        this.usrTypet = usrTypet;
//    }
//
//    public String getUsrLevelt()
//    {
//        return usrLevelt;
//    }
//
//    public void setUsrLevelt( String usrLevelt )
//    {
//        this.usrLevelt = usrLevelt;
//    }
//
//    public String getReqOrigin()
//    {
//        return reqOrigin;
//    }
//
//    public void setReqOrigin( String reqOrigin )
//    {
//        this.reqOrigin = reqOrigin;
//    }
//
//    public Integer getRecordId()
//    {
//        return recordId;
//    }
//
//    public void setRecordId( Integer recordId )
//    {
//        this.recordId = recordId;
//    }
//    
//    public Integer getUsrParentIdt()
//    {
//        return usrParentIdt;
//    }
//
//    public void setUsrParentIdt( Integer usrParentIdt )
//    {
//        this.usrParentIdt = usrParentIdt;
//    }
//
//    @Override
//    public String toString()
//    {
//        return "DataDtoReq [" + ( source != null ? "source=" + source + ", " : "" )
//                + ( clientIPAddress != null ? "clientIPAddress=" + clientIPAddress + ", " : "" )
//                + ( transactionId != null ? "transactionId=" + transactionId + ", " : "" )
//                + ( loginUsrId != null ? "loginUsrId=" + loginUsrId + ", " : "" )
//                + ( paramValue != null ? "paramValue=" + paramValue + ", " : "" )
//                + ( token != null ? "token=" + token + ", " : "" )
//                + ( usrNamet != null ? "usrNamet=" + usrNamet + ", " : "" )
//                + ( usrTypet != null ? "usrTypet=" + usrTypet + ", " : "" )
//                + ( usrLevelt != null ? "usrLevelt=" + usrLevelt + ", " : "" )
//                + ( reqOrigin != null ? "reqOrigin=" + reqOrigin + ", " : "" )
//                + ( recordId != null ? "recordId=" + recordId + ", " : "" )
//                + ( usrParentIdt != null ? "usrParentIdt=" + usrParentIdt : "" ) + "]";
//    }
//}
