package pl.library.components.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.library.components.address.Address;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    @Test
    void toDto_ShouldReturnCustomerDto() {
        //given
        Address address = createAddress(1L,"Krakow","33-100");
        Customer customer = createCustomer(1L,"Dawid","Klient",address);
        CustomerDto customerDto = createCustomerDto(1L,"Dawid","Klient",address);
        //when
        //then
        CustomerDto resultCustomer = CustomerMapper.toDto(customer);
        assertThat(resultCustomer).isEqualTo(customerDto);
    }

    @Test
    void toEntity_ShouldReturnCustomerEntity() {
        //given
        Address address = createAddress(1L,"Krakow","33-100");
        Customer customer = createCustomer(1L,"Dawid","Klient",address);
        CustomerDto customerDto = createCustomerDto(1L,"Dawid","Klient",address);
        //when
        //then
        Customer resultCustomer = CustomerMapper.toEntity(customerDto);
        assertThat(resultCustomer).isEqualTo(customer);
    }

    private Customer createCustomer(Long id, String name, String surname, Address address){
        Customer customer = new Customer();
        customer.setCustomerId(id);
        customer.setCustomerName(name);
        customer.setCustomerSurname(surname);
        customer.setAddress(address);
        customer.setLoanList(new ArrayList<>());
        return customer;
    }

    private Address createAddress(Long id, String city, String postalCode){
        Address address = new Address();
        address.setAddressId(id);
        address.setCity(city);
        address.setPostalCode(postalCode);
        return address;
    }

    private CustomerDto createCustomerDto(Long id, String name, String surname, Address address){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(id);
        customerDto.setCustomerName(name);
        customerDto.setCustomerSurname(surname);
        customerDto.setAddress(address);
        return customerDto;
    }
}