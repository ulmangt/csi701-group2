package edu.gmu.csi.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.gmu.csi.model.ResultKey;

public class PopulateResultListQuery extends DatabaseQuery
{
	private List<ResultKey> results;

	@Override
	public String getQuery( )
	{
		return "SELECT Result.sKey, COUNT(*) as count FROM Handwriting.Result GROUP BY Result.sKey";
	}

	@Override
	public void setResults( ResultSet resultSet ) throws SQLException
	{
		results = new ArrayList<ResultKey>( );
		
		while ( resultSet.next( ) )
		{
			String sKey = resultSet.getString( "sKey" );
			int count = resultSet.getInt( "count" );
			
			ResultKey result = new ResultKey( sKey, count );
			results.add( result );
		}
	}
	
	public List<ResultKey> getResults( )
	{
		return results;
	}

}
