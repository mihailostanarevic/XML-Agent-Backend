//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.07.11 at 11:06:49 PM CEST 
//


package com.rentacar.agentbackend.soap.wsdl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ad complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ad"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="adID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="carModel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="gearshiftType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fuelType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="agentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="limitedDistance" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="availableKilometersPerRent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="kilometersTraveled" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="seats" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="cdw" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="simpleUser" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ad", propOrder = {
    "adID",
    "carModel",
    "gearshiftType",
    "fuelType",
    "agentId",
    "limitedDistance",
    "availableKilometersPerRent",
    "kilometersTraveled",
    "seats",
    "cdw",
    "simpleUser"
})
public class Ad {

    protected String adID;
    protected String carModel;
    protected String gearshiftType;
    protected String fuelType;
    protected String agentId;
    protected boolean limitedDistance;
    protected String availableKilometersPerRent;
    protected String kilometersTraveled;
    protected int seats;
    protected boolean cdw;
    protected boolean simpleUser;

    /**
     * Gets the value of the adID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdID() {
        return adID;
    }

    /**
     * Sets the value of the adID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdID(String value) {
        this.adID = value;
    }

    /**
     * Gets the value of the carModel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarModel() {
        return carModel;
    }

    /**
     * Sets the value of the carModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarModel(String value) {
        this.carModel = value;
    }

    /**
     * Gets the value of the gearshiftType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGearshiftType() {
        return gearshiftType;
    }

    /**
     * Sets the value of the gearshiftType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGearshiftType(String value) {
        this.gearshiftType = value;
    }

    /**
     * Gets the value of the fuelType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFuelType() {
        return fuelType;
    }

    /**
     * Sets the value of the fuelType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFuelType(String value) {
        this.fuelType = value;
    }

    /**
     * Gets the value of the agentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentId() {
        return agentId;
    }

    /**
     * Sets the value of the agentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentId(String value) {
        this.agentId = value;
    }

    /**
     * Gets the value of the limitedDistance property.
     * 
     */
    public boolean isLimitedDistance() {
        return limitedDistance;
    }

    /**
     * Sets the value of the limitedDistance property.
     * 
     */
    public void setLimitedDistance(boolean value) {
        this.limitedDistance = value;
    }

    /**
     * Gets the value of the availableKilometersPerRent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAvailableKilometersPerRent() {
        return availableKilometersPerRent;
    }

    /**
     * Sets the value of the availableKilometersPerRent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAvailableKilometersPerRent(String value) {
        this.availableKilometersPerRent = value;
    }

    /**
     * Gets the value of the kilometersTraveled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKilometersTraveled() {
        return kilometersTraveled;
    }

    /**
     * Sets the value of the kilometersTraveled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKilometersTraveled(String value) {
        this.kilometersTraveled = value;
    }

    /**
     * Gets the value of the seats property.
     * 
     */
    public int getSeats() {
        return seats;
    }

    /**
     * Sets the value of the seats property.
     * 
     */
    public void setSeats(int value) {
        this.seats = value;
    }

    /**
     * Gets the value of the cdw property.
     * 
     */
    public boolean isCdw() {
        return cdw;
    }

    /**
     * Sets the value of the cdw property.
     * 
     */
    public void setCdw(boolean value) {
        this.cdw = value;
    }

    /**
     * Gets the value of the simpleUser property.
     * 
     */
    public boolean isSimpleUser() {
        return simpleUser;
    }

    /**
     * Sets the value of the simpleUser property.
     * 
     */
    public void setSimpleUser(boolean value) {
        this.simpleUser = value;
    }

}
