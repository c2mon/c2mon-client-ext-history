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

import cern.c2mon.client.ext.history.dbaccess.HistoryMapper;
import cern.tim.shared.common.supervision.SupervisionConstants.SupervisionEntity;

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
