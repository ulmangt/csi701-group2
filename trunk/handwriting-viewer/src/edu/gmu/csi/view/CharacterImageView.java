package edu.gmu.csi.view;

import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.swt.SWT;
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

import edu.gmu.csi.model.data.CharacterData;

public class CharacterImageView extends ViewPart
{
	public static final String ID = "handwriting-viewer.characterimageview";

	private Canvas canvas;
	private CharacterImagePainter painter;
	
	private PaletteData palette;
	
	public CharacterImageView( )
	{
	    RGB[] colors = new RGB[256];
	    for ( int i = 0 ; i < 256 ; i++ )
	    {
	    	int color = 255-i;
	    	colors[i] = new RGB(color,color,color);
	    }
	    
	    palette = new PaletteData( colors );
	}
	
	private class CharacterImagePainter implements PaintListener
	{
		private volatile boolean imageChanged;
		private ReentrantLock imageLock;
		private Image newImage;
		private Image displayImage;
		
		public CharacterImagePainter( )
		{
			imageLock = new ReentrantLock( );
		}
		
		public void setImage( Image _image )
		{
			imageLock.lock( );
			try
			{
				newImage = _image;
				imageChanged = true;
			}
			finally
			{
				imageLock.unlock( );
			}
		}
		
		@Override
		public void paintControl( PaintEvent e )
		{
			Point p = canvas.getSize( );
			int width = p.x;
			int height = p.y;
			
			GC gc = e.gc;
			
			if ( imageChanged )
			{
				imageLock.lock( );
				try
				{
					displayImage = newImage;
					newImage = null;
					imageChanged = false;
				}
				finally
				{
					imageLock.unlock( );
				}
			}

			gc.drawLine( 0, 0, width, height );
			
			if ( displayImage != null )
			{
				gc.drawImage( displayImage, 0, 0, 28, 28, 0, 0, width, height );
			}
		}
	}
	
	public void setImage( CharacterData data )
	{
		setImage( createImage( data ) );
	}
	
	public void setImage( Image image )
	{
		painter.setImage( image );
		canvas.redraw( );
	}
	
	public Image createImage( CharacterData data )
	{
		Display display = Display.getDefault( );
	    ImageData sourceData = new ImageData( data.getImageColumns( ), data.getImageRows( ), 8, palette, 1, data.getImageData( ) );
	    return new Image(display, sourceData);
	}
	
	@Override
	public void createPartControl( Composite parent )
	{
		canvas = new Canvas( parent, SWT.DOUBLE_BUFFERED );
		painter = new CharacterImagePainter( );
		canvas.addPaintListener( painter );
	}
	
	@Override
	public void setFocus( )
	{
		// TODO Auto-generated method stub
		
	}
}
