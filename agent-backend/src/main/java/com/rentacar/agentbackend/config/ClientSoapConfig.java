package com.rentacar.agentbackend.config;

import com.rentacar.agentbackend.soap.CarClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ClientSoapConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.rentacar.agentbackend.soap");
        return marshaller;
    }

    @Bean
    public CarClient carClient(Jaxb2Marshaller marshaller) {
        CarClient client = new CarClient();
        client.setDefaultUri("http://localhost:8080/ad/ads/ws");
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

}
