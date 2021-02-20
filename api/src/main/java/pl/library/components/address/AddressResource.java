package pl.library.components.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value = "/api/address", produces = MediaType.APPLICATION_JSON_VALUE)
public class AddressResource {

    private AddressService addressService;

    @Autowired
    public AddressResource(AddressService addressService){
        this.addressService = addressService;
    }

    @GetMapping("")
    public List<Address> getAllAddresses(){
        return addressService.getAllAddresses();
    }

    @PostMapping("")
    public ResponseEntity<Address> saveAddress(@Valid @RequestBody Address address, BindingResult result){
        if(result.hasErrors())
            addressService.checkErrors(result);
        Address savedAddress = addressService.saveAddress(address);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAddress.getAddressId())
                .toUri();
        return ResponseEntity.created(location).body(savedAddress);
    }

    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable Long id){
        return addressService.getAddressById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id,@Valid @RequestBody Address address, BindingResult result){
        if(result.hasErrors())
            addressService.checkErrors(result);
        if(!id.equals(address.getAddressId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Address must have id same as path variable");
        Address updatedAddress = addressService.updateAddress(address);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build()
                .toUri();
        return ResponseEntity.created(location).body(updatedAddress);
    }

    @DeleteMapping("/{id}")
    public Address deleteAddress(@PathVariable Long id){
        return addressService.deleteAddress(id);
    }
}
