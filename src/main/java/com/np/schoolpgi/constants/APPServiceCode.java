package com.np.schoolpgi.constants;

public enum APPServiceCode {

	APP001("APP001", "Your request has been processed successfully"),
	APP002("APP002", "Oops! It seems your credentials did not match with our records"),
	APP003("APP003", "Email template not found"),
	APP004("APP004", "Image can not be uploaded, please provide valid path"),
	APP005("APP005", "Unable to upload image, please try again later"), 
	APP996("APP996", "No record found"), 
	APP997("APP997", "Invalid Request, Validation Error"),
	APP998("APP998", "Sorry! Security measures are not being followed"),
	APP999("APP999", "Unable to process your request, please try later"),
	// user management
	APP006("APP006", "Invalid Username"),
	APP007("APP007", "Invalid Password"),
	
	APP013("APP013", "User with same username already exists"), 
	APP014("APP014", "User registered  successfully"),
	APP015("APP015", "User is not registered"), 
	APP016("APP016", "Email is not sent. Please try again later"),
	APP017("APP017", "Email ID Not Found, please provide"), 
	APP018("APP018", "Invalid OTP type"),
	APP019("APP019", "Unable to upload file. Please try again later"),
	APP020("APP020", "Please provide valid module type"), 
	APP021("APP021", "Invalid OTP"),
	APP022("APP022", "Invalid Request, Please provide atleast one image"),
	APP023("APP023", "Invalid Request, You can upload max 3 images"),
	APP025("APP025", "Invalid files uploaded"),
	APP026("APP026", "Sorry! You exceed limit to request OTP."), 
	APP027("APP027", "Invalid scheme provided"),
	APP028("APP028", "Reset password time is expired"), 
	APP029("APP029", "Invalid Password"),
	APP030("APP030", "Reset Password should be same to confirm password."),
	APP031("APP031", "Please provide valid section type"), 
	APP032("APP032", "Please provide valid content type"),
	APP033("APP033", "Please provide valid file content"), 
	APP034("APP034", "Please provide valid file"),
	APP035("APP035", "Please upload valid file type"), 
	APP036("APP036", "Please provide valid size file"),
	APP037("APP037", "Please upload valid file type"), 
	APP038("APP038", "Unknown Error"),
	APP039("APP039", "User has been logged in successfully"),
	APP040("APP040", "This email id does not exist"),
	APP041("APP041", "OTP sent to your registered email"),
	
	APP012("APP012", "OTP can not sent"),
	APP011("APP011", "OTP verified successfully"),
	APP500("APP500", "Reset password to continue"),
	APP501("APP501", "Please resend OTP"),
	APP502("APP502", "Please check the OTP and try again"),
	APP503("APP503", "Please contact to admin"),
	APP010("APP010", "OTP time expired"),
	APP009("APP009", "Password reset successfully"),
	APP008("APP008", "Password not matched"),
	
	APP067("APP067", "Please upload only xls/xlsx/csv file"),

	APP042("APP042", "Invalid Request / Unauthorized"), 
	APP043("APP043", "Invalid Request"),
	APP044("APP044", "Please provide Remarks"), 
	APP045("APP045", "Please provide valid status"),
	APP046("APP046", "Please provide atleast one filter"),

