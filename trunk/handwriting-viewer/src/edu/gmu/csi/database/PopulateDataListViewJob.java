package edu.gmu.csi.database;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

import edu.gmu.csi.model.DataSet;
import edu.gmu.csi.model.SourceRoot;
import edu.gmu.csi.model.Source;
import edu.gmu.csi.view.DataListView;

public class PopulateDataListViewJob extends Job
{
	private DataListView view;

	public PopulateDataListViewJob( DataListView view )
	{
		super( "Loading Database..." );

		this.view = view;
	}

	@Override
	protected IStatus run( IProgressMonitor monitor )
	{
		final SourceRoot root = new SourceRoot( "Handwriting" );

		PoulateSourcesQuery poulateSourcesQuery = new PoulateSourcesQuery( root );
		poulateSourcesQuery.runQuery( );
		List<Source> sourceList = poulateSourcesQuery.getResults( );

		PopulateDataSetsQuery populateDataSetsQuery = new PopulateDataSetsQuery( sourceList );
		populateDataSetsQuery.runQuery( );
		List<DataSet> dataSetList = populateDataSetsQuery.getResults( );

		PopulateCharacterAndDataQuery populateDataQuery = new PopulateCharacterAndDataQuery( dataSetList );
		populateDataQuery.runQuery( );
		
		Display.getDefault( ).asyncExec( new Runnable( )
		{
			public void run( )
			{
				view.setRoot( root );
			}
		} );

		return Status.OK_STATUS;
	}
}
