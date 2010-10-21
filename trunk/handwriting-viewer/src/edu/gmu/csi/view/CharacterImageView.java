package edu.gmu.csi.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class CharacterImageView extends ViewPart
{
	public static final String ID = "handwriting-viewer.characterimageview";

	@Override
	public void createPartControl( Composite parent )
	{
		final Canvas canvas = new Canvas( parent, SWT.DOUBLE_BUFFERED );
		
		
		
		canvas.addPaintListener( new PaintListener( )
		{
			@Override
			public void paintControl( PaintEvent e )
			{
				GC gc = e.gc;
				
				
			}
		});
	}

	@Override
	public void setFocus( )
	{
		// TODO Auto-generated method stub
		
	}
}
