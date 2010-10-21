package edu.gmu.csi.view;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.ui.progress.IWorkbenchSiteProgressService;
import org.eclipse.ui.IWorkbenchPartSite;

import edu.gmu.csi.database.PopulateDataListViewJob;
import edu.gmu.csi.model.Root;
import edu.gmu.csi.model.TreeNode;

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

		treeViewer.expandAll( );

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
