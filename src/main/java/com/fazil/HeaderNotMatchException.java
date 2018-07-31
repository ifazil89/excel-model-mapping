package com.fazil;

public class HeaderNotMatchException extends RuntimeException{

	public HeaderNotMatchException() {
	}
	
	public HeaderNotMatchException(String message) {
		super(message);
	}
	
	public HeaderNotMatchException(String message,Throwable e) {
		super(message,e);
	}
}
