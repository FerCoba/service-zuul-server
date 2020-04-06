package com.springboot.microservice.servicezuulserver.exceptions;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.microservice.servicezuulserver.response.ResponseError;

public enum EnumExceptionsHandler {

	THROW_EXCEPTION("NoHandlerFoundException", "NoSuchElementException", "NullPointerException",
			"AccessDeniedException", "NumberFormatException", "IllegalArgumentException",
			"InvalidDataAccessResourceUsageException", "ConstraintViolationException", "Exception500Status", "BadRequest", "ZuulException",
			"NotFound", "ClientException") {

		private Object conversion(Object obj) {

			ObjectMapper jsonMapper = new ObjectMapper();
			String resultado = null;
			try {
				resultado = jsonMapper.writeValueAsString(obj);
			} catch (JsonProcessingException e) {
				e.getMessage();
			}

			return resultado;
		}

		public ResponseEntity<Object> exceptionBuilder(Exception ex, WebRequest wr, String tipoException) {
			ResponseError resp = null;
			if (THROW_EXCEPTION.getValues().contains((tipoException))) {
				switch (tipoException) {
				
				case "ClientException":
					resp = new ResponseError(HttpStatus.GATEWAY_TIMEOUT,
							"oh nooooo!!.");
					return new ResponseEntity<>(conversion(resp), HttpStatus.GATEWAY_TIMEOUT);
				case "NotFound":
					resp = new ResponseError(HttpStatus.NOT_FOUND,
							"the resource not exist.");
					return new ResponseEntity<>(conversion(resp), HttpStatus.NOT_FOUND);
				case "ZuulException":
					resp = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR,
							"time out, try later.");
					return new ResponseEntity<>(conversion(resp), HttpStatus.OK);
				case "BadRequest":
					resp = new ResponseError(HttpStatus.OK,
							"Not data found.");
					return new ResponseEntity<>(conversion(resp), HttpStatus.OK);
				case "Exception500Status":
					resp = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR,
							"in this moment we can't respond a your request, please try again.");
					return new ResponseEntity<>(conversion(resp), HttpStatus.INTERNAL_SERVER_ERROR);
				case "BadRequestException":
					resp = new ResponseError(HttpStatus.BAD_REQUEST,
							"your request not is valid, please review your information.");
					return new ResponseEntity<>(conversion(resp), HttpStatus.BAD_REQUEST);
				default:
					resp = new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR,
							"in this moment we can't respond a your request, please try again.");
					return new ResponseEntity<>(conversion(resp), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			return new ResponseEntity<>(conversion(resp), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	};

	private final List<String> values;

	EnumExceptionsHandler(String... values) {
		this.values = Arrays.asList(values);
	}

	public List<String> getValues() {
		return values;
	}

	public abstract ResponseEntity<Object> exceptionBuilder(Exception ex, WebRequest wr, String tipoException);

}
