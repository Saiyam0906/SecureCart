package com.example.Ecommerce.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse {
	
	private String messgae;
	
	private Object data;

}
