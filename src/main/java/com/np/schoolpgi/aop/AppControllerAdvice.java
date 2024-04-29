package com.np.schoolpgi.aop;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.LRUMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.np.schoolpgi.constants.AppConstants;
import com.np.schoolpgi.util.StringUtils;

@Component
@Aspect
public class AppControllerAdvice {
	private static final String SIGNATURE_HEADER_KEY = "signature";
	private static final String REQUEST_TIMESTAMP_KEY = "timestamp";
	private static final Map<String, String> methodNameMap = new HashMap<String, String>();
	private static final LRUMap<String, String> LRUMAP = new LRUMap<>(10000);
//	private static final String API_SIGNATURE = "api.signature";
	final static Logger LOGGER = LogManager.getLogger(AppControllerAdvice.class);

	@Value("${signature.validate}")
	private boolean singnatureValidate;

	@Pointcut("execution(* com.np.schoolpgi.controller.*.*(..))")
	public void controllerMethods() {
	}

//	@Pointcut("execution(@com.np.schoolpgi.aop.CheckSignature * *(..))")
//	public void signatureValidation() {
//	}

	@Before("controllerMethods()")
	public void signatureCheckerBeforeAdvice(JoinPoint jp) throws Throwable {
		System.out.println("==BEFORE==From Before Advice on Check Signature : "
				+ jp.getSignature().getDeclaringTypeName() + ",Signature Name :" + jp.getSignature().getName()+", Signature :"+jp.getSignature());

		if (singnatureValidate) {
			final Object[] args = jp.getArgs();
			System.out.println("args Length :" + args.length);
//			ContentCachingRequestWrapper request = getWrapper((ProceedingJoinPoint) jp);
//			System.out.println(getRequestBody(request));
			String methodName = null;
			methodNameMap.put(jp.getSignature().getName().toString(),jp.getSignature().toString());
			if (args != null && args.length >= 3) {
				try {
					@SuppressWarnings("rawtypes")
					final Map httpHeaders = (Map) args[0];
					final String timeStamp = (String) httpHeaders.get(REQUEST_TIMESTAMP_KEY);
					if (!validTimeStatmp(timeStamp)) {

						LOGGER.info("Notification :: :: Timestamp Invalid:" + timeStamp);
						//LOGGER.info("-----jp.getSignature() "+jp.getSignature());
						throw new SignatureValidationFailed("Bad Request - Signature Validation Failed : ",
								getMethodName(jp.getSignature()));
					}
					
//					final String objectDto = getRequestBody(request);
//					final String inServiceParam = (String) args[1];
//					final String inClientParam = (String) args[2];
					//LOGGER.info("#######33jp.getSignature() "+jp.getSignature());
					methodName = getMethodName(jp.getSignature());
					final String signature = (String) httpHeaders.get(SIGNATURE_HEADER_KEY);
					LOGGER.info("RequestHeader Signature - "+signature);
					final String key = methodName.hashCode() + timeStamp;
					LOGGER.info("key key = methodName.hashCode() + timeStamp- "+key);
					if (LRUMAP.containsKey(key)) {
						if (StringUtils.equals(signature, LRUMAP.get(key))) {

							// change_vishal
							LOGGER.error("Notification :: "  
									+ " :: Duplicate request with same signature :" + timeStamp);

							throw new SignatureValidationFailed(
									"Bad Request - Signature Validation Failed :" + getMethodName(jp.getSignature()));
						}
					}
					if(LRUMAP.containsValue(signature)) {
						LOGGER.error("Notification :: "  
								+ " :: Duplicate request with same signature :" + timeStamp);

						throw new SignatureValidationFailed(
								"Bad Request - Signature Validation Failed :" + getMethodName(jp.getSignature()));
					}
					
//					System.out.println("Given Signature:" + signature + ",Signature Param:" + methodName + ","
//							+ inServiceParam + "," + inClientParam + "," + timeStamp + "," + objectDto);

//					if (inClientParam.equalsIgnoreCase("FileUploaded")) {
//						System.out.println("signature bypassed");
//					} else if (!SignatureUtil.isSignatureValid(signature, methodName, inServiceParam, inClientParam,
//							timeStamp, objectDto)) {
//						throw new SignatureValidationFailed(
//								"Bad Request - Signature Validation Failed : " + getMethodName(jp.getSignature()));
//					} else {
//						System.out.println("signature match");
//					}
					LRUMAP.put(key, signature);
				} catch (Exception ex) {

					// change_vishal
					LOGGER.error(
							"Notification :: " + "AppControllerAdvice_signatureCheckerBeforeAdvice :: Exception :: "
									 
									+ " :: Exception validating signature -" + ex);

					throw ex;
				}
			} else {

				// change_vishal
				LOGGER.error("Notification :: " + "AppControllerAdvice_signatureCheckerBeforeAdvice :: Exception :: "
						 
						+ ":: Expecting signatures on method not having the required number of arguments");

				throw new SignatureValidationFailed("Bad Request - Signature Validation Failed");
			}
		}
	}

//	@Around("controllerMethods()")
//	public Object timingAroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
//		System.out.println(pjp.getSignature());
//		String methodName = getMethodName(pjp.getSignature());
//		System.out.println(methodName);
//		if (StringUtils.isNotBlank(methodName)) {
//			ThreadContext.put("PARAM3", methodName);
//		}
//		StopWatch sw = new StopWatch();
//		sw.start(methodName);
//		Object[] args = pjp.getArgs();
//		System.out.println("########## " + args.length);
//		System.out.println("&$&$&$&$------------" + args[0] + "--------------" + args[1] + "--------------------"
//				+ args[2] + "----------" + args[3] + "---------" + args[4] + "------" + args[5] + "-------------"
//				+ args[6]);
//		if (args != null && args.length >= 3) {
//			String clientIP = null;
//			Object payload = args[3];
//			Object api = methodName;
//			ThreadContext.put("api", api.toString());
//			String user = null, user1 = null;
//
//			user = SafeFieldUtils.readField(payload, "notification_type", true, String.class);
//			if(StringUtils.isValidObj(user))
//			{
//				if (user.equalsIgnoreCase("sms")) {
//					user1 = SafeFieldUtils.readField(payload, "mobileNo", true, String.class);
//				}
//				if (user.equalsIgnoreCase("email")) {
//					user1 = SafeFieldUtils.readField(payload, "emailToList", true, String.class);
//				}
//			}
//
//			if (StringUtils.isNotBlank(user)) {
//				ThreadContext.put("PARAM", user);
//			}
//			if (StringUtils.isNotBlank(user1)) {
//				ThreadContext.put("PARAM1", user1);
//			}
//		}
//		System.out.println("===============AROUND - BEFORE================ From Around Advice : "
//				+ pjp.getSignature().getDeclaringTypeName() + " " + pjp.getSignature().getName());
//		if (LOGGER.isDebugEnabled()) {
//			int i = 1;
//			for (Object o : pjp.getArgs()) {
//				System.out.println("Args [ " + i++ + "]" + o.toString());
//			}
//		}
//		Object retVal = pjp.proceed();
//		sw.stop();
//		System.out.println("===============AROUND - AFTER================" + sw);
//
//		ThreadContext.remove("PARAM");
//		ThreadContext.remove("PARAM1");
//		ThreadContext.remove("api");
//		return retVal;
//	}

