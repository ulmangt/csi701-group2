package edu.gmu.csi;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import edu.gmu.csi.view.DataListView;

public class Perspective implements IPerspectiveFactory
{

	public void createInitialLayout( IPageLayout layout )
	{
		layout.setEditorAreaVisible( false );
		layout.setFixed( false );

		layout.addView( DataListView.ID, IPageLayout.LEFT, 0.2f, layout.getEditorArea( ) );

	}

}
