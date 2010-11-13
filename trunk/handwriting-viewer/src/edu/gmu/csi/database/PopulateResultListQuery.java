package edu.gmu.csi.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.gmu.csi.manager.DataResultManager;
import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.Result;
import edu.gmu.csi.model.Run;

public class PopulateResultListQuery extends DatabaseQuery
{
	private Run run;
	
	private List<Result> results;
	
	public PopulateResultListQuery( Run run )
	{
		this.run = run;
	}
	
	@Override
	public String getQuery( )
	{
		return String.format( "SELECT * FROM Handwriting.Result WHERE Result.ixRun = %d", run.getId( ) );
	}

	@Override
	public void setResults( ResultSet resultSet ) throws SQLException
	{
		results = new ArrayList<Result>( );
	
		DataResultManager manager = DataResultManager.getInstance( );
		
		while ( resultSet.next( ) )
		{
			int ixResult = resultSet.getInt( "ixResult" );
			int ixData = resultSet.getInt( "ixData" );
			String sClassification = resultSet.getString( "sClassification" );

			Data data = manager.getData( ixData );
			
			if ( data != null )
			{
				results.add( new Result( ixResult, data, run, sClassification ) );
			}
			else
			{
				System.out.printf( "Got Result %d for Data %d which does not exist.%n", ixResult, ixData );
			}
		}
	}
	
	public List<Result> getResults( )
	{
		return results;
	}
}
