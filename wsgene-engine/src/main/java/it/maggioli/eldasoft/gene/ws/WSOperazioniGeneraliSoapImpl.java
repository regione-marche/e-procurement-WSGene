
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package it.maggioli.eldasoft.gene.ws;

import it.eldasoft.utils.utility.UtilityDate;
import it.maggioli.eldasoft.gene.bl.ComunicazioniManager;
import it.maggioli.eldasoft.gene.bl.DatiCodificatiManager;
import it.maggioli.eldasoft.gene.bl.DocumentiManager;
import it.maggioli.eldasoft.wsOperazioniGenerali.daticomuni.DatoCodificatoType;
import it.maggioli.eldasoft.wsOperazioniGenerali.daticomuni.ListaDatiCodificatiDocument;
import it.maggioli.eldasoft.wsOperazioniGenerali.daticomuni.ListaDatiCodificatiType;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

/**
 * This class was generated by Apache CXF 2.7.18
 * 2016-09-01T14:08:21.368+02:00
 * Generated source version: 2.7.18
 * 
 */

////////////////////////////////////////////////////////////////////////////////

// Gestione attachments con MTOM+XOP
// ---------------------------------
// abilitare la gestione configurando i parametri in 
//    wsgene-webapp/src/main/webapp/WEB-INF/xml/spring/operazioniGenerali.xml
/*
@SOAPBinding(
		style = SOAPBinding.Style.DOCUMENT,
	    use = SOAPBinding.Use.LITERAL,
	    parameterStyle = SOAPBinding.ParameterStyle.WRAPPED
)
@BindingType(
		value = javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_MTOM_BINDING
)		
@MTOM(
		enabled = true,
		threshold = 4000000
)
*/

////////////////////////////////////////////////////////////////////////////////

@javax.jws.WebService(
      serviceName = "WSOperazioniGenerali",
      portName = "WSOperazioniGeneraliSOAP",
      targetNamespace = "http://www.eldasoft.it/WSOperazioniGenerali/",
      endpointInterface = "it.maggioli.eldasoft.gene.ws.WSOperazioniGeneraliSoap"
)

public class WSOperazioniGeneraliSoapImpl implements WSOperazioniGeneraliSoap {

	private static final Logger LOG = Logger.getLogger(WSOperazioniGeneraliSoapImpl.class.getName());

	private DatiCodificatiManager datiCodificatiManager;

	private ComunicazioniManager comunicazioniManager;

