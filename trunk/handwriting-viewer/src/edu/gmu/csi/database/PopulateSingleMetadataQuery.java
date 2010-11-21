package edu.gmu.csi.database;

public class PopulateSingleMetadataQuery extends PopulateMetadataQuery
{
	private int ixData;

	public PopulateSingleMetadataQuery( int ixData )
	{
		this.ixData = ixData;
	}
	
	@Override
	public String getQuery( )
	{
		return String.format( "SELECT * FROM Handwriting.Metadata WHERE ixData = %d", ixData );
	}
}
