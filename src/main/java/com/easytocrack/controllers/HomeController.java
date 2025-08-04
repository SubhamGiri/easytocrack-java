package com.easytocrack.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.easytocrack.services.HomeServices;

@RestController
public class HomeController{
	
	@Autowired
	HomeServices					 homeServices;

	@GetMapping("/home")
	public String test()
	{
		return "Welcome to EasyToCrack";
	}
	@PostMapping("/")
	public Map<String, Object> defaultPage()
	{
		System.out.println("Home");
		String message = "Home page";
		Map<String,Object> response = new HashMap<>();
		
		response.put("status", true);
		response.put("response", message);
		
		return response;
		
	}
	
	@PostMapping("/run-program")
	public Map<String,Object> runProgram(@RequestBody Map<String,String> code)
	{
		Map<String,Object> response = new HashMap<>();
		
		System.out.println();
		String sourceCode = code.get("code"); 
		
		try {
			String output = homeServices.runProgram(sourceCode);
			
			response.put("status", true);
			response.put("response", output);
			
			System.out.println(response);
		}
		catch(Exception e)
		{
			System.out.println(e);
			response.put("status", false);
		}
		return response;
	}
}
