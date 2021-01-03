package pl.library.components.address;

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

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
class AddressResourceTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;
    private MockMvc mockMvc;
    @Mock
    private AddressService addressService;
    @InjectMocks
    private AddressResource addressResource;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(addressResource).build();
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    public void end(){
        validatorFactory.close();
    }

    @DisplayName("GelAllAddressesTest")
    @Test
    void testGetAllAddresses() throws Exception {
        //given
        Address a1 = createAddress(1L, "Mielec", "39-300");
        Address a2 = createAddress(2L, "Krakow", "32-800");
        List<Address> addresses = List.of(a1, a2);
        String url = "http://localhost:8080/api/address";
        //when
        when(addressService.getAllAddresses()).thenReturn(addresses);
        //then
        mockMvc.perform(get(url)).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].addressId").value(1L))
                .andExpect(jsonPath("$[0].city").value("Mielec"))
                .andExpect(jsonPath("$[0].postalCode").value("39-300"))
                .andExpect(jsonPath("$[1].addressId").value(2L))
                .andExpect(jsonPath("$[1].city").value("Krakow"))
                .andExpect(jsonPath("$[1].postalCode").value("32-800"));
    }
    @Nested
    class SaveAddressTest {
        @Test
        void saveAddressTest() throws Exception {
            //given
            Address address = createAddress(1L, "Mielec", "39-300");
            String url = "http://localhost:8080/api/address";
            //when
            when(addressService.saveAddress(any(Address.class))).thenReturn(address);
            //then
            mockMvc.perform(MockMvcRequestBuilders.post(url)
                    .content(new ObjectMapper().writeValueAsString(address))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.addressId").exists())
                    .andExpect(jsonPath("$.city").value("Mielec"))
                    .andExpect(jsonPath("$.postalCode").value("39-300"))
                    .andExpect(header().string("Location", "http://localhost:8080/api/address/1"));
        }

        @Test
        void shouldThrowException_WhenAddressNameIsEmpty() throws Exception {
            //given
            Address address = createAddress(1L, "", "39-300");
            String url = "http://localhost:8080/api/address";
            //when
            when(addressService.saveAddress(address)).thenReturn(address);
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "City field cannot be empty")).when(addressService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(address)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("City field cannot be empty"));
        }

        @Test
        void shouldThrowException_WhenAddressPostalCodeBadPattern() throws Exception {
            //given
            Address address = createAddress(1L, "City", "39-30022");
            String url = "http://localhost:8080/api/address";
            //when
            when(addressService.saveAddress(address)).thenReturn(address);
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST,"Postal Code must match the expression 00-000")).when(addressService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(address)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Postal Code must match the expression 00-000"));
        }
    }

    @DisplayName("GettAddressByIdTest")
    @Test
    void getAddressByIdTest() throws Exception {
        //given
        Address address = createAddress(1L, "Mielec", "39-300");
        String url = "http://localhost:8080/api/address/10";
        //when
        when(addressService.getAddressById(anyLong())).thenReturn(address);
        //then
        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressId").exists())
                .andExpect(jsonPath("$.city").value("Mielec"))
                .andExpect(jsonPath("$.postalCode").value("39-300"));
    }
    @Nested
    class UpdateAddressTest {
        @Test
        void updateAddressTest() throws Exception {
            //given
            Address address = createAddress(1L, "Mielec", "39-300");
            String url = "http://localhost:8080/api/address/1";
            //when
            when(addressService.updateAddress(any(Address.class))).thenReturn(address);
            //then
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(address)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.addressId").exists())
                    .andExpect(jsonPath("$.city").value("Mielec"))
                    .andExpect(jsonPath("$.postalCode").value("39-300"))
                    .andExpect(header().string("Location", "http://localhost:8080/api/address/1"));
        }

        @Test
        void shouldThrowException_WhenAddressHasId_OtherThanInPathVariable() throws Exception {
            //given
            Address address = createAddress(1L, "Mielec", "39-300");
            String url = "http://localhost:8080/api/address/2";
            //when
            when(addressService.updateAddress(any(Address.class))).thenReturn(address);
            //then
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(address)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Address must have id same as path variable"));
        }

        @Test
        void shouldThrowException_WhenAddressCityIsEmpty() throws Exception {
            //given
            Address address = createAddress(1L, "", "39-300");
            String url = "http://localhost:8080/api/address/1";
            //when
            when(addressService.updateAddress(any(Address.class))).thenReturn(address);
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "City field cannot be empty")).when(addressService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(
                            new ObjectMapper().writeValueAsString(address)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("City field cannot be empty"));
        }

        @Test
        void shouldThrowException_WhenAddressPostalCodeBadPattern() throws Exception {
            //given
            Address address = createAddress(1L, "Mielec", "39-30022");
            String url = "http://localhost:8080/api/address/1";
            //when
            when(addressService.updateAddress(any(Address.class))).thenReturn(address);
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Postal Code must match the expression 00-000")).when(addressService).checkErrors(any(BindingResult.class));
            //then
            mockMvc.perform(put(url)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(new ObjectMapper().writeValueAsString(address)))
                    .andExpect(status().isBadRequest())
                    .andExpect(status().reason("Postal Code must match the expression 00-000"));
        }
    }

    @DisplayName("DeleteAddressTest")
    @Test
    void deleteAddressTest() throws Exception {
        //given
        Address address = createAddress(1L, "Mielec", "39-300");
        String url = "http://localhost:8080/api/address/{id}";
        //when
        when(addressService.deleteAddress(anyLong())).thenReturn(address);
        //then
        mockMvc.perform(delete(url,1L)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(new ObjectMapper().writeValueAsString(address)))
                .andExpect(jsonPath("$.addressId").value(1))
                .andExpect(jsonPath("$.city").value("Mielec"))
                .andExpect(jsonPath("$.postalCode").value("39-300"))
                .andExpect(status().isOk());
    }

    private Address createAddress(Long id, String city, String postalCode){
        Address address = new Address();
        address.setAddressId(id);
        address.setCity(city);
        address.setPostalCode(postalCode);
        return address;
    }

}