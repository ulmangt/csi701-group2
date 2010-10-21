package edu.gmu.csi.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseQuery
{
	public abstract String getQuery( );
	public abstract void setResults( ResultSet resultSet ) throws SQLException;
	
	public void runQuery( )
	{
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		
		try
		{
			connection = DatabaseManager.getInstance( ).getConnection( );
			statement = connection.createStatement( );
			resultSet = statement.executeQuery( getQuery( ) );
			setResults( resultSet );
			
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
}
