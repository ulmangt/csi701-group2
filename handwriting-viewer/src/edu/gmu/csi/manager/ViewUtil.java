package edu.gmu.csi.manager;

import java.util.Arrays;
import java.util.List;

import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import edu.gmu.csi.view.CharacterImageView;
import edu.gmu.csi.view.ConfusionMatrixView;

public class ViewUtil
{
	public static ConfusionMatrixView getConfusionMatrixView( )
	{
		IWorkbench workbench = PlatformUI.getWorkbench( );
		if ( workbench == null ) return null;

		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow( );
		if ( workbenchWindow == null ) return null;

		IWorkbenchPage workbenchPage = workbenchWindow.getActivePage( );
		if ( workbenchPage == null ) return null;

		List<IViewReference> viewRefList = Arrays.asList( workbenchPage.getViewReferences( ) );

		for ( IViewReference viewRef : viewRefList )
		{
			if ( ConfusionMatrixView.ID.equals( viewRef.getId( ) ) )
			{
				return ( ConfusionMatrixView ) viewRef.getView( false );
			}
		}

		return null;
	}
	
	public static CharacterImageView getCharacterImageView( )
	{
		IWorkbench workbench = PlatformUI.getWorkbench( );
		if ( workbench == null ) return null;

		IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow( );
		if ( workbenchWindow == null ) return null;

		IWorkbenchPage workbenchPage = workbenchWindow.getActivePage( );
		if ( workbenchPage == null ) return null;

		List<IViewReference> viewRefList = Arrays.asList( workbenchPage.getViewReferences( ) );

		for ( IViewReference viewRef : viewRefList )
		{
			if ( CharacterImageView.ID.equals( viewRef.getId( ) ) )
			{
				return ( CharacterImageView ) viewRef.getView( false );
			}
		}

		return null;
	}
}
