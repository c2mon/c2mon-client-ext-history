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
package cern.c2mon.client.ext.history.dbaccess.beans;

import java.sql.Timestamp;

import cern.c2mon.client.ext.history.dbaccess.HistoryMapper;
import cern.c2mon.shared.common.supervision.SupervisionConstants.SupervisionEntity;

/**
 * This class is passed as an argument when requesting data from through the
 * {@link HistoryMapper}
 * 
 * @author vdeila
 * 
 */
public class SupervisionEventRequestBean {

  /** The id */
  private Long id;

  /** The entities for the id to get */
  private SupervisionEntity entity;

  /** From time */
  private Timestamp fromTime;

  /** To time */
  private Timestamp toTime;
  /**
   * 
   * @param id
   *          The id
   * @param entity
   *          The entities for the id to get
   * @param fromTime
   *          the start time
   * @param toTime
   *          the end time
   */
  public SupervisionEventRequestBean(final Long id, final SupervisionEntity entity, final Timestamp fromTime, final Timestamp toTime) {
    this.id = id;
    this.entity = entity;
    this.fromTime = fromTime;
    this.toTime = toTime;
  }

  /**
   * @return the id
   */
  public Long getId() {
    return id;
  }

  /**
   * @return the entity
   */
  public SupervisionEntity getEntity() {
    return entity;
  }

  /**
   * 
   * @return the from time
   */
  public Timestamp getFromTime() {
    return fromTime;
  }

  /**
   * 
   * @return the to time
   */
  public Timestamp getToTime() {
    return toTime;
  }
}
