package pl.library.components.customer;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.address.Address;
import pl.library.components.customer.exceptions.CustomerNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
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

    private CustomerDto mapAndSaveCustomer(CustomerDto dto){
        Customer customerToSave = CustomerMapper.toEntity(dto);
        Customer savedCustomer = customerRepository.save(customerToSave);
        return CustomerMapper.toDto(savedCustomer);
    }
}
