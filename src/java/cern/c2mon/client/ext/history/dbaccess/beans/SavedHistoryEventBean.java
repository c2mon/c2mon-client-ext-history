/******************************************************************************
 * This file is part of the Technical Infrastructure Monitoring (TIM) project.
 * See http://ts-project-tim.web.cern.ch
 * 
 * Copyright (C) 2004 - 2011 CERN This program is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program; if not,
 * write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 * 
 * Author: TIM team, tim.support@cern.ch
 *****************************************************************************/
package cern.c2mon.client.ext.history.dbaccess.beans;

import java.util.Date;
import java.util.TimeZone;

import cern.c2mon.client.ext.history.common.SavedHistoryEvent;
import cern.c2mon.client.ext.history.dbaccess.util.TimeZoneUtil;

/**
 * Represents a saved event from the database
 * 
 * @author vdeila
 * 
 */
public class SavedHistoryEventBean implements SavedHistoryEvent {

  /** The timezone which all {@link SavedHistoryEventBean}s will be created with */
  private static final TimeZone DEFAULT_INITIAL_TIMEZONE = TimeZone.getTimeZone("UTC");
  
  /** the id */
  private long id;

  /** the name */
  private String name;

  /** the description */
  private String description;

  /** the start date */
  private Date startDate;

  /** the end date */
  private Date endDate;

  /** The time zone that the dates have */
  private TimeZone timeZone;
  
  /**
   * @param id the id of the history event
   */
  public SavedHistoryEventBean(final Long id) {
    this(id, null, null, null, null);
    this.timeZone = DEFAULT_INITIAL_TIMEZONE;
  }

  /**
   * 
   * @param id
   *          the red_id
   * @param name
   *          the name
   * @param description
   *          the description
   * @param startDate
   *          the start date
   * @param endDate
   *          the end date
   */
  public SavedHistoryEventBean(final long id, final String name, final String description, final Date startDate, final Date endDate) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.startDate = startDate;
    this.endDate = endDate;

  }

  /**
   * Converts all the dates and times into the local time zone
   */
  public void convertIntoLocalTimeZone() {
    convertIntoTimeZone(TimeZone.getDefault());
  }
  
  /**
   * Converts all the dates and times into the new time zone
   * 
   * @param newTimeZone
   *          the new time zone to set
   */
  public void convertIntoTimeZone(final TimeZone newTimeZone) {
    if (this.timeZone.equals(newTimeZone)) {
      return;
    }
    if (this.startDate != null) {
      this.startDate = TimeZoneUtil.convertDateTimezone(newTimeZone, this.startDate, timeZone);
    }
    if (this.endDate != null) {
      this.endDate = TimeZoneUtil.convertDateTimezone(newTimeZone, this.endDate, timeZone);
    }
    this.timeZone = newTimeZone;
  }
  
  @Override
  public long getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Date getEndDate() {
    return endDate;
  }

  @Override
  public Date getStartDate() {
    return startDate;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(final long id) {
    this.id = id;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * @param description
   *          the description to set
   */
  public void setDescription(final String description) {
    this.description = description;
  }

  /**
   * @param startDate
   *          the startDate to set
   */
  public void setStartDate(final Date startDate) {
    this.startDate = startDate;
  }

  /**
   * @param endDate
   *          the endDate to set
   */
  public void setEndDate(final Date endDate) {
    this.endDate = endDate;
  }
}
