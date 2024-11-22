package io.selferral.admin.api.config.restTemplate;

import java.util.Collections;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class RestTemplateConfig { 

	
	static final RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	Environment env;
	
//	  @Value("${proxy.host}")
//    private String proxyHost;
//
//    @Value("${proxy.port}")
//    private int proxyPort;
    
	@Bean
	RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//		RootUriTemplateHandler handler = new RootUriTemplateHandler(restTemplateHost.getUri(), restTemplate.getUriTemplateHandler());
		String[] profiles = env.getActiveProfiles();
		if(profiles.length == 0) {
			profiles = env.getDefaultProfiles();
		}
		log.info("profile is {}", profiles[0] );
		String profile = profiles[0];
		
		CloseableHttpClient client = null;
		if(profile.equals("prod")) {
			client = HttpClientBuilder.create()
					.setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
							.setMaxConnTotal(500)
							.setMaxConnPerRoute(20)
							.build())
					.setProxy(new HttpHost("proxy.tethermax.io", 3128))
					.build();
			
		}else {
			client = HttpClientBuilder.create()
					.setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
							.setMaxConnTotal(500)
							.setMaxConnPerRoute(20)
							.build())
					.setProxy(new HttpHost("stg-proxy.tethermax.io", 3128))
					.build();
		}
		
		factory.setHttpClient(client);
		factory.setConnectTimeout(5000);
		factory.setReadTimeout(5000);
		restTemplate.setErrorHandler(new RestTemplateErrorHandler());
		restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(factory));
//		restTemplate.setUriTemplateHandler(handler);
		restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
		return restTemplate;
	}
}