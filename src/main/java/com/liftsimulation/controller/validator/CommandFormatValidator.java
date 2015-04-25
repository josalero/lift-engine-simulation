package com.liftsimulation.controller.validator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.liftsimulation.exception.ValidationException;

/**
 * This step is meant to parse a specific JSON file and the output must be a
 * dealer inventory
 * 
 * @author Jose Aleman
 * 
 */
@Component(value="commandFormatValidator")
public class CommandFormatValidator implements Validator<String> {

	private static final String COMMAND_NOT_VALID_GENERIC_MESSAGE = "This command %s is not valid";

	public CommandFormatValidator() {

	}

	@Override
	public boolean validate(String command) throws ValidationException {
		// Check empty or null command string
		boolean isValid = true;

		// Validation chain
		if (StringUtils.isBlank(command)) {
			isValid = false;
		} else {
			// Check if string contains exactly 3 values
			String commandTokens[] = StringUtils.split(command, ',');
			if (commandTokens.length == 3) {
				switch (commandTokens[0].toUpperCase()) {
				case COMMAND_IN:
					isValid = validateInCommand(commandTokens[1],
							commandTokens[2]);
					break;

				case COMMAND_OUT:
					isValid = validateOutCommand(commandTokens[1],
							commandTokens[2]);
					break;

				default:
					throw new ValidationException(
							generateValidationErrorMessage(command));
				}
			}else{
				isValid = false;
			}
		}

		return isValid;
	}

	/**
	 * 
	 * @param command
	 * @return error message
	 */
	private String generateValidationErrorMessage(String command) {
		return String.format(COMMAND_NOT_VALID_GENERIC_MESSAGE, command);
	}

	/**
	 * 
	 * @param direction
	 * @param floor
	 * @return true if command is valid
	 */
	private boolean validateOutCommand(String direction, String floor) {
		boolean isValid = false;
		switch (direction.toUpperCase()) {
		case COMMAND_OUT_UP:
			isValid = true;
			break;

		case COMMAND_OUT_DOWN:
			isValid = true;
			break;

		default:
			// log error
			break;
		}

		isValid = isValid && validateIsNumeric(floor);
		return isValid;

	}

	/**
	 * 
	 * @param liftNumber
	 * @param floor
	 * @return true if command is valid
	 */
	private boolean validateInCommand(String liftNumber, String floor) {

		return validateIsNumeric(liftNumber) && validateIsNumeric(floor);
	}

	/**
	 * 
	 * @param floor
	 * @return true if numeric
	 */
	private boolean validateIsNumeric(String floor) {
		return StringUtils.isNumeric(floor);
	}

}
