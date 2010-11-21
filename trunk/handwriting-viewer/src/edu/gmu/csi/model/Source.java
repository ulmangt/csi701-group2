package edu.gmu.csi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.gmu.csi.manager.ViewUtil;

public class Source implements TreeNode
{
	private int ixSource;
	private SourceRoot parent;
	private String sName;
	private String sUrl;

	private List<DataSet> children;

	public Source( int ixSource, SourceRoot parent, String sName, String sUrl )
	{
		this.ixSource = ixSource;
		this.parent = parent;
		this.sName = ViewUtil.intern( sName );
		this.sUrl = ViewUtil.intern( sUrl );

		this.children = new ArrayList<DataSet>( );
	}

	public void addChildren( Collection<DataSet> dataSets )
	{
		this.children.addAll( dataSets );
	}

	public void addChild( DataSet dataSet )
	{
		this.children.add( dataSet );
	}

	public List<DataSet> getDataSetList( )
	{
		return children;
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

	@Override
	public SourceRoot getParent( )
	{
		return parent;
	}

	@Override
	public boolean hasChildren( )
	{
		return !getDataSetList( ).isEmpty( );
	}

	@Override
	public Object[] getChildren( )
	{
		return getDataSetList( ).toArray( );
	}
}
