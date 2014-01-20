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

import java.sql.Timestamp;
import java.util.TimeZone;

import cern.c2mon.client.ext.history.dbaccess.util.TimeZoneUtil;
import cern.c2mon.shared.common.supervision.SupervisionConstants.SupervisionEntity;
import cern.c2mon.shared.common.supervision.SupervisionConstants.SupervisionStatus;

/**
 * A row from the supervision log is converted into this object
 * 
 * @author vdeila
 * 
 */
public class SupervisionRecordBean {

  /** the entity type */
  private final SupervisionEntity entity;

  /** the id of the entity to which the status applies */
  private final Long id;

  /** the time at which the event object was created */
  private Timestamp date;

  /** the current/new status */
  private SupervisionStatus status;

  /** an optional text message; can be <code>null</code> */
  private String message;
  
  /** The time zone that the dates have */
  private TimeZone timeZone;

  /** <code>true</code> if the value is a initial value */
  private boolean initialValue = false;
  
  /**
   * 
   * @param entity
   *          the entity type
   * @param id
   *          the id of the entity to which the status applies
   */
  public SupervisionRecordBean(final SupervisionEntity entity, final Long id) {
    this.entity = entity;
    this.id = id;
    this.timeZone = TimeZone.getTimeZone("UTC");
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
    if (this.date != null) {
      this.date = TimeZoneUtil.convertDateTimezone(newTimeZone, this.date, timeZone);
    }
    this.timeZone = newTimeZone;
  }
  
  /**
   * 
   * @return the timezone which are currently used for the dates and times
   */
  public TimeZone getTimeZone() {
    return this.timeZone;
  }

  /**
   * @return the date
   */
  public Timestamp getDate() {
    return date;
  }

  /**
   * @param date
   *          the date to set
   */
  public void setDate(final Timestamp date) {
    this.date = date;
  }

  /**
   * @return the status
   */
  public SupervisionStatus getStatus() {
    return status;
  }

  /**
   * @param status
   *          the status to set
   */
  public void setStatus(final SupervisionStatus status) {
    this.status = status;
  }

  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @param message
   *          the message to set
   */
  public void setMessage(final String message) {
    this.message = message;
  }

  /**
   * @return the entity
   */
  public SupervisionEntity getEntity() {
    return entity;
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  public boolean isInitialValue() {
    return initialValue;
  }

  public void setInitialValue(final boolean initialValue) {
    this.initialValue = initialValue;
  }
  
  
}
