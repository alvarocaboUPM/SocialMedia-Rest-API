package com.sos.rest.API;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private Connection connection;

    public DB(){
        connectDB();
    }

    private void connectDB(){
		if (this.connection == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver"); // Busca que está instanciado el driver
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			String host = "localhost:3306";
			String user = "root";
			String passwd = "root";
			String database = "SOS";
			String url = "jdbc:mysql://" + host + "/" + database; // URL para la conexión

			try {
				this.connection = DriverManager.getConnection(url,user,passwd); // Conectamos con estos credenciales
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

    public Connection getConnection(){
        return this.connection;
    }

    public boolean isClosed() throws SQLException{
        return this.connection.isClosed();
    }
}
