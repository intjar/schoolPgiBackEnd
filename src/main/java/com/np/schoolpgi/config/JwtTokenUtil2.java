//package com.np.schoolpgi.config;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Stream;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import com.np.schoolpgi.util.StringUtils;
//
////import com.np.aim.cu.utils.StringUtils;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
//@Component
//public class JwtTokenUtil2
//    implements Serializable, UserDetailsService
//{
//    private static final Logger LOGGER              = LogManager.getLogger( JwtTokenUtil2.class );
//    private static final long   serialVersionUID    = -2550185165626007488L;
//    private static final long   JWT_TOKEN_VALIDITY  = 5 * 60 * 60;
//    private static final String JWT_TOKEN_ISSUER    = "NPGAIM";
//    private static final String JWT_TOKEN_ID        = "54871";
//    private static final String JWT_TOKEN_AUDIENCE  = "W";
//    private static final String LOGIN_USER_ID_FNAME = "loginUserIdt";
//    private static final String USERNAME_FNAME      = "usrNamet";
//    private static final String USERLEVEL_FNAME     = "userLevelt";
//    private static final String USERTYPE_FNAME      = "usrTypet";
//    private static final String ROLES_FNAME         = "roles";
//    private static final String USER_PARENTID_FNAME = "usrParentIdt";
//    @Value("${jwt.secret}")
//    private String              secret;
//
//    //retrieve subject from jwt token
//    public String getSubjectFromToken( String token )
//    {
//        return getClaimFromToken( token, Claims::getSubject );
//    }
//
//    //retrieve login user ID from jwt token
//    public Integer getLoginUserIdFromToken( String token )
//    {
//        Claims claims = getAllClaimsFromToken( token );
//        return (Integer) claims.get( LOGIN_USER_ID_FNAME );
//    }
//
//    //retrieve login user info from jwt token
//    public String[] getUserNameFromToken( String token )
//    {
//        Claims claims = getAllClaimsFromToken( token );
//        String[] userInfo =
//        { (String) claims.get( USERNAME_FNAME ), (String) claims.get( USERTYPE_FNAME ),
//          (String) claims.get( USERLEVEL_FNAME ), (String) claims.get( USER_PARENTID_FNAME ) };
//        return userInfo;
//    }
//
//    //retrieve expiration date from jwt token
//    public Date getExpirationDateFromToken( String token )
//    {
//        return getClaimFromToken( token, Claims::getExpiration );
//    }
//
//    public <T> T getClaimFromToken( String token, Function<Claims, T> claimsResolver )
//    {
//        final Claims claims = getAllClaimsFromToken( token );
//        return claimsResolver.apply( claims );
//    }
//
//    //for retrieveing any information from token we will need the secret key
//    private Claims getAllClaimsFromToken( String token )
//    {
//        return Jwts.parser().setSigningKey( secret ).parseClaimsJws( token ).getBody();
//    }
//
//    //check if the token has expired
//    private Boolean isTokenExpired( String token )
//    {
//        final Date expiration = getExpirationDateFromToken( token );
//        return expiration.before( new Date() );
//    }
//
//    //generate token for user
//    public String generateToken( Authentication authUser,
//                                 Integer loginUserId,
//                                 String userName,
//                                 String userType,
//                                 String level,
//                                 Integer parentId )
//    {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put( ROLES_FNAME, authUser.getAuthorities().toString() );
//        claims.put( LOGIN_USER_ID_FNAME, loginUserId );
//        claims.put( USERNAME_FNAME, userName );
//        claims.put( USERTYPE_FNAME, userType );
//        claims.put( USERLEVEL_FNAME, level );
//        claims.put( USER_PARENTID_FNAME, parentId.toString() );
//        return doGenerateToken( claims, authUser.getPrincipal().toString() );
//    }
//
//    //while creating the token -
//    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//    //2. Sign the JWT using the HS512 algorithm and secret key.
//    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//    //   compaction of the JWT to a URL-safe string 
//    private String doGenerateToken( Map<String, Object> claims, String subject )
//    {
//        return Jwts.builder().setClaims( claims ).setSubject( subject )
//                .setIssuedAt( new Date( System.currentTimeMillis() ) )
//                .setExpiration( new Date( System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000 ) )
//                .setIssuer( JWT_TOKEN_ISSUER ).setId( JWT_TOKEN_ID ).setAudience( JWT_TOKEN_AUDIENCE )
//                .signWith( SignatureAlgorithm.HS512, secret ).compact();
//    }
//
//    //validate token
//    public Boolean validateToken( String token, UserDetails userDetails )
//    {
//        final String username = getSubjectFromToken( token );
//        return ( username.equals( userDetails.getUsername() ) && !isTokenExpired( token ) );
//    }
//
//    @Override
//    public UserDetails loadUserByUsername( String token )
//        throws UsernameNotFoundException
//    {
//        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
//        Claims claims = getAllClaimsFromToken( token );
//        String userName = (String) claims.get( Claims.SUBJECT );
//        String roles = (String) claims.get( ROLES_FNAME );
//        Integer loginUserIdt = (Integer) claims.get( LOGIN_USER_ID_FNAME );
//        System.out.println( "roles:" + roles );
//        if ( StringUtils.isValidObj( roles ) )
//        {
//            roles = roles.replace( "[", "" ).replace( "]", "" );
//            if ( !StringUtils.isEmpty( roles.trim() ) )
//            {
//                String[] roleNames = roles.split( "," );
//                Stream.of( roleNames ).forEach( role -> {
//                    authorities.add( new SimpleGrantedAuthority( role.trim() ) );
//                } );
//                //Stream.of( authorities ).forEach( LOGGER::info );
//            }
//        }
//        return new org.springframework.security.core.userdetails.User( userName,
//                                                                       loginUserIdt.toString(),
//                                                                       true,
//                                                                       true,
//                                                                       true,
//                                                                       true,
//                                                                       authorities );
//    }
//}