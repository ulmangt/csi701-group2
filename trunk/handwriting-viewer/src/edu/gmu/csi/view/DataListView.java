package edu.gmu.csi.view;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class DataListView extends ViewPart {
	public static final String ID = "handwriting-viewer.datalistview";

	private TreeViewer treeViewer;
	
	public DataListView( )
	{
		
	}
	
	@Override
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent);
		
		treeViewer.setContentProvider(new DataListContentProvider());
		treeViewer.setLabelProvider(new DataListLabelProvider());
		treeViewer.setInput(getInitialInput());
		
		treeViewer.expandAll();
	}	
	
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}
	
	private Object getInitialInput( )
	{
		return null;
	}
	
	private class DataListContentProvider implements ITreeContentProvider
	{

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object[] getElements(Object inputElement) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getParent(Object element) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	private class DataListLabelProvider extends LabelProvider
	{
		public Image getImage(Object element) {
			return null;
		}

		public String getText(Object element) {
			return element == null ? "" : element.toString();
		}
	}
}
