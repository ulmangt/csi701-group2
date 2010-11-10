package edu.gmu.csi.manager;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import edu.gmu.csi.database.PopulateResultListQuery;
import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.Result;
import edu.gmu.csi.model.Run;

public class DataResultManager
{
	private static final DataResultManager instance = new DataResultManager( );
	
	private Map<Integer, Data> dataMap;
	private Map<Run, SoftReference<List<Result>>> runMap;
	
	private ExecutorService threadPool = Executors.newFixedThreadPool( 1 );
	
	public DataResultManager( )
	{
		dataMap = Collections.synchronizedMap( new HashMap<Integer, Data>( ) );
		runMap = Collections.synchronizedMap( new HashMap<Run, SoftReference<List<Result>>>( ) );
	}

	public static DataResultManager getInstance( )
	{
		return instance;
	}
	
	private class GetResults implements Callable<List<Result>>
	{
		private Run run;
		
		public GetResults( Run run )
		{
			this.run = run;
		}
		
		@Override
		public List<Result> call( ) throws Exception
		{
			return getGetResults0( run );
		}
	}
	
	protected List<Result> getGetResults0( Run run )
	{
		SoftReference<List<Result>> dataRef = runMap.get( run );

		List<Result> results = null;

		if ( dataRef == null )
		{
			results = queryResults( run );
			runMap.put( run, new SoftReference<List<Result>>( results ) );
			return results;
		}
		else
		{
			results = dataRef.get( );

			if ( results == null )
			{
				results = queryResults( run );
				runMap.put( run, new SoftReference<List<Result>>( results ) );
				return results;
			}
			else
			{
				return results;
			}
		}
	}
	
	protected List<Result> queryResults( Run run )
	{
		PopulateResultListQuery query = new PopulateResultListQuery( run );
		query.runQuery( );
		return query.getResults( );
	}
	
	public Future<List<Result>> getResults( Run run )
	{
		return threadPool.submit( new GetResults( run ) );
	}
	
	public Data getData( int id )
	{
		return dataMap.get( id );
	}
	
	public void putData( Data data )
	{
		dataMap.put( data.getId( ), data );
	}
}
