package io.selferral.admin.api.config.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.selferral.admin.api.core.CD.ErrorCode;
import io.selferral.admin.api.core.exception.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenProvider tokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = parseBearerToken(req);
			User user = parseUserSpecification(token);
			if(StringUtils.hasText(token) && !"anonymous".equals(user.getUsername())) {
				AbstractAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(user, token, user.getAuthorities());
				authenticated.setDetails(new WebAuthenticationDetails(req));
				SecurityContextHolder.getContext().setAuthentication(authenticated);	
			} 
		} catch (SignatureException e ) {
			req.setAttribute("exception", new ErrorResponse(ErrorCode.INVALID_JWT_SIGNATURE));
		} catch (MalformedJwtException e ) {
			req.setAttribute("exception", new ErrorResponse(ErrorCode.INVALID_JWT_TOKEN));
        } catch (ExpiredJwtException e) {
        	req.setAttribute("exception", new ErrorResponse(ErrorCode.EXPIRED_JWT_TOKEN));
        } catch (UnsupportedJwtException e) {
        	req.setAttribute("exception", new ErrorResponse(ErrorCode.NOT_SUPPORTED_JWT_TOKEN));
        } catch (IllegalArgumentException e) {
        	req.setAttribute("exception", new ErrorResponse(ErrorCode.INVALID_JWT_TOKEN));
        } finally {
        	filterChain.doFilter(req, res);
		} 
	}
	
	private String parseBearerToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(token -> token.substring(0, 7).equalsIgnoreCase("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }
	
	private User parseUserSpecification(String token) {
        String[] split = Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 10)
                .map(tokenProvider::validateTokenAndGetSubject)
                .orElse("anonymous:anonymous")
                .split(":");
        return new User(split[0], "", List.of(new SimpleGrantedAuthority(split[1])));
    }
	 
	protected void doFilterWrapped(ContentCachingRequestWrapper req, ContentCachingResponseWrapper res, FilterChain filterChain) throws IOException, ServletException {
		try {
			filterChain.doFilter(req, res);
			logRequest(req);
		} finally {
			logResponse(res);
			res.copyBodyToResponse();
		}
	}
	  
	private static void logRequest(ContentCachingRequestWrapper request) throws IOException {
		String queryString = request.getQueryString();
		log.info("Request : {} uri=[{}] content-type[{}]", request.getMethod(),
				queryString == null ? request.getRequestURI() : request.getRequestURI() + queryString,
				request.getContentType());
		logPayload("Request", request.getContentType(), request.getContentAsByteArray());
	}

	private static void logResponse(ContentCachingResponseWrapper response) throws IOException {
		logPayload("Response", response.getContentType(), response.getContentAsByteArray());
	}

	private static void logPayload(String prefix, String contentType, byte[] rowData) throws IOException {
		boolean visible = isVisible(MediaType.valueOf(contentType == null ? "application/json" : contentType));
	    if (visible) {
	      if (rowData.length > 0) {
	        String contentString = new String(rowData);
	        log.info("{} Payload: {}", prefix, contentString);
	      }
	    } else {
	      log.info("{} Payload: Binary Content", prefix);
	    }
	}
	
	private static boolean isVisible(MediaType mediaType) {
	    final List<MediaType> VISIBLE_TYPES = Arrays.asList(
	        MediaType.valueOf("text/*"),
	        MediaType.APPLICATION_FORM_URLENCODED,
	        MediaType.APPLICATION_JSON,
	        MediaType.APPLICATION_XML,
	        MediaType.valueOf("application/*+json"),
	        MediaType.valueOf("application/*+xml"),
	        MediaType.MULTIPART_FORM_DATA
    );

    return VISIBLE_TYPES.stream()
        .anyMatch(visibleType -> visibleType.includes(mediaType));
	}
}