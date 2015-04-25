/**
 * 
 */
package com.liftsimulation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.liftsimulation.configuration.AppConfiguration;
import com.liftsimulation.controller.ControlPanelController;
import com.liftsimulation.listener.LiftListener;
import com.liftsimulation.model.Lift;
import com.liftsimulation.thread.LiftWorker;

/**
 * Lift Engine Simulation Main Class
 * 
 * @author Jose Aleman
 *
 */
public class LiftEngineSimulation {
	private final static Logger log = Logger
			.getLogger(LiftEngineSimulation.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfiguration.class);
		
		ControlPanelController controller = ctx.getBean("controlPanelController", ControlPanelController.class);
		List<String> commandQueue = (List<String>)ctx.getBean("commandQueue");
		LiftListener liftListener = ctx.getBean("liftListener", LiftListener.class);
		
		Scanner sc = new Scanner(System.in);
		
	    log.info("Enter number of floors");
	    Integer floors = Integer.parseInt(sc.nextLine());
	    controller.setFloors(floors);
	    log.info("Enter number of lifts");
	    Integer numberOfLifts = Integer.parseInt(sc.nextLine());
	    
		Lift lifts[] = new Lift[numberOfLifts+1];
		
		Map<Runnable, Thread> liftThreadMap = new HashMap<Runnable, Thread> ();
		
		for (int index=1; index < lifts.length; index++){
			lifts[index] = (Lift) ctx.getBean("lift");
			lifts[index].setId(index);
			lifts[index].setFloors(floors);
			liftListener.addLiftToListener(lifts[index]);
			LiftWorker worker = new LiftWorker(lifts[index]);
			Thread thread = new Thread(worker);
			liftThreadMap.put(worker, thread);
			thread.start();
		}
		
		boolean shouldContinue = true;
		do {		
			log.info("Enter command ");
		    String command = sc.nextLine();
		    if (!StringUtils.equalsIgnoreCase(command, "EXIT")){
		    	controller.handleRequest(command);
		    }else{
		    	shouldContinue = false;
		    }
		    
		    
		}while(shouldContinue);
		//Stop threads of execution
		for (Runnable lift : liftThreadMap.keySet()){
			Thread thread = liftThreadMap.get(lift);
			((LiftWorker)lift).goToRest();
			try {
				thread.join();
			} catch (InterruptedException e) {
				//
			}
		}
		//Gracefully program termination 
		System.exit(0);
	}

}
