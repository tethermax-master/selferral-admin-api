package io.selferral.admin.api.config.restTemplate;

import java.io.IOException;  
import java.nio.charset.Charset;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {
      
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException 
    {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
 
        //Add optional additional headers
        response.getHeaders().add("headerName", "VALUE");
 
        return response;
    }
  
    private void logRequest(HttpRequest request, byte[] body) throws IOException 
    {
        if (true) 
        {
            log.debug("===========================request begin================================================");
            log.debug("URI         : {}", request.getURI());
            log.debug("Method      : {}", request.getMethod());
            log.debug("Headers     : {}", request.getHeaders());
            log.debug("Request body: {}", new String(body, "UTF-8"));
            log.debug("==========================request end================================================");
        }
    }
  
    private void logResponse(ClientHttpResponse response) throws IOException 
    {
        if (log.isDebugEnabled()) 
        {
            log.debug("============================response begin==========================================");
            log.debug("Status code  : {}", response.getStatusCode());
            log.debug("Status text  : {}", response.getStatusText());
            log.debug("Headers      : {}", response.getHeaders());
            log.debug("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
            log.debug("=======================response end=================================================");
        }
    }
}