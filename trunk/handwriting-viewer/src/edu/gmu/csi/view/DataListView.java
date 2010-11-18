package edu.gmu.csi.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;

import edu.gmu.csi.database.PopulateDataListViewJob;
import edu.gmu.csi.manager.ViewUtil;
import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.SourceRoot;
import edu.gmu.csi.model.TreeNode;
import edu.gmu.csi.view.tree.TreeNodeContentProvider;

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

		treeViewer.setContentProvider( new TreeNodeContentProvider( ) );
		treeViewer.setLabelProvider( new DataListLabelProvider( ) );

		treeViewer.addSelectionChangedListener( new ISelectionChangedListener( )
		{
			@Override
			@SuppressWarnings( "rawtypes" )
			public void selectionChanged( SelectionChangedEvent event )
			{
				if ( event.getSelection( ).isEmpty( ) )
				{
					ViewUtil.getCharacterImageView( ).setSelection( null );
				}
				if ( event.getSelection( ) instanceof IStructuredSelection )
				{
					List<Data> selectedData = new ArrayList<Data>( );
					IStructuredSelection selection = ( IStructuredSelection ) event.getSelection( );
					
					for ( Iterator iterator = selection.iterator( ); iterator.hasNext( ); )
					{
						TreeNode treeNode = ( TreeNode ) iterator.next( );

						if ( treeNode instanceof Data )
						{
							selectedData.add( (Data) treeNode );
						}
					}
					
					ViewUtil.getCharacterImageView( ).setSelection( selectedData );
				}
			}
		} );

		IWorkbenchPartSite site = getSite( );
		IWorkbenchSiteProgressService siteService = ( IWorkbenchSiteProgressService ) site.getAdapter( IWorkbenchSiteProgressService.class );

//		PopulateDataListViewJob job = new PopulateDataListViewJob( this );
//
//		siteService.showInDialog( site.getShell( ), job );
//		siteService.schedule( job, 0 /* now */, true /* use the half-busy cursor in the part */);
	}

	public void setRoot( SourceRoot root )
	{
		treeViewer.setInput( root );
		
		treeViewer.expandToLevel( 3 );
	}

	@Override
	public void setFocus( )
	{
		treeViewer.getTree( ).setFocus( );
	}

	private class DataListLabelProvider extends LabelProvider
	{
		Image categoryImage = PlatformUI.getWorkbench( ).getSharedImages( ).getImage( ISharedImages.IMG_OBJ_ELEMENT );
		Image dataImage = PlatformUI.getWorkbench( ).getSharedImages( ).getImage( ISharedImages.IMG_OBJ_FILE );

		public Image getImage( Object element )
		{
			if ( element instanceof TreeNode )
			{
				if ( element instanceof Data )
				{
					return dataImage;
				}
				else
				{
					return categoryImage;
				}
			}

			return null;
		}

		public String getText( Object element )
		{
			return element == null ? "" : element.toString( );
		}
	}
}
