package pl.library.components.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Cannot find loan with provided id")
public class LoanNotFoundException extends RuntimeException{
}
