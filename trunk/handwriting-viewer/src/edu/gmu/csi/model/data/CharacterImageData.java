package edu.gmu.csi.model.data;

import org.eclipse.swt.graphics.Image;

import edu.gmu.csi.model.Data;

public class CharacterImageData
{
	private CharacterData data;
	private Image image;
	
	public CharacterImageData( CharacterData data, Image image )
	{
		this.data = data;
		this.image = image;
	}
	
	public Data getData( )
	{
		return data.getData( );
	}
	
	public byte[] getImageData( )
	{
		return data.getImageData( );
	}

	public Image getImage( )
	{
		return image;
	}

	public int getImageRows( )
	{
		return data.getData( ).getRows( );
	}

	public int getImageColumns( )
	{
		return data.getData( ).getCols( );
	}
	
	@Override
	public String toString( )
	{
		return data.toString( );
	}
}
