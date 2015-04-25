package com.liftsimulation.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.liftsimulation.controller.ControlPanelController;
import com.liftsimulation.controller.validator.Validator;
import com.liftsimulation.listener.LiftListener;

/**
 * 
 * Lift Engine Java Bean
 * 
 * @author Jose Aleman
 * 
 */
@Component
@Scope("prototype")
public class Lift implements InitializingBean {

	private final static Logger log = Logger
			.getLogger(ControlPanelController.class);

	private Integer id;
	private Map<Integer, List<String>> floorMap;
	private List<String> controlPanelQueue;
	private Integer floors;
	private Integer onFloorNumber = 1;

	@Autowired
	private ApplicationContext ctx;
	
	@Autowired
	private LiftListener liftListener;

	public Lift() {
		this.floorMap = new TreeMap<Integer, List<String>>();
		
	}

	public Lift(Integer id, Integer floors) {
		this();
		this.floors = floors;
		this.id = id;
	}

	/**
	 * 
	 * @param command
	 */
	public void addRequest(String command) {

		String tokens[] = command.split("[,]");
		Integer floor = Integer.parseInt(tokens[2]); // floor

		if (floor > 0) {
			List<String> commandList = floorMap.get(floor);
			
			if (commandList == null){
				commandList = new ArrayList<String>();
			}
			
			commandList.add(command);
			floorMap.put(floor, commandList);
		}

	}

	/**
	 * 
	 */
	public void work() {

		// reviews the queue is new requests are for him
		liftListener.collectCommandByLift(id);

		boolean proceed = hasRequests();
			
		if (proceed) {
			switch (calculateDirection()) {
				case 1:
					goingUp();
					break;

				case -1:
					goingDown();
					break;
					
				default:
					trackOnFloor(onFloorNumber);
					break;
			}
		}
	}



	/**
	 * 
	 */
	private void goingDown() {

		if (this.onFloorNumber > 0) {
			this.onFloorNumber--;
			trackOnFloor(this.onFloorNumber);

		}

	}
	
	/**
	 * 
	 */
	private void goingUp() {

		if (this.onFloorNumber < floors) {
			this.onFloorNumber++;
			trackOnFloor(this.onFloorNumber);
		}
	}
	
	/**
	 * 
	 * @return 1 to go up, 0 to stay and -1 to go down
	 */
	public Integer calculateDirection(){
		Integer status = null;
		Integer onFloor = 1;//Lobby

		if (this.floorMap.size() > 0 ){
			// It is supposed to be sorted by Floor
			onFloor = (Integer)this.floorMap.keySet().toArray()[0]; 
		}
		
		if (onFloor > onFloorNumber){
			status = 1;
		}else if (onFloor < onFloorNumber){
			status = -1;
		} else{
			status = 0;
		}
		
		return status;
	}
	/**
	 * 
	 * @param floor
	 */
	private void trackOnFloor(Integer floor) {

		List<String> commandList = this.floorMap.get(floor);
		
		if (commandList != null && commandList.size() > 0){
			for (String command : commandList){
				if (floor == onFloorNumber){
					trackOpenDoorRequest(floor, command);
				}else if (StringUtils.contains(command,
						Validator.COMMAND_IN)) {
					trackServedRequest(floor, command);
				} else if (StringUtils.contains(command,
						Validator.COMMAND_OUT)) {
					trackServedRequest(floor, command);
				}
			}
			this.floorMap.remove(floor);
		}else{
			trackFloor(floor);
		}

	}

	/**
	 * 
	 * @param floor
	 */
	private void trackFloor(int floor) {
		log.info(String.format("Lift %s crossed floor %s ", id, floor));
		pause(5000);
	}

	/**
	 * 
	 * @param floor
	 * @param command
	 */
	private void trackServedRequest(int floor, String command) {

		log.info(String.format(
				"Lift %s stopped on floor %s to serve request (%s)", id, floor,
				command));
		pause(10000);
	}
	
	/**
	 * 
	 * @param floor
	 * @param command
	 */
	private void trackOpenDoorRequest(int floor, String command) {

		log.info(String.format(
				"Lift %s opened the door on floor %s to serve request (%s)", id, floor,
				command));
		pause(10000);
	}
	
	/**
	 * 
	 * @param seconds
	 */
	private void pause(int seconds) {
		try {
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			// nothing
		}
	}

	/******* SETTERS AND GETTERS ***********/

	public Integer getFloors() {
		return floors;
	}

	public Integer getOnFloorNumber() {
		return onFloorNumber;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		controlPanelQueue = (List<String>) ctx.getBean("commandQueue");
	}

	public List<String> getControlPanelQueue() {
		return controlPanelQueue;
	}

	public void setId(Integer id) {
		if (this.id == null) {
			this.id = id;
			Integer floor = this.onFloorNumber == null ? 1 : this.onFloorNumber;
			log.info(String.format("Lift %s is in the floor %d and is ready to start working", id, floor));
		}
	}

	public Integer getId() {
		return id;
	}

	public void setFloors(Integer floors) {
		if (this.floors == null) {
			this.floors = floors;
		}
	}

	public boolean hasRequests(){
		return this.floorMap != null && this.floorMap.size() > 0;
	}

	public void setFloorMap(Map<Integer, List <String>> floorMap) {
		if (floorMap != null){
			this.floorMap = floorMap;
		}
	}

	public void setLiftListener(LiftListener liftListener) {
		if (liftListener != null){
			this.liftListener = liftListener;
		}
		
	}
	
	public void setControlPanelQueue(List<String> controlPanelQueue) {
		if (controlPanelQueue != null){
			this.controlPanelQueue = controlPanelQueue;
		}
	}
	
	public void setOnFloorNumber(Integer onFloorNumber) {
		this.onFloorNumber = onFloorNumber == null || onFloorNumber < 1 ? 1 : onFloorNumber;
	}
}
