package fr.paris.lutece.plugins.greetingscard.business;

/**
 * DTO describing statistics values of a greetings card template.
 */
public class GreetingsCardStatistic
{
	private int _nIdGCT;
	private String _strDescription;
	private int _nNbCardSentInternal;
	private int _nNbCardSentExternal;
	private int _nNbCardRedInternal;
	private int _nNbCardRedExternal;

	/**
	 * Get the id of the greetings card template
	 * @return nIdGCT the id of the greetings card template
	 */
	public int getIdGCT( )
	{
		return _nIdGCT;
	}

	/**
	 * Set the id of the greetings card template
	 * @param nIdGCT the id of the greetings card template
	 */
	public void setIdGCT( int nIdGCT )
	{
		_nIdGCT = nIdGCT;
	}

	/**
	 * Get the description of the greetings card template
	 * @return The description of the greetings card template
	 */
	public String getDescription( )
	{
		return _strDescription;
	}

	/**
	 * Set the description of the greetings card template
	 * @param strDescription The description of the greetings card template
	 */
	public void setDescription( String strDescription )
	{
		_strDescription = strDescription;
	}

	/**
	 * Get the number of cards sent to internal people
	 * @return The number of cards sent to internal people
	 */
	public int getNbCardSentInternal( )
	{
		return _nNbCardSentInternal;
	}

	/**
	 * Set the number of cards sent to internal people
	 * @param nNbCardSentInternal The number of cards sent to internal people
	 */
	public void setNbCardSentInternal( int nNbCardSentInternal )
	{
		_nNbCardSentInternal = nNbCardSentInternal;
	}

	/**
	 * Get the number of cards sent to external people
	 * @return The number of cards sent to external people
	 */
	public int getNbCardSentExternal( )
	{
		return _nNbCardSentExternal;
	}

	/**
	 * Set the number of cards sent to external people
	 * @param nNbCardSentExternal The number of cards sent to external people
	 */
	public void setNbCardSentExternal( int nNbCardSentExternal )
	{
		_nNbCardSentExternal = nNbCardSentExternal;
	}

	/**
	 * Get the number of cards red by internal people
	 * @return The number of cards red by internal people
	 */
	public int getNbCardRedInternal( )
	{
		return _nNbCardRedInternal;
	}

	/**
	 * Set the number of cards red by internal people
	 * @param nNbCardRedInternal The number of cards red by internal people
	 */
	public void setNbCardRedInternal( int nNbCardRedInternal )
	{
		_nNbCardRedInternal = nNbCardRedInternal;
	}

	/**
	 * Get the number of cards red by external people
	 * @return The number of cards red by external people
	 */
	public int getNbCardRedExternal( )
	{
		return _nNbCardRedExternal;
	}

	/**
	 * Set the number of cards red by external people
	 * @param nNbCardRedInternal The number of cards red by external people
	 */
	public void setNbCardRedExternal( int nNbCardRedExternal )
	{
		_nNbCardRedExternal = nNbCardRedExternal;
	}

	public int getNbCardSent( )
	{
		return _nNbCardSentExternal + _nNbCardSentInternal;
	}

	public int getNbCardRed( )
	{
		return _nNbCardRedExternal + _nNbCardRedInternal;
	}

}
