package com.app.test;
import com.model.database.Manager;

/*
 * Esempio d'uso del package com.model.database.
 * 
 * Manager è la classe cui facciamo riferimento per creare il database e le tabelle.
 * Manager usa Connector per collegarsi a SQLite (il sistema di database integrato nella libreria).
 * 
 * Per eseguire le query in futuro converrà utilizzare la classe SQLiteHelper inclusa nella libreria.
 * SQLite è il database utilizzato da Android (almeno fino a 3 o 4 versioni fa..) e questo può facilitare
 * un eventuale porting su Android, perché si usa proprio quella classe.
 */
public class make_db {
	
	
	public static void main(String[] args){
		
		Manager manager = new Manager();
		try {
			manager.initDatabase();
			//manager.dropDatabaseTables();
		}
		catch(Exception e) {
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
		}
		
	}
}