	private DocumentiManager documentiManager;

	
//SEPR-81 FIX 
//	/**
//	 * inizializzazione del servizio 
//	 */
//	{
//		try {
////			Resources.setCharset(StandardCharsets.UTF_8);
//
//			java.lang.reflect.Field charset = Charset.class.getDeclaredField("defaultCharset");
//			charset.setAccessible(true);
//		    charset.set(null, null);
//		    System.setProperty("file.encoding", "UTF-8");
//		    
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}	    
//	}
	
	
	/**
	 * @param datiCodificatiManager
	 *            the datiCodificatiManager to set
	 */
	public void setDatiCodificatiManager(DatiCodificatiManager datiCodificatiManager) {
		this.datiCodificatiManager = datiCodificatiManager;
	}

	/**
	 * @param comunicazioniManager
	 *            the comunicazioniManager to set
	 */
	public void setComunicazioniManager(ComunicazioniManager comunicazioniManager) {
		this.comunicazioniManager = comunicazioniManager;
	}

	/**
	 * @param documentiManager the documentiManager to set
	 */
	public void setDocumentiManager(DocumentiManager documentiManager) {
		this.documentiManager = documentiManager;
	}
	
	/**
	 * Popola il documento XML con le occorrenze costituite da codice e
	 * descrizione a partire dai dati presenti nella lista
	 * 
	 * @param listaDati
	 *            lista di coppie (chiave,descrizione) da convertire in stringa
	 * @return documento XML contenente la lista di codifica
	 */
	public static String buildXML(List<DatoCodificatoType> listaDati) {
		ListaDatiCodificatiDocument documento = ListaDatiCodificatiDocument.Factory
		.newInstance();
		ListaDatiCodificatiType lista = documento.addNewListaDatiCodificati();
		lista.setElementoArray(listaDati.toArray(new DatoCodificatoType[]{}));
		return documento.toString();
	}

	public java.lang.String getElencoProvince() {
		if (LOG.isDebugEnabled())
			LOG.debug("getElencoProvince: inizio metodo");

		List<DatoCodificatoType> listaProvince = this.datiCodificatiManager
		.getTipiTab3("Agx15");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaProvince);

		if (LOG.isDebugEnabled())
			LOG.debug("getElencoProvince: fine metodo");

		return documentoXML;
	}

	public java.lang.String getElencoTipiNaturaGiuridica() {
		if (LOG.isDebugEnabled())
			LOG.debug("getElencoTipiNaturaGiuridica: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("G_043");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		if (LOG.isDebugEnabled())
			LOG.debug("getElencoTipiNaturaGiuridica: fine metodo");

		return documentoXML;
	}

	public java.lang.String getElencoTipiImpresa() {
		if (LOG.isDebugEnabled())
			LOG.debug("getElencoTipiImpresa: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("Ag008");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		if (LOG.isDebugEnabled())
			LOG.debug("getElencoTipiImpresa: fine metodo");

		return documentoXML;
	}

	public it.maggioli.eldasoft.gene.ws.ErroreOutType updateStatoComunicazioni(java.util.List<it.maggioli.eldasoft.gene.ws.DettaglioComunicazioneType> comunicazioni,java.lang.String stato) {
		if (LOG.isDebugEnabled()) {
			String ids = "[";
			for (DettaglioComunicazioneType d : comunicazioni) {
				ids += d.getId();
			}
			ids += "]";
			LOG.debug("updateStatoComunicazioni(" + ids + "," + stato
					+ "): inizio metodo");
		}

		ErroreOutType risultato = new ErroreOutType();

		try {
			this.comunicazioniManager.updateStatoComunicazioni(
					comunicazioni.toArray(new DettaglioComunicazioneType[]{}), 
					stato);
		} catch (DataAccessException e) {
		    LOG.error(
				    "Errore inaspettato durante l'interazione con la base dati",
				    e);
		    risultato.setErrore(e.getMessage());
		}
	    LOG.debug("updateStatoComunicazioni: fine metodo");

		return risultato;
	}

	public java.lang.String getElencoModalitaComunicazione() {
		if (LOG.isDebugEnabled())
			LOG.debug("getElencoModalitaComunicazione: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab3("Agx01");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		if (LOG.isDebugEnabled())
			LOG.debug("getElencoModalitaComunicazione: fine metodo");

		return documentoXML;
	}

	public java.lang.String getElencoOrganismiCertificatoriSOA() {
		if (LOG.isDebugEnabled())
			LOG.debug("getElencoOrganismiCertificatoriSOA: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("Ag020");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		if (LOG.isDebugEnabled())
			LOG.debug("getElencoOrganismiCertificatoriSOA: fine metodo");

		return documentoXML;
	}

	public java.lang.String getElencoTipiCertificazioneISO() {
		if (LOG.isDebugEnabled())
			LOG.debug("getElencoTipiCertificazioneISO: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("G_024");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		if (LOG.isDebugEnabled())
			LOG.debug("getElencoTipiCertificazioneISO: fine metodo");

		return documentoXML;
	}

	public it.maggioli.eldasoft.gene.ws.SendComunicazioneOutType sendComunicazione(
			it.maggioli.eldasoft.gene.ws.ComunicazioneType comunicazione) {
		LOG.debug("sendComunicazione: inizio metodo");

		SendComunicazioneOutType risultato = new SendComunicazioneOutType();

		try {
		DettaglioComunicazioneType dettaglio = comunicazione
			.getDettaglioComunicazione();

			// test idcomunicazione
			if (dettaglio.getId() == null) {
				// invio di una comunicazione ex-novo
				if (LOG.isDebugEnabled())
					LOG.debug("Invio di una nuova comunicazione");
				this.comunicazioniManager.insertComunicazione(comunicazione);
			} else {
				// reinoltro di una comunicazione aggiornata
				if (LOG.isDebugEnabled())
					LOG.debug("Aggiornamento di una comunicazione esistente");
				this.comunicazioniManager.updateComunicazione(comunicazione);
			}
			// ritorno la comunicazione eventualmente aggiornata nell'id e dataInserimento (caso di inserimento)
			risultato.setIdComunicazione(comunicazione.getDettaglioComunicazione().getId());

		} catch (DataAccessException e) {
			LOG.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato.setErrore(e.getMessage());
		} catch (Throwable e) {
			LOG.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato.setErrore(e.getMessage());
		}
		LOG.debug("sendComunicazione: fine metodo");

		return risultato;
	}
	
	public java.lang.String getElencoOrganismiCertificatoriISO() {
		if (LOG.isDebugEnabled())
			LOG.debug("getElencoOrganismiCertificatoriISO: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("Ag021");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		if (LOG.isDebugEnabled())
			LOG.debug("getElencoOrganismiCertificatoriISO: fine metodo");

		return documentoXML;
	}

	public java.lang.String getElencoGradiAbilitazionePreventiva() {
		if (LOG.isDebugEnabled())
			LOG.debug("getElencoGradiAbilitazionePreventiva: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("Ag018");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		if (LOG.isDebugEnabled())
			LOG.debug("getElencoGradiAbilitazionePreventiva: fine metodo");

		return documentoXML;
	}

	public it.maggioli.eldasoft.gene.ws.ProtocollaComunicazioneOutType protocollaComunicazione(java.lang.String applicativo,long id,java.lang.String numeroProtocollo,Date dataProtocollo,java.lang.String stato,it.maggioli.eldasoft.gene.ws.WSDocumentoType documento,java.util.List<it.maggioli.eldasoft.gene.ws.WSAllegatoType> allegati) { 
		if (LOG.isDebugEnabled()) {
			LOG.debug("protocollaComunicazione(" + applicativo + "," + id
					+ "," + numeroProtocollo
					+ "," + UtilityDate.convertiData(dataProtocollo, UtilityDate.FORMATO_GG_MM_AAAA)
					+ "," + stato
					+ "): inizio metodo");
		}

		ProtocollaComunicazioneOutType risultato = new ProtocollaComunicazioneOutType();
		try {
			it.maggioli.eldasoft.gene.ws.WSAllegatoType[] lista = null;
			if(allegati != null) { 
				lista = allegati.toArray(new it.maggioli.eldasoft.gene.ws.WSAllegatoType[]{});
			}			
			this.comunicazioniManager.updateProtocolloComunicazione(
					applicativo, id, numeroProtocollo, dataProtocollo, stato, documento, 
					lista);
		} catch (DataAccessException e) {
			LOG.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato.setErrore(e.getMessage());
		}

		LOG.debug("protocollaComunicazione: fine metodo");
		return risultato;
    }

	public it.maggioli.eldasoft.gene.ws.IsComunicazioneProcessataOutType isComunicazioneProcessata(java.lang.String applicativo,long id) { 
		if (LOG.isDebugEnabled())
			LOG.debug("isComunicazioneProcessata(" + applicativo + "," + id
					+ "): inizio metodo");

		IsComunicazioneProcessataOutType risultato = new IsComunicazioneProcessataOutType();

		try {
			Boolean comunicazioneProcessata = this.comunicazioniManager
			.isComunicazioneProcessata(applicativo, id);
			risultato.setComunicazioneProcessata(comunicazioneProcessata);
		} catch (DataAccessException e) {
			LOG
			.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato.setErrore(e.getMessage());
		}
		if (LOG.isDebugEnabled())
			LOG.debug("isComunicazioneProcessata: fine metodo");

		return risultato;
    }

	public java.lang.String getElencoTipiIndirizzo() {
		if (LOG.isDebugEnabled())
			LOG.debug("getElencoTipiIndirizzo: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("Ag030");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		if (LOG.isDebugEnabled())
			LOG.debug("getElencoTipiIndirizzo: fine metodo");

		return documentoXML;
	}

	public java.lang.String getElencoTipiAltraCarica() {
		LOG.debug("getElencoTipiAltraCarica: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("Ag007");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		LOG.debug("getElencoTipiAltraCarica: fine metodo");

		return documentoXML;
	}

	public it.maggioli.eldasoft.gene.ws.ErroreOutType updateSessionKeyComunicazione(java.lang.String applicativo,long id,java.lang.String sessionKey,java.lang.String stato) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateSessionKeyComunicazione(" + applicativo + ","
					+ id + "," + sessionKey + "," + stato + "): inizio metodo");
		}

		ErroreOutType risultato = new ErroreOutType();

		try {
			this.comunicazioniManager.updateSessionKeyComunicazione(
					applicativo, id, sessionKey, stato);
		} catch (DataAccessException e) {
			LOG.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato.setErrore(e.getMessage());
		}

		LOG.debug("updateSessionKeyComunicazione: fine metodo");
		return risultato;
    }

	public java.lang.String getElencoTipiCollaborazione() {
		LOG.debug("getElencoTipiCollaborazione: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("G_039");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		LOG.debug("getElencoTipiCollaborazione: fine metodo");

		return documentoXML;
	}

	public java.lang.String getElencoTipiTitoloTecnico() {
		LOG.debug("getElencoTipiTitoloTecnico: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("Ag004");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		LOG.debug("getElencoTipiTitoloTecnico: fine metodo");

		return documentoXML;
	}

	public java.lang.String getElencoTipiAlboProfessionale() {
		LOG.debug("getElencoTipiAlboProfessionale: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("G_040");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		LOG.debug("getElencoTipiAlboProfessionale: fine metodo");

		return documentoXML;
	}

	public java.lang.String getElencoTipiCassaPrevidenza() {
		LOG.debug("getElencoTipiAltraCarica: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("G_041");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		LOG.debug("getElencoTipiAltraCarica: fine metodo");

		return documentoXML;
	}

	public java.lang.String getElencoAliquoteIVA() {
		LOG.debug("getElencoAliquoteIVA: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("G_055");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		LOG.debug("getElencoAliquoteIVA: fine metodo");

		return documentoXML;
	}

	public java.lang.String getElencoSettoriProduttivi() {
		LOG.debug("getElencoSettoriProduttivi: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("G_056");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		LOG.debug("getElencoSettoriProduttivi: fine metodo");

		return documentoXML;
	}

	public it.maggioli.eldasoft.gene.ws.DocumentoOutType getDocumento(java.lang.String idProgramma,long idDocumento,java.lang.String tokenRichiedente) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getDocumento(" + idProgramma + "," + idDocumento
					+ "," + tokenRichiedente + "): inizio metodo");
		}

		DocumentoOutType risultato = new DocumentoOutType();
		try {
			FileType file = this.documentiManager.getDocumentoDigitale(
					idProgramma, idDocumento, tokenRichiedente);
			risultato.setFile(file);
		} catch (DataAccessException e) {
			LOG.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato.setErrore(e.getMessage());
		}

		LOG.debug("getDocumento: fine metodo");
		return risultato;
    }

	public String getUsernameDocumentoRiservato(java.lang.String idProgramma, long idDocumento) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("getUsernameDocumentoRiservato(" + 
					idProgramma + "," + Long.toString(idDocumento) + "): inizio metodo");
		}

		LOG.debug("getUsernameDocumentoRiservato: inizio metodo");

		String risultato = null;
		try {
			risultato = this.documentiManager.getUsernameDocumentoRiservato(
					idProgramma, idDocumento);
		} catch (DataAccessException e) {
			LOG.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato = null;
		}

		LOG.debug("getUsernameDocumentoRiservato: fine metodo");
		return risultato;		
    }

	public java.lang.String getElencoNazioni() {
		LOG.debug("getElencoNazioni: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab1("Ag010");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		LOG.debug("getElencoNazioni: fine metodo");

		return documentoXML;
	}

	public it.maggioli.eldasoft.gene.ws.GetComunicazioneOutType getComunicazione(			
			java.lang.String applicativo, 
			long id,
			java.lang.String idDocumento) { 
		if (LOG.isDebugEnabled())
			LOG.debug("getComunicazione(" + applicativo + "," + id + "," + idDocumento
					  + "): inizio metodo");

		GetComunicazioneOutType risultato = new GetComunicazioneOutType();
		try {
			ComunicazioneType comunicazione = this.comunicazioniManager
				.getComunicazione(applicativo, id, idDocumento);
			risultato.setComunicazione(comunicazione);
		} catch (DataAccessException e) {
			LOG
			.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato.setErrore(e.getMessage());
		}
		if (LOG.isDebugEnabled())
			LOG.debug("getComunicazione: fine metodo");

		return risultato;
    }

	public it.maggioli.eldasoft.gene.ws.ErroreOutType updateDataLetturaDestinatarioComunicazione(java.lang.String applicativo,long idComunicazione,long idDestinatario) { 
		if (LOG.isDebugEnabled()) {
			LOG.debug("updateDataLetturaDestinatarioComunicazione(" + applicativo + "," + idComunicazione+ "," + idDestinatario
					+ "): inizio metodo");
		}

		ErroreOutType risultato = new ErroreOutType();

		try {
			this.comunicazioniManager.updateDataLetturaDestinatarioComunicazione(applicativo, idComunicazione, idDestinatario);
		} catch (DataAccessException e) {
			LOG.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato.setErrore(e.getMessage());
		}

		LOG.debug("updateDataLetturaDestinatarioComunicazione: fine metodo");

		return risultato;
    }

	public java.lang.String getElencoTipiComunicazione() {
		LOG.debug("getElencoTipiComunicazione: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab3("G_x10");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		LOG.debug("getElencoTipiComunicazione: fine metodo");

		return documentoXML;
	}

	public it.maggioli.eldasoft.gene.ws.GetElencoComunicazioniOutType getElencoComunicazioni(it.maggioli.eldasoft.gene.ws.DettaglioComunicazioneType testata) {
		if (LOG.isDebugEnabled())
			LOG.debug("getElencoComunicazioni: inizio metodo");

		GetElencoComunicazioniOutType risultato = new GetElencoComunicazioniOutType();

		try {
			List<DettaglioComunicazioneType> comunicazioni = this.comunicazioniManager
			.getElencoComunicazioni(testata); //criteriRicerca);	    
			risultato.setComunicazione(comunicazioni);

		} catch (DataAccessException e) {
			LOG
			.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato.setErrore(e.getMessage());
		}

		if (LOG.isDebugEnabled())
			LOG.debug("getElencoComunicazioni: fine metodo");

		return risultato;
    }

	public java.lang.String getElencoStatiComunicazione() {
		LOG.debug("getElencoStatiComunicazione: inizio metodo");

		List<DatoCodificatoType> listaTipi = this.datiCodificatiManager
		.getTipiTab2("G_z20");
		String documentoXML = WSOperazioniGeneraliSoapImpl.buildXML(listaTipi);

		LOG.debug("getElencoStatiComunicazione: fine metodo");

		return documentoXML;
	}

	public it.maggioli.eldasoft.gene.ws.ErroreOutType deleteComunicazione(java.lang.String applicativo,long id) {
		if (LOG.isDebugEnabled())
			LOG.debug("deleteComunicazione(" + applicativo + "," + id
					+ "): inizio metodo");

		ErroreOutType risultato = new ErroreOutType();

		try {
			this.comunicazioniManager.deleteComunicazione(applicativo, id);
		} catch (DataAccessException e) {
			LOG.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato.setErrore(e.getMessage());
		}

		LOG.debug("deleteComunicazione: fine metodo");

		return risultato;
    }


	public ErroreOutType updateStatoComunicazioni(
			DettaglioComunicazioneType[] comunicazioni, String stato) {
		if (LOG.isDebugEnabled()) {
			String ids = "[";
			for (DettaglioComunicazioneType d : comunicazioni) {
				ids += d.getId();
			}
			ids += "]";
			LOG.debug("updateStatoComunicazioni(" + ids + "," + stato
					+ "): inizio metodo");
		}

		ErroreOutType risultato = new ErroreOutType();

		try {
			this.comunicazioniManager.updateStatoComunicazioni(comunicazioni, stato);
		} catch (DataAccessException e) {
			LOG
			.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato.setErrore(e.getMessage());
		}
		LOG.debug("updateStatoComunicazioni: fine metodo");

		return risultato;
	}

	public ProtocollaComunicazioneOutType protocollaComunicazione(String applicativo, long id,
			String numeroProtocollo, Date dataProtocollo, String stato,
			WSDocumentoType documento, WSAllegatoType[] allegati) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("protocollaComunicazione(" + applicativo + "," + id
					+ "," + numeroProtocollo
					+ "," + UtilityDate.convertiData(dataProtocollo, UtilityDate.FORMATO_GG_MM_AAAA)
					+ "," + stato
					+ "): inizio metodo");
		}

		ProtocollaComunicazioneOutType risultato = new ProtocollaComunicazioneOutType();
		try {
			this.comunicazioniManager.updateProtocolloComunicazione(
					applicativo, id,
					numeroProtocollo, dataProtocollo, stato, documento, allegati);
		} catch (DataAccessException e) {
			LOG.error(
					"Errore inaspettato durante l'interazione con la base dati",
					e);
			risultato.setErrore(e.getMessage());
		}

		LOG.debug("protocollaComunicazione: fine metodo");
		return risultato;
	}

}
