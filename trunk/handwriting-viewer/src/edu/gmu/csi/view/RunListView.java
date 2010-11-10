package edu.gmu.csi.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;

import edu.gmu.csi.database.PopulateRunListJob;
import edu.gmu.csi.manager.DataResultManager;
import edu.gmu.csi.model.IdKeyValue;
import edu.gmu.csi.model.Result;
import edu.gmu.csi.model.Run;
import edu.gmu.csi.model.RunRoot;
import edu.gmu.csi.view.tree.TreeNodeContentProvider;

public class RunListView extends ViewPart
{
	public static final String ID = "handwriting-viewer.runlistview";

	private DateFormat dateFormat;
	
	private TreeViewer tableViewer;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider
	{
		public String getColumnText( Object obj, int index )
		{
			if ( obj instanceof Run )
			{
				Run result = ( Run ) obj;

				switch ( index )
				{
					case 0:
						return String.valueOf( result.getDescription( ) );
					case 1:
						return dateFormat.format( result.getRunDate( ) );
					default:
						return "";
				}
			}
			else if ( obj instanceof IdKeyValue )
			{
				IdKeyValue<?> parameter = ( IdKeyValue<?> ) obj;

				switch ( index )
				{
					case 0:
						return String.valueOf( parameter.getKey( ) );
					case 1:
						return String.valueOf( parameter.getValue( ) );
					default:
						return "";
				}
			}
			else
			{
				return getText( obj );
			}
		}

		public Image getColumnImage( Object obj, int index )
		{
			return getImage( obj );
		}

		public Image getImage( Object obj )
		{
			return PlatformUI.getWorkbench( ).getSharedImages( ).getImage( ISharedImages.IMG_OBJ_ELEMENT );
		}
	}

	@Override
	public void createPartControl( Composite parent )
	{
		dateFormat = new SimpleDateFormat( );
		
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		Tree table = new Tree( parent, style );
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TreeColumn column = new TreeColumn(table, SWT.LEFT);		
		column.setText("Description");
		column.setWidth( 250 );
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText("Value");
		column.setWidth( 50 );
		
		tableViewer = new TreeViewer( table );

		tableViewer.setUseHashlookup( true );

		String[] columnNames = new String[] { "Description", "Value" };
		tableViewer.setColumnProperties( columnNames );

		tableViewer.setContentProvider( new TreeNodeContentProvider( ) );
		tableViewer.setLabelProvider( new ViewLabelProvider( ) );
		
		tableViewer.addSelectionChangedListener( new ISelectionChangedListener( )
		{
			@Override
			@SuppressWarnings( "rawtypes" )
			public void selectionChanged( SelectionChangedEvent event )
			{
				System.out.println( event.getSelection( ) );
				
				if ( event.getSelection( ).isEmpty( ) )
				{
					//TODO do something here
				}
				else if ( event.getSelection( ) instanceof IStructuredSelection )
				{
					IStructuredSelection selection = ( IStructuredSelection ) event.getSelection( );
					
					for ( Iterator iterator = selection.iterator( ); iterator.hasNext( ); )
					{
						Run run = ( Run ) iterator.next( );
						
						Future<List<Result>> futureResults = DataResultManager.getInstance( ).getResults( run );
					
						try
						{
							System.out.println( futureResults.get( ) );
						}
						catch ( InterruptedException e )
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch ( ExecutionException e )
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} );

		IWorkbenchPartSite site = getSite( );
		IWorkbenchSiteProgressService siteService = ( IWorkbenchSiteProgressService ) site.getAdapter( IWorkbenchSiteProgressService.class );

		PopulateRunListJob job = new PopulateRunListJob( this );

		siteService.showInDialog( site.getShell( ), job );
		siteService.schedule( job, 0 /* now */, true /* use the half-busy cursor in the part */);
	}

	@Override
	public void setFocus( )
	{
		// TODO Auto-generated method stub

	}

	public void setInput( RunRoot results )
	{	
		tableViewer.setInput( results );
	}
}
