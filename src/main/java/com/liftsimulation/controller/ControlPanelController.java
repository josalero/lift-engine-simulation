package com.liftsimulation.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import com.liftsimulation.controller.validator.CommandFormatValidator;
import com.liftsimulation.controller.validator.Validator;
import com.liftsimulation.exception.ValidationException;

/**
 * Control Panel for all Lifts
 * <ul>
 * <li>Add User Requests to queue</li>
 * <li>Validate User Requests before adding to queue</li>
 * </ul>
 * 
 * 
 * @author Jose Aleman
 *
 */
@Controller
@Qualifier("controlPanelController")
public class ControlPanelController implements InitializingBean{

	private final static Logger log = Logger.getLogger(ControlPanelController.class);
	
	@Autowired
	@Qualifier("commandFormatValidator")
	private Validator<String> commandFormatValidator;
	
	private List<String> commandQueue;
	
	private Integer floors;
	
	@Autowired
	ApplicationContext ctx;
	
	public ControlPanelController() {
		
	}

	/**
	 * Handles the request sent by the user
	 * 
	 * @param command
	 */
	public void handleRequest(String command){

		//1. Validate the request
		String cleanCommand = cleanRequest(command.toUpperCase());
		
		boolean validFloor = validateFloor(cleanCommand);
		if (!validFloor){
			log.error("Floor is not valid");
		}else if (validateRequest(cleanCommand) ){
			log.info("Adding command to queue : " + command);
			commandQueue.add(command);
		}
	}
	
	/**
	 * 
	 * @param command
	 * @return true if is a valid request
	 */
	private boolean validateRequest(String command){
		boolean isValid = false;
		try {
			isValid = commandFormatValidator.validate(command);
		}
		catch(ValidationException e){
			log.error(e.getLocalizedMessage());
		}
		
		return isValid;		
	}
	
	/**
	 * 
	 * @param command
	 * @return true is floor is in the correct range
	 */
	private boolean validateFloor(String command){
		Integer floor = Integer.parseInt(command.split("[,]")[2]);
		if (floor < 1 || floor > floors){
			return false;
		}
		return true;
	}
	/**
	 * 
	 * @param command
	 * @return a clean command
	 */
	private String cleanRequest(String command){
		StringBuilder newCommand = new StringBuilder(); 
				
		if (command != null){
			String tokens [] = command.split("[,]");
			int length = tokens.length;
			for (String token : tokens){
				newCommand.append(token.trim().toUpperCase());
				if (--length > 0){
					newCommand.append(",");
				}
			}
		}
		
		return newCommand.toString();
	}
	
	public void setValidator(Validator<String> commandFormatValidator) {
		this.commandFormatValidator = commandFormatValidator;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		this.commandQueue =   (List<String>)ctx.getBean("commandQueue");
	}

	public void setFloors(Integer floors) {
		this.floors = floors;
	}
	
	public List<String> getCommandQueue() {
		return commandQueue;
	}
}
