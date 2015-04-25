/**
 * 
 */
package com.liftsimulation;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.liftsimulation.listener.LiftListener;
import com.liftsimulation.model.Lift;

/**
 * 
 * 
 * @author Jose Aleman
 *
 */
public class LiftListenerTest extends BaseTestCase {

	private final static Logger log = Logger
			.getLogger(LiftListenerTest.class);
	LiftListener liftListener;
	
	@Mock
	Lift lift;
	
	List<String> commandQueue;
	
	/**
	 * 
	 */
	public LiftListenerTest() {
	}

	/* (non-Javadoc)
	 * @see com.liftsimulation.BaseTestCase#beforeTest()
	 */
	@Override
	public void beforeTest() {
		MockitoAnnotations.initMocks(this);
		liftListener = context.getBean("liftListener", LiftListener.class);
		commandQueue = (List<String>)context.getBean("commandQueue");
		commandQueue.add("IN,1,4");
		liftListener.setControlPanelQueue(commandQueue);
	}

	/* (non-Javadoc)
	 * @see com.liftsimulation.BaseTestCase#afterTest()
	 */
	@Override
	public void afterTest() {
		liftListener = null;
	}

	@Test 
	public void testAddLiftToListener(){
		liftListener.addLiftToListener(lift);
		boolean result = liftListener.getLiftMap().size() == 1;
		assertTrue (result);
		log.info("Lift Map should be empty : " + result);
	}
	
	@Test
	public void testCollectCommandByLift(){
		when(lift.getId()).thenReturn(1);
		liftListener.addLiftToListener(lift);
		
		liftListener.collectCommandByLift(1);
		
		boolean result = commandQueue.size() == 0;
		assertTrue(result);
		
		log.info("Command queue should be empty : " + result);
	}
}
