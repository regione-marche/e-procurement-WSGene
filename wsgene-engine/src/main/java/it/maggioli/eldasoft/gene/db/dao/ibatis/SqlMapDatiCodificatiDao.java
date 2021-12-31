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
package it.maggioli.eldasoft.gene.db.dao.ibatis;

import it.maggioli.eldasoft.gene.db.dao.DatiCodificatiDao;
import it.maggioli.eldasoft.wsOperazioniGenerali.daticomuni.DatoCodificatoType;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * SqlMap per iBatis
 * 
 * @author Stefano.Sabbadin
 */
public class SqlMapDatiCodificatiDao extends SqlMapClientDaoSupport implements
		DatiCodificatiDao {

	@SuppressWarnings("unchecked")
	public List<DatoCodificatoType> getTipiTab1(String codiceTabellato) throws DataAccessException {
		return (List<DatoCodificatoType>) this.getSqlMapClientTemplate().queryForList(
				"getTipiTab1", codiceTabellato);
	}

	@SuppressWarnings("unchecked")
	public List<DatoCodificatoType> getTipiTab2(String codiceTabellato) throws DataAccessException {
		return (List<DatoCodificatoType>) this.getSqlMapClientTemplate().queryForList(
				"getTipiTab2", codiceTabellato);
	}

	@SuppressWarnings("unchecked")
	public List<DatoCodificatoType> getTipiTab3(String codiceTabellato) throws DataAccessException {
		return (List<DatoCodificatoType>) this.getSqlMapClientTemplate().queryForList(
				"getTipiTab3", codiceTabellato);
	}
	
	@SuppressWarnings("unchecked")
	public List<DatoCodificatoType> getTipiTab5(String codiceTabellato) throws DataAccessException {
		return (List<DatoCodificatoType>) this.getSqlMapClientTemplate().queryForList(
				"getTipiTab5", codiceTabellato);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DatoCodificatoType> getNazioniCodificate() throws DataAccessException {
		return (List<DatoCodificatoType>) this.getSqlMapClientTemplate().queryForList(
				"getNazioniCodificate");
	}
	
	

}
