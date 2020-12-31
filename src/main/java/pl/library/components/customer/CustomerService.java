package pl.library.components.customer;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.address.Address;
import pl.library.components.customer.exceptions.CustomerNotFoundException;
import pl.library.components.loan.Loan;
import pl.library.components.loan.LoanDto;
import pl.library.components.loan.LoanMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;
    private LoanMapper loanMapper;

    public CustomerService(CustomerRepository customerRepository, LoanMapper loanMapper){
        this.customerRepository = customerRepository;
        this.loanMapper = loanMapper;
    }

    public List<CustomerDto> getCustomers(){
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toDto)
                .collect(Collectors.toList());
    }

    public CustomerDto saveCustomer(CustomerDto dto){
        if(dto.getCustomerId() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Customer cannot have id");
       return mapAndSaveCustomer(dto);
    }

    public CustomerDto getCustomerById(Long id){
        Customer customer = customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);
        return CustomerMapper.toDto(customer);
    }

    public CustomerDto updateCustomer(CustomerDto dto){
        return mapAndSaveCustomer(dto);
    }

    public CustomerDto deleteCustomer(Long id){
        Customer foundCustomer = customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);
        customerRepository.delete(foundCustomer);
        return CustomerMapper.toDto(foundCustomer);
    }

    public Address getAddressFromCustomerById(Long id){
        Customer customer = customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);
        return customer.getAddress();
    }

    public List<LoanDto> getAllLoansFromCustomerById(Long id){
        Customer foundCustomer = customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);
        return foundCustomer.getLoanList()
                .stream()
                .map(loanMapper::toDto)
                .collect(Collectors.toList());
    }

    public void checkErrors(BindingResult result){
        List<ObjectError> errors = result.getAllErrors();
        String message = errors
                .stream()
                .map(ObjectError::getDefaultMessage)
                .map(s -> s.toString() +" ")
                .collect(Collectors.joining());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
    }

    private CustomerDto mapAndSaveCustomer(CustomerDto dto){
        Customer customerToSave = CustomerMapper.toEntity(dto);
        Customer savedCustomer = customerRepository.save(customerToSave);
        return CustomerMapper.toDto(savedCustomer);
    }
}
