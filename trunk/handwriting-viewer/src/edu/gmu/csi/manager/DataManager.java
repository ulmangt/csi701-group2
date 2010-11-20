package edu.gmu.csi.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.gmu.csi.model.Character;
import edu.gmu.csi.model.Data;

public class DataManager
{
	private static final DataManager instance = new DataManager( );

	private Map<Integer, Data> dataMap = Collections.synchronizedMap( new HashMap<Integer, Data>( ) );
	private Map<Integer, Map<String,Character>> characterMap = Collections.synchronizedMap( new HashMap<Integer, Map<String,Character>>( ) );

	public static DataManager getInstance( )
	{
		return instance;
	}
	
	public void putDataSet( int ixDataSet, Map<String,Character> map )
	{
		for ( Entry<String, Character> entry : map.entrySet( ) )
		{
			for ( Data data : entry.getValue( ).getDataList( ) )
			{
				dataMap.put( data.getId( ), data );
			}
		}
		
		characterMap.put( ixDataSet, map );
	}
	
	public Data getData( int ixData )
	{
		return dataMap.get( ixData );
	}
	
	public Character getCharacterData( int ixDataSet, String character )
	{
		return characterMap.get( ixDataSet ).get( character );
	}
	
}
