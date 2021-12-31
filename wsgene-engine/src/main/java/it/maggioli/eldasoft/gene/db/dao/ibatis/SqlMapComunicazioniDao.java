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
package it.maggioli.eldasoft.gene.db.dao.ibatis;

import it.maggioli.eldasoft.gene.db.dao.ComunicazioniDao;
import it.maggioli.eldasoft.gene.ws.AllegatoComunicazioneType;
import it.maggioli.eldasoft.gene.ws.DettaglioComunicazioneType;
import it.maggioli.eldasoft.gene.ws.WSAllegatoType;
import it.maggioli.eldasoft.gene.ws.WSDocumentoType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * SqlMap per iBatis
 * 
 * @author Stefano.Sabbadin
 */
public class SqlMapComunicazioniDao extends SqlMapClientDaoSupport implements
	ComunicazioniDao {

	private static final Logger LOG = Logger.getLogger(SqlMapComunicazioniDao.class.getName());

	public Long getMaxIdComunicazione(String applicativo)
	throws DataAccessException {
		Long id = (Long) this.getSqlMapClientTemplate().queryForObject(
				"getMaxIdComunicazione", applicativo);
		if (id == null)
			id = new Long(0);
		return id;
	}

	public void insertComunicazione(DettaglioComunicazioneType comunicazione)
	throws DataAccessException {
		HashMap<String, Object> hash = getHashForQuery(comunicazione);
		hash.put("dataInserimento", new Date());
		this.getSqlMapClientTemplate().insert("insertComunicazione", hash);
	}

	public void updateComunicazione(DettaglioComunicazioneType comunicazione)
	throws DataAccessException {
		HashMap<String, Object> hash = getHashForQuery(comunicazione);
		this.getSqlMapClientTemplate().update("updateComunicazione", hash);
	}

	/**
	 * Crea la hash con i valori comuni necessari per l'esecuzione di una query
	 * di estrazione o aggiornamento sulla testata della comunicazione
	 * 
	 * @param comunicazione
	 *            comunicazione da inserire o aggiornare o filtrare
	 * @return hash con i valori della comunicazione
	 */
	private HashMap<String, Object> getHashForQuery(
			DettaglioComunicazioneType comunicazione) {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("applicativo", comunicazione.getApplicativo());
		hash.put("id", comunicazione.getId());
		hash.put("entita", comunicazione.getEntita());
		hash.put("chiave1", comunicazione.getChiave1());
		hash.put("chiave2", comunicazione.getChiave2());
		hash.put("chiave3", comunicazione.getChiave3());
		hash.put("chiave4", comunicazione.getChiave4());
		hash.put("chiave5", comunicazione.getChiave5());
		hash.put("idOperatore", comunicazione.getIdOperatore());
		hash.put("mittente", comunicazione.getMittente());
		hash.put("stato", comunicazione.getStato());
		hash.put("oggetto", comunicazione.getOggetto());
		hash.put("testo", comunicazione.getTesto());
		Integer comPubb = null;
//		if (comunicazione.getComunicazionePubblica() != null) { 
		if (comunicazione.isComunicazionePubblica() != null) {
			comPubb = (comunicazione.isComunicazionePubblica().booleanValue() ? 1 : 2);
		}
		hash.put("comunicazionePubblica", comPubb);
		hash.put("tipoComunicazione", comunicazione.getTipoComunicazione());
		hash.put("dataAggStato", new Date());
		hash.put("dataPubblicazione", comunicazione.getDataPubblicazione());
		hash.put("sessionKey", comunicazione.getSessionKey());
		hash.put("applicativoRisposta", comunicazione.getApplicativoRisposta());
		hash.put("idRisposta", comunicazione.getIdRisposta());
		hash.put("modello", comunicazione.getModello());
		hash.put("tipoBusta", comunicazione.getTipoBusta());
		return hash;
	}

	public Long getMaxIdAllegato(String applicativo) throws DataAccessException {
		Long id = (Long) this.getSqlMapClientTemplate().queryForObject(
				"getMaxIdAllegato", applicativo);
		if (id == null)
			id = new Long(0);
		return id;
	}

	public void insertAllegato(String applicativo, Long idAllegato,
			Long idComunicazione, int progressivoAllegato,
			AllegatoComunicazioneType allegato) throws DataAccessException {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("applicativo", applicativo);
		hash.put("id", idAllegato);
		hash.put("entita", "W_INVCOM");
		hash.put("idComunicazione", idComunicazione);
		hash.put("progressivo", progressivoAllegato);
		hash.put("tipo", allegato.getTipo());
		hash.put("nomeFile", allegato.getNomeFile());
		hash.put("descrizione", allegato.getDescrizione());
		hash.put("file", allegato.getFile());
		hash.put("uuid", (!StringUtils.isEmpty(allegato.getUuid()) ? allegato.getUuid() : null) );

		boolean isEmptyStream = (allegato.getFile() == null || 
   			    			     (allegato.getFile() != null && allegato.getFile().length <= 0));

		if(isEmptyStream) {
			// 21/01/2019 (bug rilevato a "Chieti")
			// Warning per insert di un allegato senza contenuto!!!
			LOG.warn( 
	             "Insert attachment with Null stream or no stream in W_DOGDIG_DIGOGG (" +
	             "applicativo=" + applicativo + ", " +
	        	 "idComunicazione=" + idComunicazione + ", " + 
	        	 "id=" + idAllegato + ", " + 
	        	 "nomeFile=" + allegato.getNomeFile() +
	        	 ")");
		}

		if( !isEmptyStream ) {
			this.getSqlMapClientTemplate().insert("insertAllegato", hash);
		}
	}

	public void updateAllegato(
			String applicativo, Long idAllegato, Long idComunicazione, 
			int progressivoAllegato, AllegatoComunicazioneType allegato) 
	{
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("applicativo", applicativo);
		hash.put("id", idAllegato);
		hash.put("entita", "W_INVCOM");
		hash.put("idComunicazione", idComunicazione);
		hash.put("progressivo", progressivoAllegato);
		hash.put("tipo", allegato.getTipo());
		hash.put("nomeFile", allegato.getNomeFile());
		hash.put("descrizione", allegato.getDescrizione());
		String uuid = (!StringUtils.isEmpty(allegato.getUuid())	? allegato.getUuid() : null);	
		if(uuid != null) {
			// 2.0.0
			// in caso di gestione degli allegati fuori dalla busta xml 
			// gestisci l'allegato solo se e' stato modificato
			if(allegato.getModificato() != 0) {
				
				// 21/01/2019 (bug rilevato a "Chieti")
				// Warning per update di un allegato senza contenuto!!!
				// Si evita di aggiornare il contenuto di un allegato nel caso
				// quest'ultimo sia marcato come "modificato" ma non sia presente 
				// il relativo contenuto.
				boolean isEmptyStream = (allegato.getFile() == null || 
						   			    (allegato.getFile() != null && allegato.getFile().length <= 0));
				
				if( !isEmptyStream ) {
					hash.put("file", allegato.getFile());
				} else {
					LOG.warn( 
                     "Update attachment with Null stream denied in W_DOGDIG_DIGOGG (" +
                     "applicativo=" + applicativo + ", " +
                	 "idComunicazione=" + idComunicazione + ", " + 
                	 "id=" + idAllegato + ", " + 
                	 "nomeFile=" + allegato.getNomeFile() +
                	 ")");
				}
			}
		} else {
			// 1.14.x
			// i documenti allegati sono all'interno della busta xml
			hash.put("file", allegato.getFile());
		}
		hash.put("uuid", uuid);

		this.getSqlMapClientTemplate().update("updateAllegato", hash);
	}

	public void deleteAllegato(String applicativo, Long idAllegato) {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("applicativo", applicativo);
		hash.put("id", String.valueOf(idAllegato));
		this.getSqlMapClientTemplate().delete("deleteAllegato", hash);
	}

	public void deleteAllegati(String applicativo, Long idComunicazione)
	throws DataAccessException {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("applicativo", applicativo);
		hash.put("entita", "W_INVCOM");
		hash.put("idComunicazione", String.valueOf(idComunicazione));

		this.getSqlMapClientTemplate().delete("deleteAllegati", hash);
	}

	public void updateStatoComunicazioni(
			DettaglioComunicazioneType[] comunicazioni, String stato)
	throws DataAccessException {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("comunicazioni", comunicazioni);
		hash.put("stato", stato);
		this.getSqlMapClientTemplate().update("updateStatoComunicazioni", hash);
	}

	public void updateDataLetturaDestinatarioComunicazione(String applicativo,
			long idComunicazione, long idDestinatario) {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("applicativo", applicativo);
		hash.put("idComunicazione", idComunicazione);
		hash.put("idDestinatario", idDestinatario);
		hash.put("dataLettura", new Date());
		this.getSqlMapClientTemplate().update("updateDataLetturaDestinatarioComunicazione", hash, 1);
	}

	public void deleteComunicazione(String applicativo, long id)
	throws DataAccessException {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("applicativo", applicativo);
		hash.put("idComunicazione", id);

		this.getSqlMapClientTemplate().delete("deleteComunicazione", hash);
	}

	public DettaglioComunicazioneType getDettaglioComunicazione(
			String applicativo, long id) throws DataAccessException {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("applicativo", applicativo);
		hash.put("idComunicazione", id);

		return (DettaglioComunicazioneType) this.getSqlMapClientTemplate()
		.queryForObject("getDettaglioComunicazione", hash);
	}

	@SuppressWarnings("unchecked")
	public List<AllegatoComunicazioneType> getAllegatiComunicazione(
			String applicativo, 
			long id, 
			String idDocumento) throws DataAccessException {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("applicativo", applicativo);
		hash.put("entita", "W_INVCOM");
		hash.put("idComunicazione", String.valueOf(id));
		if(!StringUtils.isEmpty(idDocumento)) {			
			hash.put("idDocumento", idDocumento);
		} 
		
		List<AllegatoComunicazioneType> allegati = this.getSqlMapClientTemplate()
			.queryForList("getAllegatiComunicazione", hash);
		
		// se viene richiesto di caricare tutti gli allegati restituisci 
		// solo le info sugli allegati ma non il contenuto binario...
		// il contenuto binario deve essere richieste in un secondo momento
		// solo se e' necessario scaricare il file!!!
		if(StringUtils.isEmpty(idDocumento)) {
			for(int i = 0; i < allegati.size(); i++) {				
				allegati.get(i).setDimensione( (allegati.get(i).getFile() != null 
											    ? allegati.get(i).getFile().length 
											    : 0) );
				
				// dalla 2.0.0 gli allegati vengono scaricati su richiesta e 
				// quindi si spediscono solo le info del file ma non il
				// contenuto!!!				 				
				if( !StringUtils.isEmpty(allegati.get(i).getUuid()) ) {				
					allegati.get(i).setFile(null);					
				}				
			}					
		}
		
		return allegati;
	}

	@SuppressWarnings("unchecked")
	public List<DettaglioComunicazioneType> getElencoComunicazioni(
			DettaglioComunicazioneType criteriRicerca)
			throws DataAccessException {
		String oggetto = criteriRicerca.getOggetto();
		String testo = criteriRicerca.getTesto();
		if (oggetto != null && oggetto.length() > 0)
			criteriRicerca.setOggetto("%" + oggetto + "%");
		if (testo != null && testo.length() > 0)
			criteriRicerca.setTesto("%" + testo + "%");
		HashMap<String, Object> hash = getHashForQuery(criteriRicerca);
		return (List<DettaglioComunicazioneType>) this
		.getSqlMapClientTemplate().queryForList(
				"getElencoComunicazioni", hash);
	}

	public Boolean isComunicazioneProcessata(String applicativo, long id)
	throws DataAccessException {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("applicativo", applicativo);
		hash.put("idComunicazione", id);
		hash.put("stati", new String[] { "6", "7" });

		Integer numComProcessate = (Integer) this.getSqlMapClientTemplate()
		.queryForObject("isComunicazioneProcessata", hash);
		return new Boolean(numComProcessate.intValue() == 1);
	}

	public void updateProtocolloComunicazione(String applicativo, long id,
			String numeroProtocollo, Date dataProtocollo, String stato) {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("applicativo", applicativo);
		hash.put("idComunicazione", id);
		hash.put("numeroProtocollo", numeroProtocollo);
		hash.put("dataProtocollo", dataProtocollo);
		hash.put("stato", stato);
		this.getSqlMapClientTemplate().update("updateProtocolloComunicazione", hash);
	}

	public void insertDocumentoProtocollo(long idDocumento,
			WSDocumentoType documento) {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("id", new Long(idDocumento));
		hash.put("entita", documento.getEntita());
		hash.put("chiave1", documento.getChiave1());
		hash.put("chiave2", documento.getChiave2());
		hash.put("chiave3", documento.getChiave3());
		hash.put("chiave4", documento.getChiave4());
		hash.put("numeroDocumento", documento.getNumeroDocumento());
		hash.put("annoProtocollo", documento.getAnnoProtocollo());
		hash.put("numeroProtocollo", documento.getNumeroProtocollo());
		hash.put("oggetto", documento.getOggetto());
		//		hash.put("verso", documento.getVerso().getValue());
		hash.put("verso", documento.getVerso());
		this.getSqlMapClientTemplate().insert("insertDocumentoProtocollo", hash);
	}

	public void insertAllegatoProtocollo(long idAllegato,
			WSAllegatoType allegato) {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("id", new Long(idAllegato));
		hash.put("entita", allegato.getEntita());
		hash.put("chiave1", allegato.getChiave1());
		hash.put("chiave2", allegato.getChiave2());
		hash.put("chiave3", allegato.getChiave3());
		hash.put("chiave4", allegato.getChiave4());
		hash.put("idDocumento", allegato.getIdDocumento());
		this.getSqlMapClientTemplate().insert("insertAllegatoProtocollo", hash);
	}

	public void updateSessionKeyComunicazione(String applicativo, long id,
			String sessionKey, String stato) {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("applicativo", applicativo);
		hash.put("idComunicazione", id);
		hash.put("sessionKey", sessionKey);
		hash.put("stato", stato);
		this.getSqlMapClientTemplate().update("updateSessionKeyComunicazione",
				hash);
	}

}
