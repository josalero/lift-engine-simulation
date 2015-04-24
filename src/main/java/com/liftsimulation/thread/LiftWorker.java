package com.liftsimulation.thread;

import java.util.List;

import org.apache.log4j.Logger;

import com.liftsimulation.model.Lift;


/**
 * 
 * @author Jose Aleman
 *
 */
public class LiftWorker implements Runnable{
	
	private final static Logger log = Logger.getLogger(LiftWorker.class);
	
	private Lift lift;
	private List<String> commandQueue;
	
	public LiftWorker(Lift lift, List<String> commandQueue){
		this.lift = lift;
		this.commandQueue = commandQueue;
	}

	@Override
	public void run() {
		
		while (true){
			lift.work();
		}
	}

}
