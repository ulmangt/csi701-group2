package edu.gmu.csi.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataSet
{
	private int ixDataSet;
	private Source ixSource;
	private String sDescription;
	private String sUrl;
	private Date dtAccessTime;
	
	private List<Character> children;
	
	public DataSet( int ixDataSet, Source ixSource, String sDescription, String sUrl, Date dtAccessTime )
	{
		this.ixDataSet = ixDataSet;
		this.ixSource = ixSource;
		this.sDescription = sDescription;
		this.sUrl = sUrl;
		this.dtAccessTime = dtAccessTime;
		
		this.children = new ArrayList<Character>( );
	}
	
	public void addChild( Character character )
	{
		this.children.add( character );
	}
	
	public List<Character> getChildren( )
	{
		return children;
	}

	public int getId( )
	{
		return ixDataSet;
	}

	public Source getParent( )
	{
		return ixSource;
	}

	public String getDescription( )
	{
		return sDescription;
	}

	public String getUrl( )
	{
		return sUrl;
	}

	public Date getAccessTime( )
	{
		return dtAccessTime;
	}
	
	@Override
	public String toString( )
	{
		return String.format( "[%d] %s", ixDataSet, sDescription );
	}
}
