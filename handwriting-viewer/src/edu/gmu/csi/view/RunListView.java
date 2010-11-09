package edu.gmu.csi.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.progress.IWorkbenchSiteProgressService;

import edu.gmu.csi.database.PopulateRunListJob;
import edu.gmu.csi.model.Run;

public class RunListView extends ViewPart
{
	public static final String ID = "handwriting-viewer.runlistview";

	private DateFormat dateFormat;
	
	private TableViewer tableViewer;

	class ViewContentProvider implements IStructuredContentProvider
	{
		public void inputChanged( Viewer v, Object oldInput, Object newInput )
		{
		}

		public void dispose( )
		{
		}

		public Object[] getElements( Object parent )
		{
			if ( parent instanceof List<?> )
			{
				return ( ( List<?> ) parent ).toArray( );
			}

			return new Object[0];
		}
	}

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
						return String.valueOf( result.getKey( ) );
					case 1:
						return String.valueOf( result.getDescription( ) );
					case 2:
						return dateFormat.format( result.getRunDate( ) );
					default:
						return null;
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
		Table table = new Table( parent, style );
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableColumn column = new TableColumn(table, SWT.LEFT, 0);		
		column.setText("ID");
		column.setWidth( 30 );
		
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Description");
		column.setWidth( 200 );
		
		column = new TableColumn(table, SWT.LEFT, 2);
		column.setText("Date");
		column.setWidth( 50 );
		
		tableViewer = new TableViewer( table );

		tableViewer.setUseHashlookup( true );

		String[] columnNames = new String[] { "ID", "Description", "Date" };
		tableViewer.setColumnProperties( columnNames );

		tableViewer.setContentProvider( new ViewContentProvider( ) );
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
						Run resultKey = ( Run ) iterator.next( );
						
						//TODO do something here
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

	public void setInput( List<Run> results )
	{
		tableViewer.setInput( results );
	}
}
