package edu.gmu.csi.model;

public class Data
{
	private int ixData;
	private Source ixDataSet;
	private String sCharacter;
	private int iRows;
	private int iCols;

	public Data( int ixData, Source ixDataSet, String sCharacter, int iRows, int iCols )
	{
		this.ixData = ixData;
		this.ixDataSet = ixDataSet;
		this.sCharacter = sCharacter;
		this.iRows = iRows;
		this.iCols = iCols;
	}
	
	public int getId( )
	{
		return ixData;
	}

	public Source getParent( )
	{
		return ixDataSet;
	}

	public String getCharacter( )
	{
		return sCharacter;
	}

	public int getRows( )
	{
		return iRows;
	}

	public int getCols( )
	{
		return iCols;
	}
	
	@Override
	public String toString( )
	{
		return String.format( "[%d] %s", ixData, sCharacter );
	}
}
