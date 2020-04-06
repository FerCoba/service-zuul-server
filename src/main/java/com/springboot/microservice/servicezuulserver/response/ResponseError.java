package com.springboot.microservice.servicezuulserver.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {

	private HttpStatus status;
	private String errorMessage;

}
