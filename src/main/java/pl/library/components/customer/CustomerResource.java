package pl.library.components.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.library.components.address.Address;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/customer", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerResource {

    private CustomerService customerService;

    public CustomerResource(CustomerService customerService){
        this.customerService = customerService;
    }

    @GetMapping("")
    public List<CustomerDto> getCustomers(){
        return customerService.getCustomers();
    }

    @PostMapping("")
    public ResponseEntity<CustomerDto> saveCustomer(@RequestBody CustomerDto dto){
        CustomerDto savedCustomer = customerService.saveCustomer(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCustomer.getCustomerId())
                .toUri();
        return ResponseEntity.created(location).body(savedCustomer);
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomerById(@PathVariable Long id){
        return customerService.getCustomerById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto dto){
        if(!id.equals(dto.getCustomerId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Customer must have id same as path variable");
        CustomerDto updatedCustomer = customerService.updateCustomer(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updatedCustomer.getCustomerId())
                .toUri();
        return ResponseEntity.created(location).body(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public CustomerDto deleteCustomer(@PathVariable Long id){
        return customerService.deleteCustomer(id);
    }

    @GetMapping("/{id}/address")
    public Address getAddressFromCustomerById(@PathVariable Long id){
        return customerService.getAddressFromCustomerById(id);
    }
}
