package com.heroku.security.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.heroku.security.entities.UserInputDto;

@Component
public class UserValidator implements Validator {

	@Override
	public boolean supports(Class<?> aClass) {
		return UserInputDto.class.equals(aClass);
	}

	@Override
	public void validate(Object o, Errors errors) {
		UserInputDto user = (UserInputDto) o;
		System.out.println("User to verify=" + user.getUsername());

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");

		if (user.getUsername().length() < 6 || user.getUsername().length() > 32) {
			errors.rejectValue("username", "Size.username", "Username has to be between 6 and 32 characters long");
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");

		if (user.getPassword().length() < 8 || user.getPassword().length() > 32) {
			errors.rejectValue("password", "Size.password", "Password has to be between 8 and 32 characters long");
		}

		if (!user.getPasswordConfirm().equals(user.getPassword())) {
			errors.rejectValue("passwordConfirm", "Diff.passwordConfirm", "Passwords does not match");
		}
	}
}
