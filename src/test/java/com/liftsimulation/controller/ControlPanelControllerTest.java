/**
 * 
 */
package com.liftsimulation.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.liftsimulation.BaseTestCase;
import com.liftsimulation.controller.ControlPanelController;
import com.liftsimulation.controller.validator.CommandFormatValidator;

/**
 * 
 * 
 * @author Jose ALeman
 *
 */
public class ControlPanelControllerTest extends BaseTestCase {
	private final static Logger log = Logger
			.getLogger(ControlPanelControllerTest.class);

	ControlPanelController controlPanelController;
	
	@Mock
	CommandFormatValidator commandFormatValidator;
	/**
	 * 
	 */
	public ControlPanelControllerTest() {
		
	}

	/* (non-Javadoc)
	 * @see com.liftsimulation.BaseTestCase#beforeTest()
	 */
	@Override
	public void beforeTest() {
		MockitoAnnotations.initMocks(this);
		controlPanelController = context.getBean("controlPanelController", ControlPanelController.class);
		controlPanelController.setFloors(10);
		controlPanelController.setValidator(commandFormatValidator);

	}

	/* (non-Javadoc)
	 * @see com.liftsimulation.BaseTestCase#afterTest()
	 */
	@Override
	public void afterTest() {
		controlPanelController = null;
	}

	@Test
	public void testHandleRequest() throws Exception{
		
		String commands[] = {"IN,1,4", "OUT,DOWN,4", "IN,2,4", "OUT,UP,4"};
		
		for (String command : commands){
			when(commandFormatValidator.validate(command)).thenReturn(true);
			controlPanelController.handleRequest(command);
		}
		
		boolean result = controlPanelController.getCommandQueue().size() == 4; 
		assertTrue(result);
		log.info("Command queue should be 4 " + result);
	}
	
	@Test
	public void testHandleRequestInvalidFloor() throws Exception{
		when(commandFormatValidator.validate("IN,1,11")).thenReturn(true);
		controlPanelController.handleRequest("IN,1,11");
		
		boolean result = controlPanelController.getCommandQueue().size() == 0; 
		assertTrue(result);
		log.info("Command queue should be empty " + result);
	}
}
