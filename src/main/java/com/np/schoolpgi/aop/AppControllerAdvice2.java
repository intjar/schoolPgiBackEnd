//package com.np.schoolpgi.aop;
//
//import java.io.UnsupportedEncodingException;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.collections4.map.LRUMap;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//
//import com.np.schoolpgi.util.SignatureUtil;
//import com.np.schoolpgi.util.StringUtils;
//
//@Component
//@Aspect
//public class AppControllerAdvice2
//{
//    private static final String                 SIGNATURE_HEADER_KEY  = "signature";
//    private static final String                 REQUEST_TIMESTAMP_KEY = "timestamp";
//    private static final String                 REQUEST_AUTHRZ_KEY    = "Authorization";
//    private static final String                 API_TO_BYPASSED       = "FileUploaded";
//    private static final Map<String, String>    methodNameMap         = new HashMap<String, String>();
//    private static final LRUMap<String, String> LRUMAP                = new LRUMap<>( 10000 );
//    private static final Logger                 LOGGER                = LogManager
//            .getLogger( AppControllerAdvice2.class );
////    @Autowired
////    private JwtTokenUtil2                        jwtTokenUtil;
//    @Value("${signature.validate}")
//    private boolean                             signatureEnabled;
//
//    @Pointcut("execution(* com.np.schoolpgi.controller.*.*(..))")
//    public void controllerMethods()
//    {
//    }
//
//    @Before("controllerMethods()")
//    public void signatureCheckerBeforeAdvice( JoinPoint jp )
//        throws Throwable
//    {
//        LOGGER.info( "===============BEFORE================ From Before Advice on Check Signature : "
//                + jp.getSignature().getDeclaringTypeName() + ",Signature Name :" + jp.getSignature().getName() );
//        final Object[] args = jp.getArgs();
//        LOGGER.info( "args Length :" + args.length );
//        Arrays.stream( args ).forEach( LOGGER::info );
//        ContentCachingRequestWrapper request = getWrapper( (ProceedingJoinPoint) jp );
//        Arrays.stream( getRequestBody( request ) ).forEach( System.out::println );
//        if ( args != null && args.length >= 3 )
//        {
//            try
//            {
//                @SuppressWarnings("rawtypes")
//                final Map httpHeaders = (Map) args[0];
//                final String[] objectDto = getRequestBody( request );
//                final String[] requestAttr = objectDto[0].split( "/" );
//                final String timeStamp = (String) httpHeaders.get( REQUEST_TIMESTAMP_KEY );
//                final String methodName = (String) requestAttr[2];
//                final String inServiceParam = (String) requestAttr[3];
//                final String inClientParam = (String) requestAttr[4];
////                final String token = (String) httpHeaders.get( REQUEST_AUTHRZ_KEY );
////                if ( StringUtils.isValidObj( token ) && !inClientParam.equalsIgnoreCase( API_TO_BYPASSED ) )
////                {
////                    validateLoginRequest( token.substring( 7 ), objectDto[1] );
////                }
//                if ( signatureEnabled )
//                {
//                    if ( !validTimeStatmp( timeStamp ) )
//                    {
//                        LOGGER.error( "Timestamp Invalid:" + timeStamp );
//                        throw new SignatureValidationFailed( "Bad Request - Signature Validation Failed :"
//                                + Arrays.toString( requestAttr ) );
//                    }
//                    final String signature = (String) httpHeaders.get( SIGNATURE_HEADER_KEY );
//                    final String key = Arrays.toString( requestAttr ).hashCode() + timeStamp;
//                    if ( LRUMAP.containsKey( key ) )
//                    {
//                        if ( StringUtils.equals( signature, LRUMAP.get( key ) ) )
//                        {
//                            LOGGER.error( "Duplicate request with same signature:" + timeStamp );
//                            throw new SignatureValidationFailed( "Bad Request - Signature Validation Failed :"
//                                    + Arrays.toString( requestAttr ) );
//                        }
//                    }
//                    LOGGER.info( "Given Signature:" + signature + ",Signature Param:" + methodName + ","
//                            + inServiceParam + "," + inClientParam + "," + timeStamp + "," + objectDto[1] );
//                    if ( inClientParam.equalsIgnoreCase( API_TO_BYPASSED ) )
//                    {
//                        LOGGER.info( "signature bypassed" );
//                    }
//                    else if ( StringUtils.isEmpty( signature ) )
//                    {
//                        throw new SignatureValidationFailed( "Bad Request - Signature Not Found : "
//                                + Arrays.toString( requestAttr ) );
//                    }
//                    else if ( !SignatureUtil.isSignatureValid( signature, methodName, inServiceParam, inClientParam,
//                                                               timeStamp, objectDto[1] ) )
//                    {
//                        throw new SignatureValidationFailed( "Bad Request - Signature Validation Failed : "
//                                + Arrays.toString( requestAttr ) );
//                    }
//                    else
//                    {
//                        LOGGER.info( "signature match" );
//                    }
//                    LRUMAP.put( key, signature );
//                }
//            }
//            catch ( Exception ex )
//            {
//                LOGGER.warn( "Exception validating signature - " + ex, ex );
//                throw ex;
//            }
//        }
//        else
//        {
//            LOGGER.error( "Expecting signatures on method not having the required number of arguments" );
//            throw new SignatureValidationFailed( "Bad Request - Signature Validation Failed" );
//        }
//    }
//
////    private void validateLoginRequest( String reqToken, String requestDto )
////        throws JsonMappingException, JsonProcessingException, CustomException
////    {
////        Integer loginUsert = jwtTokenUtil.getLoginUserIdFromToken( reqToken );
////        DataDtoReq2 dataDtoReq = new ObjectMapper().readValue( requestDto, DataDtoReq2.class );
////        if ( !loginUsert.equals( dataDtoReq.getLoginUsrId() ) )
////        {
////            throw new CustomException( "User Login Id validation failed",
////                                       new Exception( "Login User ID is mismatched, got from token:" + loginUsert
////                                               + " and in request:" + dataDtoReq.getLoginUsrId() ),
////                                       APPServiceCode.APP999 );
////        }
////        LOGGER.info( "validateLoginRequest: Success- got from token:" + loginUsert + " and in request:"
////                + dataDtoReq.getLoginUsrId() );
////    }
//
//    private boolean validTimeStatmp( String inTimeStamp )
//    {
//        long timeStamp = StringUtils.numericValue( inTimeStamp );
//        if ( timeStamp <= 0 || ( Calendar.getInstance().getTimeInMillis() - timeStamp > ( 48*60 * 60 * 1000 ) ) )
//        {
//            return false;
//        }
//        return true;
//    }
//
//    private String[] getRequestBody( final ContentCachingRequestWrapper wrapper )
//    {
//        String payload = null;
//        if ( wrapper != null )
//        {
//            byte[] buf = wrapper.getContentAsByteArray();
//            if ( buf.length > 0 )
//            {
//                try
//                {
//                    int maxLength = buf.length > 5000 ? 5000 : buf.length;
//                    payload = new String( buf, 0, maxLength, wrapper.getCharacterEncoding() );
//                }
//                catch ( UnsupportedEncodingException e )
//                {
//                    LOGGER.error( "UnsupportedEncoding.", e );
//                }
//            }
//        }
//        String URI = wrapper.getRequestURI().toString();
//        System.out.println( wrapper.getRequestURI().toString() );
//        String[] reqDetail =
//        { URI, payload };
//        System.out.println( "payload in AOP:" + payload );
//        return reqDetail;
//    }
//
//    private ContentCachingRequestWrapper getWrapper( ProceedingJoinPoint joinPoint )
//    {
//        Object[] args = joinPoint.getArgs();
//        ContentCachingRequestWrapper request = null;
//        for ( Object arg : args )
//        {
//        	LOGGER.info("arg: "+arg);
//            if ( arg instanceof ContentCachingRequestWrapper )
//            {
//                request = (ContentCachingRequestWrapper) arg;
//                break;
//            }
//        }
//        return request;
//    }
//}
//
