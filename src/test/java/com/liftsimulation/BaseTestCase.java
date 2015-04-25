/**
 * 
 */
package com.liftsimulation;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.liftsimulation.configuration.AppConfiguration;

/**
 * @author Jose Aleman
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public abstract class BaseTestCase {

	protected ApplicationContext context;
	/**
	 * 
	 */
	public BaseTestCase() {
		// TODO Auto-generated constructor stub
	}

	@Before
	public void before() {
		context = new AnnotationConfigApplicationContext(AppConfiguration.class);
		beforeTest();
	}

	@After
	public void after() {
		afterTest();
	}
	
	public abstract void beforeTest();
	
	public abstract void afterTest();
}
