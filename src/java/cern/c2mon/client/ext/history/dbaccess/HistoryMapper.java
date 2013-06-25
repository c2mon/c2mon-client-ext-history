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
package cern.c2mon.client.ext.history.dbaccess;

import java.util.List;

import cern.c2mon.client.ext.history.dbaccess.beans.DailySnapshotRequestBean;
import cern.c2mon.client.ext.history.dbaccess.beans.HistoryRecordBean;
import cern.c2mon.client.ext.history.dbaccess.beans.InitialRecordHistoryRequestBean;
import cern.c2mon.client.ext.history.dbaccess.beans.ShortTermLogHistoryRequestBean;
import cern.c2mon.client.ext.history.dbaccess.beans.SupervisionEventRequestBean;
import cern.c2mon.client.ext.history.dbaccess.beans.SupervisionRecordBean;

/**
 * Used to get history data from the sql server. Instantiated by iBatis or by
 * {@link HistorySessionFactory}
 * 
 * 
 * @see HistorySessionFactory
 * @see ShortTermLogHistoryRequestBean
 * @see InitialRecordHistoryRequestBean
 * 
 * @author vdeila
 * 
 */
public interface HistoryMapper {

  /**
   * 
   * @param request
   *          A request bean describing what to request
   * @return a list of records meeting the criteria of the request bean
   */
  HistoryRecordBean getInitialRecord(final InitialRecordHistoryRequestBean request);

  /**
   * Requests data for a set of tags from the daily snapshot table.
   * 
   * @param request
   *          A request bean describing what to request
   * @return a list of records that meets criteria of the request bean
   */
  List<HistoryRecordBean> getDailySnapshotRecords(final DailySnapshotRequestBean request);
  
  /**
   * 
   * @param request
   *          A request bean describing what to request
   * @return a list of records meeting the criteria of the request bean
   */
  List<HistoryRecordBean> getRecords(final ShortTermLogHistoryRequestBean request);

  /**
   * This method requests only the initial values for the events
   * 
   * @param request
   *          A request bean describing what to request
   * @return a list of records meeting the criteria of the request bean
   */
  List<SupervisionRecordBean> getInitialSupervisionEvents(final SupervisionEventRequestBean request);

  /**
   * Use the {@link #getInitialSupervisionEvents(SupervisionEventRequestBean)}
   * to get the initial values, this function only returns what's in between.
   * 
   * @param request
   *          A request bean describing what to request
   * @return a list of records meeting the criteria of the request bean
   */
  List<SupervisionRecordBean> getSupervisionEvents(final SupervisionEventRequestBean request);
}
