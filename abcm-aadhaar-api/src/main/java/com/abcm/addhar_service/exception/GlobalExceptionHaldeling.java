package com.abcm.addhar_service.exception;

import java.time.OffsetDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;



@RestControllerAdvice
public class GlobalExceptionHaldeling {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, WebRequest request) {
	    ErrorResponse response = new ErrorResponse();
	    response.setRequest_timestamp(OffsetDateTime.now());
	    response.setResponse_code(ex.getCode());
	    response.setResponse_massage(ex.getMessage());
	    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

    

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
    	//log.error("Defualt Exception",ex.getMessage());
        ErrorResponse response = new ErrorResponse();
        response.setRequest_timestamp(OffsetDateTime.now());
        response.setResponse_code(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    response.setResponse_massage(ex.getMessage());
     //   response.setPath(request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    
    
    
    @ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoResourceFound(
			NoResourceFoundException ex, WebRequest request) {

		//log.error("Unexpected error occurred: {}", ex.getMessage(), ex);

		ErrorResponse response = ErrorResponse.builder()
				.request_timestamp(OffsetDateTime.now())
				.response_code(HttpStatus.NOT_FOUND.value())
				.response_massage("Resource not found: "+request.getDescription(false).replace("uri=", ""))
				.path(request.getDescription(false).replace("uri=", ""))
				.build();
		return ((BodyBuilder) ResponseEntity.notFound()).body(response);
	}

    
    
    
    

   
   
}
