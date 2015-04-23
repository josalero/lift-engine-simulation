package com.liftsimulation.controller;

import java.util.Queue;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import com.liftsimulation.controller.validator.CommandFormatValidator;
import com.liftsimulation.exception.ValidationException;

@Controller
public class ControlPanelController implements InitializingBean{

	private final static Logger log = Logger.getLogger(ControlPanelController.class);
	
	@Autowired
	private CommandFormatValidator commandFormatValidator;
	
	private Queue<String> commandQueue;
	
	@Autowired
	ApplicationContext ctx;
	
	public ControlPanelController() {
		
	}

	public void handleRequest(String command){

		//1. Validate the request
		boolean isValidRequest = validateRequest(command);
		
		//2. Add  
		if (isValidRequest){
			log.info("Adding command to queue : " + command);
			commandQueue.add(command);
		}
	}
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	private boolean validateRequest(String command){
		boolean isValid = false;
		try {
			isValid = commandFormatValidator.validate(command);
		}
		catch(ValidationException e){
			log.error("Command String is not valid ", e);
		}
		
		return isValid;		
	}
	

	public void setCommandFormatValidator(CommandFormatValidator commandFormatValidator) {
		this.commandFormatValidator = commandFormatValidator;
	}


	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() throws Exception {
		this.commandQueue =   (Queue<String>)ctx.getBean("commandQueue");
	}
}
