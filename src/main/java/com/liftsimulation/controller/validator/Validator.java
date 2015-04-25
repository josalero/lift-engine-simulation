/**
 * 
 */
package com.liftsimulation.controller.validator;

import com.liftsimulation.exception.ValidationException;



/**
 * Validator interface
 * 
 * 
 * @author Jose Aleman
 *
 */

public interface Validator<T> {
	public static final String COMMAND_OUT = "OUT";
	public static final String COMMAND_OUT_UP = "UP";
	public static final String COMMAND_OUT_DOWN = "DOWN";
	public static final String COMMAND_IN = "IN";
	
	public boolean validate (T command) throws ValidationException;
	
}