	// Resource creation
	APP047("APP047", "Please provide Login Id"), 
	APP048("APP048", "Please provide Record Id"),
	APP049("APP049", "Please provide Resource Type"), 
	APP050("APP050", "Please provide project id"),
	APP051("APP051", "You can not upload files for project type video"),
	APP052("APP052", "Invalid operation type provided"), 
	APP053("APP053", "Please provide atleast one filter"),
	APP054("APP054", "Please provide page no and page size"), 
	APP055("APP055", "Page size can not be less than 1"),
	APP056("APP056", "Invalid Request, please provide project details"),
	APP057("APP057", "Invalid Request, please provide valid file to upload"),
	APP058("APP058", "Record already exist"),
	APP504("APP504", "Record is empty."),
	APP505("APP505", "Something went wrong."),
	APP506("APP506", "Id does not exist"),
	APP507("APP507", "Please provide valid source id"),
	APP508("APP508", "UDISE CODE already exist"),
	APP509("APP509", "Please provide valid UDISE CODE."),
	APP510("APP510", "Instance ID does not exist in Instance Master"),
	APP511("APP511", "Please provide valid source ID"),
	APP512("APP512", "Please provide valid Code"),
	APP513("APP513", "Please provide valid Name"),
	APP516("APP516", "QuestionType ID does not exist in QuestionTypeMaster Table"),
	APP517("APP517", "UDISE CODE does not exist in Instance Master"),
	APP518("APP518", "Website data could not be copied"),
	
	//Survey
	APP525("APP525", "Survey Id does not exist in Survey Master."),
	APP526("APP526", "Domain Id does not exist in Domain Master."),
	APP527("APP527", "SubDomain Id does not exist in SubDomain Master."),
	APP528("APP528", "Question Id does not exist in Question Master."),
	APP529("APP529", "Please provide valid survey id."),
	APP530("APP530", "Survey Id does not exist"),
	APP531("APP531", "Please provide valid id."),


	//Domain
	APP535("APP535", "Domain Id does not exist in SurveyQuestionMapping"),
	APP536("APP536", "Sub Domain Id does not exist in SurveyQuestionMapping"),
	APP537("APP537", "Question Id does not exist in SurveyQuestionMapping"),
	APP538("APP538", "Data Source Id does not exist in Data Source Master"),
	APP539("APP539", "Please provide valid Data Source."),
	APP540("APP540", "This Domain is already assigned to the Survey with Map Question."),
	APP541("APP541", "This Sub Domain is already assigned to the Survey with Map Question."),
	APP542("APP542", "The Map Question of this Survey Id does not exist."),
	APP543("APP543", "The survey data entry list is empty."),
	APP544("APP544", "Invalid Survey with Empty Header And Details."),
//	APP535("APP535", "Domain Id does not exist in SurveyQuestionMapping"),
	
	

	APP059("APP059", "Please provide remarks"), 
	APP060("APP060", "Invalid status"),
	APP061("APP061", "Invalid current password"),
	APP065("APP065", "Unauthorize Access"),

	APP087("APP087", "Invalid Captcha Entered"),
	APP096("APP096", "User Level is Invalid"),
	APP098("APP098", "Unable to upload file"),

	// Incubator Management
	APP099("APP099", "Center Name is Duplicate"), 
	APP100("APP100", "Email ID is Duplicate"),
	APP101("APP101", "Mobile number is Duplicate"), 
	APP102("APP102", "Member Name is Duplicate"),

	// Mentor
	APP103("APP103", "Mentor Name is Duplicate"), 
	APP106("APP106", "Organization Name is Duplicate"),
	APP107("APP107", "Social Linkedin  is Duplicate"),
	APP108("APP108", "User with same email already exists"),
	APP109("APP109", "Role not exists"), 
	APP114("APP114", "Role name already exist"),
	APP110("APP110", "User has been successfully updated"),
	APP111("APP047", "Please enter a valid loginId"), 
	APP112("APP112", "Please check input json format"),
	APP113("APP113", "Insert fail, please contact administrator"),
	APP115("APP115", "List of links is not present"),
	APP116("APP116", "Role id mapping inserted successfully"),
	APP117("APP117", "Role id mapping updated successfully"),
	APP118("APP118", "Record is Duplicate"),
	APP119("APP119", "Domain ID does not exist in DomainMaster"),
	
