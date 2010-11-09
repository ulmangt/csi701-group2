package edu.gmu.csi.view.tree;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.gmu.csi.model.TreeNode;

public class TreeNodeContentProvider implements ITreeContentProvider
{

	@Override
	public void dispose( )
	{
		// do nothing
	}

	@Override
	public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
	{
		// do nothing
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