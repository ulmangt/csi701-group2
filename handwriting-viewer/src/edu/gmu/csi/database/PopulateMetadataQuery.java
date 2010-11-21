package edu.gmu.csi.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.gmu.csi.manager.DataManager;
import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.Metadata;

public class PopulateMetadataQuery extends DatabaseQuery
{
	@Override
	public String getQuery( )
	{
		return "SELECT * FROM Handwriting.Metadata";
	}

	@Override
	public void setResults( ResultSet resultSet ) throws SQLException
	{
		DataManager manager = DataManager.getInstance( );
		
		while ( resultSet.next( ) )
		{
			int ixMetadata = resultSet.getInt( "ixMetadata" );
			int ixData = resultSet.getInt( "ixData" );
			String sKey = resultSet.getString( "sKey" );
			String sValue = resultSet.getString( "sValue" );

			Data data = manager.getData( ixData );
			
			if ( data == null )
			{
				System.out.printf( "Metadata: %d [%s=%s] is associated with Data: %d which does not exist.", ixMetadata, sKey, sValue, ixData );
			}
			else
			{
				Metadata metadata = new Metadata( ixMetadata, data, sKey, sValue );
				data.addChild( metadata );
			}
		}
	}
}
