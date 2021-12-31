/*
 * Created on 07-lug-2006
 *
 * Copyright (c) EldaSoft S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di EldaSoft S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di
 * aver prima formalizzato un accordo specifico con EldaSoft.
 */
package it.maggioli.eldasoft.gene.db.dao.ibatis;

import it.maggioli.eldasoft.gene.db.dao.GenChiaviDao;

import java.util.HashMap;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

/**
 * Classe di appoggio per l'esecuzione tramite estensione del framework Spring
 * di interazioni con la tabella W_GENCHIAVI tramite iBatis.
 * 
 * @author Luca.Giacomazzo
 */
public class SqlMapGenChiaviDao extends SqlMapClientDaoSupport implements
		GenChiaviDao {

	/**
	 * @see it.eldasoft.gene.db.dao.GenChiaviDao#getNextId(java.lang.String,
	 *      int)
	 */
	public long getNextId(String tabella, int numIdAllocati) {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("tabella", tabella);
		hash.put("incremento", numIdAllocati > 0 ? numIdAllocati : 1);
		getSqlMapClientTemplate().update("updateId", hash);
		// ultimo id da usare nel ciclo di inserimenti
		Long i = (Long) getSqlMapClientTemplate().queryForObject(
				"getNextId", tabella);
		// primo id da usare nel ciclo di inserimenti
		return i.longValue() - numIdAllocati + 1;
	}

	/**
	 * @see it.eldasoft.gene.db.dao.GenChiaviDao#getMaxId(java.lang.String,java.lang.String,java.lang.String)
	 */
	public long getMaxId(String tabella, String chiave, String condizioniExtra) {
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("chiave", chiave);
		hash.put("tabella", tabella);
		hash.put("condizioniExtra", condizioniExtra);

		Long i = (Long) getSqlMapClientTemplate().queryForObject(
				"getMaxId", hash);
		// Se la tabella e' vuota, allora si ritorna zero come maxId, in modo
		// che l'eventuale insert sia fatto con maxId + 1 = 1
		if (i == null) {
			i = new Long(0);
		}

		return i.intValue();
	}
}