package com.np.schoolpgi.constants;

public enum Regex {
	
	PATTERN_IPV4("IPV4","^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$","error.pattern.ip.v4"), 
    PATTERN_CAPS_TEXT_3_28("CAPS_TEXT_3_28","[A-Za-z]{3,28}", "error.pattern.caps.text.3.28"), 
    PATTERN_CAPS_SPACE_TEXT_3_28("CAPS_SPACE_TEXT_3_28","[A-Za-z\\s]{3,28}", "error.pattern.caps.text.3.28"), 
    PATTERN_CAPS_TEXT_3_128("CAPS_TEXT_3_128","[A-Za-z]{3,128}", "error.pattern.caps.text.3.128"),
    PATTERN_CAPS_SPACE_DOT_DASH_3_28("PATTERN_CAPS_SPACE_DOT_DASH_3_28","[A-Z0-9\\s.-]{3,28}", "error.pattern.caps.space.dot.dash.3.28"),
    PATTERN_PASSWORD("PASSWORD","((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,15})","error.password.format"),
    PATTERN_ALPHA_NUMERIC_SPACE_3_50("ALPHA_NUMERIC_SPACE_3_50","[a-zA-Z0-9_\\s]{3,50}","error.pattern.alphanumeric.3.50"),
    PATTERN_ALPHA_NUMERIC_SPACE_DASH_3_30("ALPHA_NUMERIC_SPACE_DASH_3_30","[a-zA-Z0-9-\\s]{3,30}","error.pattern.alphanumeric.space.dash.3.30"),
    PATTERN_ALPHA_NUMERIC_SPACE_DASH_3_28("ALPHA_NUMERIC_SPACE_DASH_3_28","[a-zA-Z0-9-.\'\\s()/&]{3,28}","error.pattern.alphanumeric.space.dash.3.28"),
    PATTERN_EMAIL("EMAIL","^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$","error.emailId.pattern"),
    PATTERN_ALT_EMAIL("EMAIL","^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$","error.altEmailId.pattern"),
    PATTERN_NUMERIC_0_23("NUMERIC_0_23","[0-9]|[1][0-9]|[2][0-3]","error.pattern.numeric.0.23"),
    PATTERN_REAL_NUMBER("REAL_NUMBER","^([0-9]*){0,}(\\.)*([0-9*]{0,})$","error.pattern.real.number"),
    PATTERN_PASSPORT_NUMBER("PASSPORT_NUMBER","[0-9a-zA-Z]{7,15}","error.passport.number"),
    PATTERN_VALIDATE_PAN_NO("VALIDATE_PAN_NO","[A-Za-z]{5}[0-9]{4}[A-Za-z]{1}","error.empty.pan.number"),
    PATTERN_VALIDATE_HEXADECIMAL("VALIDATE_HEXADECIMAL","[0-9a-f]{4}[.][0-9a-f]{4}[.][0-9a-f]{4}","error.pattern.haxadecimal"),
    PATTERN_VALIDATE_FILE_TYPE("VALIDATE_FILE_TYPE","^.*(.xls|.xlsx)$","error.invalid.file.type"),
    PATTERN_FILE_DEPOSIT("FILE_DEPOSIT","^NEXTRATELERMCOLRECON.*(.xls|.xlsx|.csv|.CSV)$","invalid.deposit.file"),
    PATTERN_FILE_CLEARANCE("FILE_CLEARANCE","^NEXTRATELECLNTASCDNL.*(.xls|.xlsx|.csv|.CSV)$","invalid.deposit.file"),
    PATTERN_DOUBLE_VALUE("DOUBLE_VALUE","^[0-9.]*$","invalid.decimal.value"),
    PATTERN_DATE_VALUE("DATE_VALUE","^[0-9/]*$","invalid.date.value"),
    PATTERN_CRFID("CRFID","RA[0-9]{6}|EA[0-9]{6}",""),
    PATTERN_CAPS_TEXT_3_200("CAPS_TEXT_3_200","[a-zA-Z0-9-.\'\\s()/&]{3,200}", "error.pattern.caps.text.3.200"),
    PATTERN_TEXT_3_50("TEXT_3_50","[A-Za-z]{3,50}", "error.pattern.caps.text.3.50"),
    PATTERN_FOR_CUSTOMER_NAME("VALIDATE_CUSTOMER_NAME","^([a-zA-Z'-]*)(.){0,1}([ a-zA-Z'-]*)$","error.pattern.for.customer.name"),
    PATTERN_CAPS_TEXT_3_45("CAPS_TEXT_3_45","[A-Z.\'\\s]{3,45}", "error.pattern.caps.text.3.45"), 
    PATTERN_CAPS_TEXT_3_50("CAPS_TEXT_3_50","[A-Z.\'\\s]{3,50}", "error.pattern.caps.text.3.50"),
    PATTERN_DECIMAL_AMOUNT("DECIMAL_AMOUNT","([0-9]{1,8})((\\.[0-9]{1,2})?)", "error.pattern.decimal.amount"),    
    PATTERN_ALPHA_NUMERIC_SPACE_DASH_OTHERS_3_50("ALPHA_NUMERIC_SPACE_DASH_OTHERS_3_50","[A-Za-z0-9-.\'\\s()/&]{3,50}", "error.pattern.alpha.numeric.space.dash.others.3.50"),
    PATTERN_CID("CID","[0-9]{7,15}",""),
    PATTERN_CRF_CUSTOMER_ID("CRF_CUSTOMER_ID","RA[0-9]{6}|EA[0-9]{6}|([0-9]{7,15})","error.invalid.crf.customer.id"),
    PATTERN_FACEBOOK_USER_ID("FACEBOOK_USER_ID","(((https?://)?(www\\.)?facebook\\.com/))?(.*/)?([a-zA-Z0-9.]*)($|\\?.*)","error.facebook.id"),
    PATTERN_TWITTER_USER_ID("TWITTER_USER_ID","((https?://)?(www\\.)?twitter\\.com/)?(@|#!/)?([A-Za-z0-9_]{1,15})(/([-a-z]{1,20}))?","error.twitter.id"),
    PATTERN_LINKEDIN_USER_ID("LINKEDIN_USER_ID","(https?:\\/\\/)?(www\\.)?linkedin\\.com\\/in\\/[A-z0-9_-]+\\/?","error.Linkedin.id"),    
    PATTERN_URL_VALUE("URL_VALUE","((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w\\-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)","error.invalid.URL"),
   
