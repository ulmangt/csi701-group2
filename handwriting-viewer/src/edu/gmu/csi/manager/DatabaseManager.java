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
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class DatabaseManager
{
	private static final DatabaseManager instance = new DatabaseManager( );

	private static final String defaultHost = "ec2-67-202-7-152.compute-1.amazonaws.com";
	private static final String defaultPort = "3306";
	private static final String defaultUser = "test";
	private static final String defaultPass = "csi710";
	
	public DatabaseManager( )
	{
		String host = defaultHost;
		String port = defaultPort;
		String user = defaultUser;
		String pass = defaultPass;
		
		Preferences nodeCore = new InstanceScope().getNode("handwriting-viewer");
		System.out.println( nodeCore );
		if (nodeCore != null) {
			
			host = nodeCore.get("database.host", defaultHost);
			port = nodeCore.get("database.port", defaultPort);
			user = nodeCore.get("database.user", defaultUser);
			pass = nodeCore.get("database.pass", defaultPass);
			
			System.out.println( host + " " + port + " " + user + " " + pass );
		
			nodeCore.put("database.host", host);
			nodeCore.put("database.port", port);
			nodeCore.put("database.user", user);
			nodeCore.put("database.pass", pass);

			try
			{
				nodeCore.flush( );
				nodeCore.sync( );
			}
			catch ( BackingStoreException e )
			{
				e.printStackTrace();
			}
		}
		
		try
		{
			// see example at http://svn.apache.org/viewvc/commons/proper/dbcp/trunk/doc/ManualPoolingDriverExample.java?view=markup
			ObjectPool connectionPool = new GenericObjectPool(null);
			ConnectionFactory connectionFactory = new DriverManagerConnectionFactory( String.format( "jdbc:mysql://%s:%s", host, port ), user, pass );
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
