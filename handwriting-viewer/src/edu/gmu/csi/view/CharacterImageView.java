package edu.gmu.csi.view;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.gmu.csi.manager.CharacterDataManager;
import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.data.CharacterData;
import edu.gmu.csi.model.data.CharacterImageData;

public class CharacterImageView extends ViewPart
{
	public static final String ID = "handwriting-viewer.characterimageview";

	private Canvas canvas;
	private CharacterImagePainter painter;
	private PaletteData palette;
	
	private ReentrantLock loadedImagesLock;
	private Map<Data,WeakReference<CharacterImageData>> loadedImages;
	
	private ReferenceQueue<CharacterImageData> referenceQueue;
	
	private ReentrantLock selectionLock;
	private List<CharacterImageData> newLoadedImages;
	private List<Data> newSelection;
	private boolean syncNewSelection = false;
	private boolean syncShowIds = false;
	
	private volatile boolean syncUpdated = false;

	public CharacterImageView( )
	{
		RGB[] colors = new RGB[256];
		for ( int i = 0; i < 256; i++ )
		{
			int color = 255 - i;
			colors[i] = new RGB( color, color, color );
		}

		palette = new PaletteData( colors );
		
		selectionLock = new ReentrantLock( );
		loadedImagesLock = new ReentrantLock( );
		
		newLoadedImages = new LinkedList<CharacterImageData>( );
		
		loadedImages = new HashMap<Data,WeakReference<CharacterImageData>>( );
		
		referenceQueue = new ReferenceQueue<CharacterImageData>( );
		
		new Thread( new ImageDisposer( ) ).start( );
	}
	
	// make sure that SWT Images (which use native system resources) are properly
	// disposed of when their WeakReferences are garbage collected
	private class ImageDisposer implements Runnable
	{
		@Override
		public void run( )
		{
			try
			{
				Reference<? extends CharacterImageData> reference = referenceQueue.remove( );
				CharacterImageData imageData = reference.get( );
				
				if ( imageData == null ) return;
				
				Image image = imageData.getImage( );
				
				if ( image == null ) return;
				
				System.out.println( "Memory low, disposing of image: " + imageData );
				
				image.dispose( );
			}
			catch ( InterruptedException e )
			{
				e.printStackTrace();
			}
		}
	}

	private class CharacterImagePainter implements PaintListener
	{
		private Map<Data,Integer> dataIndex;
		private Map<Integer,CharacterImageData> imageIndex;
		private int columns = 0;
		private int rows = 0;
		private int total = 0;
		private boolean showIds = false;
		
		public CharacterImagePainter( )
		{
			this.dataIndex = new HashMap<Data,Integer>( );
			this.imageIndex = new HashMap<Integer,CharacterImageData>( );
		}

		@Override
		public void paintControl( PaintEvent e )
		{
			Point p = canvas.getSize( );
			int width = p.x;
			int height = p.y;

			GC gc = e.gc;

			if ( syncUpdated )
			{
				selectionLock.lock( );
				try
				{
					if ( syncNewSelection )
					{
						total = newSelection == null ? 0 : newSelection.size( );
						double _columns = Math.ceil( Math.sqrt( total ) );
						double _rows = Math.ceil( total / _columns );
						
						columns = (int) _columns;
						rows = (int) _rows;
						
						dataIndex.clear( );
						imageIndex.clear( );
						
						for ( int i = 0 ; i < total ; i++ )
						{
							dataIndex.put( newSelection.get(i), i );
						}
						
						syncNewSelection = false;
					}
					
					for ( CharacterImageData newImage : newLoadedImages )
					{
						Integer index = dataIndex.get( newImage.getData( ) );
						if ( index != null ) imageIndex.put( index, newImage );
					}
					
					showIds = syncShowIds;
					
					newLoadedImages.clear( );
					syncUpdated = false;
				}
				finally
				{
					selectionLock.unlock( );
				}
			}
			
			if ( columns ==  0 )
				return;

			double widthStep = width / columns;
			double heightStep = height / rows;
			
			for ( Entry<Data,Integer> dataEntry : dataIndex.entrySet( ) )
			{
				Data data = dataEntry.getKey( );
				Integer index = dataEntry.getValue( );
				
				CharacterImageData image = imageIndex.get( index );
				
				int col = index % columns;
				int row = index / columns;
				
				int posX = (int) (col * widthStep);
				int posY = (int) (row * heightStep);
				
				if ( image != null )
				{
					gc.drawImage( image.getImage( ),
							      0, 0, image.getImageColumns( ), image.getImageRows( ),
							      posX, posY, (int) widthStep, (int) heightStep );
				}
				
				if ( syncShowIds )
				{
					gc.drawString( String.valueOf( data.getId( ) ), posX, posY );
				}
			}
		}
	}

