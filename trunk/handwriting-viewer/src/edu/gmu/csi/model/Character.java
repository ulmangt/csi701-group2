package edu.gmu.csi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Character implements TreeNode
{
	private DataSet dataSet;
	private String sCharacter;
	
	private List<Data> children;

	public Character( DataSet dataSet, String sCharacter )
	{
		this.dataSet = dataSet;
		this.sCharacter = sCharacter;
		
		this.children = new ArrayList<Data>( );

	}
	
	public void addChildren( Collection<Data> data )
	{
		this.children.addAll( data );
	}
	
	public void addChild( Data data )
	{
		this.children.add( data );
	}
	
	public List<Data> getDataList( )
	{
		return children;
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

	@Override
	public boolean hasChildren( )
	{
		return !getDataList( ).isEmpty( );
	}
	
	@Override
	public DataSet getParent( )
	{
		return dataSet;
	}

	@Override
	public Object[] getChildren( )
	{
		return getDataList( ).toArray( );
	}
}
