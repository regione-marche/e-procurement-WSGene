package it.maggioli.eldasoft.gene.ws;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.17
 * 2016-09-13T12:00:21.430+02:00
 * Generated source version: 2.7.17
 * 
 */
@WebServiceClient(name = "WSOperazioniGenerali", 
                  wsdlLocation = "http://localhost:8100/WSGene/services/WSOperazioniGeneraliSOAP?wsdl",
                  targetNamespace = "http://www.eldasoft.it/WSOperazioniGenerali/") 
public class WSOperazioniGenerali extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://www.eldasoft.it/WSOperazioniGenerali/", "WSOperazioniGenerali");
    public final static QName WSOperazioniGeneraliSOAP = new QName("http://www.eldasoft.it/WSOperazioniGenerali/", "WSOperazioniGeneraliSOAP");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/WSGene/services/WSOperazioniGeneraliSOAP?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(WSOperazioniGenerali.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:8080/WSGene/services/WSOperazioniGeneraliSOAP?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public WSOperazioniGenerali(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public WSOperazioniGenerali(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public WSOperazioniGenerali() {
        super(WSDL_LOCATION, SERVICE);
    }
    

    /**
     *
     * @return
     *     returns WSOperazioniGeneraliSoap
     */
    @WebEndpoint(name = "WSOperazioniGeneraliSOAP")
    public WSOperazioniGeneraliSoap getWSOperazioniGeneraliSOAP() {
        return super.getPort(WSOperazioniGeneraliSOAP, WSOperazioniGeneraliSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns WSOperazioniGeneraliSoap
     */
    @WebEndpoint(name = "WSOperazioniGeneraliSOAP")
    public WSOperazioniGeneraliSoap getWSOperazioniGeneraliSOAP(WebServiceFeature... features) {
        return super.getPort(WSOperazioniGeneraliSOAP, WSOperazioniGeneraliSoap.class, features);
    }

}
