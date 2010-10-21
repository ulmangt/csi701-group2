package edu.gmu.csi.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.gmu.csi.model.Root;
import edu.gmu.csi.model.Source;

public class PoulateSourcesQuery extends DatabaseQuery
{
	private Root parent;
	private List<Source> results;

	public PoulateSourcesQuery( Root parent )
	{
		this.parent = parent;
	}

	@Override
	public String getQuery( )
	{
		return "SELECT * FROM Handwriting.Source";
	}

	@Override
	public void setResults( ResultSet resultSet ) throws SQLException
	{
		results = new ArrayList<Source>( );

		while ( resultSet.next( ) )
		{
			int ixSource = resultSet.getInt( "ixSource" );
			String sName = resultSet.getString( "sName" );
			String sUrl = resultSet.getString( "sUrl" );

			results.add( new Source( ixSource, parent, sName, sUrl ) );
		}

		parent.addChildren( results );
	}

	public List<Source> getResults( )
	{
		return results;
	}
}
