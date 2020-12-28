package pl.library.components.author.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Cannot find author with provided id")
public class AuthorNotFoundException extends RuntimeException{
}
