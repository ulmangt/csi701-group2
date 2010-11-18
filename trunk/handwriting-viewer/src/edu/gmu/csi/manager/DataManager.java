package edu.gmu.csi.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.gmu.csi.model.Character;

public class DataManager
{
	private static final DataManager instance = new DataManager( );

	private Map<Integer, Map<String,Character>> characterMap = Collections.synchronizedMap( new HashMap<Integer, Map<String,Character>>( ) );

	public static DataManager getInstance( )
	{
		return instance;
	}
	
	public void putDataSet( int ixDataSet, Map<String,Character> map )
	{
		characterMap.put( ixDataSet, map );
	}
	
	public Character getCharacterData( int ixDataSet, String character )
	{
		return characterMap.get( ixDataSet ).get( character );
	}
	
}
