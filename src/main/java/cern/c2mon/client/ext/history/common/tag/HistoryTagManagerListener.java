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
package cern.c2mon.client.ext.history.common.tag;

import java.util.Collection;

/**
 * Describes the methods for a {@link HistoryTagManager} listener.
 * 
 * @author vdeila
 */
public interface HistoryTagManagerListener {

  /**
   * Invoked when the data is loaded
   * 
   * @param filter
   *          the configuration that is specified for the data 
   * @param data
   *          the data that is retrieved
   */
  void onLoaded(final HistoryTagConfiguration filter, final Collection<HistoryTagRecord> data);

  /**
   * Invoked when some data for a tag id were cancelled, due to failure or any
   * other reason.
   * 
   * @param filter
   *          the configuration that is specified for the data 
   */
  void onCancelled(final HistoryTagConfiguration filter);

  /**
   * All listeners must keep a updated list of data that matches with the
   * subscribed history configurations. If the list no longer updated, it should
   * no longer be subscribed. This data is used for others who subscribes to the
   * same data.
   * 
   * @param configuration
   *          the history configuration that it wants the data for.
   * @return the current data according to the subscribed configuration
   */
  Collection<HistoryTagRecord> getCurrentData(final HistoryTagConfiguration configuration);
}
