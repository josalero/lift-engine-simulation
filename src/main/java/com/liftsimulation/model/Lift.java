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
import com.liftsimulation.controller.validator.CommandFormatValidator;

/**
 * 
 * Lift Engine Model
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
	private boolean goingUp = true;
	private Integer floors;
	private Integer onFloorNumber = 1;
	private Map<Integer, String> liftMap;

	@Autowired
	private ApplicationContext ctx;

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

		String tokens[] = command.split(",");

		Integer floor = Integer.parseInt(tokens[2]); // floor

		if (floor > 0) {
			List<String> commandList = floorMap.get(floor - 1);
			
			if (commandList == null){
				commandList = new ArrayList<>();
			}
			
			commandList.add(command);
			floorMap.put(floor - 1, commandList);
		}

	}

	/**
	 * 
	 */
	public void work() {

		// reviews the queue is new requests are for him
		collectCommandsForMe();

		if (this.floorMap.size() > 0) {
			// evaluates if needs to go up or down
			goingUp = needsToGoUp();
			// executes the direction according to "goingUp"
			if (goingUp) {
				goingUp();
			} else {
				goingDown();
			}
		}

	}

	/**
	 * 
	 * @return true if lift needs to go up else false
	 */
	private boolean needsToGoUp() {

		boolean goesUp = true;

		if (this.onFloorNumber > floors) {
			goesUp = false;
		} else if (this.onFloorNumber < 1) {
			goesUp = true;
		}

		return goesUp;
	}

	/**
	 * 
	 */
	private void collectCommandsForMe() {

		// Evaluates if queue is not empty to proceed
		if (controlPanelQueue.size() > 0) {
			synchronized (controlPanelQueue) {

				List<String> outCommands = new ArrayList<>();

				for (String command : controlPanelQueue) {
					String tokens[] = command.split(",");
					if (StringUtils.equals(tokens[0],
							CommandFormatValidator.COMMAND_IN)
							&& StringUtils.equals(tokens[1],
									String.valueOf(this.id))) {
						addRequest(command);
						controlPanelQueue.remove(command);
					}

					if (StringUtils.equals(tokens[0],
							CommandFormatValidator.COMMAND_OUT)) {
						outCommands.add(command);

					}
				}// /End For

				if (this.floorMap.size() == 0) {
					
					for (String outCommand : outCommands) {
						String outCommandTokens[] = outCommand.split(",");
						Integer outFloor = Integer.valueOf(outCommandTokens[2]);
						
						for (Integer id : liftMap.keySet()) {
							if (id != this.id) {
								String liftStatus = liftMap.get(id);
								String tokens[] = liftStatus.split(",");//false,1
								
								boolean liftGoingUp = Boolean.valueOf(tokens[0]);
								Integer liftOnFloor = Integer.valueOf(tokens[1]);
								
								if ((StringUtils.equals(outCommandTokens[1],
										CommandFormatValidator.COMMAND_OUT_UP) && 
										liftGoingUp && outFloor > liftOnFloor)) {
								

								} else {

								}
							}

						}
					}

					// addRequest();
					controlPanelQueue.remove(outCommands.get(0));
				}

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
	 * @param floor
	 */
	private void trackOnFloor(Integer floor) {

		this.liftMap.put(id, generateStatus());

		List<String> commandList = this.floorMap.get(onFloorNumber - 1);
		
		if (commandList != null && commandList.size() > 0){
			for (String command : commandList){
				if (StringUtils.contains(command,
						CommandFormatValidator.COMMAND_IN)) {
					trackServedRequest(onFloorNumber, command);
				} else if (StringUtils.contains(command,
						CommandFormatValidator.COMMAND_OUT)) {
					trackServedRequest(onFloorNumber, command);
				}
			}
		}else{
			trackFloor(onFloorNumber);
		}

	}

	/**
	 * 
	 */
	private void goingUp() {

		if (this.onFloorNumber <= floors) {
			this.onFloorNumber++;
			trackOnFloor(this.onFloorNumber);
		}
	}

	private void trackFloor(int floor) {
		log.info(String.format("Lift %s crossed floor %s ", id, floor));
		pause(5000);
	}

	private void trackServedRequest(int floor, String command) {
		this.floorMap.remove(floor);
		log.info(String.format(
				"Lift %s stopped on floor %s to serve request (%s)", id, floor,
				command));
		pause(10000);
	}

	private void pause(int seconds) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// nothing
		}
	}

	/******* SETTERS AND GETTERS ***********/

	public boolean isGoingUp() {
		return goingUp;
	}

	public Integer getFloors() {
		return floors;
	}

	public Integer getOnFloorNumber() {
		return onFloorNumber;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		controlPanelQueue = (List<String>) ctx.getBean("commandQueue");
		liftMap = (Map<Integer, String>) ctx.getBean("liftMap");
	}

	public List<String> getControlPanelQueue() {
		return controlPanelQueue;
	}

	public void setId(Integer id) {
		if (this.id == null) {
			this.id = id;
			liftMap.put(id, generateStatus());
		}
	}

	private String generateStatus() {
		return String.valueOf(goingUp) + "," + this.onFloorNumber;
	}

	public void setFloors(Integer floors) {
		if (this.floors == null) {
			this.floors = floors;
		}
	}

	public Map<Integer, String> getFloorMap() {
		return floorMap;
	}

	public void setFloorMap(Map<Integer, String> floorMap) {
		this.floorMap = floorMap;
	}

}
