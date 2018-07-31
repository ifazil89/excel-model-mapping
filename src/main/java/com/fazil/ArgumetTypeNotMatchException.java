package com.fazil;

public class ArgumetTypeNotMatchException extends RuntimeException{

	public ArgumetTypeNotMatchException() {
	}
	
	public ArgumetTypeNotMatchException(String message) {
		super(message);
	}
	
	public ArgumetTypeNotMatchException(String message,Throwable e) {
		super(message,e);
	}
}
