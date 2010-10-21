package edu.gmu.csi;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor
{

	private static final String PERSPECTIVE_ID = "handwriting-viewer.perspective";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor( IWorkbenchWindowConfigurer configurer )
	{
		return new ApplicationWorkbenchWindowAdvisor( configurer );
	}

	public String getInitialWindowPerspectiveId( )
	{
		return PERSPECTIVE_ID;
	}

}
