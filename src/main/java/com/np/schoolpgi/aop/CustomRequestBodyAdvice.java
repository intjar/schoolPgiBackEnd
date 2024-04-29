//package com.np.schoolpgi.aop;
//
//import java.io.IOException;
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Map;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpInputMessage;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
//
////import com.np.aim.cu.constants.IAppConstants;
////import com.np.aim.cu.utils.StringUtils;
////import com.np.aim.service.config.JwtTokenUtil;
////import com.np.aim.service.dto.request.mstr.DataDtoReq;
//import com.np.schoolpgi.config.JwtTokenUtil2;
//import com.np.schoolpgi.constants.IAppConstants;
//import com.np.schoolpgi.dto.request.DataDtoReq2;
//import com.np.schoolpgi.util.StringUtils;
//
//import io.jsonwebtoken.ExpiredJwtException;
//
//@ControllerAdvice
//public class CustomRequestBodyAdvice
//    implements
//    RequestBodyAdvice
//{
//    @Value("${domain.url}")
//    private String              domainUrl;
//    @Autowired
//    private JwtTokenUtil2        jwtTokenUtil;
//    private static final Logger LOGGER = LogManager.getLogger( CustomRequestBodyAdvice.class );
//    @Override
//    public Object afterBodyRead( Object body,
//                                 HttpInputMessage inputMessage,
//                                 MethodParameter parameter,
//                                 Type targetType,
//                                 Class<? extends HttpMessageConverter<?>> converterType )
//    {
//        if ( body instanceof DataDtoReq2 )
//        {
//            DataDtoReq2 requestDto = (DataDtoReq2) body;
//            setTokenDetailInReq( inputMessage.getHeaders(), requestDto );
//            return requestDto;
//        }
//        return body;
//    }
//
//    @Override
//    public HttpInputMessage beforeBodyRead( HttpInputMessage inputMessage,
//                                            MethodParameter parameter,
//                                            Type targetType,
//                                            Class<? extends HttpMessageConverter<?>> converterType )
//        throws IOException
//    {
//        return inputMessage;
//    }
//
//    @Override
//    public Object handleEmptyBody( Object body,
//                                   HttpInputMessage inputMessage,
//                                   MethodParameter parameter,
//                                   Type targetType,
//                                   Class<? extends HttpMessageConverter<?>> converterType )
//    {
//        return body;
//    }
//
//    @Override
//    public boolean supports( MethodParameter methodParameter,
//                             Type targetType,
//                             Class<? extends HttpMessageConverter<?>> converterType )
//    {
//        return true;
//    }
//
//    @SuppressWarnings("rawtypes")
//    private void setTokenDetailInReq( Map httpHeaders, DataDtoReq2 requestDto )
//    {
//        if ( StringUtils.isValidObj( httpHeaders ) && httpHeaders.size() > 0 )
//        {
//            // httpHeaders.entrySet().stream().forEach( System.out::println );
//            ArrayList requestTokenHeaderArr = (ArrayList) httpHeaders.get( "Authorization" );
//            ArrayList reqOrigin = (ArrayList) httpHeaders.get( "Origin" );
//            ArrayList remoteIPXForward = (ArrayList) httpHeaders.get( "X-Forwarded-For" );
//            String remoteIP = IAppConstants.DEFAULT_IP; //inHttpRequest.getRemoteAddr();
//            LOGGER.info( "remoteIPXForward:" + remoteIPXForward + ",requestTokenHeaderArr:" + requestTokenHeaderArr
//                    + ",reqOrigin:" + reqOrigin );
//            if ( StringUtils.isValidObj( remoteIPXForward ) )
//            {
//                remoteIP = (String) remoteIPXForward.get( 0 );
//                remoteIP = remoteIP.substring( remoteIP.lastIndexOf( "," ) + 1 ).trim();
//                remoteIP = remoteIP.length() > 25 ? remoteIP.substring( 0, 24 ) : remoteIP;
//            }
//            requestDto.setClientIPAddress( remoteIP );
//            if ( reqOrigin == null )
//            {
//                requestDto.setReqOrigin( domainUrl );
//            }
//            else
//            {
//                requestDto.setReqOrigin( (String) reqOrigin.get( 0 ) );
//            }
//            if ( StringUtils.isValidObj( requestTokenHeaderArr ) )
//            {
//                String requestTokenHeader = (String) requestTokenHeaderArr.get( 0 );
//                if ( requestTokenHeader != null && requestTokenHeader.startsWith( "Bearer " ) )
//                {
//                    String jwtToken = requestTokenHeader.substring( 7 );
//                    requestDto.setToken( jwtToken );
//                    try
//                    {
//                        String[] userInfo = jwtTokenUtil.getUserNameFromToken( jwtToken );
//                        requestDto.setUsrNamet( userInfo[0] );
//                        requestDto.setUsrTypet( userInfo[1] );
//                        requestDto.setUsrLevelt( userInfo[2] );
//                        requestDto.setUsrParentIdt( Integer.parseInt( userInfo[3] ) );
//                        LOGGER.info( "username got from token:" + Arrays.toString( userInfo ) );
//                    }
//                    catch ( IllegalArgumentException e )
//                    {
//                        LOGGER.error( "Unable to get JWT Token", e );
//                    }
//                    catch ( ExpiredJwtException e )
//                    {
//                        LOGGER.warn( "JWT Token has expired" );
//                    }
//                }
//            }
//        }
//    }
//}
//
