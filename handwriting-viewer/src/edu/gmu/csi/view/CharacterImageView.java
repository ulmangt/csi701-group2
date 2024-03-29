package edu.gmu.csi.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.gmu.csi.manager.CharacterDataManager;
import edu.gmu.csi.manager.DataManager;
import edu.gmu.csi.manager.MetadataColorManager;
import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.Metadata;
import edu.gmu.csi.model.data.CharacterData;
import edu.gmu.csi.model.data.CharacterImageData;

public class CharacterImageView extends ViewPart
{
	public static final String ID = "handwriting-viewer.characterimageview";
	
	private static final int REDRAW_INTERVAL_MILLIS = 2000;
	
	private Canvas canvas;
	private CharacterImagePainter painter;
	private PaletteData palette;
	
	private String selectedMetadata;
	
	private ReentrantLock redrawTimer;
	private long lastRedrawTime;
	private long lastRedrawRequest;
	
	private ReentrantLock selectionLock;
	private List<CharacterImageData> newLoadedImages;
	private List<Data> newSelection;
	private boolean syncNewSelection = false;
	private boolean syncShowIds = false;
	private int selectionCounter;
	private int selectionTotal;
	
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
		redrawTimer = new ReentrantLock( );
		
		newLoadedImages = new LinkedList<CharacterImageData>( );
		
		(new Thread( )
		{
			public void run( )
			{
				for ( ;; )
				{
					redrawTimer.lock( );
					try
					{
						long time = System.currentTimeMillis( );
						
						if ( syncUpdated ||
							 (lastRedrawRequest != lastRedrawTime &&
							  time - lastRedrawRequest > REDRAW_INTERVAL_MILLIS &&
							  time - lastRedrawTime > REDRAW_INTERVAL_MILLIS) )
						{
							lastRedrawRequest = time;
							lastRedrawTime = time;
							
							redrawCanvas0( );
						}
					}
					finally
					{
						redrawTimer.unlock( );
					}
					
					try
					{
						Thread.sleep( REDRAW_INTERVAL_MILLIS );
					}
					catch ( InterruptedException e )
					{
						e.printStackTrace();
					}
				}
			}
		}).start( );
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
			
			MetadataColorManager colorManager = MetadataColorManager.getInstance( );
			
			for ( Entry<Data,Integer> dataEntry : dataIndex.entrySet( ) )
			{
				Data data = dataEntry.getKey( );
				Integer index = dataEntry.getValue( );
				
				Metadata metadata = data.getMetadata( selectedMetadata );
				Color color = null;
				
				if ( metadata != null )
				{
					color = colorManager.getColor( metadata.getKey( ), metadata.getValue( ) );
				}
				
				CharacterImageData image = imageIndex.get( index );
				
				int col = index % columns;
				int row = index / columns;
				
				int posX = (int) (col * widthStep);
				int posY = (int) (row * heightStep);
				
				if ( image != null )
				{
					gc.setAlpha( 255 );
					gc.drawImage( image.getImage( ),
							      0, 0, image.getImageColumns( ), image.getImageRows( ),
							      posX, posY, (int) widthStep, (int) heightStep );
					
					if ( color != null )
					{
						gc.setBackground( color );
						gc.setAlpha( 100 );
						gc.fillRectangle( posX, posY, (int) widthStep, (int) heightStep );
					}
				}
				
				if ( showIds )
				{
					gc.drawString( String.valueOf( data.getId( ) ), posX, posY );
				}
			}
		}
	}
	
	public void setSelection( final List<Data> _newSelection )
	{
		selectionLock.lock( );
		try
		{
			selectionCounter = 0;
			selectionTotal = _newSelection != null ? _newSelection.size( ) : 0;
			newSelection = _newSelection;
			syncNewSelection = true;
			syncUpdated = true;
			
		}
		finally
		{
			selectionLock.unlock( );
		}
		
		(new Thread( )
		{
			public void run( )
			{
				if ( _newSelection == null || _newSelection.isEmpty( ) )
				{
					// nothing to load
				}
				else
				{
					CharacterDataManager manager = CharacterDataManager.getInstance( );
					
					for ( final Data data : _newSelection )
					{
						queryForData( manager.getCharacterData( data ) );
					}
				}
			}
		}).start( );
		
		redrawCanvas( );
	}

	protected void queryForData( final Future<CharacterData> data )
	{
		(new Thread( )
		{
			public void run( )
			{
				try
				{
					Display display = Display.getDefault( );
					CharacterData image = data.get( );
				
					if ( image != null )
					{
						
						CharacterDataManager manager = CharacterDataManager.getInstance( );
						CharacterImageData imageData = manager.getCharacterImage0( image, display, palette );
						
						selectionLock.lock( );
						try
						{
							if ( ++selectionCounter == selectionTotal )
							{
								System.out.println( "Finished loading. Redraw immediate." );
								redrawCanvas0( );
							}
								
							newLoadedImages.add( imageData );
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
	
	protected void selectMetadata( String key )
	{
		selectedMetadata = key;
		
		redrawCanvas0( );
	}
	
	protected void redrawCanvas( )
	{
		redrawTimer.lock( );
		try
		{
			lastRedrawRequest = System.currentTimeMillis( );
			
			if ( lastRedrawRequest - lastRedrawTime > REDRAW_INTERVAL_MILLIS )
			{
				lastRedrawTime = lastRedrawRequest;
				
				redrawCanvas0( );
			}
		}
		finally
		{
			redrawTimer.unlock( );
		}
	}
	
	protected void redrawCanvas0( )
	{
		Display.getDefault( ).asyncExec( new Runnable( )
		{
			@Override
			public void run( )
			{
				canvas.redraw( );
				canvas.update( );
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
				
				redrawCanvas0( );
			}
		});
		
		toolbarManager.add( new ColorByMetadataMenu( ) );
	}
	
	class ColorByMetadataMenu extends Action implements IMenuCreator
	{
		private Menu fMenu;
		
		public ColorByMetadataMenu( )
		{
			super( "Color By Metadata", IAction.AS_DROP_DOWN_MENU );

			ImageDescriptor openImage = PlatformUI.getWorkbench( ).getSharedImages( ).getImageDescriptor( ISharedImages.IMG_DEF_VIEW );
			setToolTipText( "Color By Metadata" );
			setImageDescriptor( openImage );
			setMenuCreator( this );
		}
		
		@Override
		public void dispose( )
		{
			// do nothing
		}

		@Override
		public Menu getMenu( Control parent )
		{
			if ( fMenu != null )
			{
				fMenu.dispose( );
			}

			fMenu = new Menu( parent );
			
			DataManager manager = DataManager.getInstance( );
			Collection<String> keys = manager.getMetadataKeys( );

			for ( final String key : keys )
			{
				Action action = new Action( key, IAction.AS_RADIO_BUTTON )
				{
					@Override
					public void run( )
					{
						selectMetadata( key );
					}
				};
				
				if ( key.equals( selectedMetadata ) )
				{
					action.setChecked( true );
				}
				
				addActionToMenu( fMenu, action );
			}
				
			return fMenu;
		}

		@Override
		public Menu getMenu( Menu parent )
		{
			return null;
		}

		private void addActionToMenu( Menu parent, Action action )
		{
			ActionContributionItem item = new ActionContributionItem( action );
			item.fill( parent, -1 );
		}
	}

	@Override
	public void setFocus( )
	{
		canvas.setFocus( );
	}
}
