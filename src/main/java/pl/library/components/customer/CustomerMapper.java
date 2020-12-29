package pl.library.components.customer;

public class CustomerMapper {

    static CustomerDto toDto(Customer entity){
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(entity.getCustomerId());
        dto.setCustomerName(entity.getCustomerName());
        dto.setCustomerSurname(entity.getCustomerSurname());
        dto.setAddress(entity.getAddress());
        return dto;
    }

    static Customer toEntity(CustomerDto dto){
        Customer entity = new Customer();
        entity.setCustomerId(dto.getCustomerId());
        entity.setCustomerName(dto.getCustomerName());
        entity.setCustomerSurname(dto.getCustomerSurname());
        entity.setAddress(dto.getAddress());
        return entity;
    }
}
