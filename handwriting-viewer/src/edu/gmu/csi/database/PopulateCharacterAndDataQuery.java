package edu.gmu.csi.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gmu.csi.manager.DatabaseManager;
import edu.gmu.csi.model.Character;
import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.DataSet;

public class PopulateCharacterAndDataQuery
{
	private List<DataSet> parents;
	
	public PopulateCharacterAndDataQuery( List<DataSet> parents )
	{
		this.parents = parents;
	}
	
	public String getQuery( )
	{
		return "SELECT ixData, sCharacter, iRows, iCols FROM Handwriting.Data WHERE Data.ixDataSet = ? AND Data.ixData < 200";
	}
	
	public void runQuery( )
	{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try
		{
			connection = DatabaseManager.getInstance( ).getConnection( );
			
			for ( DataSet dataSet : parents )
			{
				try
				{
					statement = connection.prepareStatement( getQuery( ) );
					statement.setInt( 1, dataSet.getId( ) );
					resultSet = statement.executeQuery( );
					setResults( dataSet, resultSet );
				}
				catch ( SQLException e )
				{
					e.printStackTrace( );
				}
				finally
				{
					if ( resultSet != null ) try { resultSet.close( ); } catch ( SQLException e ) { }
					if ( statement != null ) try { statement.close( ); } catch ( SQLException e ) { }
				}
			}
		}
		catch ( SQLException e )
		{
			e.printStackTrace( );
		}
		finally
		{
			if ( connection != null ) try { connection.close( ); } catch ( SQLException e ) { }
		}
	}
	
	public void setResults( DataSet parent, ResultSet resultSet ) throws SQLException
	{
		Map<String,Character> characters = new HashMap<String,Character>( );
		
		while ( resultSet.next( ) )
		{
			int ixData = resultSet.getInt( "ixData" );
			String sCharacter = resultSet.getString( "sCharacter" );
			int iRows = resultSet.getInt( "iRows" );
			int iCols = resultSet.getInt( "iCols" );
			
			Character character = characters.get( sCharacter );
			if ( character == null )
			{
				character = new Character( parent, sCharacter );
				characters.put( sCharacter, character );
			}
			
			character.addChild( new Data( ixData, character, sCharacter, iRows, iCols ) );
		}
		
		parent.addChildren( characters.values( ) );
	}
}
