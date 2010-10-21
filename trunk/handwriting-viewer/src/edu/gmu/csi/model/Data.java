package edu.gmu.csi.model;

public class Data implements TreeNode
{
	private int ixData;
	private Character character;
	private String sCharacter;
	private int iRows;
	private int iCols;

	public Data( int ixData, Character character, String sCharacter, int iRows, int iCols )
	{
		this.ixData = ixData;
		this.character = character;
		this.sCharacter = sCharacter;
		this.iRows = iRows;
		this.iCols = iCols;
	}
	
	public int getId( )
	{
		return ixData;
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
	
	@Override
	public Character getParent( )
	{
		return character;
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
