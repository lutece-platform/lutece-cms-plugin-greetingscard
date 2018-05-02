/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.greetingscard.business;

import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.xml.XmlUtil;


/**
 * This class represents a GreetingsCardTemplate object.
 */
public class GreetingsCard
{
	public static final int STATUS_SENT = 0;
	public static final int STATUS_RED = 1;
	public static final int STATUS_RED_NOTIFIED = 2;
	private static final String PROPERTY_SYMBOL_AT_SIGN = "@";
	private static final String PROPERTY_ADRESS_EMAIL1 = "greetingscard.isInternal.Email1";
	private static final String PROPERTY_ADRESS_EMAIL2 = "greetingscard.isInternal.Email2";
	private static final String TAG_GREETINGS_CARD_DATA = "greetingscard-data";
	private static final String TAG_SENDER_NAME = "sender-name";
	private static final String TAG_SENDER_MAIL = "sender-mail";
	private static final String TAG_RECIPIENT_EMAIL = "recipient-email";
	private static final String TAG_MESSAGE_1 = "message1";
	private static final String TAG_MESSAGE_2 = "message2";
	private static final String TAG_GREETTINGSCARD_TEMPLATE_ID = "greetingscard-template-id";
	private static final String TAG_DATE = "date";
	private static final String TAG_STATUS = "status";
	private static final String TAG_IS_COPY = "is-copy";
	private String _strIdGC;
	private String _strSenderName;
	private String _strSenderIp;
	private String _strSenderEmail;
	private String _strRecipientEmail;
	private String _strMessage;
	private String _strMessage2;
	private int _nStatus;
	private boolean _bCopy;
	private boolean _bNotifySender;
	private java.sql.Date _date;
	private int _nIdGCT;

	/**
	 * Creates a new GreetingsCardTemplate object.
	 */
	public GreetingsCard( )
	{
	}

	/**
	 * Returns the identifier of the greetings card
	 * @return The identifier of the greetings card
	 */
	public String getId( )
	{
		return _strIdGC;
	}

	/**
	 * Sets the identifier of the greetings card to the specified value
	 * @param strIdGC The new value
	 */
	public void setId( String strIdGC )
	{
		_strIdGC = strIdGC;
	}

	/**
	 * Returns the sender name string
	 * @return The sender name string
	 */
	public String getSenderName( )
	{
		return _strSenderName;
	}

	/**
	 * Sets the sender name string to the specified value
	 * @param strSenderName The new value
	 */
	public void setSenderName( String strSenderName )
	{
		_strSenderName = strSenderName;
	}

	/**
	 * Returns the sender name string
	 * @return The sender name string
	 */
	public String getSenderIp( )
	{
		return _strSenderIp;
	}

	/**
	 * Sets the sender Ip string to the specified value
	 * @param strSenderIp The new value
	 */
	public void setSenderIp( String strSenderIp )
	{
		_strSenderIp = strSenderIp;
	}

	/**
	 * Returns the sender email string
	 * @return The sender email string
	 */
	public String getSenderEmail( )
	{
		return _strSenderEmail;
	}

	/**
	 * Sets the sender email string to the specified value
	 * @param strSenderEmail The new value
	 */
	public void setSenderEmail( String strSenderEmail )
	{
		_strSenderEmail = strSenderEmail;
	}

	/**
	 * Returns the recipient email string
	 * @return The recipient email string
	 */
	public String getRecipientEmail( )
	{
		return _strRecipientEmail;
	}

	/**
	 * Sets the recipient email string to the specified value
	 * @param strRecipientEmail The new value
	 */
	public void setRecipientEmail( String strRecipientEmail )
	{
		_strRecipientEmail = strRecipientEmail;
	}

	/**
	 * Returns the message string
	 * @return The message string
	 */
	public String getMessage( )
	{
		return _strMessage;
	}

	/**
	 * Sets the message string to the specified value
	 * @param strMessage The new value
	 */
	public void setMessage( String strMessage )
	{
		_strMessage = strMessage;
	}

	/**
	 * Returns the message2 string
	 * @return The message2 string
	 */
	public String getMessage2( )
	{
		return _strMessage2;
	}

	/**
	 * Sets the message2 string to the specified value
	 * @param message2 The new value
	 */
	public void setMessage2( String message2 )
	{
		_strMessage2 = message2;
	}

	/**
	 * -
	 * @param date The new value
	 */
	public void setDate( java.sql.Date date )
	{
		_date = date;
	}

	/**
	 * Returns the date of the greetings card sending
	 * 
	 * @return The date
	 */
	public java.sql.Date getDate( )
	{
		return _date;
	}

	/**
	 * Returns the identifier of the greetings card template
	 * @return The identifier of the greetings card template
	 */
	public int getIdGCT( )
	{
		return _nIdGCT;
	}

	/**
	 * Sets the identifier of the greetings card template to the specified value
	 * @param nIdGCT The new value
	 */
	public void setIdGCT( int nIdGCT )
	{
		_nIdGCT = nIdGCT;
	}

	/**
	 * Return the state of read of the card
	 * @return 1 if the card has been read or 0 if not read
	 */
	public boolean isRead( )
	{
		return _nStatus >= STATUS_RED;
	}

