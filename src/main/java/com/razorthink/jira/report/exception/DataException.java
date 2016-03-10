package com.razorthink.jira.report.exception;
@SuppressWarnings( { "unused" } )
public class DataException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String errorCode;
	private final String errorMessage;

	public DataException( String errorCode, String errorMessage )
	{
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;

	}

}
