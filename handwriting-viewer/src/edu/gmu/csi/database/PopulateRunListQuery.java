package edu.gmu.csi.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.gmu.csi.model.Run;

public class PopulateRunListQuery extends DatabaseQuery
{
	private List<Run> results;

	@Override
	public String getQuery( )
	{
		return "SELECT * FROM Handwriting.Run";
	}

	@Override
	public void setResults( ResultSet resultSet ) throws SQLException
	{
		results = new ArrayList<Run>( );

		while ( resultSet.next( ) )
		{
			int ixRun = resultSet.getInt( "ixRun" );
			String sDesciption = resultSet.getString( "sDescription" );
			Date dtAccessTime = resultSet.getDate( "dtRunDate" );

			results.add( new Run( ixRun, sDesciption, dtAccessTime ) );
		}
	}

	public List<Run> getResults( )
	{
		return results;
	}

}
