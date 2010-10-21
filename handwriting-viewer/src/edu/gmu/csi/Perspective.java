package edu.gmu.csi;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import edu.gmu.csi.view.CharacterImageView;
import edu.gmu.csi.view.DataListView;

public class Perspective implements IPerspectiveFactory
{

	public void createInitialLayout( IPageLayout layout )
	{
		layout.setEditorAreaVisible( false );
		layout.setFixed( false );

		layout.addView( DataListView.ID, IPageLayout.LEFT, 0.8f, layout.getEditorArea( ) );
		layout.addView( CharacterImageView.ID, IPageLayout.RIGHT, 0.2f, DataListView.ID );

	}

}
