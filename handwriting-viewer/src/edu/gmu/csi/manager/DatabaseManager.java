package edu.gmu.csi.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager
{
	private static final DatabaseManager instance = new DatabaseManager( );

	private static final String host = "ec2-67-202-7-152.compute-1.amazonaws.com";
	private static final int port = 3306;
	private static final String user = "test";
	private static final String pass = "csi710";

	public Connection getConnection( ) throws SQLException
	{
		return DriverManager.getConnection( String.format( "jdbc:mysql://%s:%d", host, port ), user, pass );
	}

	public static DatabaseManager getInstance( )
	{
		return instance;
	}
}
