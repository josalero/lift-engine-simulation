/**
 * 
 */
package com.liftsimulation.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.liftsimulation.controller.validator.Validator;
import com.liftsimulation.model.Lift;

/**
 * Class meant to "hear" requests and assign to correct Lift Engines
 * 
 * @author Jose Aleman
 *
 */
@Component
public class LiftListener implements InitializingBean{

	@Autowired
	private ApplicationContext ctx;
	
	private Map<Integer, Lift> liftMap = new TreeMap<Integer, Lift>();
	
	private List<String> controlPanelQueue;
	
	/**
	 * 
	 */
	public LiftListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param lift
	 */
	public void addLiftToListener(Lift lift){
		liftMap.put(lift.getId(), lift);
	}
	
	/**
	 * Add requests to Lift Queue according to liftId
	 * 
	 * 
	 * @param liftId
	 */
	public void collectCommandByLift(Integer liftId){
		
		Lift lift = liftMap.get(liftId);
		
		// Evaluates if queue is not empty to proceed
		if (controlPanelQueue.size() > 0) {
			synchronized (controlPanelQueue) {
				// Put In Commands to Queue and return Out Commands
				List<String> outCommands = processInRequests(lift);
				//Process Out Commands just in case no In Commands where added to Queue
				processOutRequests(outCommands, lift);
			}
		}



	}

	/**
	 * @param liftId
	 * @param lift
	 */
	private void processOutRequests(List<String> outCommands, Lift lift) {
		
		Integer liftId = lift.getId();
		
		if (!lift.hasRequests()) {
			
			for (String outCommand : outCommands) {
				boolean booked = false;
				//UP or DOWN
				String outCommandTokens[] = outCommand.split("[,]");
				//Floor Number
				Integer outFloor = Integer.valueOf(outCommandTokens[2]);
				
				for (Integer id : liftMap.keySet()) {
					
					if (id.intValue() != liftId.intValue()) {
						Lift otherLift = liftMap.get(id);
						
						Integer direction = otherLift.calculateDirection();
						Integer liftOnFloor = otherLift.getOnFloorNumber();
						
						boolean canAddToLiftQueue = ((StringUtils.equals(outCommandTokens[1],
								Validator.COMMAND_OUT_UP) && 
								direction == 1 && outFloor > liftOnFloor));
						
						canAddToLiftQueue = canAddToLiftQueue || (StringUtils.equals(outCommandTokens[1],
								Validator.COMMAND_OUT_DOWN) && 
								direction == -1 && outFloor < liftOnFloor);

						if (canAddToLiftQueue) {
							lift.addRequest(outCommand);
							controlPanelQueue.remove(outCommand);
							booked = true;
						} 
					}
					break;

				}//End For
				if (!booked){
					lift.addRequest(outCommand);
					controlPanelQueue.remove(outCommand);
				}
			}
			
		}
	}

	/**
	 * 
	 * @param lift
	 * @return
	 */
	private List<String> processInRequests(Lift lift) {
		
		Integer liftId = lift.getId();
		List<String> outCommands = new ArrayList<String>();

		for (String command : controlPanelQueue) {
			String tokens[] = command.split("[,]");
			if (StringUtils.equals(tokens[0],
					Validator.COMMAND_IN)
					&& StringUtils.equals(tokens[1],
							String.valueOf(liftId))) {
				lift.addRequest(command);
				controlPanelQueue.remove(command);
			}

			if (StringUtils.equals(tokens[0],
					Validator.COMMAND_OUT)) {
				outCommands.add(command);

			}
		}// /End For

		return outCommands;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		controlPanelQueue = (List<String>) ctx.getBean("commandQueue");
		
	}
	
	public Map<Integer, Lift> getLiftMap() {
		return liftMap;
	}
	
	public void setControlPanelQueue(List<String> controlPanelQueue) {
		this.controlPanelQueue = controlPanelQueue;
	}
}
