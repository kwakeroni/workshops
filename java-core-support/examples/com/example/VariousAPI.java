package com.example;

import java.io.OutputStream;
import java.util.Base64;

public class VariousAPI {

	public void testBase64(){
		Base64.getEncoder().encodeToString("ABC".getBytes());
	}
	
	public OutputStream toBase64(OutputStream stream){
		return Base64.getEncoder().wrap(stream);
	}
	
}
