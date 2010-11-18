package edu.gmu.csi.view;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

import net.miginfocom.swt.MigLayout;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.gmu.csi.manager.CharacterDataManager;
import edu.gmu.csi.manager.DataManager;
import edu.gmu.csi.manager.ViewUtil;

import edu.gmu.csi.model.Character;
import edu.gmu.csi.model.Data;
import edu.gmu.csi.model.data.CharacterData;

public class ClassificatonView extends ViewPart
{
	public static final String ID = "handwriting-viewer.classificationview";
	
	public static final int IMAGE_HEIGHT = 28;
	public static final int IMAGE_WIDTH = 28;
		
	protected CharacterImagePainter painter;
	protected HistogramPainter histogramPainter;
	
	protected PaletteData palette;
	protected Canvas canvas;
	protected Canvas histogramCanvas;
	protected Image image;
	protected boolean mouseDown = false;
	protected ReentrantLock imageLock;
	
	protected int brushWidth = 1;
	
	protected int selectedCount = -1;
	protected int maxCount;
	protected List<List<Data>> countData;
	protected int[] counts = new int[10];
	protected ReentrantLock resultLock;
	

	protected Action smallBrush;
	protected Action medBrush;
	protected Action largeBrush;
	
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
		resultLock = new ReentrantLock( );
	}
	
	private class HistogramPainter implements PaintListener
	{
		@Override
		public void paintControl( PaintEvent e )
		{
			Point size = histogramCanvas.getSize( );
			int width = size.x;
			int height = size.y;
			
			int rows = 10;

			GC gc = e.gc;
			
			int widthStep = width / rows;
			int legendHeight = 20;
			
			for ( int x = 0 ; x < rows ; x++ )
			{
				String s = String.valueOf( x );
				Point p = gc.stringExtent( s );
				
				if ( p.x > widthStep || p.y > legendHeight )
					s = "";
				
				int barWidth = x == 9 ? widthStep-2 : widthStep;
				
				gc.drawString( s, (int) (x * widthStep + widthStep / 2.0 - p.x / 2.0), (int) ( height - legendHeight / 2 - p.y / 2.0 )  );
				
				gc.drawRectangle( x * widthStep, height - legendHeight, barWidth, legendHeight-4 );
			}
			
			gc.setBackground( Display.getDefault( ).getSystemColor(SWT.COLOR_BLACK ) );
			
			resultLock.lock( );
			try
			{
				for ( int x = 0 ; x < rows ; x++ )
				{
					int barWidth = x == 9 ? widthStep-2 : widthStep;
					
					int count = counts[x];
					double fraction = maxCount == 0 ? 0.0 : count / (double) maxCount;
					int barHeight = (int) ( fraction * ( height - legendHeight ) );
					
					gc.fillRectangle( x * widthStep, height - barHeight - legendHeight, barWidth, barHeight );
				}
				
				if ( selectedCount != -1 )
				{
					gc.setForeground( Display.getDefault( ).getSystemColor(SWT.COLOR_RED ) );
					int barWidth = selectedCount == 9 ? widthStep-2 : widthStep;
					gc.drawRectangle( selectedCount * widthStep, height - legendHeight, barWidth, legendHeight-4 );
				}
			}
			finally
			{
				resultLock.unlock( );
			}
		}
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
	public void createPartControl( final Composite parent )
	{
		parent.setLayout( new MigLayout( ) );
		
		canvas = new Canvas( parent, SWT.DOUBLE_BUFFERED | SWT.BORDER );
		canvas.setLayoutData( "cell 0 0 3 1, align center, growy 100, growx 100, pushy 100, pushx 100, wrap" );
		painter = new CharacterImagePainter( );
		canvas.addPaintListener( painter );
		
		histogramCanvas = new Canvas( parent, SWT.DOUBLE_BUFFERED | SWT.BORDER );
		histogramCanvas.setLayoutData( "cell 0 1 3 1, align center, growy 30, growx 100, pushy 30, pushx 100, wrap" );
		histogramPainter = new HistogramPainter( );
		histogramCanvas.addPaintListener( histogramPainter );
		
		Button classify = new Button( parent, SWT.PUSH );
		classify.setText( "Classify" );
		classify.setLayoutData( "cell 0 2 1 1, pushx 100, growx 100" );
		
		classify.addListener( SWT.Selection, new Listener( )
		{
			@Override
			public void handleEvent( Event event )
			{
				System.out.println( "clicked" );
				classify( );
			}
		});
		
		Button clear = new Button( parent, SWT.PUSH );
		clear.setText( "Clear" );
		clear.setLayoutData( "cell 1 2 1 1, pushx 100, growx 100" );
		
		clear.addListener( SWT.Selection, new Listener( )
		{
			@Override
			public void handleEvent( Event event )
			{
				ImageData data = image.getImageData( );
				
				for ( int dx = 0 ; dx < IMAGE_WIDTH ; dx++ )
				{
					for ( int dy = 0 ; dy < IMAGE_HEIGHT ; dy++ )
					{
						data.setPixel( dx, dy, -1 );
					}
				}
				
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
		});
		
		Button load = new Button( parent, SWT.PUSH );
		load.setText( "Upload" );
		load.setLayoutData( "cell 2 2 1 1, pushx 100, growx 100" );
		
		load.addListener( SWT.Selection, new Listener( )
		{
			@Override
			public void handleEvent( Event event )
			{
				final Shell dialog = new Shell ( parent.getShell( ), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL );
				dialog.setText( "Upload Handwriting Sample" );
				dialog.setLayout( new MigLayout( ) );
				
				Label label = new Label (dialog, SWT.NONE);
				label.setText ("Name:");
				label.setLayoutData( "cell 0 0 1 1" );
				
				final Text nameText = new Text (dialog, SWT.BORDER);
				nameText.setText( "anonymous" );
				nameText.setLayoutData( "cell 1 0 2 1, growx, pushx" );
				
				Label labelAge = new Label (dialog, SWT.NONE);
				labelAge.setText ("Age:");
				labelAge.setLayoutData( "cell 0 1 1 1" );
				
				final Text ageText = new Text (dialog, SWT.BORDER);
				ageText.setText( "" );
				ageText.setLayoutData( "cell 1 1 2 1, growx, pushx" );
				
				Label labelHanded = new Label (dialog, SWT.NONE);
				labelHanded.setText ("Handedness:");
				labelHanded.setLayoutData( "cell 0 2 1 1" );
				
				final Combo comboHanded = new Combo( dialog, SWT.DROP_DOWN | SWT.BORDER);
				comboHanded.setLayoutData( "cell 1 2 2 1, growx, pushx" );
				comboHanded.add( "" );
				comboHanded.add( "Right" );
				comboHanded.add( "Left" );
				
				Label labelGender = new Label (dialog, SWT.NONE);
				labelGender.setText ("Gender:");
				labelGender.setLayoutData( "cell 0 3 1 1" );
				
				final Combo comboGender = new Combo( dialog, SWT.DROP_DOWN | SWT.BORDER);
				comboGender.setLayoutData( "cell 1 3 2 1, growx, pushx" );
				comboGender.add( "" );
				comboGender.add( "Male" );
				comboGender.add( "Female" );
				
				Label labelCharacter = new Label (dialog, SWT.NONE);
				labelCharacter.setText ("Character:");
				labelCharacter.setLayoutData( "cell 0 4 1 1" );
				
				final Combo comboCharacter = new Combo( dialog, SWT.DROP_DOWN | SWT.BORDER);
				comboCharacter.setLayoutData( "cell 1 4 2 1, growx, pushx" );
				for ( int i = 0 ; i < 10 ; i++)
					comboCharacter.add( String.valueOf( i ) );
				
				Button cancel = new Button (dialog, SWT.PUSH);
				cancel.setText ("Cancel");
				cancel.setLayoutData( "cell 1 5 1 1, growx, pushx, gaptop 10" );
				cancel.addSelectionListener (new SelectionAdapter () {
					public void widgetSelected (SelectionEvent e) {
						System.out.println("User cancelled dialog");
						dialog.close ();
					}
				});

				Button ok = new Button (dialog, SWT.PUSH);
				ok.setText ("OK");
				ok.setLayoutData( "cell 2 5 1 1, growx, pushx, gaptop 10" );
				ok.addSelectionListener (new SelectionAdapter () {
					public void widgetSelected (SelectionEvent e) {
						
						int handedSelection = comboHanded.getSelectionIndex( );
						String handed = "";
						if ( handedSelection == 1 )
							handed = "R";
						else if ( handedSelection == 2 )
							handed = "L";
						
						int genderSelection = comboGender.getSelectionIndex( );
						String gender = "";
						if ( handedSelection == 1 )
							gender = "M";
						else if ( handedSelection == 2 )
							gender = "F";
						
						int characterSelection = comboCharacter.getSelectionIndex( );
						String character = String.valueOf( characterSelection );
						
						upload( nameText.getText( ), ageText.getText( ), handed, gender, character );
						
						dialog.close ();
					}
				});
				
				dialog.setDefaultButton (ok);
				dialog.pack ();
				dialog.open ();
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
							for ( int dx = -brushWidth ; dx <= brushWidth ; dx++ )
							{
								for ( int dy = -brushWidth ; dy <= brushWidth ; dy++ )
								{
									int x = dx + imageX;
									int y = dy + imageY;
									
									if ( x < 0 || x >= IMAGE_WIDTH || y < 0 || y >= IMAGE_HEIGHT )
										continue;
									
									data.setPixel( x, y, -1 );
								}
							}
							
							modified = true;
						}
					}
					else
					{
						if ( data.getPixel( imageX, imageY ) != 0 )
						{
							for ( int dx = -brushWidth ; dx <= brushWidth ; dx++ )
							{
								for ( int dy = -brushWidth ; dy <= brushWidth ; dy++ )
								{
									int x = dx + imageX;
									int y = dy + imageY;
									
									if ( x < 0 || x >= IMAGE_WIDTH || y < 0 || y >= IMAGE_HEIGHT )
										continue;
									
									data.setPixel( x, y, 0 );
								}
							}
							
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
		
		histogramCanvas.addMouseListener( new MouseListener( )
		{
			@Override
			public void mouseDoubleClick( MouseEvent e )
			{
			}

			@Override
			public void mouseDown( MouseEvent e )
			{
				resultLock.lock( );
				try
				{
					Rectangle r = canvas.getBounds( );
					double widthX = r.width / 10.;
					
					int indexX = (int) ( e.x / widthX );
					
					if ( indexX < 0 ) indexX = 0;
					if ( indexX >= 10 ) indexX = 9;
					
					System.out.println( indexX );
					
					selectedCount = indexX;
					 
					if ( countData != null )
					{
						ViewUtil.getCharacterImageView( ).setSelection( countData.get( selectedCount ) );
					}
				}
				finally
				{
					resultLock.unlock( );
				}
				
				redrawHistogramCanvas( );
			}

			@Override
			public void mouseUp( MouseEvent e )
			{
			}
		});
		
		smallBrush = new Action( "Small Brush", IAction.AS_RADIO_BUTTON )
		{
			@Override
			public void run( )
			{
				brushWidth = 0;
			}
		};
		
		medBrush = new Action( "Medium Brush", IAction.AS_RADIO_BUTTON )
		{
			@Override
			public void run( )
			{
				brushWidth = 1;
			}
		};
		
		largeBrush = new Action( "Large Brush", IAction.AS_RADIO_BUTTON )
		{
			@Override
			public void run( )
			{
				brushWidth = 2;
			}
		};
		
		IToolBarManager toolbarManager = getViewSite( ).getActionBars( ).getToolBarManager( );
		
		BrushSizeMenu menu = new BrushSizeMenu( );
		toolbarManager.add( menu );
		
	}
	
	class BrushSizeMenu extends Action implements IMenuCreator
	{
		private Menu fMenu;
		
		public BrushSizeMenu( )
		{
			super( "Adjust Brush Size", IAction.AS_DROP_DOWN_MENU );

			ImageDescriptor openImage = PlatformUI.getWorkbench( ).getSharedImages( ).getImageDescriptor( ISharedImages.IMG_DEF_VIEW );
			setToolTipText( "Adjust Brush Size" );
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

			addActionToMenu( fMenu, smallBrush );
			addActionToMenu( fMenu, medBrush );
			addActionToMenu( fMenu, largeBrush );
				
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
	
	protected void redrawHistogramCanvas( )
	{
		Display.getDefault( ).asyncExec( new Runnable( )
		{
			@Override
			public void run( )
			{
				Rectangle r = histogramCanvas.getBounds( );
				histogramCanvas.redraw( 0, 0, r.width, r.height, true );
			}
		});
	}
	
	protected void upload( String name, String age, String handed, String gender, String character )
	{
		System.out.printf( "%s %s %s %s %s%n", name, age, handed, gender, character );
	}
	
	protected void classify( )
	{
		(new Thread( )
		{
			public void run( )
			{
				final int SAMPLES_PER_CHARACTER = 100;
				final int NEAREST_SAMPLES = 50;
				final int DATA_SET_ID = 1;
				
				int[] data = flipValues( convertSignedToUnsigned( image.getImageData( ).data, 3 ) );
				
//				System.out.println( data.length + " " + ( 28 * 28 ) + " " + Arrays.toString( data ) );
				
				DataManager dataManager = DataManager.getInstance( );
				CharacterDataManager characterManager = CharacterDataManager.getInstance( );
				
				NavigableSet<CharacterDistance> closestCharacters = new TreeSet<CharacterDistance>( );
				double largestDistance = 0;
				
				for ( int i = 0 ; i < 10 ; i++ )
				{
					Character character = dataManager.getCharacterData( DATA_SET_ID, String.valueOf( i ) );
					
					System.out.println( character );
					
					List<Data> dataList = new ArrayList<Data>( character.getDataList( ) );
					//Collections.shuffle( dataList ); // for now don't randomize
					List<Data> trainingSet = dataList.subList( 0, Math.min( dataList.size(), SAMPLES_PER_CHARACTER ) );
					
					for ( Data trainingData : trainingSet )
					{
						CharacterData characterData;
						
						try
						{
							characterData = characterManager.getCharacterData( trainingData ).get( );
							int[] trainingDataArray = convertSignedToUnsigned( characterData.getImageData( ) );
//							System.out.println( i + " " + trainingDataArray.length + " " + Arrays.toString( trainingDataArray ) );
							double distance = calculateDistance( trainingDataArray, data );
							
							if ( closestCharacters.size( ) < NEAREST_SAMPLES )
							{
								closestCharacters.add( new CharacterDistance( character, trainingData, distance ) );
								if ( distance > largestDistance )
									largestDistance = distance;
							}
							else
							{
								if ( distance < largestDistance )
								{
									closestCharacters.pollLast( );
									closestCharacters.add( new CharacterDistance( character, trainingData, distance ) );
									largestDistance = closestCharacters.last( ).getDistance( );
								}
							}
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
				
				List<List<Data>> dataArray = new ArrayList<List<Data>>( );
				for ( int i = 0 ; i < 10 ; i++ )
					dataArray.add( new ArrayList<Data>( ) );
				
				int[] countsArray = new int[10];
				
				for ( CharacterDistance character : closestCharacters )
				{
					int index = Integer.parseInt( character.getCharacter( ).getCharacter( ) );
					dataArray.get( index ).add( character.getData( ) );
					countsArray[index]++;
				}
				
				int highestIndex = 0;
				int highestCount = 0;
				for ( int i = 0 ; i < 10 ; i++ )
				{
					int count = countsArray[i];
					if ( count > highestCount )
					{
						highestCount = count;
						highestIndex = i;
					}
				}
				
				resultLock.lock( );
				try
				{
					selectedCount = -1;
					countData = dataArray;
					maxCount = highestCount;
					counts = countsArray;
				}
				finally
				{
					resultLock.unlock( );
				}
				
				redrawHistogramCanvas( );
			}
		}).start( );
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
	
	protected int[] flipValues( int[] data )
	{
		for ( int i = 0 ; i < data.length ; i++ )
		{
			data[i] = 255 - data[i];
		}
		
		return data;
	}
	
	protected double calculateDistance( int[] data1, int[] data2 )
	{
		double distance = 0;
		
		for ( int i = 0 ; i < data1.length ; i++ )
		{
			distance += Math.abs( data1[i] - data2[i] );
		}
		
		return distance;
	}

	public class CharacterDistance implements Comparable<CharacterDistance>
	{
		protected Data data;
		protected Character character;
		protected double distance;
		
		public CharacterDistance( Character character, Data data, double distance )
		{
			this.data = data;
			this.character = character;
			this.distance = distance;
		}

		@Override
		public int compareTo( CharacterDistance o )
		{
			if ( distance < o.distance )
			{
				return -1;
			}
			else if ( distance > o.distance )
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
		
		public Data getData( )
		{
			return data;
		}
		
		public Character getCharacter( )
		{
			return character;
		}
		
		public double getDistance( )
		{
			return distance;
		}
	}
}