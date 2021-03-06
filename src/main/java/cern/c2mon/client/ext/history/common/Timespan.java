/******************************************************************************
 * Copyright (C) 2010-2016 CERN. All rights not expressly granted are reserved.
 * 
 * This file is part of the CERN Control and Monitoring Platform 'C2MON'.
 * C2MON is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the license.
 * 
 * C2MON is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with C2MON. If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************/
package cern.c2mon.client.ext.history.common;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Simple class to represent a time frame
 * 
 * @author vdeila
 * 
 */
public class Timespan {

  /** The start date */
  private Timestamp start;

  /** The end date */
  private Timestamp end;

  /**
   * 
   * @param start
   *          The start date
   * @param end
   *          The end date
   */
  public Timespan(final Timestamp start, final Timestamp end) {
    this.start = start;
    this.end = end;
  }

  /**
   * 
   * @param start
   *          The start date
   * @param end
   *          The end date
   */
  public Timespan(final Date start, final Date end) {
    this(new Timestamp(start.getTime()), new Timestamp(end.getTime()));
  }

  /**
   * Copy constructor
   * 
   * @param timespan
   *          the timespan to copy
   */
  public Timespan(final Timespan timespan) {
    this.start = timespan.start;
    this.end = timespan.end;
  }

  /**
   * @return the start time
   */
  public Timestamp getStart() {
    return start;
  }

  /**
   * @param start
   *          the start time to set
   */
  public void setStart(final Timestamp start) {
    this.start = start;
  }

  /**
   * @return the time end
   */
  public Timestamp getEnd() {
    return end;
  }

  /**
   * @param end
   *          the end time to set
   */
  public void setEnd(final Timestamp end) {
    this.end = end;
  }
  
  /**
   * 
   * @return the duration from start to end in milliseconds. (<code>end - start</code>)
   */
  public long getDuration() {
    return getEnd().getTime() - getStart().getTime();
  }

  @Override
  public String toString() {
    final String startDate;
    if (this.start == null) {
      startDate = "null";
    }
    else {
      startDate = this.start.toString();
    }
    final String endDate;
    if (this.end == null) {
      endDate = "null";
    }
    else {
      endDate = this.end.toString();
    }
    return String.format("Timespan: %s to %s", startDate, endDate);
  }
  
  
}
