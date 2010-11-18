package edu.gmu.csi.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import edu.gmu.csi.manager.DatabaseManager;

public class UploadMetadataQuery
{
	protected int ixData;
	protected Map<String,String> map;
	
	public UploadMetadataQuery( int ixData, Map<String,String> map )
	{
		this.ixData = ixData;
		this.map = map;
	}
	
	public void runQuery( )
	{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{
			connection = DatabaseManager.getInstance( ).getConnection( );
			statement = connection.prepareStatement( "INSERT INTO Handwriting.Metadata (ixData, sKey, sValue) VALUES (?,?,?)" );
			
			for ( Entry<String, String> entry : map.entrySet( ) )
			{
				statement.setInt( 1, ixData );
				statement.setString( 2, entry.getKey( ) );
				statement.setString( 3, entry.getValue( ) );
				statement.addBatch( );
			}
			
			statement.executeBatch( );

		}
		catch ( SQLException e )
		{
			e.printStackTrace( );
		}
		finally
		{
			if ( resultSet != null ) try
			{
				resultSet.close( );
			}
			catch ( SQLException e )
			{
			}
			if ( statement != null ) try
			{
				statement.close( );
			}
			catch ( SQLException e )
			{
			}
			if ( connection != null ) try
			{
				connection.close( );
			}
			catch ( SQLException e )
			{
			}
		}
	}
}