    //Regex
    CODE_VALUE("CODE_VALUE","([1-9]{1}[0-9]{2,5}+)","error.invalid.CODE"),
    
    
    ;
    
    private String             patternName         = null;
    private String             pattern             = null;
    private String             errorKey            = null;
    public final static String EMAIL_VALIDATION    = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public final static String PHONE_VALIDATION    = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";
    public final static String TWITTER_VALIDATION  = "((https?://)?(www\\.)?twitter\\.com/)?(@|#!/)?([A-Za-z0-9_]{1,15})(/([-a-z]{1,20}))?";
    public final static String LINKEDIN_VALIDATION = "(https?:\\/\\/)?(www\\.)?linkedin\\.com\\/in\\/[A-z0-9_-]+\\/?";
    public final static String LANDLINE_VALIDATION =  "^[0-9]\\d{2,4}-\\d{6,8}$";
    public final static String NAME_VALIDATION =  "^([A-Za-z]+([a-zA-Z0-9 /\\)\\(._-])*)$";
    public final static String DOMAIN_SUB_DOMAIN_NAME_VALIDATION =  "^[A-Za-z]+([a-zA-Z0-9 /._&,%()\\-])*$";
//    public final static String CHAR_VALIDATION =  "^([A-Za-z]+([._-])*)$";
    public final static String CODE_VALIDATION =  "^([0-9]{3,6})$";
    public final static String PASSWORD_VALIDATION =  "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,20}$";
    public final static String USER_NAME =  "^(?=.*?[a-z])(?=.*?[0-9])?(?=.*?[-._])?.{6,256}$";
    public final static String QUESTION_VALIDATION =  "^((?!.*<[^>]+>).*)$";
    
   
    

    private Regex(String inPatternName, String inPattern, String inErrorKey )
    {
        this.patternName = inPatternName;
        this.pattern = inPattern;
        this.errorKey = inErrorKey;
    }

    public String getPattern()
    {
        return pattern;
    }

    public String getErrorKey()
    {
        return errorKey;
    }
    
    public String getPatternName()
    {
        return patternName;
    }

    public static String getErrorKeyByPattern( String inPattern )
    {
        String strErrorKey = null;
        for ( Regex crmRegex : Regex.values() )
        {
            if ( crmRegex.getPattern().equals( inPattern ) )
            {
                strErrorKey = crmRegex.getErrorKey();
                break;
            }
        }
        return strErrorKey;
    }
    public static String getErrorKeyByPatternName( String inPatternName )
    {
        String strErrorKey = null;
        for ( Regex crmRegex : Regex.values() )
        {
            if ( crmRegex.getPatternName().equals( inPatternName ) )
            {
                strErrorKey = crmRegex.getErrorKey();
                break;
            }
        }
        return strErrorKey;
    }
    public static Regex getPatternByPatternName( String inPatternName )
    {
        for ( Regex crmRegex : Regex.values() )
        {
            if ( crmRegex.getPatternName().equals( inPatternName ) )
            {
                return crmRegex;
            }
        }
        return null;
    }
}
