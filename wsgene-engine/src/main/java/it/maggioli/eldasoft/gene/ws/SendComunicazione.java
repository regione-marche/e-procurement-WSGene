
package it.maggioli.eldasoft.gene.ws;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="comunicazione" type="{http://www.eldasoft.it/WSOperazioniGenerali/}ComunicazioneType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "comunicazione"
})
@XmlRootElement(name = "SendComunicazione")
public class SendComunicazione
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(required = true)
    protected ComunicazioneType comunicazione;

    /**
     * Gets the value of the comunicazione property.
     * 
     * @return
     *     possible object is
     *     {@link ComunicazioneType }
     *     
     */
    public ComunicazioneType getComunicazione() {
        return comunicazione;
    }

    /**
     * Sets the value of the comunicazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComunicazioneType }
     *     
     */
    public void setComunicazione(ComunicazioneType value) {
        this.comunicazione = value;
    }

}
