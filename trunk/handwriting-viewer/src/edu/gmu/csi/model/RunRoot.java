package edu.gmu.csi.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RunRoot implements TreeNode
{
	private List<Run> children;

	public RunRoot( )
	{
		this.children = new ArrayList<Run>( );
	}

	public void addChildren( Collection<Run> run )
	{
		this.children.addAll( run );
	}

	public void addChild( Run run )
	{
		this.children.add( run );
	}

	public List<Run> getRunList( )
	{
		return children;
	}

	@Override
	public String toString( )
	{
		return "root"; 
	}
	
	@Override
	public Object getParent( )
	{
		return null;
	}

	@Override
	public boolean hasChildren( )
	{
		return !getRunList( ).isEmpty( );
	}

	@Override
	public Object[] getChildren( )
	{
		return getRunList( ).toArray( );
	}
}