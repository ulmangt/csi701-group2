package edu.gmu.csi.view;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

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

import edu.gmu.csi.manager.ViewUtil;
import edu.gmu.csi.model.ConfusionMatrix;
import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.Result;

public class ConfusionMatrixView extends ViewPart
{
	public static final String ID = "handwriting-viewer.confusionmatrixview";

	private Canvas canvas;
	
	private ReentrantLock lock;
	private volatile boolean matrixUpdated;
	private ConfusionMatrix newConfusionMatrix;
	private ConfusionMatrix confusionMatrix;
	
	public ConfusionMatrixView( )
	{
		this.lock = new ReentrantLock( );
	}
	
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
				if ( matrixUpdated )
				{
					lock.lock( );
					try
					{
						confusionMatrix = newConfusionMatrix;
						matrixUpdated = false;
					}
					finally
					{
						lock.unlock( );
					}
				}
				
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
						gc.setForeground( colorForeground );
						gc.drawRectangle( (int) (x * widthStep), (int) (y * heightStep), (int) widthStep, (int) heightStep );
					}
				}
				
				if ( confusionMatrix != null )
				{
					for ( int x = 2 ; x < rows ; x++ )
					{
						for ( int y = 2 ; y < cols ; y++ )
						{
							List<Data> dataList = confusionMatrix.get( x-2, y-2 );
							String s = String.valueOf( dataList == null ? 0 : dataList.size( ) );
							Point p = gc.stringExtent( s );
							
							if ( p.x > widthStep || p.y > heightStep )
								s = "";
							
							gc.setForeground( color );
							gc.drawString( s, (int) (x * widthStep + widthStep / 2 - p.x / 2), (int) (y * heightStep + heightStep / 2 - p.y / 2), true );
						}
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
				
				List<Data> dataList = null;
				
				lock.lock( );
				try
				{
					dataList = newConfusionMatrix.get( xIndex, yIndex );
				}
				finally
				{
					lock.unlock( );
				}
				
				ViewUtil.getCharacterImageView( ).setSelection( dataList );
			}
			
		});
		
	}
	
	@Override
	public void setFocus( )
	{
		// do nothing
	}
	
	public void setConfusionMatrix( List<Result> results )
	{
		this.lock.lock( );
		try
		{
			if ( results == null )
			{
				this.newConfusionMatrix = null;
			}
			else
			{
				this.newConfusionMatrix = new ConfusionMatrix( results );
			}
			
			this.matrixUpdated = true;
		}
		finally
		{
			this.lock.unlock( );
		}
		
		redrawCanvas( );
	}
	
	protected void redrawCanvas( )
	{
		Display.getDefault( ).asyncExec( new Runnable( )
		{
			@Override
			public void run( )
			{
				canvas.redraw( );
			}
		});
	}
}
