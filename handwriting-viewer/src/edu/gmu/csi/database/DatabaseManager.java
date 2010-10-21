package edu.gmu.csi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager
{
	private static final DatabaseManager instance = new DatabaseManager( );

	private static final String host = "ec2-67-202-7-152.compute-1.amazonaws.com";
	private static final int port = 3306;
	private static final String user = "test";
	private static final String pass = "csi710";

	protected DatabaseManager( )
	{
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try
		{
			connection = DriverManager.getConnection( String.format( "jdbc:mysql://%s:%d", host, port ), user, pass );
			statement = connection.createStatement( );
			resultSet = statement.executeQuery( "SELECT * FROM Handwriting.Source" );
			
			while ( resultSet.next( ) )
			{
				int ixSource = resultSet.getInt( "ixSource" );
				String sName = resultSet.getString( "sName" );
				String sUrl = resultSet.getString( "sUrl" );
				
				System.out.printf( "%d %s %s%n", ixSource, sName, sUrl );
			}
			
		}
		catch ( SQLException e )
		{
			e.printStackTrace( );
		}
		finally
		{
			if ( resultSet != null ) try { resultSet.close( ); } catch ( SQLException e ) { }
			if ( statement != null ) try { statement.close( ); } catch ( SQLException e ) { }
			if ( connection != null ) try { connection.close( ); } catch ( SQLException e ) { }
		}
	}

	public static DatabaseManager getInstance( )
	{
		return instance;
	}
	
	public static void main( String[] args )
	{
		DatabaseManager.getInstance( );
	}

}
