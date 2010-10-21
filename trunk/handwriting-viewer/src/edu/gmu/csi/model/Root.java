package edu.gmu.csi.model;

import java.util.ArrayList;
import java.util.List;

public class Root
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
	
	public List<Source> getChildren( )
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
}
