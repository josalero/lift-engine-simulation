/**
 * 
 */
package com.liftsimulation.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

/**
 * Application configuration using Spring  Configuration Java based
 * 
 * @author Jose Aleman
 *
 */
@Order(2)
@Configuration
@Import(ComponentConfiguration.class)
@ComponentScan(basePackages = {"com.liftsimulation"})
public class AppConfiguration {
	
	
}
