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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.data.CharacterData;

public class CharacterDataManager
{
	private static final CharacterDataManager instance = new CharacterDataManager( );

	private static final int NUM_THREADS = 4;
	
	private Map<Data, SoftReference<CharacterData>> characterDataMap;
	
	private ExecutorService threadPool = Executors.newFixedThreadPool( NUM_THREADS );

	public CharacterDataManager( )
	{
		characterDataMap = Collections.synchronizedMap( new HashMap<Data, SoftReference<CharacterData>>( ) );
	}

	public static CharacterDataManager getInstance( )
	{
		return instance;
	}
	
	private class GetCharacterData implements Callable<CharacterData>
	{
		private Data data;
		
		public GetCharacterData( Data data )
		{
			this.data = data;
		}
		
		@Override
		public CharacterData call( ) throws Exception
		{
			return getCharacterData0( data );
		}
	}

	public Future<CharacterData> getCharacterData( Data data )
	{
		return threadPool.submit( new GetCharacterData( data ) );
	}
	
	protected CharacterData getCharacterData0( Data data )
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

	protected CharacterData queryCharacterData( Data data )
	{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{
			connection = DatabaseManager.getInstance( ).getConnection( );
			statement = connection.prepareStatement( "SELECT Data.bData FROM Handwriting.Data WHERE Data.ixData = ?" );
			statement.setInt( 1, data.getId( ) );
			resultSet = statement.executeQuery( );

			if ( resultSet.next( ) )
			{
				InputStream stream = new DataInputStream( new BufferedInputStream( resultSet.getBinaryStream( 1 ) ) );

				int expected = data.getCols( ) * data.getRows( );
				int index = 0;
				int remaining = expected;
				byte imageData[] = new byte[expected];

				try
				{
					while ( remaining > 0 )
					{
						int read = stream.read( imageData, index, remaining );
						index += read;
						remaining -= read;
					}
				}
				catch ( IOException e )
				{
					e.printStackTrace( );
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

		return null;
	}
}
