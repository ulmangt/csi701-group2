package edu.gmu.csi.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

public class DatabaseManager
{
	private static final DatabaseManager instance = new DatabaseManager( );

	private static final String host = "ec2-67-202-7-152.compute-1.amazonaws.com";
	private static final int port = 3306;
	private static final String user = "test";
	private static final String pass = "csi710";
	
	public DatabaseManager( )
	{
		try
		{
			// see example at http://svn.apache.org/viewvc/commons/proper/dbcp/trunk/doc/ManualPoolingDriverExample.java?view=markup
			ObjectPool connectionPool = new GenericObjectPool(null);
			ConnectionFactory connectionFactory = new DriverManagerConnectionFactory( String.format( "jdbc:mysql://%s:%d", host, port ), user, pass );
			new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
			Class.forName("org.apache.commons.dbcp.PoolingDriver");
			PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			driver.registerPool("pool",connectionPool);
		}
		catch ( ClassNotFoundException e )
		{
			e.printStackTrace();
		}
		catch ( SQLException e )
		{
			e.printStackTrace();
		}
	}
	
	public Connection getConnection( ) throws SQLException
	{
		return DriverManager.getConnection( "jdbc:apache:commons:dbcp:pool" );
	}

	public static DatabaseManager getInstance( )
	{
		return instance;
	}
}
