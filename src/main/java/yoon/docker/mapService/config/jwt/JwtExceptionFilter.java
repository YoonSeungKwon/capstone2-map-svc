package yoon.docker.mapService.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request, response);
        }catch(JwtException e){
            setErrorResponse(response, e);
        }
    }

    public void setErrorResponse(HttpServletResponse response, JwtException e) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();

        String message;

        if(e.getMessage().equals("ACCESS_TOKEN_EXPIRED")){
            message = "ACCESS_TOKEN이 만료되었습니다.";
        }
        else if(e.getMessage().equals("REFRESH_TOKEN_EXPIRED")){
            message = "REFRESH_TOKEN이 만료되었습니다.";
        }
        else{
            message = "알 수 없는 에러. 개발자에게 알려주세요.";
        }
        response.setStatus(401);
        mapper.writeValue(response.getOutputStream(), message);
    }
}
