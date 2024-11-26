package io.selferral.admin.api.config.security.exception;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.selferral.admin.api.core.CD.ErrorCode;
import io.selferral.admin.api.core.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private ObjectMapper mapper = new ObjectMapper();

	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws  IOException, ServletException {
		Object exception = request.getAttribute("exception");
    	if(ObjectUtils.isEmpty(exception)) { // Header에 JWT Token이 없을 경우 setting
    		response.getWriter().write(mapper.writeValueAsString(new ErrorResponse(ErrorCode.UNAUTHORIZED)));
    	} else {
    		response.getWriter().write(mapper.writeValueAsString(exception));
    	}
    	response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    	response.setStatus(HttpServletResponse.SC_OK);
    }
}