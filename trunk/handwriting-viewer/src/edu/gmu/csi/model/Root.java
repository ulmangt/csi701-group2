package edu.gmu.csi.model;

import java.util.ArrayList;
import java.util.List;

public class Root implements TreeNode
{
	private String databaseName;
	
	private List<Source> children;
	
	public Root( String databaseName )
	{
		this.databaseName = databaseName;
		
		this.children = new ArrayList<Source>( );
	}
	
	public void addChild( Source source )
	{
		this.children.add( source );
	}
	
	public List<Source> getSourceList( )
	{
		return children;
	}
	
	public String getDatabaseName( )
	{
		return databaseName;
	}
	
	@Override
	public String toString( )
	{
		return String.format( "%s", databaseName );
	}

	@Override
	public Object getParent( )
	{
		return null;
	}

	@Override
	public boolean hasChildren( Object element )
	{
		return !getSourceList( ).isEmpty( );
	}

	@Override
	public Object[] getChildren( )
	{
		return getSourceList( ).toArray( );
	}
}
