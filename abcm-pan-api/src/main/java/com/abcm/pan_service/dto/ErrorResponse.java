package com.abcm.pan_service.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

   
	private LocalDateTime localDate;
    private int statusCode;
    private String error;
    private String message;
    private String path;
}
