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
package it.maggioli.eldasoft.gene.db.dao.ibatis;

import java.util.List;
import java.util.HashMap;

import it.maggioli.eldasoft.gene.db.dao.DocumentiDao;
import it.maggioli.eldasoft.gene.ws.FileType;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * SqlMap per iBatis
 * 
 * @author Stefano.Sabbadin
 */
public class SqlMapDocumentiDao extends SqlMapClientDaoSupport implements
	DocumentiDao {

	public FileType getDocumentoDigitale(String idProgramma, long idDocumento, String username)
			throws DataAccessException {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("prg", idProgramma);
		hash.put("id", idDocumento);
		hash.put("username", username);
 
		// Idea per limitare la complessità di "v_ws_documenti_digitali"
		// nel caso di gruppo 6,11,12 (inviti) 
		// nel caso di RT la query può restituire più di una riga
		//
		// HINT: recuperare queryforList() e restituire il primo della lista
	
		// NB: non è possibile introdurre nella select DISTINCT a causa di campi blob !!!
//		return (FileType) this.getSqlMapClientTemplate().queryForObject(
//				"getDocumentoDigitale", hash);

		List<FileType> lista = (List<FileType>) this.getSqlMapClientTemplate()
			.queryForList("getDocumentoDigitale", hash);
		return ((lista != null && lista.size() > 0) ? lista.get(0) : null);
	}
	
	public String getUsernameDocumentoRiservato(String idProgramma, long idDocumento)
		throws DataAccessException {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("prg", idProgramma);
		hash.put("id", idDocumento);
		List<String> lista = (List<String>) this.getSqlMapClientTemplate()
			.queryForList("getUsernameDocumentoRiservato", hash);
		return ((lista != null && lista.size() > 0) ? lista.get(0) : null);
	}

}
