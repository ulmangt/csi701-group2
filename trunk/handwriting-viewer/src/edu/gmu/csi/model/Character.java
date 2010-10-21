package edu.gmu.csi.model;

import java.util.ArrayList;
import java.util.List;

public class Character
{
	private int ixDataSet;
	private String sCharacter;
	
	private List<Data> children;

	public Character( int ixDataSet, String sCharacter )
	{
		this.ixDataSet = ixDataSet;
		this.sCharacter = sCharacter;
		
		this.children = new ArrayList<Data>( );

	}
	
	public void addChild( Data data )
	{
		this.children.add( data );
	}
	
	public List<Data> getChildren( )
	{
		return children;
	}
	
	public int getParent( )
	{
		return ixDataSet;
	}

	public String getCharacter( )
	{
		return sCharacter;
	}
	
	@Override
	public String toString( )
	{
		return String.format( "%s", sCharacter );
	}
}
