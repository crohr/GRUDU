/*
 * $Id: DateUtils.java,v 1.1 2007/07/13 09:59:41 dloureir Exp $
 */
package org.robsite.jswingreader.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DateUtils 
{
  private DateUtils()
  {
  }
  
  
  /**
   * Tests the format for <code>dateString</code> for ISO8601 conformance.
   * @param dateString The string containin the date
   * @return <code>true</code> if the dateString is in proper ISO-8601 format.
   */
  public static boolean isDateISO8601( String dateString )
  {
    if ( dateString == null )
    {
      return false;
    }
    
    try
    {
      if ( dateString.charAt( 4 ) != '-' )
      {
        return false;
      }
      if ( dateString.charAt( 7 ) != '-' )
      {
        return false;
      }
      if ( dateString.charAt( 10 ) != 'T' )
      {
        return false;
      }
      if ( dateString.charAt( 13 ) != ':' )
      {
        return false;
      }
      if ( dateString.charAt( 16 ) != ':' )
      {
        return false;
      }
      if ( dateString.charAt( 19 ) != '-' && dateString.charAt( 19 ) != '+' )
      {
        return false;
      }
    }
    catch ( StringIndexOutOfBoundsException ex )
    {
      return false;      
    }
    return true;
  }
  
  
  /**
   * Gets a <code>Date</code> object from the <code>dateString</code>
   * @param dateString The date as a string in ISO-8601 format
   * @return a Java <code>Date</code> object.
   */
  public static Date getDateFromISO8601( String dateString )
  {
    String tz = "-08:00";
    if ( dateString.endsWith( ":00" ) )
    {
      tz = dateString.substring( dateString.length() - 6 );
      //System.out.println( "debug: tz = " + tz );
    }
    String dateNoTimeZone = dateString.substring( 0, dateString.length() - 6 );
    //System.out.println( "debug: dateNoTimeZone = " + dateNoTimeZone );
    SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
    TimeZone timezone = TimeZone.getTimeZone( "GMT" + tz );
    Calendar cal = Calendar.getInstance( timezone );
    format.setCalendar( cal );
    Date date = null;
    try
    {
      date = format.parse( dateNoTimeZone );
    }
    catch ( ParseException ex )
    {
    }
    return date;
    
  }
  
  
  /**
   * Gets a <code>String</code> representation of the date in ISO-8601 format
   * @param date the <code>Date</code> object
   * @return a String format of the date
   */
  public static String getDateInISO8601( Date date )
  {
    SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
    Calendar cal = Calendar.getInstance();
    int offset = cal.getTimeZone().getOffset( date.getTime() );
    char delta = '+';
    if ( offset < 0 )
    {
      delta = '-';
      offset = Math.abs( offset );
    }
    StringBuffer result = new StringBuffer( 30 );
    result.append( format.format( date ) );
    result.append( delta );
    int hourOffset = offset / ( 60 * 60 * 1000 );
    if ( hourOffset < 10 )
    {
      result.append( "0" );
    }
    result.append( hourOffset );
    result.append( ":00" );
    return result.toString();
    
  }
  // ISO-8601 Format: 2003-10-08T14:05:31-08:00


  /**
   * Utility method used in test.
   */
  static boolean areDatesEqualEnough( Date d1, Date d2 )
  {
    long t1 = d1.getTime();
    long t2 = d2.getTime();
    
    if ( t1 == t2 )
    {
      return true;
    }
    
    if ( Math.abs( t1 - t2 ) < 999 )
    {
      return true;
    }
    
    return false;
  }


  /**
   * Test main
   */
  public static void main(String[] args)
  {
    Date d1 = new Date();
    String test = DateUtils.getDateInISO8601( d1 );
    Date d2 = getDateFromISO8601( test );
    
    if ( areDatesEqualEnough( d1, d2 ) )
    {
      System.out.println( "Dates match" );
    }
    System.out.println( isDateISO8601( test ) );
    System.out.println( isDateISO8601( "2003-10-08T14:05:31-08:00" ) );
    System.out.println( isDateISO8601( "2003-10-08X14:05:31-08:00" ) );
    System.out.println( isDateISO8601( "2003-11-10T20:01:17" ) );
    System.out.println( isDateISO8601( d2.toString() ) );
  }

}