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
	
	private Lift lift;
	
	public LiftWorker(Lift lift){
		this.lift = lift;
	}

	@Override
	public void run() {
		//infinite loop to make lifts work until program finishes
		while (true){
			lift.work();
		}
	}

}
