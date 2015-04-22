/**
 * 
 */
package com.liftsimulation.configuration;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Components and Beans configuration
 * 
 * @author Jose Aleman
 *
 */
@Order(1)
@Configuration
public class ComponentConfiguration {
	
	@Bean(name="commandQueue")
	public Queue<String> commandQueue(){
		return new LinkedBlockingQueue<String>();
	}
}
