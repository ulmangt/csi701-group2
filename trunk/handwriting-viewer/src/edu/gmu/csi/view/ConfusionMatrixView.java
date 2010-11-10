package edu.gmu.csi.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

public class ConfusionMatrixView extends ViewPart
{
	public static final String ID = "handwriting-viewer.confusionmatrixview";

	private Canvas canvas;
	
	private int[][] confusionMatrix = new int[10][10]; //[x][y] //[truth][classification]
	
	@Override
	public void createPartControl( Composite parent )
	{
		Display display = Display.getDefault( );
		
		final Color colorForeground = display.getSystemColor( SWT.COLOR_WIDGET_FOREGROUND );
		final Color color = display.getSystemColor( SWT.COLOR_WIDGET_BORDER );
//		final Color color = display.getSystemColor( SWT.COLOR_RED );
		
		canvas = new Canvas( parent, SWT.DOUBLE_BUFFERED | SWT.BORDER );
		canvas.addPaintListener( new PaintListener( )
		{
			@Override
			public void paintControl( PaintEvent e )
			{
				GC gc = e.gc;
				
				int width = e.width;
				int height = e.height;
				
				int rows = 12;
				int cols = 12;
				
				float widthStep = width / rows;
				float heightStep = height / cols;
			
				gc.setForeground( color );
				gc.setLineWidth( 2 );
				
				gc.setForeground( color );
				
				for ( int x = 2 ; x < rows ; x++ )
				{
					String s = String.valueOf( x - 2 );
					Point p = gc.stringExtent( s );
					
					if ( p.x > widthStep || p.y > heightStep )
						s = "";
					
					gc.drawString( s, (int) (x * widthStep + widthStep / 2 - p.x / 2), (int) (heightStep + heightStep / 2 - p.y / 2)  );
				}
				
				for ( int y = 2 ; y < rows ; y++ )
				{
					String s = String.valueOf( y - 2 );
					Point p = gc.stringExtent( s );
					
					if ( p.x > widthStep || p.y > heightStep )
						s = "";
					
					gc.drawString( s, (int) (widthStep + widthStep / 2 - p.x / 2), (int) (y * heightStep + heightStep / 2 - p.y / 2)  );
				}
				
				for ( int x = 2 ; x < rows ; x++ )
				{
					for ( int y = 2 ; y < cols ; y++ )
					{
						String s = String.valueOf( confusionMatrix[x-2][y-2] );
						Point p = gc.stringExtent( s );
						
						if ( p.x > widthStep || p.y > heightStep )
							s = "";
						
						gc.setForeground( colorForeground );
						gc.drawRectangle( (int) (x * widthStep), (int) (y * heightStep), (int) widthStep, (int) heightStep );
						gc.setForeground( color );
						gc.drawString( s, (int) (x * widthStep + widthStep / 2 - p.x / 2), (int) (y * heightStep + heightStep / 2 - p.y / 2), true );
					}
				}
			}
		} );
		
		canvas.addMouseListener( new MouseListener( )
		{

			@Override
			public void mouseDoubleClick( MouseEvent e )
			{
				// do nothing
			}

			@Override
			public void mouseDown( MouseEvent e )
			{
				// do nothing
			}

			@Override
			public void mouseUp( MouseEvent e )
			{
				int width = canvas.getBounds( ).width;
				int height = canvas.getBounds( ).height;
				
				int rows = 12;
				int cols = 12;
				
				float widthStep = width / rows;
				float heightStep = height / cols;
				
				int xIndex = (int) Math.floor(e.x / widthStep) - 2;
				int yIndex = (int) Math.floor(e.y / heightStep) - 2;
				
				System.out.println( xIndex  +  " " + yIndex );
			}
			
		});
		
	}
	
	@Override
	public void setFocus( )
	{
		// do nothing
	}
}
