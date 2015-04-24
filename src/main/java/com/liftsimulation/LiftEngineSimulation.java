/**
 * 
 */
package com.liftsimulation;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.liftsimulation.configuration.AppConfiguration;
import com.liftsimulation.controller.ControlPanelController;
import com.liftsimulation.model.Lift;
import com.liftsimulation.thread.LiftWorker;

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
		List<String> commandQueue = (List<String>)ctx.getBean("commandQueue");
		
		controller.handleRequest("OUT,UP,5");
		controller.handleRequest("IN,1,10");
		controller.handleRequest("IN,1,5");
		controller.handleRequest("OUT,UP,7");
		controller.handleRequest("IN,2,1");
		
		Lift lifts[] = new Lift[3];
		
		for (int index=0; index < lifts.length; index++){
			lifts[index] = (Lift) ctx.getBean("lift");
			lifts[index].setId(index + 1);
			lifts[index].setFloors(10);
			Thread thread = new Thread(new LiftWorker(lifts[index], commandQueue));
			thread.start();
		}

	}

}
