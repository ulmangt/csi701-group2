package edu.gmu.csi.model;

public class ResultKey
{
	protected String key;
	protected int count;
	
	public ResultKey( String key, int count )
	{
		this.key = key;
		this.count = count;
	}
	
	public String getKey( )
	{
		return key;
	}
	
	public int getCount( )
	{
		return count;
	}
	
	public Object[] toArray( )
	{
		return new Object[] { String.valueOf( key ), String.valueOf( count ) };
	}
	
	@Override
	public String toString( )
	{
		return String.format( "[ %s %d ]", key, count );
	}
}
