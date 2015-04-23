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

	public boolean validate (T command) throws ValidationException;
	
}
