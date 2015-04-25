/**
 * 
 */
package com.liftsimulation.configuration;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
	public List<String> commandQueue(){
		return new CopyOnWriteArrayList<String>();
	}
	
}
