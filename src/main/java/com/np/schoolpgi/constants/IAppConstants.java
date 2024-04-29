package com.np.schoolpgi.constants;

import java.text.SimpleDateFormat;

public interface IAppConstants {
	
	int              ACTIVE                         = 1;
    String           EMAIL_USERNAME                 = "email.username";
    String           EMAIL_PASSWORD                 = "email.password";
    String           EMAIL_SMTP_AUTH                = "email.smtp.auth";
    String           EMAIL_SMTP_HOST                = "email.smtp.host";
    String           EMAIL_SMTP_PORT                = "email.smtp.port";
    String           EMAIL_DEFAULT_TO               = "email.defaultTO";
    String           EMAIL_DEFAULT_CC               = "email.defaultCC";
    String           EMAIL_DEFAULT_BCC              = "email.defaultBCC";
    String           EMAIL_SEND_ALERTS              = "email.sendEmailAlerts";
    String           EMAIL_CONNECTION_TIMEOUT       = "email.smtp.connectiontimeout";
    String           EMAIL_SOCKET_TIMEOUT           = "email.smtp.sockettimeout";
    String           EMAIL_NOTIFY_ERROR_MAILID      = "email.notify.error.mailId";
    String           EMAIL_SMTP_STARTTLS_ENABLE     = "mail.smtp.starttls.enable";
    String           START                          = "START";
    String           END                            = "END";
    String           DASH                           = "-";
    String           UNDERSCORE                     = "_";
    String           DAY_START_TIME                 = " 00:00:00";
    String           DAY_END_TIME                   = " 23:59:59";
    String           EMAIL_SMS_KEY_LIST             = "message.keys";
    String           EMAIL_SMS_TEMPLATE_FILE_NAME   = "email.sms.templates.file";
    String           SPACE                          = " ";
    int              inactive                       = 0;
    String           DISPLAY_POJO_SEPEARATOR        = ":::::";
    String           POJO_SEPEARATOR                = ",,,,,";
    String           DISPLAY_POJO_SEPEARATOR_OTHR   = "#####";
    String           pattern                        = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat               = new SimpleDateFormat( pattern );
    String           UPLOAD_FILE_SIZE_MAX           = "maxUploadFileSize.";
    String           REPORT_UPLOAD_FILE_SIZE_MIN    = "minReportUploadFileSize.";
    String           UPLOAD_FILE_SIZE_MIN           = "minUploadFileSize.";
    String           API_SIGNATURE                  = "api.signature";
    String           DOCUMENT_UPLOAD                = "documentUpload";
    String           AIM_UTIL_PROP_FILE_NAME        = "aim-common-utils.properties";
    byte             OTP_VALIDATE                   = 2;
    String           OTP_EXPIRE_TIME                = "otpExpireTime";
    String           OTP_VERIFICATION_TIME_INTERVAL = "otpVerificationTime";
    String           RESET_PASS_TIME                = "resetPasswordTime";
    String           OTP_MAX_THRESHOLD_TIME         = "maxThresholdTime";
    String           DOT                            = ".";
    String           MAX_UPLOAD_SIZE                = "maxUploadSize";
    String           MIN_UPLOAD_SIZE                = "minUploadSize";
    String           BACK_SLASH                     = "/";
    String           DELETE                         = "delete";
    String           CHANAGE_STATUS                 = "changeStatus";
    String           EVENT_LIST_NOT_TO_DISPLAY      = "event.list.not.to.display";
    String           SR_TYPE_CONTACT                = "SR_CONTACT";
    String           SR                             = "SR";
    String           CR                             = "CR";
    String           FILEUPLOAD                     = "fileupd.";
    String           MANDATRY                       = ".mandatry";
    String           COUNT_MIN_STR                  = ".count.min";
    String           COUNT_MAX_STR                  = ".count.max";
    String           UPDATE_PROJECT                 = "updateProject";
    String           UPDATE                         = "update";
    String           PROJECT_SORT_BY_TYPE           = "type";
    int              FILE_TEXT_MAX_LENGTH           = 99;
    String           ALL_STATE                      = "All State";
    String           OPR_TYPE_GNRL                  = "gnrl";
    String           FILE                           = "file";
    //Proposal activity PDF
    String           MAIN_HEADER_TITLE              = "header.title";
    String           FINANCIAL_YEAR                 = "Financial Year: ";
    String           PROPOSAL_CODE                  = "Proposal Code: ";
    String           PROPOSAL_SUBMISSION_DATE       = "Submission Date: ";
    String           CREATION_DATE                  = "Date: ";
    String           COMPONENT                      = "Component ";
    String           LACS                           = "Lacs";
    String           SUB_BUDGET                     = "Sub Total";
    String           GRAND_TOTAL_BUDGET             = "Grand Total Budget";
    String           COVER_PHOTO_LOCATION_PATH      = "cover.photo.location.path";
    String           FILE_NAME                      = "fileName";
    // Excel 
    String           NA                             = "NA";
    int              EXCEL_COL_WIDTH_BASE           = 255;
    String           SUMMARY                        = "summary";
    String           OPR_TYPE_PROPOSAL              = "proposal";
    String           STATE_USER_2                   = "STATE2";
    String           SERVICE_METHOD_NAME            = "service.method.name";
    String           PERCENTAGE                     = "%";
    String           SUB_CATEGORY                   = "SC";
    String           PROJECT_SUMMARY                = "project.summary";
    String           ACTIVITY_PROGRESS              = "activity.progress";
    String           TOTAL_MERGE                    = "total.merge";
    String           QUARTER_NUMBER                 = "Quarter: ";
    String           FORMAT_FINANCIAL_YEAR          = "1 April {0} to 31 March 20{1}";
    Short            TOTAL_POPULATION_YOUTH         = 18;
    Short            TOTAL_POPULATION_SC_YOUTH      = 19;
    Short            PERC_SC_YOUTH                  = 20;
    Short            TOTAL_POPULATION_YOUTH_2       = 25;
    Short            TOTAL_POPULATION_ST_YOUTH      = 26;
    Short            PERC_ST_YOUTH                  = 27;
    String           ACTIVITY_DELETED_REMARK        = "Activity deleted by Ministry\r\n";
    int              CAPTCHA_MIN_SIZE               = 7;
    int              CAPTCHA_MAX_SIZE               = 7;
    String           MOBILE_NUMBER_PRIFIX           = "+91";
    String           STR_SUCCESS                    = "Success";
    String           STR_FAIL                       = "Fail";
    /**
     * KCI Constant Start
     */
    String           MSDE_EMAIL_ID                  = "msde.email.id";
    byte             KCI_SENT_SUCCESS               = 1;
    byte             KCI_SENT_FAIL                  = 2;
    byte             KCI_SENT_NA                    = 0;
    String           EDM_KEY_CREATE_FILE_ERROR      = "write.file.error";
    String           EDM_KEY_FORGET_PASSWORD        = "forget.password";
    String           EDM_KEY_SR_CREATION            = "sr.creation";
    String           EDM_KEY_DSDP_USER_REGISTRATION = "dsdp.user.registration";
    /**
     * KCI Constant END
     */
    String           ERROR                          = "Error";
    String           ARCHIVED                       = "Archived";
    String           ONGOING                        = "Ongoing";
    String           ACTIVESTR                      = "Active";
    String           DEFAULT_IP                     = "0.0.0.0";
    /**
     * Encryption related
     */
    String           DEV                            = "dev";
    String           AENC                           = "aenc";
    String           IENC                           = "ienc";
    String           ENC                            = "enc";

}
