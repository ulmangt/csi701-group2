package edu.gmu.csi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Data implements TreeNode
{
	private int ixData;
	private Character character;
	private String sCharacter;
	private int iRows;
	private int iCols;
	
	private List<Metadata> metadata;

	public Data( int ixData, Character character, String sCharacter, int iRows, int iCols )
	{
		this.ixData = ixData;
		this.character = character;
		this.sCharacter = sCharacter;
		this.iRows = iRows;
		this.iCols = iCols;
	}
	
	public void addChildren( Collection<Metadata> metadata )
	{
		if ( this.metadata == null )
			this.metadata = new ArrayList<Metadata>( );
		
		this.metadata.addAll( metadata );
	}

	public void addChild( Metadata metadata )
	{
		if ( this.metadata == null )
			this.metadata = new ArrayList<Metadata>( );
	
		this.metadata.add( metadata );
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
			return metadata.toArray( );
	}

	@Override
	public boolean hasChildren( )
	{
		return metadata != null && !metadata.isEmpty( );
	}
}
