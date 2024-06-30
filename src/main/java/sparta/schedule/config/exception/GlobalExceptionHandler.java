package sparta.schedule.config.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@Component
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseBody
	@ExceptionHandler(sparta.schedule.config.exception.BusinessException.class)
	protected ResponseEntity<Object> handleBusinessException(sparta.schedule.config.exception.BusinessException exception) {

		log.error("business exception! :: {}", exception.toString());

		return new ResponseEntity<>(exception.getMessage(), exception.getHttpStatus()); // httpStatus
	}

	@ResponseBody
	@ExceptionHandler(SystemException.class)
	protected ResponseEntity<Object> handleSystemException(SystemException exception) {

		log.error("SystemException", exception);
		
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // httpStatus : 500
	}

	@ResponseBody
	@ExceptionHandler(JsonProcessingException.class)
	protected ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException exception) {

		log.error("SystemException", exception);

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // httpStatus : 500
	}

	@ResponseBody
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {

		log.error("SystemException", exception);

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // httpStatus : 500
	}

}