	public CharacterImageData createImage( CharacterData data )
	{
		Display display = Display.getDefault( );
		ImageData sourceData = new ImageData( data.getImageColumns( ), data.getImageRows( ), 8, palette, 1, data.getImageData( ) );
		return new CharacterImageData( data, new Image( display, sourceData ) );
	}
	
	public void setSelection( List<Data> _newSelection )
	{
		selectionLock.lock( );
		try
		{
			newSelection = _newSelection;
			syncNewSelection = true;
			syncUpdated = true;
			
		}
		finally
		{
			selectionLock.unlock( );
		}
		
		if ( _newSelection == null || _newSelection.isEmpty( ) )
		{
			// nothing to load
		}
		else
		{
			for ( final Data data : _newSelection )
			{
				loadedImagesLock.lock( );
				try
				{
					WeakReference<CharacterImageData> imageRef = loadedImages.get( data );
					if ( imageRef != null )
					{
						CharacterImageData image = imageRef.get( );
						if ( image != null )
						{
							selectionLock.lock( );
							try
							{
								newLoadedImages.add( image );
								syncUpdated = true;
							}
							finally
							{
								selectionLock.unlock( );
							}
						}
						else
						{
							queryForData( data );
						}
					}
					else
					{
						queryForData( data );
					}
				}
				catch ( Exception e )
				{
					e.printStackTrace( );
				}
				finally
				{
					loadedImagesLock.unlock( );
				}
			}
		}
		
		redrawCanvas( );
	}

	protected void queryForData( final Data data )
	{
		(new Thread( ) {
			public void run( )
			{
				try
				{
					Future<CharacterData> dataFuture = CharacterDataManager.getInstance( ).getCharacterData( data );	
					CharacterData characterData = dataFuture.get( );
					
					if ( characterData != null )
					{
						CharacterImageData image = createImage( characterData );
						
						loadedImagesLock.lock( );
						try
						{
							loadedImages.put( data, new WeakReference<CharacterImageData>( image, referenceQueue ) );
						}
						finally
						{
							loadedImagesLock.unlock( );
						}
						
						selectionLock.lock( );
						try
						{
							newLoadedImages.add( image );
							syncUpdated = true;
						}
						finally
						{
							selectionLock.unlock( );
						}
						
						redrawCanvas( );
					}
				}
				catch ( Exception e )
				{
					e.printStackTrace( );
				}
			}
		}).start( );
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
	
	@Override
	public void createPartControl( Composite parent )
	{
		canvas = new Canvas( parent, SWT.DOUBLE_BUFFERED );
		painter = new CharacterImagePainter( );
		canvas.addPaintListener( painter );
		
		IToolBarManager toolbarManager = getViewSite( ).getActionBars( ).getToolBarManager( );
		toolbarManager.add( new Action( "Show Ids", IAction.AS_CHECK_BOX )
		{
			{
				ImageDescriptor openImage = PlatformUI.getWorkbench( ).getSharedImages( ).getImageDescriptor( ISharedImages.IMG_OBJS_INFO_TSK );
				setImageDescriptor( openImage );
			}
			
			@Override
			public void run( )
			{
				selectionLock.lock( );
				try
				{
					syncShowIds = isChecked( );
					syncUpdated = true;
				}
				finally
				{
					selectionLock.unlock( );
				}
				
				redrawCanvas( );
			}
		});
	}

	@Override
	public void setFocus( )
	{
		canvas.setFocus( );
	}
}