	/*
	 * @Around("controllerMethods()") public Object timingAroundAdvice(
	 * ProceedingJoinPoint pjp ) throws Throwable { System.out.println(
	 * pjp.getSignature() ); String methodName = getMethodName( pjp.getSignature()
	 * ); System.out.println( methodName ); if ( StringUtils.isNotBlank( methodName
	 * ) ) { MDC.put( "PARAM3", methodName ); } StopWatch sw = new StopWatch();
	 * sw.start( methodName ); Object[] args = pjp.getArgs(); if ( args != null &&
	 * args.length >= 3 ) { String clientIP = null; Object payload = args[3]; if (
	 * args.length >= 5 ) { HttpServletRequest request = (HttpServletRequest)
	 * args[4]; clientIP = request.getRemoteAddr(); } String user =
	 * SafeFieldUtils.readField( payload, "user", true, String.class ); if (
	 * StringUtils.isBlank( user ) ) { user = SafeFieldUtils.readField( payload,
	 * "emailId", true, String.class ); } if ( StringUtils.isNotBlank( user ) ) {
	 * MDC.put( "PARAM1", user ); } if ( StringUtils.isNotBlank( clientIP ) ) {
	 * MDC.put( "PARAM2", clientIP ); } } System.out.println(
	 * "===============AROUND - BEFORE================ From Around Advice : " +
	 * pjp.getSignature().getDeclaringTypeName() + " " +
	 * pjp.getSignature().getName() ); if ( LOGGER.isDebugEnabled() ) { int i = 1;
	 * for ( Object o : pjp.getArgs() ) { System.out.println( "Args [ " + i++ + "]"
	 * + o.toString() ); } } Object retVal = pjp.proceed(); sw.stop();
	 * System.out.println( "===============AROUND - AFTER================" + sw );
	 * MDC.remove( "PARAM1" ); MDC.remove( "PARAM2" ); MDC.remove( "PARAM3" );
	 * return retVal; }
	 */
	private boolean validTimeStatmp(String inTimeStamp) {
		long timeStamp = StringUtils.numericValue(inTimeStamp);
		System.out.println("Calendar.getInstance().getTimeInMillis() - "
		+Calendar.getInstance().getTimeInMillis()+"---timestamp - "+timeStamp);
		System.out.println("Subtract of timestamp - "+(Calendar.getInstance().getTimeInMillis() - timeStamp));
		System.out.println("AppConstants.SIGNATURE_TIME_VALIDITY - "+AppConstants.SIGNATURE_TIME_VALIDITY);
		if (timeStamp <= 0 || (Calendar.getInstance().getTimeInMillis() - timeStamp > (AppConstants.SIGNATURE_TIME_VALIDITY))) {
			return false;
		}
		return true;
	}

