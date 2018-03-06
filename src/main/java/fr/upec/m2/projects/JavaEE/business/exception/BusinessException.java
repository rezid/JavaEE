package fr.upec.m2.projects.JavaEE.business.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public abstract class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

}