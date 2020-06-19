package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.CreateAddressRequest;
import com.rentacar.agentbackend.entity.Address;
import com.rentacar.agentbackend.entity.Agent;
import com.rentacar.agentbackend.repository.IAddressRepository;
import com.rentacar.agentbackend.repository.IAgentRepository;
import com.rentacar.agentbackend.service.IAddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressService implements IAddressService {

    private final IAddressRepository _addressRepository;

    private final IAgentRepository _agentRepository;

    public AddressService(IAddressRepository addressRepository, IAgentRepository agentRepository) {
        _addressRepository = addressRepository;
        _agentRepository = agentRepository;
    }

    @Override
    public void createAddress(CreateAddressRequest request) throws Exception {
        Address address = new Address();
        address.setCity(request.getCity());
        address.setCountry(request.getCountry());
        address.setNumber(Integer.valueOf(request.getNumber()));
        address.setStreet(request.getStreet());
        Agent agent = _agentRepository.findOneById(request.getAgentId());
        address.getAgent().add(agent);
        Address savedAddress = _addressRepository.save(address);
        agent.getAddress().add(savedAddress);
        _agentRepository.save(agent);
    }
}
