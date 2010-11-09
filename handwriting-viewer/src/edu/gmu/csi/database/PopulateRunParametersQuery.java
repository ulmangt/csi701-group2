package edu.gmu.csi.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.gmu.csi.model.IdKeyValue;
import edu.gmu.csi.model.Run;

public class PopulateRunParametersQuery extends DatabaseQuery
{
	private Run run;
	
	public PopulateRunParametersQuery( Run run )
	{
		this.run = run;
	}

	@Override
	public String getQuery( )
	{
		return String.format( "SELECT * FROM Handwriting.Parameter WHERE Parameter.ixRun = %d", run.getKey( ) );
	}

	@Override
	public void setResults( ResultSet resultSet ) throws SQLException
	{
		while ( resultSet.next( ) )
		{
			int ixParameter = resultSet.getInt( "ixParameter" );
			String sKey = resultSet.getString( "sKey" );
			String sValue = resultSet.getString( "sValue" );

			run.addChild( new IdKeyValue<Run>( ixParameter, run, sKey, sValue ) );
		}
	}
	
	public Run getRun( )
	{
		return run;
	}

}
