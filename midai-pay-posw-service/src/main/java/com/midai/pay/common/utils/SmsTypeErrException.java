package com.midai.pay.common.utils;
public class SmsTypeErrException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public SmsTypeErrException(String message) {
        super(message);
    }

    public SmsTypeErrException(String message, Throwable t) {
        super(message, t);
    }
}