package edu.gmu.csi.model;

import edu.gmu.csi.manager.ViewUtil;

public class IdKeyValue<P extends TreeNode> implements TreeNode
{	
	private int id;
	private P parent;

	private String key;
	private String value;
	
	public IdKeyValue( int id, P parent, String key, String value )
	{
		this.id = id;
		this.parent = parent;
		
		this.key = ViewUtil.intern( key );
		this.value = ViewUtil.intern( value );
	}

	public int getId( )
	{
		return id;
	}
	
	public String getKey( )
	{
		return key;
	}

	public String getValue( )
	{
		return value;
	}

	@Override
	public Object[] getChildren( )
	{
		return null;
	}

	@Override
	public P getParent( )
	{
		return parent;
	}

	@Override
	public boolean hasChildren( )
	{
		return false;
	}

}
