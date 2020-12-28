package pl.library.components.customer;

import pl.library.components.address.Address;
import pl.library.components.loan.Loan;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;
    private String customerName;
    private String customerSurname;

    @OneToMany(mappedBy = "customer")
    private List<Loan> loanList;

    @OneToOne
    @JoinColumn(name = "address_id")
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

    public List<Loan> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<Loan> loanList) {
        this.loanList = loanList;
    }

    public void addLoan(Loan loan){
        loan.setCustomer(this);
        this.loanList.add(loan);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId) &&
                Objects.equals(customerName, customer.customerName) &&
                Objects.equals(customerSurname, customer.customerSurname) &&
                Objects.equals(loanList, customer.loanList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, customerName, customerSurname, loanList);
    }
}
