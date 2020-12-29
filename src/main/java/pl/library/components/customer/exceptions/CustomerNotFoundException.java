package pl.library.components.customer.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Cannot find customer with provided id")
public class CustomerNotFoundException extends RuntimeException{
}
