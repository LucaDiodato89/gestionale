package com.model.database;

import java.sql.*;
import org.sqlite.*;

/**
 * Realizzata per la connessione al motore SQLite. Il codice dipende da alcune caratteristiche
 * di SQLite ma può essere facilmente aggiornato per operare con altri motori.
 * 
 * Viene impiegata una sola connessione per ogni thread applicativo: SQLite è
 * in grado di gestire l'esecuzione di statement concorrenti su singola connessione.
 * 
 * Il database verrà collocato, sotto Windows, nel percorso C:\gestionale\
 * e avrà nome "gestionaledb".
 * 
 * TODO - Adattare il codice per funzionare anche sotto altri OS. Può tornare utile la classe
 * org.sqlite.util.OSInfo
 *
 * @author Enrico Sbrighi
 */
public final class Connector{
	private static SQLiteDataSource dataSource = null;
	private static Connection connection = null;
	private static String dbname = "gestionaledb";
	static{
		dataSource = new SQLiteDataSource();
		/*
		 * Il supporto alle chiavi esterne non è
		 * predefinito in SQLite
		 */
		dataSource.setEnforceForeignKeys(true);
		
		/*
		 * TODO - se la cartella "c:/gestionale/" non esiste già si verifica un errore
		 * controllare se la cartella esiste, altrimenti crearla. Magari creare
		 * questa cartella nella cartella dell'applicativo o quella dei documenti sul pc
		 */
		dataSource.setUrl("jdbc:sqlite:c:/gestionale/" + dbname);
		try{
			connection = dataSource.getConnection();
		}
		catch(SQLException sqle){
			System.out.println(sqle.getClass().getName() + ": " + sqle.getMessage());
		}
	}
	
	/**
	 * Restituisce il riferimento all'unica connessione attiva lungo il ciclo
	 * di vita dell'applicazione. La connessione può essere utilizzata
	 * contemporaneamente da più thread.
	 * 
	 * @return connessione al database SQLite
	 */
	public static synchronized Connection getConnection(){
		try{
			/*
			 * La connessione potrebbe essere 
			 * chiusa a causa del timeout
			 */
			if(!connection.isClosed())
				return connection;
			connection = dataSource.getConnection();
		}
		catch(SQLException sqle){
			System.out.println(sqle.getClass().getName() + ": " + sqle.getMessage());
		}
		return connection;
	}
	
	/**
	 * Permette di abilitare o disabilitare il supporto di SQLite alle chiavi esterne.
	 *
	 * @param active true per abilitare il supporto alle chiavi primarie,
	 * false per disabilitarlo
	 */
	public static void setEnforceForeignKeys(boolean active){
		dataSource.setEnforceForeignKeys(active);
	}
	
	/**
	 * Questo metodo dovrebbe essere chiamato ogni volta che si finisce di operare
	 * con una connessione.
	 *
	 * @param connection la connessione, non ancora chiusa, sulla quale si è finito di operare
	 */
	public static synchronized void releaseConnection(Connection connection){
		/*
		 * Da implementare nel caso in cui si decida di adottare
		 * una strategia di connection pooling.
		 */
	}
	
	/**
	 * Questo metodo controlla se il database è stato creato ma non ancora inizializzato
	 * con le tabelle. Si utilizza la tabella sqlite_master per determinare la presenza
	 * di almeno una tabella.
	 * 
	 * @return true se il database non ha tabelle
	 * false se il database ha delle tabelle oppure se si va in eccezione (nel dubbio se fare, non fare)
	 */
	public static synchronized boolean isEmptyDatabase(){
		
		Connection connection = getConnection();
		Statement controlStm = null;
		
		try {
			/*
			 * NOTA: se restituisci valori in un blocco try, il blocco finally viene comunque
			 * eseguito prima della restituzione del valore.
			 */
			controlStm = connection.createStatement();
			String controlSql = "SELECT COUNT(*) FROM sqlite_master WHERE sqlite_master.type = 'table'";
			ResultSet controlRs = controlStm.executeQuery(controlSql);
			
			if(controlRs.next() && controlRs.getInt(1) > 0) {
				return false;
			}
			
			return true;
		}
		catch(SQLException e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
		finally {
			try {
				if(controlStm != null) 
					controlStm.close();
			}
			catch(SQLException e) {
				System.out.println("Errore nella chiusura dello statement durante la ricerca di tabelle.");
				System.out.println(e.getClass().getName() + ": " + e.getMessage());
			}
			finally {
				Connector.releaseConnection(connection);
			}
		}
	}
}