	APP120("APP120", "Some fileds are missing"),
	APP200("APP200", "Name already exist"),
	APP201("APP201", "Code already exist"),
	APP121("APP121", "This username already exist"),
	APP122("APP122", "Question Master ID is not exist in QuestionMaster"),
	APP123("APP123", "Role is not assigned to this user"),
	APP124("APP124", "Level is not assigned to this user"),
	APP125("APP125", "Instance is not assigned to this user"),
	APP126("APP126", "Survey Name of selected year already exist."),
	APP127("APP127", "Survey added  successfully"),
	APP128("APP128", "Enter valid level id"),
	APP129("APP129", "You don't have permission to access this portal; please contact the admin."),
	APP170("APP170", "Refresh token was expired. Please make a new signin request"),
	APP171("APP171", "Please provide valid refresh token."),
	APP172("APP172", " Survey uploaded successfully."),
	APP173("APP173", "User with this email is inactive."),
	APP176("APP176", "User with this username is inactive."),
	APP174("APP174","Please provide loggedInUserId in case of child instance user"),
	APP175("APP175","School level name does not exist in Level Master."),
	APP177("APP177","This role is already assigned to the User."),
	APP179("APP179","This level is already assigned to the Instance."),
	APP178("APP178","School name already exists."),
	
	//Media File
	APP300("APP300","File Uploaded Successfully."),
	APP301("APP301","Category Id does not exist."),
	APP302("APP302","Maximum upload size exceeded"),
	APP303("APP303","Required filed is missing."),
	APP304("APP304","Mismatch input fileds."),
	APP305("APP305","Please upload valid file"),
	APP306("APP306","Survey question validated! Please check your answers."),
	
	//Validation Message
	APP400("APP400","Minimum length should be 3 and Maximum length should be 100"),	//Name length validation
	APP401("APP401","Please enter the valid name"),	//Name format validation
	APP402("APP402","Please enter the valid code"),	//Code format validation
	APP403("APP403","Minimum length should be 3 and Maximum length should be 6")	//Code length validation
	
	;
	
	 public final static String NAME_LENGTH_MESSAGE="Minimum length should be 3 and Maximum length should be 100";
	 public final static String EMAIL_LENGTH_MESSAGE="Minimum length should be 6 and Maximum length should be 256";
	 public final static String NAME_FORMAT_MESSAGE="Please enter the valid name";
	 public final static String CODE_FORMAT_MESSAGE="Please enter the valid code";
	 public final static String USERNAME_FORMAT_MESSAGE="Please enter the valid username";
	 public final static String CODE_LENGTH_MESSAGE="Minimum length should be 3 and Maximum length should be 6";
	 public final static String MUST_CONTAIN="Should not be null";
	 public final static String UDISE_CODE_LENGTH="Minimum and Maximum length should be 11";
	 public final static String QUESTION_LENGTH_MESSAGE="Minimum length should be 3 and Maximum length should be 500";
	 public final static String OPTIONS_LENGTH_MESSAGE="Limitation exceed";
	 public final static String QUESTION_FORMAT_MESSAGE="No html tags allowed";
	 
	 

	String statusCode = null;
	String statusDesc = null;

	private APPServiceCode(String statusCode, String statusDesc) {
		this.statusCode = statusCode;
		this.statusDesc = statusDesc;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public static APPServiceCode getServiceByErrorKey(String errorKey) {
		for (APPServiceCode serviceCode : APPServiceCode.values()) {
			if (equals(serviceCode.getStatusCode(), errorKey)) {
				return serviceCode;
			}
		}
		return APPServiceCode.APP997;
	}
	public static boolean equals(final CharSequence cs1, final CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        }
        if (cs1 == null || cs2 == null) {
            return false;
        }
        if (cs1.length() != cs2.length()) {
            return false;
        }
        if (cs1 instanceof String && cs2 instanceof String) {
            return cs1.equals(cs2);
        }
        // Step-wise comparison
        final int length = cs1.length();
        for (int i = 0; i < length; i++) {
            if (cs1.charAt(i) != cs2.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
