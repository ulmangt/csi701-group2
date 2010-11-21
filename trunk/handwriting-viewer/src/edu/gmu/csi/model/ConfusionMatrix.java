package edu.gmu.csi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.gmu.csi.manager.ViewUtil;

public class ConfusionMatrix
{
	private Map<ConfusionKey,List<Data>> map;
	
	private int maxCount;
	private int maxOffDiagonalCount;
	
	private class ConfusionKey
	{
		private String guess;
		private String actual;
		
		public ConfusionKey( Result result )
		{
			this( result.getClassification( ), result.getData( ).getCharacter( ) );
		}
		
		public ConfusionKey( String guess, String actual )
		{
			this.guess = ViewUtil.intern( guess );
			this.actual = ViewUtil.intern( actual );
		}
		
		@Override
		public int hashCode( )
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ( ( actual == null ) ? 0 : actual.hashCode( ) );
			result = prime * result + ( ( guess == null ) ? 0 : guess.hashCode( ) );
			return result;
		}

		@Override
		public boolean equals( Object obj )
		{
			if ( this == obj ) return true;
			if ( obj == null ) return false;
			if ( getClass( ) != obj.getClass( ) ) return false;
			ConfusionKey other = ( ConfusionKey ) obj;
			if ( actual == null )
			{
				if ( other.actual != null ) return false;
			}
			else if ( !actual.equals( other.actual ) ) return false;
			if ( guess == null )
			{
				if ( other.guess != null ) return false;
			}
			else if ( !guess.equals( other.guess ) ) return false;
			return true;
		}
	}
	
	public ConfusionMatrix( List<Result> results )
	{
		this.map = new HashMap<ConfusionKey,List<Data>>();
		
		for ( Result result : results )
		{
			ConfusionKey key = new ConfusionKey( result );
			List<Data> data = this.map.get( key );
			
			if ( data == null )
			{
				data = new ArrayList<Data>( );
				this.map.put( key, data );
			}
			
			data.add( result.getData( ) );
		}
		
		maxCount = 0;
		maxOffDiagonalCount = 0;
		for ( Entry<ConfusionKey,List<Data>> e : map.entrySet( ) )
		{
			ConfusionKey k = e.getKey( );
			List<Data> l = e.getValue( );
			
			if ( l.size( ) > maxCount )
				maxCount = l.size( );
			
			if ( !k.actual.equals( k.guess ) && l.size( ) > maxOffDiagonalCount )
			{
				maxOffDiagonalCount = l.size( );
			}
		}
	}
	
	public List<Data> get( int guess, int actual )
	{
		return get( String.valueOf( guess ), String.valueOf( actual ) );
	}
	
	public List<Data> get( String guess, String actual )
	{
		return map.get( new ConfusionKey( guess, actual ) );
	}
	
	public int getMaxOffDiagonalCount( )
	{
		return maxOffDiagonalCount;
	}
	
	public int getMaxCount( )
	{
		return maxCount;
	}
}