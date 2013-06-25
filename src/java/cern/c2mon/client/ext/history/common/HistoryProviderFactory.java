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
package cern.c2mon.client.ext.history.common;

import cern.c2mon.client.ext.history.common.exception.HistoryProviderException;

/**
 * Describes the methods of a history provider factory.
 * 
 * @author vdeila
 */
public interface HistoryProviderFactory {

  /**
   * @param event
   *          the event which will be requested.
   * 
   * @return A {@link HistoryProvider} which can be used to easily get event
   *         history data
   * @throws HistoryProviderException
   *           If the history provider could not be retrieved for any reason.
   */
  HistoryProvider createSavedHistoryProvider(final SavedHistoryEvent event) throws HistoryProviderException;

  /**
   * Use this provider to get the list of saved events. The records of a
   * particular event can then be retrieved using
   * {@link #createSavedHistoryProvider(SavedHistoryEvent)}
   * 
   * @return A {@link SavedHistoryEventsProvider} which can be used to easily
   *         get the list of saved history events
   * @throws HistoryProviderException
   *           If the history provider could not be retrieved for any reason.
   */
  SavedHistoryEventsProvider createSavedHistoryEventsProvider() throws HistoryProviderException;
  
  /**
   * @return A {@link HistoryProvider} which can be used to easily get data from
   *         the last 30 days of history data
   * @throws HistoryProviderException
   *           If the history provider could not be retrieved for any reason.
   */
  HistoryProvider createHistoryProvider() throws HistoryProviderException;
}
