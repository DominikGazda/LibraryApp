package pl.library.components.customer;

import java.util.ArrayList;

public class CustomerMapper {

    public static CustomerDto toDto(Customer entity){
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(entity.getCustomerId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setCustomerSurname(entity.getCustomerSurname());
        dto.setAddress(entity.getAddress());
        return dto;
    }

    public static Customer toEntity(CustomerDto dto){
        Customer entity = new Customer();
        entity.setCustomerId(dto.getCustomerId());
        entity.setCustomerName(dto.getCustomerName());
        entity.setCustomerSurname(dto.getCustomerSurname());
        entity.setAddress(dto.getAddress());
        entity.setLoanList(new ArrayList<>()); // Test
        return entity;
    }
}
