package edu.gmu.csi.database;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;

import edu.gmu.csi.model.Run;
import edu.gmu.csi.view.RunListView;

public class PopulateRunListJob extends Job
{
	private RunListView view;

	public PopulateRunListJob( RunListView view )
	{
		super( "Loading Database..." );

		this.view = view;
	}

	@Override
	protected IStatus run( IProgressMonitor monitor )
	{
		PopulateRunListQuery populateRunListQuery = new PopulateRunListQuery( );
		populateRunListQuery.runQuery( );
		final List<Run> results = populateRunListQuery.getResults( );

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
