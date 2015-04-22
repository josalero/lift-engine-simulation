/**
 * 
 */
package com.liftsimulation;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.liftsimulation.configuration.AppConfiguration;
import com.liftsimulation.controller.ControlPanelController;

/**
 * Main Class
 * 
 * @author Jose Aleman
 *
 */
public class LiftEngineSimulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
		
		ControlPanelController controller = ctx.getBean("controlPanelController", ControlPanelController.class);
		
		controller.handleRequest("IN,1,10");

	}

}
