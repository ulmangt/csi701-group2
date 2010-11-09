package edu.gmu.csi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Run implements TreeNode
{
	private int ixRun;
	private String sDescription;
	private Date dtRunDate;
	
	private List<IdKeyValue<Run>> parameters;
	
	public Run( int ixRun, String sDescription, Date dtRunDate )
	{
		this.ixRun = ixRun;
		this.sDescription = sDescription;
		this.dtRunDate = dtRunDate;
		
		this.parameters = new ArrayList<IdKeyValue<Run>>( );
	}
	
	public void addChildren( Collection<IdKeyValue<Run>> dataSets )
	{
		this.parameters.addAll( dataSets );
	}

	public void addChild( IdKeyValue<Run> dataSet )
	{
		this.parameters.add( dataSet );
	}

	public List<IdKeyValue<Run>> getParameterList( )
	{
		return parameters;
	}
	
	public int getKey( )
	{
		return ixRun;
	}

	public String getDescription( )
	{
		return sDescription;
	}

	public Date getRunDate( )
	{
		return dtRunDate;
	}

	@Override
	public Object[] getChildren( )
	{
		return parameters.toArray( );
	}

	@Override
	public Object getParent( )
	{
		return null;
	}

	@Override
	public boolean hasChildren( )
	{
		return !parameters.isEmpty( );
	}

}
