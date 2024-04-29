package com.np.schoolpgi.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateRequestDtoUtil {
	
	Logger LOGGER = LogManager.getLogger(ValidateRequestDtoUtil.class);

	@Autowired
	Validator validator;
	
	public Set<ConstraintViolation<Object>> validateReqDto(Object reqDto) {
			return validator.validate(reqDto);
	}
}
