package com.example.demo.utils;

import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class JwtService {

    /*
    JWT 생성
    @param userIdx
    @return String
     */
    public String createJwt(int userIdx){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",userIdx)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }

    /*
    Header에서 X-ACCESS-TOKEN 으로 JWT 추출
    @return String
     */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /*
    JWT에서 userIdx 추출
    @return int
    @throws BaseException
     */
    public int getUserIdx() throws BaseException {
        //1. JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

        // 3. userIdx 추출
        return claims.getBody().get("userIdx",Integer.class);
    }

    /**
     * Access token 에서 user_id 추출
     * @return Long
     */
    public Long getUserId() throws BaseException {
        String accessToken = getAccessToken();
        this.isValidAccessToken(accessToken);
        Claims claims = getClaims(accessToken);

        return claims.get("userIdx", Long.class);
    }

    /**
     * Header에서 ACCESS-TOKEN 으로 JWT 추출
     * @return String
     */
    private String getAccessToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        return extract(request, "Bearer ");
    }

    //Header에서 JWT 추출하기
    //AuthorizationExtractor
    public static final String AUTHORIZATION = "Authorization";
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String extract(HttpServletRequest request, String type) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }
        return Strings.EMPTY;
    }

    /**
     * Access token 유효성 확인
     * @param jwt
     * @return boolean
     */
    protected boolean isValidAccessToken(String jwt) throws BaseException {
        try {
            Claims claims = getClaims(jwt);
            return true;
        } catch (ExpiredJwtException e) {
            throw new BaseException(EXPIRED_ACCESS_TOKEN);
        } catch (JwtException e) {
            System.out.println(e);
            throw new BaseException(INVALID_ACCESS_TOKEN);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    private Claims getClaims(String jwt) throws BaseException {
        try{
            return Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e){
            throw new BaseException(EMPTY_JWT);
        }
    }

}
