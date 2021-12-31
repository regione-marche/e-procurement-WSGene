/*
 * Created on 29/lug/10
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di 
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.maggioli.eldasoft.gene.bl;

import it.maggioli.eldasoft.gene.db.dao.DatiCodificatiDao;
import it.maggioli.eldasoft.wsOperazioniGenerali.daticomuni.DatoCodificatoType;

import java.util.List;

/**
 * Manager per la gestione dei dati codificati
 * 
 * @author Stefano.Sabbadin
 */
public class DatiCodificatiManager {
	
	private DatiCodificatiDao datiCodificatiDao;
	
	/**
	 * @param datiCodificatiDao the datiCodificatiDao to set
	 */
	public void setDatiCodificatiDao(DatiCodificatiDao datiCodificatiDao) {
		this.datiCodificatiDao = datiCodificatiDao;
	}

	public List<DatoCodificatoType> getTipiTab1(String codiceTabellato) {
		return this.datiCodificatiDao.getTipiTab1(codiceTabellato);
	}

	public List<DatoCodificatoType> getTipiTab2(String codiceTabellato) {
		return this.datiCodificatiDao.getTipiTab2(codiceTabellato);
	}

	public List<DatoCodificatoType> getTipiTab3(String codiceTabellato) {
		return this.datiCodificatiDao.getTipiTab3(codiceTabellato);
	}
	
	public List<DatoCodificatoType> getTipiTab5(String codiceTabellato) {
		return this.datiCodificatiDao.getTipiTab5(codiceTabellato);
	}
	
	public List<DatoCodificatoType> getNazioniCodificate() {
		return this.datiCodificatiDao.getNazioniCodificate();
	}
	
	

}
