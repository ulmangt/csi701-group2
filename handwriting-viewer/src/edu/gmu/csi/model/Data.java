package edu.gmu.csi.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.gmu.csi.manager.ViewUtil;

public class Data implements TreeNode
{
	private int ixData;
	private Character character;
	private String sCharacter;
	private int iRows;
	private int iCols;
	
	private Map<String, Metadata> metadata;

	public Data( int ixData, Character character, String sCharacter, int iRows, int iCols )
	{
		this.ixData = ixData;
		this.character = character;
		this.sCharacter = ViewUtil.intern( sCharacter );
		this.iRows = iRows;
		this.iCols = iCols;
	}
	
	public void addChildren( Collection<Metadata> metadata )
	{
		for ( Metadata m : metadata )
			addChild( m );
	}

	public void addChild( Metadata metadata )
	{
		if ( this.metadata == null )
			this.metadata = new HashMap<String,Metadata>( );
	
		this.metadata.put( metadata.getKey( ), metadata );
	}

	public Metadata getMetadata( String key )
	{
		if ( metadata == null )
			return null;
		
		return metadata.get( key );
	}
	
	public int getId( )
	{
		return ixData;
	}

	public String getCharacter( )
	{
		return sCharacter;
	}

	public int getRows( )
	{
		return iRows;
	}

	public int getCols( )
	{
		return iCols;
	}

	@Override
	public String toString( )
	{
		return String.format( "[%d] %s", ixData, sCharacter );
	}

	@Override
	public Character getParent( )
	{
		return character;
	}

	@Override
	public Object[] getChildren( )
	{
		if ( metadata == null )
			return null;
		else
			return metadata.values( ).toArray( );
	}

	@Override
	public boolean hasChildren( )
	{
		return metadata != null && !metadata.isEmpty( );
	}
}
