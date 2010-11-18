package edu.gmu.csi.view;

import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.data.CharacterData;
import edu.gmu.csi.model.data.CharacterImageData;

public class ClassificatonView extends ViewPart
{
	public static final String ID = "handwriting-viewer.classificationview";
	
	public static final int IMAGE_HEIGHT = 28;
	public static final int IMAGE_WIDTH = 28;
	
	protected CharacterImagePainter painter;
	protected PaletteData palette;
	protected Canvas canvas;
	protected Image image;
	protected boolean mouseDown = false;

	public ClassificatonView( )
	{
		RGB[] colors = new RGB[256];
		for ( int i = 0; i < 256; i++ )
		{
			int color = 255 - i;
			colors[i] = new RGB( color, color, color );
		}

		palette = new PaletteData( colors );
		
		image = createBlankImage( );
	}
	
	private class CharacterImagePainter implements PaintListener
	{

		@Override
		public void paintControl( PaintEvent e )
		{
			Point p = canvas.getSize( );
			int width = p.x;
			int height = p.y;

			GC gc = e.gc;
			
			gc.drawImage( image,
					      0, 0, IMAGE_WIDTH, IMAGE_HEIGHT,
					      0, 0, width, height );
		}
	
	}
	
	public Image createBlankImage( )
	{
		Display display = Display.getDefault( );
		ImageData sourceData = new ImageData( IMAGE_WIDTH, IMAGE_HEIGHT, 8, palette, 1, new byte[ IMAGE_WIDTH * IMAGE_HEIGHT ] );
		Image image = new Image( display, sourceData );
		return image;
	}

	
	@Override
	public void createPartControl( Composite parent )
	{
		canvas = new Canvas( parent, SWT.DOUBLE_BUFFERED );
		painter = new CharacterImagePainter( );
		canvas.addPaintListener( painter );
		
		canvas.addMouseMoveListener( new MouseMoveListener( )
		{
			@Override
			public void mouseMove( MouseEvent e )
			{
				System.out.println( mouseDown );
				
				if ( mouseDown )
				{
					int imageX = e.x / IMAGE_WIDTH;
					if ( imageX < 0 ) imageX = 0;
					if ( imageX >= IMAGE_WIDTH ) imageX = IMAGE_WIDTH - 1;
					
					int imageY = e.x / IMAGE_WIDTH;
					if ( imageY < 0 ) imageY = 0;
					if ( imageY >= IMAGE_WIDTH ) imageY = IMAGE_WIDTH - 1;
					
					// if shift is held down, delete
					if ( ( e.stateMask & SWT.SHIFT ) != 0 )
					{
						image.getImageData( ).setPixel( imageX, imageY, 0 );
					}
					else
					{
						image.getImageData( ).setPixel( imageX, imageY, 255 );
					}
					
					redrawCanvas( );
				}
			}
		});
		
		canvas.addMouseListener( new MouseListener( )
		{
			@Override
			public void mouseDoubleClick( MouseEvent e )
			{
			}

			@Override
			public void mouseDown( MouseEvent e )
			{
				mouseDown = true;
				
				System.out.println( mouseDown );
			}

			@Override
			public void mouseUp( MouseEvent e )
			{
				mouseDown = false;
				
				System.out.println( mouseDown );
			}
		});
	}

	@Override
	public void setFocus( )
	{
		canvas.setFocus( );
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
