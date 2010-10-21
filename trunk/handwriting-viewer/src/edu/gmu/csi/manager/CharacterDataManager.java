package edu.gmu.csi.manager;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.data.CharacterData;

public class CharacterDataManager
{
	private static final CharacterDataManager instance = new CharacterDataManager( );
	
	private Map<Data, SoftReference<CharacterData>> characterDataMap;
	private Connection connection;
	
	public CharacterDataManager( )
	{
		characterDataMap = Collections.synchronizedMap( new HashMap<Data, SoftReference<CharacterData>>( ) );
		
		try
		{
			connection = DatabaseManager.getInstance( ).getConnection( );
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
		}
	}
	
	public static CharacterDataManager getInstance( )
	{
		return instance;
	}
	
	public CharacterData getCharacterData( Data data )
	{
		SoftReference<CharacterData> dataRef = characterDataMap.get( data );
		
		CharacterData characterData = null;
		
		if ( dataRef == null )
		{
			characterData = queryCharacterData( data );
			characterDataMap.put( data, new SoftReference<CharacterData>( characterData ) );
			return characterData;
		}
		else
		{
			characterData = dataRef.get( );
			
			if ( data == null )
			{
				characterData = queryCharacterData( data );
				characterDataMap.put( data, new SoftReference<CharacterData>( characterData ) );
				return characterData;
			}
			else
			{
				return characterData;
			}
		}
	}
	
	public synchronized CharacterData queryCharacterData( Data data )
	{
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		try
		{
			statement = connection.prepareStatement( "SELECT Data.bData FROM Handwriting.Data WHERE Data.ixData = ?" );
			statement.setInt( 1, data.getId( ) );
			resultSet = statement.executeQuery( );
			
			if ( resultSet.next( ) )
			{
				InputStream stream = new DataInputStream( new BufferedInputStream( resultSet.getBinaryStream( 1 ) ) );
				
				int expected = data.getCols( ) * data.getRows( );
				int index = 0;
				int[] imageData = new int[ data.getCols( ) * data.getRows( ) ];
				
				try
				{
					for ( ; index < expected ; index++ )
					{
						imageData[ index ] = stream.read( );
					}
				}
				catch ( IOException e )
				{
					e.printStackTrace();
					System.out.printf( "Expected %d data values. Received only %d data values.", expected, index );
				}
				
				return new CharacterData( data, imageData );
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
		}
		
		return null;
	}
}
