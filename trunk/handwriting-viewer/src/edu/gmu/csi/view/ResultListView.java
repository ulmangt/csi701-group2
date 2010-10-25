package edu.gmu.csi.view;

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

import edu.gmu.csi.database.PopulateResultListJob;
import edu.gmu.csi.model.ResultKey;

public class ResultListView extends ViewPart
{
	public static final String ID = "handwriting-viewer.resultlistview";

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
			if ( obj instanceof ResultKey )
			{
				ResultKey result = ( ResultKey ) obj;

				switch ( index )
				{
					case 0:
						return result.getKey( );
					case 1:
						return String.valueOf( result.getCount( ) );
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
		int style = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		Table table = new Table( parent, style );
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableColumn column = new TableColumn(table, SWT.LEFT, 0);		
		column.setText("Name");
		column.setWidth( 200 );
		
		column = new TableColumn(table, SWT.LEFT, 1);
		column.setText("Count");
		column.setWidth( 50 );
		
		tableViewer = new TableViewer( table );

		tableViewer.setUseHashlookup( true );

		String[] columnNames = new String[] { "Name", "Count" };
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
						ResultKey resultKey = ( ResultKey ) iterator.next( );
						
						//TODO do something here
					}
				}
			}
		} );

		IWorkbenchPartSite site = getSite( );
		IWorkbenchSiteProgressService siteService = ( IWorkbenchSiteProgressService ) site.getAdapter( IWorkbenchSiteProgressService.class );

		PopulateResultListJob job = new PopulateResultListJob( this );

		siteService.showInDialog( site.getShell( ), job );
		siteService.schedule( job, 0 /* now */, true /* use the half-busy cursor in the part */);
	}

	@Override
	public void setFocus( )
	{
		// TODO Auto-generated method stub

	}

	public void setInput( List<ResultKey> results )
	{
		tableViewer.setInput( results );
		System.out.println( results );
	}

}
