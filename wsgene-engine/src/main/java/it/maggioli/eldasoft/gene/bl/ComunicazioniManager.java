/*
 * Created on 25/ott/10
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.maggioli.eldasoft.gene.bl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.maggioli.eldasoft.gene.db.dao.ComunicazioniDao;
import it.maggioli.eldasoft.gene.ws.AllegatoComunicazioneType;
import it.maggioli.eldasoft.gene.ws.ComunicazioneType;
import it.maggioli.eldasoft.gene.ws.DettaglioComunicazioneType;
import it.maggioli.eldasoft.gene.ws.WSAllegatoType;
import it.maggioli.eldasoft.gene.ws.WSDocumentoType;

/**
 * Manager per la gestione delle comunicazioni
 * 
 * @author Stefano.Sabbadin
 */
public class ComunicazioniManager {

    private ComunicazioniDao comunicazioniDao;
    
    private GenChiaviManager genChiaviManager;

    /**
     * @param comunicazioniDao
     *            the comunicazioniDao to set
     */
	public void setComunicazioniDao(ComunicazioniDao comunicazioniDao) {
		this.comunicazioniDao = comunicazioniDao;
	}
    
    /**
	 * @param genChiaviManager the genChiaviManager to set
	 */
	public void setGenChiaviManager(GenChiaviManager genChiaviManager) {
		this.genChiaviManager = genChiaviManager;
	}



	public void insertComunicazione(ComunicazioneType comunicazione) {
		DettaglioComunicazioneType dettaglioComunicazione = comunicazione
			.getDettaglioComunicazione();
		Long id = null;
	
		synchronized (ComunicazioniManager.class) {
		    // estrazione dell'id da utilizzare per l'inserimento
			id = this.comunicazioniDao
			    .getMaxIdComunicazione(dettaglioComunicazione
				    .getApplicativo());
		    dettaglioComunicazione.setId(new Long(id.longValue() + 1));
	
		    // inserimento della testata della comunicazione
		    this.comunicazioniDao.insertComunicazione(dettaglioComunicazione);
		}
		insertAllegatiComunicazione(comunicazione);
    }

	public void updateComunicazione(ComunicazioneType comunicazione) {
		DettaglioComunicazioneType dettaglioComunicazione = comunicazione
				.getDettaglioComunicazione();

		// aggiornamento della testata della comunicazione
		this.comunicazioniDao.updateComunicazione(dettaglioComunicazione);

		// aggiornamento degli allegati
		updateAllegatiComunicazione(comunicazione);
	}

	/**
	 * Aggiorna i documenti allegati. Si agganciano i documenti ricevuti ai
	 * corrispondenti esistenti, si inseriscono eventuali nuovi documenti, si
	 * rimuovono i documenti presenti in w_docdig ma non ricevuti tra gli
	 * allegati.
	 * 
	 * @param comunicazione
	 *            dati della comunicazione da aggiornare
	 */
	private void updateAllegatiComunicazione(ComunicazioneType comunicazione) {
		DettaglioComunicazioneType dettaglioComunicazione = comunicazione.getDettaglioComunicazione();
		
		List<AllegatoComunicazioneType> allegatiEsistenti = this.comunicazioniDao
				.getAllegatiComunicazione(
						dettaglioComunicazione.getApplicativo(),
						dettaglioComunicazione.getId(),
						null);
		
		// popolamento dati di supporto
		Set<Long> idAllegatiDaRimuovere = new HashSet<Long>();
		Map<String, Long> hashNomeFileId = new HashMap<String, Long>();
		String key;
		for (AllegatoComunicazioneType allegato : allegatiEsistenti) {
			// elenco degli id documenti, dal quale tolgo poi quelli da aggiornare
			idAllegatiDaRimuovere.add(allegato.getId());
			// hash per reperire agevolmente l'id documento dal nome file			
			key = allegato.getNomeFile() + "|" +					
				  //BUG: con la "descrizione" se carico 2 "doc richiesti" con lo stesso nomefile il I viene sovrascritto dal II!!!
			      //(allegato.getDescrizione() != null ? allegato.getDescrizione() : ""); 	
				  (allegato.getUuid() != null ? allegato.getUuid() : "");			
			hashNomeFileId.put(key, allegato.getId());
		}
		
		if (comunicazione.getAllegato() != null) {
			Long idAllegato = null;
			int progressivo = 0;
			for (int i = 0; i < comunicazione.getAllegato().size(); i++) {
				AllegatoComunicazioneType allegato = comunicazione
						.getAllegato().get(i);
							
				key = allegato.getNomeFile() + "|" + 
					  (allegato.getUuid() != null ? allegato.getUuid() : "");

				if (hashNomeFileId.containsKey(key)) {
					// esiste l'allegato nella comunicazione da aggiornare, 
					// mi aggancio allo stesso record e lo aggiorno
					allegato.setId(hashNomeFileId.get(key));
					idAllegatiDaRimuovere.remove(allegato.getId());					
					this.comunicazioniDao.updateAllegato(
							dettaglioComunicazione.getApplicativo(),
							allegato.getId(), 
							dettaglioComunicazione.getId(), 
							++progressivo, 
							allegato);
				} else {
					// si tratta di un nuovo record da allegare, pertanto
					// procedo all'inserimento
					if (idAllegato == null) {
						idAllegato = this.comunicazioniDao
								.getMaxIdAllegato(dettaglioComunicazione
										.getApplicativo());
					}
					this.comunicazioniDao.insertAllegato(
							dettaglioComunicazione.getApplicativo(),
							++idAllegato, 
							dettaglioComunicazione.getId(),
							++progressivo, 
							allegato);
				}
			}
		}
		
		for (Iterator<Long> iterator = idAllegatiDaRimuovere.iterator(); iterator.hasNext();) {
			// tutti i record presenti in precedenza in backoffice e non aggiornati 
			// vanno a questo punto rimossi
			Long idDaRimuovere = (Long) iterator.next();
			this.comunicazioniDao.deleteAllegato(
					dettaglioComunicazione.getApplicativo(),
					idDaRimuovere);
		}
	}

