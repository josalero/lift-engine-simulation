/**
 * 
 */
package com.liftsimulation;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.liftsimulation.listener.LiftListener;
import com.liftsimulation.model.Lift;

/**
 * @author Jose Aleman
 * 
 */
public class LiftTest extends BaseTestCase {

	private final static Logger log = Logger.getLogger(LiftTest.class);

	Lift lift;
	
	@Mock
	private LiftListener liftListener;
	
	@Mock
	private List<String> commandQueue;

	/**
	 * 
	 */
	public LiftTest() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void beforeTest() {
		// TODO Auto-generated method stub
		lift = context.getBean("lift", Lift.class);
		MockitoAnnotations.initMocks(this);
	}

	@Override
	public void afterTest() {
		// TODO Auto-generated method stub
		lift = null;
	}

	@Test
	public void testAddRequest() {
		lift.addRequest("IN,2,1");
		lift.addRequest("OUT,UP,2");

		assertTrue("Adding commands error", lift.hasRequests());
		log.info("Commands are correct");
	}

	@Test
	public void testGoingUp() throws InterruptedException {
		Map<Integer, List<String>> floorMap = new HashMap<Integer, List<String>>();
		floorMap.put(4, Arrays.asList("IN,1,4"));

		lift.setLiftListener(liftListener);
		lift.setControlPanelQueue(commandQueue);
		lift.setFloorMap(floorMap);
		lift.setFloors(10);
		lift.setOnFloorNumber(2);
		lift.setId(1);
		
		
		Map<Integer, Lift> liftMap = new HashMap<Integer, Lift>();
		liftMap.put(1, lift);

		when(liftListener.getLiftMap()).thenReturn(liftMap);
		when(commandQueue.size()).thenReturn(1);
		when(commandQueue.get(0)).thenReturn("IN,1,4");
		//when(lift.hasRequests()).thenReturn(true);
		
		while (lift.hasRequests()){
			lift.work();
		}
		
		assertTrue(true);
	}
	
	@Test
	public void testGoingDown() throws InterruptedException {
		Map<Integer, List<String>> floorMap = new HashMap<Integer, List<String>>();
		floorMap.put(7, Arrays.asList("IN,1,7"));

		lift.setLiftListener(liftListener);
		lift.setControlPanelQueue(commandQueue);
		lift.setFloorMap(floorMap);
		lift.setFloors(10);
		lift.setOnFloorNumber(10);
		lift.setId(1);
		
		
		Map<Integer, Lift> liftMap = new HashMap<Integer, Lift>();
		liftMap.put(1, lift);

		when(liftListener.getLiftMap()).thenReturn(liftMap);
		when(commandQueue.size()).thenReturn(1);
		when(commandQueue.get(0)).thenReturn("IN,1,7");
		//when(lift.hasRequests()).thenReturn(true);
		
		while (lift.hasRequests()){
			lift.work();
		}
		
		assertTrue(true);
	}
}
