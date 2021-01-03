package pl.library.components.address;

import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.address.exceptions.AddressNotFoundException;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    @Mock AddressRepository addressRepository;
    @InjectMocks AddressService addressService;

    @BeforeEach
    public void init (){
        MockitoAnnotations.openMocks(this);
    }

        @DisplayName("GetAllAddresses")
        @Test
        public void getAllAddresses() {
            //given
            Address a1 = createAddress(1L, "Mielec", "39-300");
            Address a2 = createAddress(2L, "Krakow", "32-100");
            Address a3 = createAddress(3L, "Tarnów", "33-100");
            List<Address> addressList = List.of(a1, a2, a3);
            //when
            when(addressRepository.findAll()).thenReturn(addressList);
            //then
            assertThat(addressService.getAllAddresses()).isEqualTo(addressList);
        }
        @Nested
        class GetAddressById {
            @Test
            void getAddressById() {
                //given
                Address a1 = createAddress(1L, "Mielec", "39-300");
                Optional<Address> optionalAddress = java.util.Optional.of(a1);
                //when
                when(addressRepository.findById(1L)).thenReturn(optionalAddress);
                //then
                assertThat(addressService.getAddressById(1L)).isEqualTo(a1);
            }

            @Test
            public void shouldThrowException_WhenCannotFind_AddressWithId() {
                //when
                when(addressRepository.findById(2L)).thenThrow(new AddressNotFoundException());

                //then
                assertThatThrownBy(() -> addressService.getAddressById(2L))
                        .isInstanceOf(AddressNotFoundException.class);
            }
        }

    @Nested
    class SaveAddress {
        @Test
        public void saveAddress() {
            //given
            Address address = createAddress(null, "Mielec", "39-300");
            //when
            addressService.saveAddress(address);
            //then
            ArgumentCaptor<Address> argumentCaptor = ArgumentCaptor.forClass(Address.class);
            verify(addressRepository).save(argumentCaptor.capture());

            Address captureAddress = argumentCaptor.getValue();
            assertThat(captureAddress).isEqualTo(address);
        }

        @Test
        public void shouldReturn_SavedAddressFromDatabase() {
            //given
            Address address = createAddress(null, "Mielec", "39-300");
            Address savedAddress = createAddress(1L, "Mielec", "39-300");
            //when
            when(addressRepository.save(address)).thenReturn(savedAddress);
            Address testAddress = addressService.saveAddress(address);
            //then
            assertThat(savedAddress).isEqualTo(testAddress);
        }

        @Test
        public void shouldThrowException_WhenAddressToSaveHas_Id() {
            //given
            Address address = createAddress(1L, "Mielec", "39-300");
            //then
            assertThatThrownBy(() -> addressService.saveAddress(address))
                    .isInstanceOf(ResponseStatusException.class)
                    .hasMessage("400 BAD_REQUEST \"Address cannot have id\"");
        }
    }

    @DisplayName("UpdateAddress")
    @Test
    public void updateAddress(){
        //given
        Address address = createAddress(2L, "Mielec", "39-300");
        Address updatedAddress = createAddress(2L,"Warszawa","33-333");
        //when
        when(addressRepository.save(address)).thenReturn(updatedAddress);
        //then
        assertThat(addressService.updateAddress(address)).isEqualTo(updatedAddress);
    }

    @Nested
    class DeleteAddress {

        @Test
        public void deleteAddress() {
            //given
            Address address = createAddress(1L, "Mielec", "39-300");
            Optional<Address> optionalAddress = Optional.of(address);
            //when
            when(addressRepository.findById(1L)).thenReturn(optionalAddress);
            Address deletedAddress = addressService.deleteAddress(1L);
            //then
            ArgumentCaptor<Address> argumentCaptor = ArgumentCaptor.forClass(Address.class);
            verify(addressRepository).delete(argumentCaptor.capture());

            assertAll(
                    () -> assertThat(argumentCaptor.getValue()).isEqualTo(address),
                    () -> assertThat(deletedAddress).isEqualTo(address)
            );
        }

        @Test
        public void shouldThrowException_WhenCannotFindAddressWith_Id(){
            //given
            Address address = createAddress(1L, "Mielec", "39-300");
            //when
            doThrow(new AddressNotFoundException()).when(addressRepository).delete(address);
            //then
            assertThatThrownBy(() -> addressService.deleteAddress(2L))
                    .isInstanceOf(AddressNotFoundException.class);
        }
    }

    private Address createAddress(Long id, String city, String postalCode){
        Address address = new Address();
        address.setAddressId(id);
        address.setCity(city);
        address.setPostalCode(postalCode);
        return address;
    }


}