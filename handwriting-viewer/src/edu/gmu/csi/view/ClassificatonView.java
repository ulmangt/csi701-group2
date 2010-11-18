package edu.gmu.csi.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

import net.miginfocom.swt.MigLayout;

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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;

import edu.gmu.csi.manager.CharacterDataManager;
import edu.gmu.csi.manager.DataManager;

import edu.gmu.csi.model.Character;
import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.data.CharacterData;

public class ClassificatonView extends ViewPart
{
	public static final String ID = "handwriting-viewer.classificationview";
	
	public static final int IMAGE_HEIGHT = 28;
	public static final int IMAGE_WIDTH = 28;
	
	public static final int SAMPLES_PER_CHARACTER = 100;
	
	protected CharacterImagePainter painter;
	protected PaletteData palette;
	protected Canvas canvas;
	protected Image image;
	protected boolean mouseDown = false;
	private ReentrantLock imageLock;

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
		
		imageLock = new ReentrantLock( );
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
			
			imageLock.lock( );
			try
			{
				gc.drawImage( image,
					          0, 0, IMAGE_WIDTH, IMAGE_HEIGHT,
					          0, 0, width, height );
			}
			finally
			{
				imageLock.unlock( );
			}
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
		parent.setLayout( new MigLayout( ) );
		
		canvas = new Canvas( parent, SWT.DOUBLE_BUFFERED );
		canvas.setLayoutData( "align center, grow, push, wrap" );
		painter = new CharacterImagePainter( );
		canvas.addPaintListener( painter );
		
		Button classify = new Button( parent, SWT.PUSH );
		classify.setText( "Classify" );
		classify.setLayoutData( "wrap" );
		
		classify.addListener( SWT.Selection, new Listener( )
		{
			@Override
			public void handleEvent( Event event )
			{
				System.out.println( "clicked" );
				classify( );
			}
		});
		
		canvas.addMouseMoveListener( new MouseMoveListener( )
		{
			@Override
			public void mouseMove( MouseEvent e )
			{
				if ( mouseDown )
				{
					Rectangle r = canvas.getBounds( );
					double stepX = r.width / (double) IMAGE_WIDTH;
					double stepY = r.height / (double) IMAGE_HEIGHT;
					
					int imageX = (int) Math.floor( e.x / stepX );
					if ( imageX < 0 ) imageX = 0;
					if ( imageX >= IMAGE_WIDTH ) imageX = IMAGE_WIDTH - 1;
					
					int imageY = (int) Math.floor( e.y / stepY );
					if ( imageY < 0 ) imageY = 0;
					if ( imageY >= IMAGE_WIDTH ) imageY = IMAGE_WIDTH - 1;
					
					ImageData data = image.getImageData( );
					boolean modified = false;
					
					// if shift is held down, delete
					if ( ( e.stateMask & SWT.SHIFT ) != 0 )
					{
						// this doesn't work for some reason, so do nothing

						if ( data.getPixel( imageX, imageY ) != -1 )
						{
							data.setPixel( imageX, imageY, -1 );
							modified = true;
						}
					}
					else
					{
						if ( data.getPixel( imageX, imageY ) != 0 )
						{
							data.setPixel( imageX, imageY, 0 );
							modified = true;
						}
					}
					
					if ( modified )
					{
						imageLock.lock( );
						try
						{
							image.dispose( );
							
							image = new Image( Display.getDefault( ), data );
						
						}
						finally
						{
							imageLock.unlock( );
						}
						
						redrawCanvas( );
					}
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
			}

			@Override
			public void mouseUp( MouseEvent e )
			{
				mouseDown = false;
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
	
	protected void classify( )
	{
		int[] data = convertSignedToUnsigned( image.getImageData( ).data, 3 );
		
		System.out.println( data.length + " " + ( 28 * 28 ) + " " + Arrays.toString( data ) );
		
		DataManager dataManager = DataManager.getInstance( );
		CharacterDataManager characterManager = CharacterDataManager.getInstance( );
		
		for ( int i = 0 ; i < 10 ; i++ )
		{
			Character character = dataManager.getCharacterData( String.valueOf( i ) );
			
			System.out.println( character );
			
			List<Data> dataList = new ArrayList<Data>( character.getDataList( ) );
			//Collections.shuffle( dataList ); // for now don't randomize
			List<Data> trainingSet = dataList.subList( 0, Math.min( dataList.size(), 1 ) );
			
			for ( Data trainingData : trainingSet )
			{
				CharacterData characterData;
				
				try
				{
					characterData = characterManager.getCharacterData( trainingData ).get( );
					int[] trainingDataArray = convertSignedToUnsigned( characterData.getImageData( ) );
					System.out.println( i + " " + trainingDataArray.length + " " + Arrays.toString( trainingDataArray ) );
					
				}
				catch ( InterruptedException e )
				{
					e.printStackTrace();
				}
				catch ( ExecutionException e )
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	protected int[] convertSignedToUnsigned( byte[] data )
	{
		return convertSignedToUnsigned( data, 1 );
	}
	
	protected int[] convertSignedToUnsigned( byte[] data, int downsample )
	{
		int[] converted = new int[ data.length / downsample ];
		
		int ci = 0;
		for ( int i = 0 ; i < data.length ; i = i + downsample )
		{
			byte raw = data[i];
			converted[ci++] = raw < 0 ? 256 + raw : raw;
		}
		
		return converted;
	}

}
