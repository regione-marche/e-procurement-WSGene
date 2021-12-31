/*
 * Created on 27/feb/14
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.maggioli.eldasoft.gene.bl;

import it.maggioli.eldasoft.gene.db.dao.DocumentiDao;
import it.maggioli.eldasoft.gene.ws.FileType;

/**
 * Manager per la gestione dei documenti digitali.
 * 
 * @author Stefano.Sabbadin
 */
public class DocumentiManager {
	
	private DocumentiDao documentiDao;
	
	/**
	 * @param documentiDao the documentiDao to set
	 */
	public void setDocumentiDao(DocumentiDao documentiDao) {
		this.documentiDao = documentiDao;
	}

	public FileType getDocumentoDigitale(String idProgramma, long idDocumento, String tokenRichiedente) {
		return this.documentiDao.getDocumentoDigitale(idProgramma, idDocumento, tokenRichiedente);
	}
	
	public String getUsernameDocumentoRiservato(String idProgramma, long idDocumento) {
		return this.documentiDao.getUsernameDocumentoRiservato(idProgramma, idDocumento);
	}
	
}
