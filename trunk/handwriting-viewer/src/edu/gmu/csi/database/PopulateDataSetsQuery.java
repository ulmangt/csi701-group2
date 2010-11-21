package edu.gmu.csi.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.gmu.csi.manager.DataManager;
import edu.gmu.csi.manager.DatabaseManager;
import edu.gmu.csi.model.DataSet;
import edu.gmu.csi.model.Source;

public class PopulateDataSetsQuery
{
	private List<Source> parents;
	private List<DataSet> results;

	public PopulateDataSetsQuery( List<Source> parents )
	{
		this.parents = parents;
		this.results = new ArrayList<DataSet>( );
	}

	public String getQuery( )
	{
		return "SELECT * FROM Handwriting.DataSet WHERE DataSet.ixSource = ?";
	}

	public void runQuery( )
	{
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try
		{
			connection = DatabaseManager.getInstance( ).getConnection( );

			for ( Source source : parents )
			{
				try
				{
					statement = connection.prepareStatement( getQuery( ) );
					statement.setInt( 1, source.getId( ) );
					resultSet = statement.executeQuery( );
					setResults( source, resultSet );
				}
				catch ( SQLException e )
				{
					e.printStackTrace( );
				}
				finally
				{
					if ( resultSet != null ) try
					{
						resultSet.close( );
					}
					catch ( SQLException e )
					{
					}
					if ( statement != null ) try
					{
						statement.close( );
					}
					catch ( SQLException e )
					{
					}
				}
			}
		}
		catch ( SQLException e )
		{
			e.printStackTrace( );
		}
		finally
		{
			if ( connection != null ) try
			{
				connection.close( );
			}
			catch ( SQLException e )
			{
			}
		}
	}

	public void setResults( Source parent, ResultSet resultSet ) throws SQLException
	{
		List<DataSet> result = new ArrayList<DataSet>( );

		while ( resultSet.next( ) )
		{
			int ixDataSet = resultSet.getInt( "ixDataSet" );
			String sDescription = resultSet.getString( "sDescription" );
			String sUrl = resultSet.getString( "sUrl" );
			Date dtAccessTime = resultSet.getDate( "dtAccessTime" );

			DataSet dataSet = new DataSet( ixDataSet, parent, sDescription, sUrl, dtAccessTime );
			
			result.add( dataSet );
			DataManager.getInstance( ).putDataSet( dataSet );
		}

		parent.addChildren( result );
		results.addAll( result );
	}

	public List<DataSet> getResults( )
	{
		return results;
	}
}
