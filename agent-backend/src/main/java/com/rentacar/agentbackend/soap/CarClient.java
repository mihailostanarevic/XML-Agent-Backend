package com.rentacar.agentbackend.soap;

import com.rentacar.agentbackend.soap.wsdl.CreateGearshiftTypeRequestDTO;
import com.rentacar.agentbackend.soap.wsdl.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

public class CarClient extends WebServiceGatewaySupport {

    public Long createGearshiftType(CreateGearshiftTypeRequestDTO value) {  //radi
        JAXBElement<CreateGearshiftTypeRequestDTO> jaxbElement =
                new JAXBElement(new QName("http://www.car.com/car","createGearshiftTypeRequest"),
                        CreateGearshiftTypeRequestDTO.class, value);
        System.out.println("pre responsa");
        getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
        System.out.println("posle responsa");
        return 1L;
    }

    public Long createFuelType(FuelType value) {   //radi
        JAXBElement<FuelType> jaxbElement =
                new JAXBElement(new QName("http://www.car.com/car","createFuelType"),
                        FuelType.class, value);
        System.out.println("pre responsa");
        getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
        System.out.println("posle responsa");
        return 1L;
    }

    public Long createCarClass(CarClass value) { //radi
        JAXBElement<CarClass> jaxbElement =
                new JAXBElement(new QName("http://www.car.com/car","createCarClass"),
                        CarClass.class, value);
        System.out.println("pre responsa");
        getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
        System.out.println("posle responsa");
        return 1L;
    }

    public Long createCarAccessories(CarAccessories value) { //radi
        JAXBElement<CarAccessories> jaxbElement =
                new JAXBElement(new QName("http://www.car.com/car","createCarAccessory"),
                        CarAccessories.class, value);
        System.out.println("pre responsa");
        getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
        System.out.println("posle responsa");
        return 1L;
    }

    public Long createCarBrand(CarBrand value) { //radi
        JAXBElement<CarBrand> jaxbElement =
                new JAXBElement(new QName("http://www.car.com/car","createCarBrand"),
                        CarBrand.class, value);
        System.out.println("pre responsa");
        getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
        System.out.println("posle responsa");
        return 1L;
    }

    public Long createCarModel(CarModel value) { //radi
        JAXBElement<CarModel> jaxbElement =
                new JAXBElement(new QName("http://www.car.com/car","createCarModel"),
                        CarModel.class, value);
        System.out.println("pre responsa");
        getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
        System.out.println("posle responsa");
        return 1L;
    }

    public Long createCar(Car value) {
        JAXBElement<Car> jaxbElement =
                new JAXBElement(new QName("http://www.car.com/car","createCar"),
                        Car.class, value);
        System.out.println("pre responsa");
        getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
        System.out.println("posle responsa");
        return 1L;
    }

    public Long createCarCarAccessories(CarCarAccessories value) { //radi
        JAXBElement<CarCarAccessories> jaxbElement =
                new JAXBElement(new QName("http://www.car.com/car","createCarCarAccessories"),
                        CarCarAccessories.class, value);
        System.out.println("pre responsa");
        getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
        System.out.println("posle responsa");
        return 1L;
    }

    public Long createMessage(Message value) {
        JAXBElement<Message> jaxbElement =
                new JAXBElement(new QName("http://www.car.com/car","createMessage"),
                        Message.class, value);
        System.out.println("pre responsa");
        getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
        System.out.println("posle responsa");
        return 1L;
    }

    public Long createAd(Ad value) {
        JAXBElement<Ad> jaxbElement =
                new JAXBElement(new QName("http://www.car.com/car","createAd"),
                        Ad.class, value);
        System.out.println("pre responsa");
        getWebServiceTemplate().marshalSendAndReceive(jaxbElement);
        System.out.println("posle responsa");
        return 1L;
    }

    //za ratinge



    //za komentare
}
