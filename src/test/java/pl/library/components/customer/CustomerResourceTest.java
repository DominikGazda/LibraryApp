package pl.library.components.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.address.Address;
import pl.library.components.loan.LoanDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
class CustomerResourceTest {

    private MockMvc mockMvc;
    @Mock
    private CustomerService customerService;
    @InjectMocks
    private CustomerResource customerResource;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerResource).build();
    }

    @Test
    void getCustomers_ShouldReturnCustomersList() throws Exception{
        //given
        CustomerDto firstCustomer = createCustomerDto(1L,"Damian","Kowal");
        CustomerDto secondCustomer = createCustomerDto(2L,"Karol","Kot");
        Address address = createAddress(1L,"Krakow","33-100");
        firstCustomer.setAddress(address);
        secondCustomer.setAddress(address);
        List<CustomerDto> customerDtoList = List.of(firstCustomer, secondCustomer);
        String url = "/api/customer";
        //when
        when(customerService.getCustomers()).thenReturn(customerDtoList);
        //then
        mockMvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].customerId").value(1L))
                .andExpect(jsonPath("$[0].customerName").value("Damian"))
                .andExpect(jsonPath("$[0].customerSurname").value("Kowal"))
                .andExpect(jsonPath("$[0].address.addressId").value(1L))
                .andExpect(jsonPath("$[0].address.city").value("Krakow"))
                .andExpect(jsonPath("$[0].address.postalCode").value("33-100"))
                .andExpect(jsonPath("$[1].customerId").value(2L))
                .andExpect(jsonPath("$[1].customerName").value("Karol"))
                .andExpect(jsonPath("$[1].customerSurname").value("Kot"))
                .andExpect(jsonPath("$[1].address.addressId").value(1L))
                .andExpect(jsonPath("$[1].address.city").value("Krakow"))
                .andExpect(jsonPath("$[1].address.postalCode").value("33-100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void saveCustomer_ShouldReturnSavedCustomer() throws Exception {
        //given
        CustomerDto customer = createCustomerDto(null,"Damian","Kowal");
        CustomerDto savedCustomer = createCustomerDto(1L,"Damian","Kowal");
        String url = "/api/customer";
        //when
        when(customerService.saveCustomer(customer)).thenReturn(savedCustomer);
        //then
        mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.customerName").value("Damian"))
                .andExpect(jsonPath("$.customerSurname").value("Kowal"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location","http://localhost/api/customer/1"));

    }

    @Test
    void saveCustomer_ShouldThrowResponseEntityException_WhenCustomerNameIsEmpty() throws Exception {
        //given
        CustomerDto customer = createCustomerDto(1L,"","Kowal");
        String url = "/api/customer";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST," Customer name cannot be empty")).when(customerService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(" Customer name cannot be empty"));
    }

    @Test
    void saveCustomer_ShouldThrowResponseEntityException_WhenCustomerSurnameIsEmpty() throws Exception{
        //given
        CustomerDto customer = createCustomerDto(1L,"Karol","");
        String url = "/api/customer";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Customer surname cannot be empty")).when(customerService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Customer surname cannot be empty"));
    }

    @Test
    void getCustomerById_ShouldReturnCustomerWithProvidedId() throws Exception {
        //given
        CustomerDto customer = createCustomerDto(1L,"Karol","Kowal");
        String url = "/api/customer/{id}";
        //when
        when(customerService.getCustomerById(anyLong())).thenReturn(customer);
        //then
        mockMvc.perform(get(url, anyLong())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.customerName").value("Karol"))
                .andExpect(jsonPath("$.customerSurname").value("Kowal"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void updateCustomer_ShouldReturnUpdatedCustomerWithProvidedId() throws Exception {
        //given
        CustomerDto customer = createCustomerDto(1L,"Karol","Kowal");
        CustomerDto updatedCustomer = createCustomerDto(1L,"Marek","Kowal");
        String url = "/api/customer/{id}";
        //when
        when(customerService.updateCustomer(customer)).thenReturn(updatedCustomer);
        //then
        mockMvc.perform(put(url,1L)
            .content(new ObjectMapper().writeValueAsString(customer))
            .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.customerName").value("Marek"))
                .andExpect(jsonPath("$.customerSurname").value("Kowal"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void updateCustomer_ShouldThrowResponseEntityException_WhenCustomerNameIsEmpty() throws Exception{
        //given
        CustomerDto customer = createCustomerDto(1L,"","Wiatrak");
        String url = "/api/customer/{id}";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Customer name cannot be empty")).when(customerService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(put(url,1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Customer name cannot be empty"));
    }

    @Test
    void updateCustomer_ShouldThrowResponseEntityException_WhenCustomerSurnameIsEmpty() throws Exception{
        //given
        CustomerDto customer = createCustomerDto(1L,"Karol","");
        String url = "/api/customer/{id}";
        //when
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Customer surname cannot be empty")).when(customerService).checkErrors(any(BindingResult.class));
        //then
        mockMvc.perform(put(url,1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Customer surname cannot be empty"));
    }

    @Test
    void updateCustomer_ShouldThrowResponseStatusException_WhenCustomerIdIsOtherThanPathVariableId() throws Exception{
        //given
        CustomerDto customer = createCustomerDto(1L,"Karol","Wolak");
        String url = "/api/customer/{id}";
        //when
        //then
        mockMvc.perform(put(url,2L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Customer must have id same as path variable"));
    }

    @Test
    void deleteCustomer_ShouldReturnDeletedCustomer() throws Exception {
        //given
        CustomerDto customer = createCustomerDto(1L, "Karol", "Wolak");
        String url = "/api/customer/{id}";
        //when
        when(customerService.deleteCustomer(anyLong())).thenReturn(customer);
        //then
        mockMvc.perform(delete(url, anyLong())
            .content(new ObjectMapper().writeValueAsString(customer)))
                .andExpect(jsonPath("$.customerId").value(1L))
                .andExpect(jsonPath("$.customerName").value("Karol"))
                .andExpect(jsonPath("$.customerSurname").value("Wolak"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void getAddressFromCustomerById_ShouldReturnCustomerWithProvidedIdAddress() throws Exception {
        //given
        CustomerDto customer = createCustomerDto(1L, "Karol", "Wolak");
        Address address = createAddress(1L,"Krakow","33-100");
        customer.setAddress(address);
        String url = "/api/customer/{id}/address";
        //when
        when(customerService.getAddressFromCustomerById(anyLong())).thenReturn(address);
        //then
        mockMvc.perform(get(url, anyLong())
            .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.addressId").exists())
                .andExpect(jsonPath("$.city").value("Krakow"))
                .andExpect(jsonPath("$.postalCode").value("33-100"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void getAllLoansFromCustomerById_ShouldReturnCustomerWithProvidedIdLoans() throws Exception {
        //given
        LoanDto firstLoanDto = createLoanDto(1L,1L,1L,1L);
        LoanDto secondLoanDto = createLoanDto(2L,1L,2L,1L);
        List<LoanDto> loanDtoList = List.of(firstLoanDto, secondLoanDto);
        String url = "/api/customer/{id}/loans";
        //when
        when(customerService.getAllLoansFromCustomerById(anyLong())).thenReturn(loanDtoList);
        //then
        mockMvc.perform(get(url, anyLong())
            .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].loanId").value(1L))
                .andExpect(jsonPath("$[0].bookId").value(1L))
                .andExpect(jsonPath("$[0].customerId").value(1L))
                .andExpect(jsonPath("$[0].librarianId").value(1L))
                .andExpect(jsonPath("$[1].loanId").value(2L))
                .andExpect(jsonPath("$[1].bookId").value(2L))
                .andExpect(jsonPath("$[1].customerId").value(1L))
                .andExpect(jsonPath("$[1].librarianId").value(1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    private CustomerDto createCustomerDto(Long id, String name, String surname){
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(id);
        customerDto.setCustomerName(name);
        customerDto.setCustomerSurname(surname);
        return customerDto;
    }

    private Address createAddress(Long id, String city, String postalCode){
        Address address = new Address();
        address.setAddressId(id);
        address.setCity(city);
        address.setPostalCode(postalCode);
        return address;
    }

    private LoanDto createLoanDto(Long loanId, Long customerId, Long bookId, Long librarianId){
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanId(loanId);
        loanDto.setCustomerId(customerId);
        loanDto.setBookId(bookId);
        loanDto.setActive(true);
        loanDto.setStartLoanDate(LocalDateTime.now());
        loanDto.setLibrarianId(librarianId);
        return loanDto;
    }
}