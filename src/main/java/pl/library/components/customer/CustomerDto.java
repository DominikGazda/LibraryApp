package pl.library.components.customer;

import pl.library.components.address.Address;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class CustomerDto {

    private Long customerId;
    @NotBlank(message = "{pl.library.components.customer.Customer.customerName.NotBlank}")
    private String customerName;
    @NotBlank(message = "{pl.library.components.customer.Customer.customerSurname.NotBlank}")
    private String customerSurname;
    private Address address;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSurname() {
        return customerSurname;
    }

    public void setCustomerSurname(String customerSurname) {
        this.customerSurname = customerSurname;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDto that = (CustomerDto) o;
        return Objects.equals(customerId, that.customerId) &&
                Objects.equals(customerName, that.customerName) &&
                Objects.equals(customerSurname, that.customerSurname) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, customerName, customerSurname, address);
    }
}
