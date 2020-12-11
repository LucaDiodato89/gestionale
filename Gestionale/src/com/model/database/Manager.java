package com.model.database;

import java.sql.*;

/**
 * Crea le tabelle nel database dell'applicazione. Il database viene creato,
 * sotto forma di file, automaticamente all'atto del caricamento della classe
 * Connector. Il codice SQL è ottimizzato per SQLite.
 * 
 * @author Enrico Sbrighi
 */
public final class Manager{

	/* 
	 * Qualsiasi campo dichiarato INTEGER PRIMARY KEY in SQLite
	 * è un alias per la pseudocolonna rowid
	 *
	 * AUTOINCREMENT is to prevent the reuse of ROWIDs from
	 * previously deleted rows.
	 */

	private Connection connection = null;

	public Manager(){
		connection = Connector.getConnection();
	}
	
	/**
	 * Crea le tabelle nel file del database.
	 * 
	 * Richiamare questo metodo nuovamente dopo aver creato il database è sicuro
	 * ma produce un SQLITE_ERROR.
	 */
	public void initDatabase() throws SQLException {
		
		Statement statement = connection.createStatement();
		String sql = null;
		
		connection.setAutoCommit(false); // INIZIO DELLA TRANSAZIONE
		
		sql =
		"CREATE TABLE COMPONENTE" +
			"(" +
			"CODICE					INTEGER			PRIMARY KEY," +
			"NOMECOMPONENTE			VARCHAR(32)		NOT NULL," +
			"IMMAGINE				VARCHAR(36)				," + // 32 caratteri + estensione
			"UNITADISPONIBILI		INTEGER			NOT NULL," +
			"PREZZOVENDITA			DECIMAL(3,2)			," +
			"DESCRIZIONE			VARCHAR(256)			" +
		");";
		statement.executeUpdate(sql);
		
		sql =
		"CREATE TABLE CLIENTE" +
		"(" +
			"CODICEFISCALE			INTEGER			PRIMARY KEY," +
			"NOME					VARCHAR(32)		NOT NULL," +
			"SECONDONOME			VARCHAR(32)				," +
			"COGNOME				VARCHAR(32)		NOT NULL," +
			"MAIL					VARCHAR(32)				," +
			"TELEFONO				CHAR(10)				," +
			"FOTOGRAFIA				VARCHAR(36)				" + // 32 caratteri è estensione
		");";
		statement.executeUpdate(sql);
		
		sql =
		"CREATE TABLE COMMESSA" +
		"(" +
			"NUMERO					INTEGER			PRIMARY KEY," +
			"IDCLIENTE				INTEGER			NOT NULL," +
			"IDCOMPONENTE			VARCHAR(32)		NOT NULL," +
			"COMPLETATA				BOOLEAN			NOT NULL," +
			"FOREIGN KEY(IDCLIENTE) REFERENCES CLIENTE(CODICEFISCALE)," +
			"FOREIGN KEY(IDCOMPONENTE) REFERENCES COMPONENTE(CODICE)" +
		");";
		statement.executeUpdate(sql);
		
		connection.commit(); // FINE DELLA TRANSAZIONE
		
		/*
		 * La connessione è condivisa quindi al termine della transazione
		 * bisogna reimpostare il commit automatico.
		 */
		connection.setAutoCommit(true);
		
		statement.close();
		Connector.releaseConnection(connection);
	}
	
	/**
	 * Elimina tutte le tabelle presenti nel database ma non elimina il file del
	 * database.
	 */
	public void dropDatabaseTables() throws SQLException {
		/*
		 * Disabilitiamo momentaneament i vincoli di chiave esterna per eliminare
		 * le tabelle più agevolmente.
		 */
		Connector.setEnforceForeignKeys(false);
		
		Statement statement = connection.createStatement();
		String sql = null;

		connection.setAutoCommit(false); //INIZIO DELLA TRANSAZIONE
		
		sql = "DROP TABLE COMMESSA;";
		statement.executeUpdate(sql);
		
		sql = "DROP TABLE CLIENTE;";
		statement.executeUpdate(sql);
		
		sql = "DROP TABLE COMPONENTE;";
		statement.executeUpdate(sql);
		
		connection.commit(); // FINE DELLA TRANSAZIONE
		
		/*
		 * La connessione è condivisa quindi al termine della transazione
		 * bisogna reimpostare il commit automatico.
		 */
		connection.setAutoCommit(true);
		
		/*
		 * Ora le chiavi primarie sono di nuovo operative.
		 */
		Connector.setEnforceForeignKeys(true);
		
		statement.close();
		Connector.releaseConnection(connection);
	}
}