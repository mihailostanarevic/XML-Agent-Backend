package com.rentacar.agentbackend.soap;

import com.rentacar.agentbackend.model.CreateGearshiftTypeRequestDTO;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

public class CarClient extends WebServiceGatewaySupport {

    public Long createGearshiftType(CreateGearshiftTypeRequestDTO value) {
        JAXBElement<CreateGearshiftTypeRequestDTO> jaxbElement =
                new JAXBElement(new QName("http://www.car.com/car","createGearshiftTypeRequest"),
                        CreateGearshiftTypeRequestDTO.class, value);
        System.out.println("pre responsa");
        getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
        System.out.println("posle responsa");
        return 1L;
    }

}
