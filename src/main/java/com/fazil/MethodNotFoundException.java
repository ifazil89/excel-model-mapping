package com.fazil;

public class MethodNotFoundException extends Exception {
	
	public MethodNotFoundException() {
		super();
	}
	public MethodNotFoundException(String message) {
		super(message);
	}
	public MethodNotFoundException(String message,Throwable t) {
		super(message, t);
	}
}
