package fr.upec.m2.projects.JavaEE.business.exception;

/**
 * Thrown when login username does exist in DB, but password does not match.
 */
public class InvalidPasswordException extends InvalidCredentialsException {

	private static final long serialVersionUID = 1L;

}