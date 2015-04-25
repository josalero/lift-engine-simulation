/**
 * 
 */
package com.liftsimulation.controller.validator;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.liftsimulation.BaseTestCase;
import com.liftsimulation.configuration.AppConfiguration;
import com.liftsimulation.exception.ValidationException;

/**
 * Validator Tests
 * 
 * @author Jose Aleman
 *
 */

public class ValidatorTest extends BaseTestCase {
	
	private final static Logger log = Logger.getLogger(ValidatorTest.class);

	Validator<String> commandFormatValidator;

	/**
	 * 
	 */
	public ValidatorTest() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void beforeTest() {
		commandFormatValidator = (Validator<String>) context.getBean("commandFormatValidator");
		
	}

	@Override
	public void afterTest() {
		commandFormatValidator = null;
	}
	
	@Test
	public void testValidatorMapping(){
		boolean result = commandFormatValidator != null;
		if (!result){
			fail("Validator is null");
		}
		log.info("Mapping is correct");
		assertTrue(result);

	}

	@Test
	public void testPositiveCases(){
		String commands[] = {"IN,1,2", "OUT,DOWN,4", "OUT, UP, 1"};
		
		for (String command : commands){
			try {
				commandFormatValidator.validate(command);
			} catch (ValidationException e) {
				fail();
			}
		}
		log.info("Commands are correct");
		assertTrue(true);
	}

	@Test(expected=ValidationException.class)
	public void testNegativeCases() throws Exception{
		commandFormatValidator.validate("NOSE, 1, Casa");
	}

	@Test
	public void testBlank() throws Exception{
		assertTrue(commandFormatValidator.validate("") == false);
	}
	
	@Test
	public void testNotValid() throws Exception{
		assertTrue(commandFormatValidator.validate("Up,2") == false);
	}

}