    /**
     * Cicla sugli allegati di una comunicazione e procede con l'inserimento
     * degli stessi
     * 
     * @param comunicazione
     *            comunicazione dalla quale estrarre ed inserire gli allegati
     *            digitali
     */
    private void insertAllegatiComunicazione(ComunicazioneType comunicazione) {
		DettaglioComunicazioneType dettaglioComunicazione = comunicazione
			.getDettaglioComunicazione();
		if (comunicazione.getAllegato() != null) {
		    Long idAllegato = this.comunicazioniDao
			    .getMaxIdAllegato(dettaglioComunicazione.getApplicativo());
		    for (int i = 0; i < comunicazione.getAllegato().size(); i++) {
				this.comunicazioniDao.insertAllegato(
						dettaglioComunicazione.getApplicativo(), 
						++idAllegato, 
						dettaglioComunicazione.getId(), 
						(i + 1), 
						comunicazione.getAllegato().get(i));
		    }
		}
    }

	/**
	 * Aggiorna le comunicazioni in input allo stato indicato.
	 * 
	 * @param comunicazioni
	 *            comunicazioni da aggiornare
	 * @param stato
	 *            nuovo stato da assegnare
	 */
	public void updateStatoComunicazioni(
			DettaglioComunicazioneType[] comunicazioni, String stato) {
		this.comunicazioniDao.updateStatoComunicazioni(comunicazioni, stato);
	}

	/**
	 * Aggiorna la data lettura per il destinatario della comunicazione in input.
	 * @param applicativo id applicativo
	 * @param idComunicazione id comunicazione 
	 * @param idDestinatario id destinatario
	 */
	public void updateDataLetturaDestinatarioComunicazione(String applicativo,
			long idComunicazione, long idDestinatario) {
		this.comunicazioniDao.updateDataLetturaDestinatarioComunicazione(
				applicativo, idComunicazione, idDestinatario);
	}
	
	/**
	 * Elimina la comunicazione in input.
	 * 
     * @param applicativo
     *            id applicativo
     * @param id
     *            id comunicazione
	 */
	public void deleteComunicazione(String applicativo, long id) {
		this.comunicazioniDao.deleteComunicazione(applicativo, id);
		this.comunicazioniDao.deleteAllegati(applicativo, id);
	}

