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
 * <p>Java class for rating complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rating"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ratingID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="grade" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="simpleUser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rating", propOrder = {
    "ratingID",
    "grade",
    "simpleUser",
    "adID"
})
public class Rating {

    protected String ratingID;
    protected String grade;
    protected String simpleUser;
    protected String adID;

    /**
     * Gets the value of the ratingID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRatingID() {
        return ratingID;
    }

    /**
     * Sets the value of the ratingID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRatingID(String value) {
        this.ratingID = value;
    }

    /**
     * Gets the value of the grade property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Sets the value of the grade property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrade(String value) {
        this.grade = value;
    }

    /**
     * Gets the value of the simpleUser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSimpleUser() {
        return simpleUser;
    }

    /**
     * Sets the value of the simpleUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSimpleUser(String value) {
        this.simpleUser = value;
    }

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

}
