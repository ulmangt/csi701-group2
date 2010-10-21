package edu.gmu.csi.model;

import java.util.ArrayList;
import java.util.List;

public class Source
{
	private int ixSource;
	private Root parent;
	private String sName;
	private String sUrl;

	private List<DataSet> children;
	
	public Source( int ixSource, Root parent, String sName, String sUrl )
	{
		this.ixSource = ixSource;
		this.parent = parent;
		this.sName = sName;
		this.sUrl = sUrl;
		
		this.children = new ArrayList<DataSet>( );
	}
	
	public void addChild( DataSet dataSet )
	{
		this.children.add( dataSet );
	}
	
	public List<DataSet> getChildren( )
	{
		return children;
	}
	
	public Root getParent( )
	{
		return parent;
	}
	
	public int getId( )
	{
		return ixSource;
	}

	public String getName( )
	{
		return sName;
	}

	public String getUrl( )
	{
		return sUrl;
	}
	
	@Override
	public String toString( )
	{
		return String.format( "[%d] %s", ixSource, sName );
	}
}
