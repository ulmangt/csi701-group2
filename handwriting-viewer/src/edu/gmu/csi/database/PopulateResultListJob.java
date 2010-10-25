package edu.gmu.csi.database;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

import edu.gmu.csi.model.ResultKey;
import edu.gmu.csi.view.ResultListView;

public class PopulateResultListJob extends Job
{
	private ResultListView view;

	public PopulateResultListJob( ResultListView view )
	{
		super( "Loading Database..." );

		this.view = view;
	}

	@Override
	protected IStatus run( IProgressMonitor monitor )
	{
		PopulateResultListQuery populateResultListQuery = new PopulateResultListQuery( );
		populateResultListQuery.runQuery( );
		final List<ResultKey> results = populateResultListQuery.getResults( );

		Display.getDefault( ).asyncExec( new Runnable( )
		{
			public void run( )
			{
				view.setInput( results );
			}
		} );

		return Status.OK_STATUS;
	}
}
