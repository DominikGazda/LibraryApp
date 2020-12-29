package pl.library.components.librarian.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Cannot find librarian with provided id")
public class LibrarianNotFoundException extends RuntimeException{
}
