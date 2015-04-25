package com.liftsimulation.thread;

import org.apache.log4j.Logger;

import com.liftsimulation.model.Lift;


/**
 * Lift wrapped in this class which manage the Thread cycle
 * 
 * 
 * @author Jose Aleman
 *
 */
public class LiftWorker implements Runnable{
	private final static Logger log = Logger
			.getLogger(LiftWorker.class);
	
	private Lift lift;
	
	private volatile boolean running = true;
	
	public LiftWorker(Lift lift){
		this.lift = lift;
	}

	
	public void goToRest(){
		running = false;
	}
	
	@Override
	public void run() {
		//infinite loop to make lifts work until program finishes
		while (running){
			try{
				lift.work();
				Thread.sleep(100);
			}catch (InterruptedException e){
				log.info("Going to rest now : " + lift.getId());
				running = false;
			}
		}
		log.info("Going to rest now  Lift : " + lift.getId());
	}

}
