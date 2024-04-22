package edu.miu.cs.cs544.domain.adapter;

import edu.miu.cs.cs544.domain.Address;
import edu.miu.cs.cs544.domain.dto.AddressDTO;

public class AddressAdapter {
    public static Address getAddress(AddressDTO addressDTO) {
        return new Address(
                addressDTO.getId(),
                addressDTO.getLine1(),
                addressDTO.getLine2(),
                addressDTO.getCity(),
                StateAdapter.getState(addressDTO.getStateDTO()),
                addressDTO.getPostalCode(),
                AuditDataAdapter.getAuditData(addressDTO.getAuditData())
        );
    }

    public static AddressDTO getAddressDTO(Address address) {
        return new AddressDTO(
                address.getId(),
                address.getLine1(),
                address.getLine2(),
                address.getCity(),
                StateAdapter.getStateDTO(address.getState()),
                address.getPostalCode(),
                AuditDataAdapter.getAuditDataDTO(address.getAuditData())
        );
    }
}
