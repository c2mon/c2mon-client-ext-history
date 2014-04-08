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

import java.sql.Timestamp;
import java.util.Collection;

import cern.c2mon.client.ext.history.common.event.HistoryProviderListener;

/**
 * Interface that provides functions to get data from the history.<br/>
 * 
 * @see cern.c2mon.client.ext.history.dbaccess.HistorySessionFactory
 * 
 * @author vdeila
 */
public interface HistoryProvider {
  
  /**
   * Cancels all requests, they will return empty lists.<br/>
   * Remember to call {@link #enableProvider()} to enable the provider again.
   */
  void disableProvider();
  
  /**
   * Restores the provider after a call to {@link #disableProvider()}.
   */
  void enableProvider();

  /**
   * 
   * @param tagIds
   *          The tag ids to get the historical data for
   * @param from
   *          The start time
   * @param to
   *          The end time
   * @return A collection of all the records that was found for the given tag
   *         ids in the given time periode. <br/>
   *         Does never return <code>null</code>.
   */
  Collection<HistoryTagValueUpdate> getHistory(final Long[] tagIds, final Timestamp from, final Timestamp to);

  /**
   * 
   * @param tagIds
   *          The tag ids to get the historical data for
   * @param from
   *          The start time
   * @param to
   *          The end time
   * @param maximumTotalRecords
   *          The maximum records to return in total
   * @return A collection of all the records that was found for the given tag
   *         ids in the given time periode <br/>
   *         Does never return <code>null</code>.
   */
  Collection<HistoryTagValueUpdate> getHistory(final Long[] tagIds, final Timestamp from, final Timestamp to, final int maximumTotalRecords);

  /**
   * Returns a historical list of the last received values for the given tags (specified by tag ids).
   * @param tagIds
   *          The tag ids to get the historical data for
   * @param maximumTotalRecords
   *          The maximum records to return in total
   * @return A collection of the newest records that are found for the given tag
   *         ids. The maximum records to return in total for each tag is set by maximumTotalRecords
   */
  Collection<HistoryTagValueUpdate> getHistory(final Long[] tagIds, final int maximumTotalRecords);

  /**
   * 
   * @param maximumRecordsPerTag
   *          The maximum records to return per tag
   * @param tagIds
   *          The tag ids to get the historical data for
   * @param from
   *          The start time
   * @param to
   *          The end time
   * 
   * @return A collection of all the records that was found for the given tag
   *         ids in the given time periode <br/>
   *         Does never return <code>null</code>.
   */
  Collection<HistoryTagValueUpdate> getHistory(final int maximumRecordsPerTag, final Long[] tagIds, final Timestamp from, final Timestamp to);

  /**
   * Returns a historical list of the last received values for the given tags (specified by tag ids).
   * @param maximumRecordsPerTag
   *          The maximum records to return <b>per tag</b>
   * @param tagIds
   *          The tag ids to get the historical data for
   * @return A collection of the newest records that are found for the given tag
   *         ids. The maximum records to return per tag is set by maximumRecordsPerTag
   * @see #getHistory(Long[], int)
   */
  Collection<HistoryTagValueUpdate> getHistory(final int maximumRecordsPerTag, final Long[] tagIds);

  /**
   * 
   * @param tagIds
   *          the tag ids to get the data of
   * @param from
   *          the start date of the day to get the snapshots for (the time is
   *          ignored)
   * @param to
   *          the end date of the day to get the snapshots for (the time is
   *          ignored)
   * @return A collection of all the records for the <code>tagIds</code> between
   *         the given time span
   */
  Collection<HistoryTagValueUpdate> getDailySnapshotRecords(final Long[] tagIds, final Timestamp from, final Timestamp to);
  
  /**
   * Gets the data tag values for the given time (<code>before</code>)
   * 
   * @param tagIds
   *          The tag ids to get the initial value for
   * @param before
   *          The requested records will be having the value that they had on
   *          this time
   * @return The data tag values which was at the end of the given day for the
   *         given tag ids. <br/>
   *         Does never return <code>null</code>.
   */
  Collection<HistoryTagValueUpdate> getInitialValuesForTags(final Long[] tagIds, final Timestamp before);

  /**
   * 
   * @param initializationTime
   *          the time to get all the intial values for
   * @param requests
   *          The supervision events that is requested
   * @return a list of supervision initial events for the given ids and entities
   */
  Collection<HistorySupervisionEvent> getInitialSupervisionEvents(final Timestamp initializationTime, final Collection<SupervisionEventRequest> requests);

  /**
   * 
   * @param from
   *          the start time
   * @param to
   *          the end time
   * @param requests
   *          The supervision events that is requested
   * @return a collection of supervision events which matches any of the ones in
   *         the collection
   */
  Collection<HistorySupervisionEvent> getSupervisionEvents(final Timestamp from, final Timestamp to, final Collection<SupervisionEventRequest> requests);

  /**
   * 
   * @param listener
   *          The listener to add
   */
  void addHistoryProviderListener(final HistoryProviderListener listener);

  /**
   * 
   * @param listener
   *          The listener to remove
   */
  void removeHistoryProviderListener(final HistoryProviderListener listener);
  
  /**
   * Removes all progress which are 100 % finish. The progress given by
   * {@link HistoryProviderListener#queryProgressChanged(double)} will by
   * affected by this.
   */
  void resetProgress();
  
  /**
   * @return the time span which can be retrieved using this history provider.
   *         {@link Timespan#getStart()} is the earliest time which can be
   *         requested, and {@link Timespan#getEnd()} is the latest time which
   *         can be requested,
   */
  Timespan getDateLimits();
}
