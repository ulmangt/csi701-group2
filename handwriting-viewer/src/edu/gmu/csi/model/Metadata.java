package edu.gmu.csi.model;

public class Metadata implements TreeNode
{
	private int ixMetadata;
	private Data data;
	private String sKey;
	private String sValue;

	public Metadata( int ixMetadata, Data data, String sKey, String sValue )
	{
		this.ixMetadata = ixMetadata;
		this.data = data;
		this.sKey = sKey;
		this.sValue = sValue;
	}

	public int getId( )
	{
		return ixMetadata;
	}

	public Data getData( )
	{
		return data;
	}

	public String getValue( )
	{
		return sValue;
	}

	public String getKey( )
	{
		return sKey;
	}

	@Override
	public String toString( )
	{
		return String.format( "[%d] %s=%s", ixMetadata, sKey, sValue );
	}

	@Override
	public Data getParent( )
	{
		return data;
	}

	@Override
	public Object[] getChildren( )
	{
		return null;
	}

	@Override
	public boolean hasChildren( )
	{
		return false;
	}
}