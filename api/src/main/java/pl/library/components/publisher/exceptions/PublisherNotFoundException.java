package pl.library.components.publisher.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Cannot find publisher with provided id")
public class PublisherNotFoundException extends RuntimeException{
}
