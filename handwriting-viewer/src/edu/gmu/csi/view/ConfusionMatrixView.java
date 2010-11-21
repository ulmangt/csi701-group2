package edu.gmu.csi.view;

import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
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
	private Point newSelection;
	private Point selection;
	
	private ColorChooser colorChooser;
	
	private ConfusionMatrix newConfusionMatrix;
	private ConfusionMatrix confusionMatrix;
	
	private Transform transform;
	
	public ConfusionMatrixView( )
	{
		this.lock = new ReentrantLock( );
	}
	
	@Override
	public void createPartControl( Composite parent )
	{
		Display display = Display.getDefault( );
		
		transform = new Transform( display );
        float cos45 = (float)Math.cos(Math.PI/2);
        float sin45 = (float)Math.sin(Math.PI/2);
        transform.setElements(cos45, sin45, -sin45, cos45, 0, 0);
		
		final Color colorForeground = display.getSystemColor( SWT.COLOR_WIDGET_FOREGROUND );
		final Color color = display.getSystemColor( SWT.COLOR_WIDGET_BORDER );
		final Color colorRed = display.getSystemColor( SWT.COLOR_RED );
		
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
						
						if ( colorChooser != null )
							colorChooser.dispose( );
						
						selection = newSelection;
						confusionMatrix = newConfusionMatrix;
						colorChooser = new ColorChooser( 0, newConfusionMatrix.getMaxOffDiagonalCount( ), 200 );
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
				
				float widthStep = (float) width / (float) rows;
				float heightStep = (float) height / (float) cols;
			
				Color bg = gc.getBackground( );
				
				gc.setForeground( color );
				gc.setLineWidth( 2 );
				
				gc.setForeground( color );
				
				for ( int x = 2 ; x < rows ; x++ )
				{
					String s = String.valueOf( x - 2 );
					Point p = gc.stringExtent( s );
					
					if ( p.x > widthStep || p.y > heightStep )
						s = "";
					
					gc.drawString( s, (int) (x * widthStep + widthStep / 2.0 - p.x / 2.0), (int) (heightStep + heightStep / 2.0 - p.y / 2.0)  );
				}
				
				for ( int y = 2 ; y < rows ; y++ )
				{
					String s = String.valueOf( y - 2 );
					Point p = gc.stringExtent( s );
					
					if ( p.x > widthStep || p.y > heightStep )
						s = "";
					
					gc.drawString( s, (int) (widthStep + widthStep / 2.0 - p.x / 2.0), (int) (y * heightStep + heightStep / 2.0 - p.y / 2.0)  );
				}
				
				for ( int x = 2 ; x < rows ; x++ )
				{
					for ( int y = 2 ; y < cols ; y++ )
					{	
						if ( confusionMatrix != null )
						{
							List<Data> dataList = confusionMatrix.get( x-2, y-2 );
							int size = dataList == null ? 0 : dataList.size( );
							Color color = colorChooser.getColor( size );
							
							if ( color != null )
							{
								gc.setBackground( color );
								gc.setAlpha( 100 );
								gc.fillRectangle( (int) (x * widthStep), (int) (y * heightStep), (int) widthStep, (int) heightStep );
							}
						}
						
						gc.setAlpha( 255 );
						gc.setForeground( colorForeground );
						gc.drawRectangle( (int) (x * widthStep), (int) (y * heightStep), (int) widthStep, (int) heightStep );
					}
				}
				
				if ( selection != null )
				{
					gc.setForeground( colorRed );
					gc.drawRectangle( (int) ( (selection.x + 2) * widthStep), (int) ( (selection.y + 2) * heightStep), (int) widthStep, (int) heightStep );
				}
				
				gc.setBackground( bg );
				
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
							gc.drawString( s, (int) (x * widthStep + widthStep / 2.0 - p.x / 2.0), (int) (y * heightStep + heightStep / 2.0 - p.y / 2.0), true );
						}
					}
				}
				
				Point p = gc.stringExtent( "Classifier" );
				gc.drawString( "Classifier", (int) (2*widthStep + ( width - 2*widthStep ) / 2.0 - p.x/2.0), (int) (heightStep/2.0 - p.y/2.0) );
				
				p = gc.stringExtent( "Truth" );
		        gc.setTransform( transform );
				gc.drawString( "Truth", (int) (2*heightStep + ( height - 2*heightStep ) / 2.0 - p.x/2.0), (int) ( -widthStep/2.0 - p.y/2.0 ) );
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
				
				float widthStep = (float) width / (float) rows;
				float heightStep = (float) height / (float) cols;
				
				int xIndex = (int) Math.floor(e.x / widthStep) - 2;
				int yIndex = (int) Math.floor(e.y / heightStep) - 2;
				
				List<Data> dataList = null;
				
				lock.lock( );
				try
				{
					dataList = newConfusionMatrix.get( xIndex, yIndex );
					newSelection = new Point( xIndex, yIndex );
					matrixUpdated = true;
					
				}
				finally
				{
					lock.unlock( );
				}
				
				ViewUtil.getCharacterImageView( ).setSelection( dataList );
				
				redrawCanvas( );
			}
			
		});
		
	}
	
	@Override
	public void setFocus( )
	{
		canvas.setFocus( );
	}
	
	public void setConfusionMatrix( final List<Result> results )
	{
		lock.lock( );
		try
		{
			if ( results == null )
			{
				newConfusionMatrix = null;
			}
			else
			{
				newConfusionMatrix = new ConfusionMatrix( results );
			}
			
			matrixUpdated = true;
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
		finally
		{
			lock.unlock( );
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
				Rectangle r = canvas.getBounds( );
				canvas.redraw( 0, 0, r.width, r.height, true );
			}
		});
	}
	
	private class ColorChooser
	{
		private class ValueColor implements Comparable<ValueColor>
		{
			public float minVal;
			public Color color;
			
			public ValueColor( float minVal, Color color )
			{
				this.minVal = minVal;
				this.color = color;
			}

			@Override
			public int compareTo( ValueColor o )
			{
				if ( minVal < o.minVal )
					return -1;
				else if ( minVal > o.minVal )
					return 1;
				else
					return 0;
			}
		}
		
		private NavigableSet<ValueColor> colors;
		
		public ColorChooser( float min, float max, int steps )
		{
			Display display = Display.getDefault( );
			
			this.colors = new TreeSet<ValueColor>( );
			
			float[] temp = new float[4];
			float step = ( max - min ) / steps;
			for ( float current = min ; current <= max ; current += step )
			{
				toColor( ( current - min )  / ( max - min ), temp );
				
				int r = (int) (temp[0] * 255);
				int g = (int) (temp[1] * 255);
				int b = (int) (temp[2] * 255);
				
				colors.add( new ValueColor( current, new Color( display, r, g, b ) ) );
			}
		}
		
		public void dispose( )
		{
			for ( ValueColor c : colors )
			{
				c.color.dispose( );
			}
		}
		
		public Color getColor( float f )
		{
			try
			{
				ValueColor value = colors.floor( new ValueColor( f, null ) );
				
				if ( value != null )
					return value.color;
				else
					return null;
			}
			catch ( Exception e )
			{
				return null;
			}
		}
		
        public void toColor(float fraction, float[] rgba)
        {
            float x = 4 * fraction;
            int segment = (int) (8 * fraction);
            switch (segment)
            {
                case 0:
                    rgba[0] = 0;
                    rgba[1] = 0;
                    rgba[2] = 0.5f + x;
                    break;
                
                case 1:
                case 2:
                    rgba[0] = 0;
                    rgba[1] = -0.5f + x;
                    rgba[2] = 1;
                    break;
                
                case 3:
                case 4:
                    rgba[0] = -1.5f + x;
                    rgba[1] = 1;
                    rgba[2] = 2.5f - x;
                    break;
                
                case 5:
                case 6:
                    rgba[0] = 1;
                    rgba[1] = 3.5f - x;
                    rgba[2] = 0;
                    break;
                
                default:
                    rgba[0] = 4.5f - x;
                    rgba[1] = 0;
                    rgba[2] = 0;
                    break;
            }
            rgba[3] = 1;
        }
	}
}
