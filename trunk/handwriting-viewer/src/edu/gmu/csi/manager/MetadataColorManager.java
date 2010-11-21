package edu.gmu.csi.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class MetadataColorManager
{
	private static final MetadataColorManager instance = new MetadataColorManager( );

	private Map<String,MetadataColorChooser> colorChoosers = new HashMap<String,MetadataColorChooser>( );
	
	protected MetadataColorManager( )
	{
		colorChoosers.put( "Gender", new GenderColorChooser( ) );
		colorChoosers.put( "Handedness", new HandednessColorChooser( ) );
		colorChoosers.put( "Quality", new QualityColorChooser( ) );
		colorChoosers.put( "Age", new AgeColorChooser( -1, 50, 100 ) );
	}
	
	public static MetadataColorManager getInstance( )
	{
		return instance;
	}
	
	public Color getColor( String key, String value )
	{
		MetadataColorChooser chooser = colorChoosers.get( key );
		
		if ( chooser == null )
			return null;
		else
			return chooser.getColor( value );
	}
	
	private interface MetadataColorChooser
	{
		public Color getColor(String s);
	}
	
	private class GenderColorChooser implements MetadataColorChooser
	{
		private String male = "M";
		private String female = "F";
		
		private Color red = new Color( Display.getDefault( ), 255, 0, 0 );
		private Color blue = new Color( Display.getDefault( ), 0, 0, 255 );
		
		@Override
		public Color getColor( String s )
		{
			if ( male.equals( s ) )
				return blue;
			else if ( female.equals( s ) )
				return red;
			else return null;
		}
	}
	
	private class HandednessColorChooser implements MetadataColorChooser
	{
		private String right = "R";
		private String left = "L";
		
		private Color red = new Color( Display.getDefault( ), 255, 0, 0 );
		private Color blue = new Color( Display.getDefault( ), 0, 0, 255 );
		
		@Override
		public Color getColor( String s )
		{
			if ( right.equals( s ) )
				return blue;
			else if ( left.equals( s ) )
				return red;
			else return null;
		}
	}
	
	private class QualityColorChooser implements MetadataColorChooser
	{
		private String good = "G";
		private String bad = "B";
		
		private Color red = new Color( Display.getDefault( ), 255, 0, 0 );
		private Color blue = new Color( Display.getDefault( ), 0, 0, 255 );
		
		@Override
		public Color getColor( String s )
		{
			if ( good.equals( s ) )
				return blue;
			else if ( bad.equals( s ) )
				return red;
			else return null;
		}
	}
	
	private class AgeColorChooser implements MetadataColorChooser
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
		
		public AgeColorChooser( float min, float max, int steps )
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
		
		@Override
		public Color getColor( String s )
		{
			try
			{
				float f = Float.parseFloat( s );
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
