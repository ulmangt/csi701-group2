package edu.gmu.csi.model;

public class Result
{
	protected int ixResult;
	protected Data data;
	protected Run run;
	
	protected String sClassification;

	public Result( int ixResult, Data data, Run run, String sClassification )
	{
		this.ixResult = ixResult;
		this.data = data;
		this.run = run;
		this.sClassification = sClassification;
	}

	public int getIxResult( )
	{
		return ixResult;
	}

	public Data getData( )
	{
		return data;
	}

	public Run getRun( )
	{
		return run;
	}

	public String getClassification( )
	{
		return sClassification;
	}
}
