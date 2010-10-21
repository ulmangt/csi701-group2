package edu.gmu.csi.view;

import java.util.Arrays;
import java.util.Iterator;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.eclipse.ui.IWorkbenchPartSite;

import edu.gmu.csi.database.PopulateDataListViewJob;
import edu.gmu.csi.manager.CharacterDataManager;
import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.Root;
import edu.gmu.csi.model.TreeNode;
import edu.gmu.csi.model.data.CharacterData;

public class DataListView extends ViewPart
{
	public static final String ID = "handwriting-viewer.datalistview";

	private TreeViewer treeViewer;

	public DataListView( )
	{

	}

	@Override
	public void createPartControl( Composite parent )
	{
		treeViewer = new TreeViewer( parent );

		treeViewer.setContentProvider( new DataListContentProvider( ) );
		treeViewer.setLabelProvider( new DataListLabelProvider( ) );

		treeViewer.addSelectionChangedListener( new ISelectionChangedListener( )
		{
			@Override
			@SuppressWarnings( "rawtypes" )
			public void selectionChanged( SelectionChangedEvent event )
			{
				if ( event.getSelection( ).isEmpty( ) )
				{
				}
				if ( event.getSelection( ) instanceof IStructuredSelection )
				{
					IStructuredSelection selection = ( IStructuredSelection ) event.getSelection( );
					for ( Iterator iterator = selection.iterator( ); iterator.hasNext( ); )
					{
						TreeNode treeNode = ( TreeNode ) iterator.next( );
						
						if ( treeNode instanceof Data )
						{
							Data data = (Data) treeNode;
							CharacterData characterData = CharacterDataManager.getInstance( ).getCharacterData( data );
							
							if ( characterData != null )
								System.out.println( Arrays.toString( characterData.getImageData( ) ) );
						}
					}
				}
			}
		} );

		IWorkbenchPartSite site = getSite( );
		IWorkbenchSiteProgressService siteService = ( IWorkbenchSiteProgressService ) site.getAdapter( IWorkbenchSiteProgressService.class );

		PopulateDataListViewJob job = new PopulateDataListViewJob( this );

		siteService.showInDialog( site.getShell( ), job );
		siteService.schedule( job, 0 /* now */, true /* use the half-busy cursor in the part */);
	}

	public void setRoot( Root root )
	{
		treeViewer.setInput( root );
	}

	@Override
	public void setFocus( )
	{
		// TODO Auto-generated method stub

	}

	private class DataListContentProvider implements ITreeContentProvider
	{

		@Override
		public void dispose( )
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
		{
			// TODO Auto-generated method stub

		}

		@Override
		public Object[] getElements( Object inputElement )
		{
			return getChildren( inputElement );
		}

		@Override
		public Object[] getChildren( Object parentElement )
		{
			if ( parentElement instanceof TreeNode )
			{
				return ( ( TreeNode ) parentElement ).getChildren( );
			}
			else
			{
				return null;
			}
		}

		@Override
		public Object getParent( Object element )
		{
			if ( element instanceof TreeNode )
			{
				return ( ( TreeNode ) element ).getParent( );
			}
			else
			{
				return null;
			}
		}

		@Override
		public boolean hasChildren( Object element )
		{
			if ( element instanceof TreeNode )
			{
				return ( ( TreeNode ) element ).hasChildren( );
			}
			else
			{
				return false;
			}
		}
	}

	private class DataListLabelProvider extends LabelProvider
	{
		public Image getImage( Object element )
		{
			return null;
		}

		public String getText( Object element )
		{
			return element == null ? "" : element.toString( );
		}
	}
}