	/**
	 * Get the status of the card.
	 * @return The status of the card. Status are STATUS_SENT if the card is send, STATUS_RED if it has been red or STATUS_RED_NOTIFIED if it has been red and the sender has been notified.
	 */
	public int getStatus( )
	{
		return _nStatus;
	}

	/**
	 * Set the status of the card.
	 * @param nStatus The status of the card. Valid status are STATUS_SENT if the card is send, STATUS_RED if it has been red or STATUS_RED_NOTIFIED if it has been red and the sender has been
	 * notified.
	 */
	public void setStatus( int nStatus )
	{
		_nStatus = nStatus;
	}

	/**
	 * Return 1 if the card is a copy or 0 if not
	 * @return 1 if the card is a copy or 0 if not
	 */
	public boolean isCopy( )
	{
		return _bCopy;
	}

	/**
	 * Sets the copy state
	 * @param read 1 if copy, 0 if not copy
	 */
	public void setCopy( boolean copy )
	{
		_bCopy = copy;
	}

	/**
	 * Check if the sender should be notified when the card is red.
	 * @return True if the sender should be notified when the status of the greetings card change, false otherwise.
	 */
	public boolean getNotifySender( )
	{
		return _bNotifySender;
	}

	/**
	 * Set the notify sender boolean
	 * @param bNotifySender True if the sender should be notified when the card is red, false otherwise
	 */
	public void setNotifySender( boolean bNotifySender )
	{
		_bNotifySender = bNotifySender;
	}

	/**
	 * Test if the greeting card was sent to an internal person.
	 * @return the value of the test.
	 */
	public boolean isInternal( )
	{
		String strEndEmail1 = AppPropertiesService.getProperty( PROPERTY_ADRESS_EMAIL1 );
		String strEndEmail2 = AppPropertiesService.getProperty( PROPERTY_ADRESS_EMAIL2 );

		int nAtPosition;
		nAtPosition = _strRecipientEmail.lastIndexOf( PROPERTY_SYMBOL_AT_SIGN );

		if ( _strRecipientEmail != null && !_strRecipientEmail.equals( "" ) && StringUtil.checkEmail( _strRecipientEmail )
				&& ( _strRecipientEmail.substring( nAtPosition ).equals( strEndEmail1 ) || _strRecipientEmail.substring( nAtPosition ).equals( strEndEmail2 ) ) )
		{
			return true;
		}

		return false;
	}

	public String getDomainName( )
	{
		if ( _strRecipientEmail != null && !_strRecipientEmail.equals( "" ) && StringUtil.checkEmail( _strRecipientEmail ) )
		{
			return _strRecipientEmail.substring( _strRecipientEmail.lastIndexOf( PROPERTY_SYMBOL_AT_SIGN ) );
		}
		return null;
	}

	/**
	 * Return a xml string with the data of the GreetingsCard.
	 * @return xml
	 */
	public StringBuffer getXml( )
	{
		StringBuffer strXml = new StringBuffer( );
		strXml.append( XmlUtil.getXmlHeader( ) );
		XmlUtil.beginElement( strXml, TAG_GREETINGS_CARD_DATA );
		XmlUtil.addElement( strXml, TAG_SENDER_NAME, getSenderName( ) );
		XmlUtil.addElement( strXml, TAG_SENDER_MAIL, getSenderEmail( ) );
		XmlUtil.addElement( strXml, TAG_RECIPIENT_EMAIL, getRecipientEmail( ) );
		XmlUtil.addElement( strXml, TAG_MESSAGE_1, getMessage( ) );
		XmlUtil.addElement( strXml, TAG_MESSAGE_2, getMessage2( ) );
		XmlUtil.addElement( strXml, TAG_GREETTINGSCARD_TEMPLATE_ID, getIdGCT( ) );
		XmlUtil.endElement( strXml, TAG_GREETINGS_CARD_DATA );

		return strXml;
	}

	/**
	 * Return a xml string with the data of the GreetingsCard, but without any XML header.
	 * @return xml describing the GreetingsCard without any XML header
	 */
	public StringBuffer getXmlWithoutHeaderForStats( )
	{
		StringBuffer strXml = new StringBuffer( );
		XmlUtil.beginElement( strXml, TAG_GREETINGS_CARD_DATA );
		XmlUtil.addElement( strXml, TAG_SENDER_NAME, getSenderName( ) );
		XmlUtil.addElement( strXml, TAG_SENDER_MAIL, getSenderEmail( ) );
		XmlUtil.addElement( strXml, TAG_DATE, getDate( ).toString( ) );
		XmlUtil.addElement( strXml, TAG_RECIPIENT_EMAIL, getRecipientEmail( ) );
		// XmlUtil.addElement( strXml, TAG_MESSAGE_1, getMessage( ) );
		// XmlUtil.addElement( strXml, TAG_MESSAGE_2, getMessage2( ) );
		XmlUtil.addElement( strXml, TAG_GREETTINGSCARD_TEMPLATE_ID, getIdGCT( ) );
		XmlUtil.addElement( strXml, TAG_STATUS, getStatus( ) );
		XmlUtil.addElement( strXml, TAG_IS_COPY, String.valueOf( isCopy( ) ) );
		XmlUtil.endElement( strXml, TAG_GREETINGS_CARD_DATA );

		return strXml;
	}

}