    /**
     * Estrae una comunicazione e tutti i suoi allegati a partire dalla chiave.
     * 
     * @param applicativo
     *            id applicativo
     * @param id
     *            id comunicazione
     * @param idDocumento
     *            id della busta xml o UUID del allegato allegato da scaricare
     *            
     * @return comunicazione individuata a partire dalla chiave
     */
    public ComunicazioneType getComunicazione(String applicativo, long id, String idDocumento) {
		DettaglioComunicazioneType dettaglio = this.comunicazioniDao
			.getDettaglioComunicazione(applicativo, id);
		List<AllegatoComunicazioneType> allegati = this.comunicazioniDao
			.getAllegatiComunicazione(applicativo, id, idDocumento);
	
		ComunicazioneType comunicazione = new ComunicazioneType();
		comunicazione.setDettaglioComunicazione(dettaglio);
		comunicazione.setAllegato(allegati);			

		return comunicazione;
    }

    /**
     * Esegue una ricerca di comunicazione in base ai criteri di filtro in input
     * 
     * @param criteriRicerca
     *            criteri di ricerca per filtrare le comunicazione
     * @return lista delle testate delle comunicazioni che rispettano i criteri
     *         di filtro
     */
    public List<DettaglioComunicazioneType> getElencoComunicazioni(
	    DettaglioComunicazioneType criteriRicerca) {
	return this.comunicazioniDao.getElencoComunicazioni(criteriRicerca);
    }

    /**
     * Verifica se una comunicazione e' stata processata dal backoffice. Ritorna
     * utile per verificare se nel lasso temporale tra la rilettura di una
     * comunicazione e il suo salvataggio delle modifiche viene processata dal
     * backoffice, perche' in tal caso va bloccato l'aggiornamento del record.
     * 
     * @param applicativo
     *            id applicativo
     * @param id
     *            id comunicazione
     * @return true se la comunicazione e' stata processata, false altrimenti
     */
    public Boolean isComunicazioneProcessata(String applicativo, long id) {
	return this.comunicazioniDao.isComunicazioneProcessata(applicativo, id);
    }

	/**
	 * Aggiorna i dati di protocollazione, eventualmente variando lo stato se
	 * specificato in input, ed associando un documento se indicato.
	 * 
	 * @param applicativo
	 *            id applicativo
	 * @param id
	 *            id comunicazione
	 * @param numeroProtocollo
	 *            numero di protocollo (obbligatorio) da attribuire
	 * @param dataProtocollo
	 *            data protocollo (obbligatoria) da attribuire
	 * @param stato
	 *            eventuale nuovo stato della comunicazione, da valorizzare solo
	 *            se si intende variare
	 * @param documento
	 *            eventuali coordinate del documento da associare al protocollo,
	 *            se necessario
	 * @param allegati
	 *            eventuali allegati da associare al protocollo,
	 *            se necessario
	 */
	public void updateProtocolloComunicazione(String applicativo, long id,
			String numeroProtocollo, Date dataProtocollo, String stato,
			WSDocumentoType documento, WSAllegatoType[] allegati) {
		long idDocumento = -1;
		this.comunicazioniDao.updateProtocolloComunicazione(applicativo, id, numeroProtocollo, dataProtocollo, stato);
		if (documento != null) {
			idDocumento = this.genChiaviManager.getNextId("WSDOCUMENTO");
			this.comunicazioniDao.insertDocumentoProtocollo(idDocumento, documento);
		}
		if (allegati != null) {
			long idAllegato = this.genChiaviManager.getNextId("WSALLEGATI", allegati.length);
			for (WSAllegatoType allegato : allegati) {
				if (idDocumento != -1) {
					allegato.setIdDocumento(idDocumento);
				}
				this.comunicazioniDao.insertAllegatoProtocollo(idAllegato++, allegato);	
			}
		}
		
	}

	/**
	 * Aggiorna la chiave di sessione per la cifratura dei dati confidenziali
	 * per la comunicazioni in input, eventualmente aggiornando anche lo stato
	 * se valorizzato.
	 * 
	 * @param applicativo
	 *            id applicativo
	 * @param id
	 *            id comunicazione
	 * @param sessionKey
	 *            chiave di cifratura di sessione
	 * @param stato
	 *            eventuale nuovo stato della comunicazione, da valorizzare solo
	 *            se si intende variare
	 */
	public void updateSessionKeyComunicazione(String applicativo, long id,
			String sessionKey, String stato) {
		this.comunicazioniDao.updateSessionKeyComunicazione(
				applicativo, id, sessionKey, stato);
	}

}
