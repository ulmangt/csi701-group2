package edu.gmu.csi.model.data;

import edu.gmu.csi.model.Data;

public class CharacterData
{
	private Data data;
	private int[] dataArray;
	
	public CharacterData( Data data, int[] dataArray )
	{
		this.data = data;
		this.dataArray = dataArray;
	}
	
	public Data getData( )
	{
		return data;
	}
	
	public int[] getImageData( )
	{
		return dataArray;
	}
	
	public int getImageRows( )
	{
		return data.getRows( );
	}
	
	public int getImageColumns( )
	{
		return data.getCols( );
	}
}