	private static String getMethodName(Signature signature) {
		String methodName = methodNameMap.get(signature.getName().toString());
//        String methodName = signature.getName();
		System.out.println(signature.getName() + "method Name::" + methodName);
		if (methodName == null) {
			final RequestMapping requestMappingAnnotation = AnnotationUtils
					.findAnnotation(((MethodSignature) signature).getMethod(), RequestMapping.class);
			final String[] value = requestMappingAnnotation.value();
			System.out.println("Method Name >>>>>" + methodName);
			methodName = value[0];
			if (StringUtils.startsWith(methodName, "/")) {
				methodName = StringUtils.removeStart(methodName, "/");
				if (StringUtils.contains(methodName, "/")) {
					methodName = StringUtils.split(methodName, "/")[0];
				}
			}
			if (StringUtils.isNotBlank(methodName)) {
				methodNameMap.put(signature.getName(), methodName);
			}
		}
		return methodName;
	}

	@SuppressWarnings("unused")
	private String getRequestBody(final ContentCachingRequestWrapper wrapper) {
		String payload = null;
		if (wrapper != null) {
			byte[] buf = wrapper.getContentAsByteArray();
			if (buf.length > 0) {
				try {
					int maxLength = buf.length;
					payload = new String(buf, 0, maxLength, wrapper.getCharacterEncoding());
				} catch (UnsupportedEncodingException e) {

					// change_vishal
					LOGGER.error("Notification :: " + "AppControllerAdvice_getRequestBody :: Exception :: "
							  + " :: UnsupportedEncoding.", e);

				}
			}
		}
		return payload;
	}

	@SuppressWarnings("unused")
	private ContentCachingRequestWrapper getWrapper(ProceedingJoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		ContentCachingRequestWrapper request = null;
		for (Object arg : args) {
			if (arg instanceof ContentCachingRequestWrapper) {
				request = (ContentCachingRequestWrapper) arg;
				break;
			}
		}
		return request;
	}
	/*
	 * @AfterReturning(pointcut =
	 * "execution(* com.np.sankalp.service.controller.AppController.*(..))",
	 * returning = "retVal") public void afterReturningAdvice( JoinPoint jp, Object
	 * retVal ) { HttpServletRequest request = ( (ServletRequestAttributes)
	 * RequestContextHolder.currentRequestAttributes() ) .getRequest(); String
	 * requestUrl = request.getScheme() + "://" + request.getServerName() + ":" +
	 * request.getServerPort() + request.getContextPath() + request.getRequestURI()
	 * + "?" + request.getQueryString(); Object[] agrs = jp.getArgs(); for ( Object
	 * object : agrs ) { System.out.println( " Object ::: " + object.toString() ); }
	 * System.out.println( " *******************Client IP :: " +
	 * request.getRemoteAddr() ); System.out.println(
	 * " *******************Method Signature: " + jp.getSignature() );
	 * System.out.println( " *******************Returning:" + retVal.toString() );
	 * System.out.println( " *******************Request Url : " + requestUrl ); }
	 */
}
