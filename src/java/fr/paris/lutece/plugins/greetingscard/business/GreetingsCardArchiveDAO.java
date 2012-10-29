package fr.paris.lutece.plugins.greetingscard.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class GreetingsCardArchiveDAO implements IGreetingsCardArchiveDAO
{
	private static final String SQL_QUERY_NEW_PRIMARY_KEY = "SELECT max( id_archive ) FROM greetings_card_archive";
	private static final String SQL_QUERY_INSERT = " INSERT INTO greetings_card_archive (id_archive, id_gct, domain_name, nb_card, nb_card_red, year_archive) VALUES (?,?,?,?,?,?) ";
	private static final String SQL_QUERY_FIND_BY_ID = " SELECT id_archive, id_gct, domain_name, domain_name, nb_card_red, year_archive FROM greetings_card_archive WHERE id_archive = ? ";
	private static final String SQL_QUERY_FIND_BY_ID_TEMPLATE = " SELECT id_archive, id_gct, domain_name, nb_card, nb_card_red, year_archive FROM greetings_card_archive WHERE id_gct = ? ";
	private static final String SQL_QUERY_FIND_BY_YEAR = " SELECT id_archive, id_gct, domain_name, nb_card, nb_card_red, year_archive FROM greetings_card_archive WHERE year_archive = ? ";
	private static final String SQL_QUERY_REMOVE = " DELETE FROM greetings_card_archive WHERE id_archive = ? ";
	private static final String SQL_QUERY_REMOVE_ALL = " DELETE FROM greetings_card_archive ";
	private static final String SQL_QUERY_UPDATE = " UPDATE greetings_card_archive SET id_gct = ?, domain_name = ?, nb_card = ?, nb_card_red = ?, year_archive = ? WHERE id_archive = ? ";
	private static final String SQL_QUERY_REMOVE_BY_ID_TEMPLATE = " DELETE FROM greetings_card_archive WHERE id_gct = ? ";
	private static final String SQL_QUERY_GET_YEARS = " SELECT DISTINCT year_archive FROM greetings_card_archive ORDER BY year_archive desc ";
	private static final String SQL_QUERY_FILTER_YEAR = " year_archive = ? ";
	private static final String SQL_QUERY_FILTER_AND = " AND ";

	/**
	 * Calculate a new primary key to add a new GreetingsCardArchive
	 * @param plugin The plugin
	 * @return The new key.
	 */
	private int newPrimaryKey( Plugin plugin )
	{
		int nKey;

		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PRIMARY_KEY, plugin );

		daoUtil.executeQuery( );

		if ( !daoUtil.next( ) )
		{
			// If the table is empty
			nKey = 1;
		}
		else
		{
			nKey = daoUtil.getInt( 1 ) + 1;
		}

		daoUtil.free( );

		return nKey;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert( GreetingsCardArchive greetingsCardArchive, Plugin plugin )
	{
		if ( greetingsCardArchive != null )
		{
			greetingsCardArchive.setIdArchive( newPrimaryKey( plugin ) );

			DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
			daoUtil.setInt( 1, greetingsCardArchive.getIdArchive( ) );
			daoUtil.setInt( 2, greetingsCardArchive.getIdGCT( ) );
			daoUtil.setString( 3, greetingsCardArchive.getDomainName( ) );
			daoUtil.setInt( 4, greetingsCardArchive.getNbCard( ) );
			daoUtil.setInt( 5, greetingsCardArchive.getNbCardRed( ) );
			daoUtil.setInt( 6, greetingsCardArchive.getYearArchive( ) );
			daoUtil.executeUpdate( );
			daoUtil.free( );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<GreetingsCardArchive> findByTemplateIdAndYear( int nIdGreetingsCardTemplate, int nYear, Plugin plugin )
	{
		StringBuilder sbSql = new StringBuilder( SQL_QUERY_FIND_BY_ID_TEMPLATE );
		if ( nYear > 0 )
		{
			sbSql.append( SQL_QUERY_FILTER_AND );
			sbSql.append( SQL_QUERY_FILTER_YEAR );
		}
		DAOUtil daoUtil = new DAOUtil( sbSql.toString( ), plugin );
		daoUtil.setInt( 1, nIdGreetingsCardTemplate );
		if ( nYear > 0 )
		{
			daoUtil.setInt( 2, nYear );
		}
		daoUtil.executeQuery( );
		List<GreetingsCardArchive> listArchive = new ArrayList<GreetingsCardArchive>( );
		while ( daoUtil.next( ) )
		{
			GreetingsCardArchive archive = new GreetingsCardArchive( );
			archive = new GreetingsCardArchive( );
			archive.setIdArchive( daoUtil.getInt( 1 ) );
			archive.setIdGCT( daoUtil.getInt( 2 ) );
			archive.setDomainName( daoUtil.getString( 3 ) );
			archive.setNbCard( daoUtil.getInt( 4 ) );
			archive.setNbCardRed( daoUtil.getInt( 5 ) );
			archive.setYearArchive( daoUtil.getInt( 6 ) );
			listArchive.add( archive );
		}
		daoUtil.free( );
		return listArchive;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<GreetingsCardArchive> findByYear( int year, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_YEAR, plugin );
		daoUtil.setInt( 1, year );

		List<GreetingsCardArchive> listArchive = new ArrayList<GreetingsCardArchive>( );
		while ( daoUtil.next( ) )
		{
			GreetingsCardArchive archive = new GreetingsCardArchive( );
			archive = new GreetingsCardArchive( );
			archive.setIdArchive( daoUtil.getInt( 1 ) );
			archive.setIdGCT( daoUtil.getInt( 2 ) );
			archive.setDomainName( daoUtil.getString( 3 ) );
			archive.setNbCard( daoUtil.getInt( 4 ) );
			archive.setNbCardRed( daoUtil.getInt( 5 ) );
			archive.setYearArchive( daoUtil.getInt( 6 ) );
			listArchive.add( archive );
		}
		daoUtil.free( );
		return listArchive;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GreetingsCardArchive findById( int nIdArchive, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_ID, plugin );
		daoUtil.setInt( 1, nIdArchive );

		GreetingsCardArchive archive = null;
		if ( daoUtil.next( ) )
		{
			archive = new GreetingsCardArchive( );
			archive.setIdArchive( daoUtil.getInt( 1 ) );
			archive.setIdGCT( daoUtil.getInt( 2 ) );
			archive.setDomainName( daoUtil.getString( 3 ) );
			archive.setNbCard( daoUtil.getInt( 4 ) );
			archive.setNbCardRed( daoUtil.getInt( 5 ) );
			archive.setYearArchive( daoUtil.getInt( 6 ) );
		}
		daoUtil.free( );
		return archive;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove( int nIdArchive, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE, plugin );
		daoUtil.setInt( 1, nIdArchive );
		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllArchives( Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_ALL, plugin );
		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update( GreetingsCardArchive greetingsCardArchive, Plugin plugin )
	{
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
		daoUtil.setInt( 1, greetingsCardArchive.getIdGCT( ) );
		daoUtil.setString( 2, greetingsCardArchive.getDomainName( ) );
		daoUtil.setInt( 3, greetingsCardArchive.getNbCard( ) );
		daoUtil.setInt( 4, greetingsCardArchive.getNbCardRed( ) );
		daoUtil.setInt( 5, greetingsCardArchive.getYearArchive( ) );
		daoUtil.setInt( 6, greetingsCardArchive.getIdArchive( ) );
		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeByIdTemplate( int nGreetingsCardTemplate, Plugin plugin )
	{
		DAOUtil daoUtil = new DAOUtil( SQL_QUERY_REMOVE_BY_ID_TEMPLATE, plugin );
		daoUtil.setInt( 1, nGreetingsCardTemplate );
		daoUtil.executeUpdate( );
		daoUtil.free( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Integer> getYearList( Plugin plugin )
	{
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_GET_YEARS, plugin );
		daoUtil.executeQuery( );

		List<Integer> listYear = new ArrayList<Integer>( );

		while ( daoUtil.next( ) )
		{
			listYear.add( daoUtil.getInt( 1 ) );
		}
		daoUtil.free( );
		return listYear;
	}

}
