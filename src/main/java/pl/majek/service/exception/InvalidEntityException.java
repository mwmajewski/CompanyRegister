package pl.majek.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by majewskm on 2016-02-27.
 */
@ResponseStatus(value= HttpStatus.CONFLICT, reason="Invalid Entity")
public class InvalidEntityException extends RuntimeException {
	public InvalidEntityException(String s) {
		super(s);
	}

}
