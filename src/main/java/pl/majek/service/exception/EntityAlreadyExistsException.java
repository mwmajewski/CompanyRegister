package pl.majek.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by majewskm on 2016-02-27.
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Entity already exists")
public class EntityAlreadyExistsException extends RuntimeException {
	public EntityAlreadyExistsException(String s) {
		super(s);
	}

}
