package com.springboot.microservice.servicezuulserver.oauth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Value("${config.security.oauth.jwt.key}")
	private String jwtKey;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/api/authorization/oauth/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/item/obtainItemsInformation",
						"/api/product/obtainInformationAllProducts", "/api/user/obtainInformationAllUsers")
				.permitAll().antMatchers(HttpMethod.GET, "/api/user/userName/{userName}/userInformationByUserName")
				.permitAll()
				.antMatchers(HttpMethod.GET,
						"http://localhost:8090/api/product/productId/{productId}/obtainProductInformation")
				.permitAll()
				.antMatchers(HttpMethod.GET, "/api/product/obtainProductInformation/{productId}",
						"/api/item/id/{id}/quantity/{quantity}/obtainInformationItemById",
						"/api/user/userName/{userName}/userInformation")
				.hasAnyRole("USER", "ADMIN").antMatchers("/api/product/**", "/api/item/**", "/api/user/**")
				.hasRole("ADMIN").anyRequest().authenticated().and().cors()
				.configurationSource(corsConfigurationSource());
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configCors = new CorsConfiguration();
		configCors.setAllowedOrigins(Arrays.asList("*"));
		configCors.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "OPTION", "DELETE"));
		configCors.setAllowCredentials(true);
		configCors.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

		UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
		configSource.registerCorsConfiguration("/", configCors);
		return configSource;
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> CorsFilter() {
		FilterRegistrationBean<CorsFilter> beanCorsFilter = new FilterRegistrationBean<CorsFilter>(
				new CorsFilter(corsConfigurationSource()));
		beanCorsFilter.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return beanCorsFilter;
	}

	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		tokenConverter.setSigningKey(jwtKey);
		return tokenConverter;
	}
}
