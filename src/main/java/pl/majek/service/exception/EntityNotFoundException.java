package pl.majek.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by majewskm on 2016-02-27.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Entity not found")
public class EntityNotFoundException extends RuntimeException {
	public EntityNotFoundException(String s) {
		super(s);
	}

}
