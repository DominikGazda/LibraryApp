package pl.library.components.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.library.components.address.exceptions.AddressNotFoundException;

import java.util.List;

@Service
public class AddressService {

    private AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    public List<Address> getAllAddresses(){
        return addressRepository.findAll();
    }

    public Address saveAddress(Address address){
        if(address.getAddressId() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Address cannot have id");
        Address savedAddress = addressRepository.save(address);
        return savedAddress;
    }

    public Address getAddressById(Long id){
        return addressRepository.findById(id)
                .orElseThrow(AddressNotFoundException::new);
    }

    public Address updateAddress(Address address){
        return addressRepository.save(address);
    }

    public Address deleteAddress(Long id){
        Address foundAddress = addressRepository.findById(id).orElseThrow(AddressNotFoundException::new);
        addressRepository.delete(foundAddress);
        return foundAddress;
    }
}
