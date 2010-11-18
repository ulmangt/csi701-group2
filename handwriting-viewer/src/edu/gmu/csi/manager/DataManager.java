package edu.gmu.csi.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import edu.gmu.csi.model.Character;

public class DataManager
{
	private static final DataManager instance = new DataManager( );

	private Map<String, Character> characterMap = Collections.synchronizedMap( new HashMap<String, Character>( ) );

	public static DataManager getInstance( )
	{
		return instance;
	}
	
	public void putAllCharacterData( Map<String,Character> map )
	{
		characterMap.putAll( map );
	}
	
	public void putCharacterData( String character, Character characterData )
	{
		characterMap.put( character, characterData );
	}
	
	public Character getCharacterData( String character )
	{
		return characterMap.get( character );
	}
	
}
