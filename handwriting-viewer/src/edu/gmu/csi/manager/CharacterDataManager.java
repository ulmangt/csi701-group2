package edu.gmu.csi.manager;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
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

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Display;

import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.data.CharacterData;
import edu.gmu.csi.model.data.CharacterImageData;

public class CharacterDataManager
{
	private static final CharacterDataManager instance = new CharacterDataManager( );

	private static final int NUM_THREADS = 4;

	private Map<Data, SoftReference<CharacterData>> characterDataMap;
	
	private Map<Data, SoftReference<CharacterImageData>> characterImageDataMap;

	private ReferenceQueue<CharacterImageData> referenceQueue;
	
	private ExecutorService threadPool = Executors.newFixedThreadPool( NUM_THREADS );

	public CharacterDataManager( )
	{
		characterDataMap = Collections.synchronizedMap( new HashMap<Data, SoftReference<CharacterData>>( ) );
		characterImageDataMap = Collections.synchronizedMap( new HashMap<Data, SoftReference<CharacterImageData>>( ) );
		referenceQueue = new ReferenceQueue<CharacterImageData>( );
		new Thread( new ImageDisposer( ) ).start( );
	}

	public static CharacterDataManager getInstance( )
	{
		return instance;
	}
	
	// make sure that SWT Images (which use native system resources) are properly
	// disposed of when their WeakReferences are garbage collected
	private class ImageDisposer implements Runnable
	{
		@Override
		public void run( )
		{
			for( ;; )
			{
				try
				{
					Reference<? extends CharacterImageData> reference = referenceQueue.remove( );
					CharacterImageData imageData = reference.get( );
					
					if ( imageData == null ) continue;
					
					Image image = imageData.getImage( );
					
					if ( image == null ) continue;
					
					System.out.println( "Memory low, disposing of image: " + imageData );
					
					image.dispose( );
				}
				catch ( InterruptedException e )
				{
					e.printStackTrace();
				}
			}
		}
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
	
	private class GetCharacterImage implements Callable<CharacterImageData>
	{
		private Data data;
		private Display display;
		private PaletteData palette;
		
		public GetCharacterImage( Data data, Display display, PaletteData palette )
		{
			this.data = data;
			this.display = display;
			this.palette = palette;
		}
		
		@Override
		public CharacterImageData call( ) throws Exception
		{
			CharacterData characterData = getCharacterData0( data );
			return getCharacterImage0( characterData, display, palette );
		}
	}
	
	public Future<CharacterImageData> getCharacterImage( Data data, Display display, PaletteData palette )
	{
		return threadPool.submit( new GetCharacterImage( data, display, palette ) );
	}

	public Future<CharacterData> getCharacterData( Data data )
	{
		return threadPool.submit( new GetCharacterData( data ) );
	}
	
	public CharacterImageData getCharacterImage0( CharacterData data, Display display, PaletteData palette )
	{
		SoftReference<CharacterImageData> dataRef = characterImageDataMap.get( data.getData( ) );
		
		CharacterImageData characterData;
		
		if ( dataRef == null )
		{
			characterData = createCharacterImage( data, display, palette);
			characterImageDataMap.put( data.getData( ), new SoftReference<CharacterImageData>( characterData ) );
			return characterData;
		}
		else
		{
			characterData = dataRef.get( );
			
			if ( characterData == null )
			{
				characterData = createCharacterImage( data, display, palette);
				characterImageDataMap.put( data.getData( ), new SoftReference<CharacterImageData>( characterData ) );
				return characterData;
			}
			else
			{
				return characterData;
			}
		}
	}
	
	protected CharacterImageData createCharacterImage( CharacterData data, Display display, PaletteData palette )
	{
		ImageData sourceData = new ImageData( data.getImageColumns( ), data.getImageRows( ), 8, palette, 1, data.getImageData( ) );
		//sourceData.alpha = 150;
		return new CharacterImageData( data, new Image( display, sourceData ) );
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

			if ( characterData == null )
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
