package edu.gmu.csi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class DataSet implements TreeNode
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
	
	public void addChildren( Collection<Character> characters )
	{
		this.children.addAll( characters );
	}
	
	public void addChild( Character character )
	{
		this.children.add( character );
	}
	
	public List<Character> getCharacterList( )
	{
		return children;
	}

	public int getId( )
	{
		return ixDataSet;
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
	
	@Override
	public Source getParent( )
	{
		return ixSource;
	}

	@Override
	public boolean hasChildren( )
	{
		return !getCharacterList( ).isEmpty( );
	}
	
	@Override
	public Object[] getChildren( )
	{
		return getCharacterList( ).toArray( );
	}
}
