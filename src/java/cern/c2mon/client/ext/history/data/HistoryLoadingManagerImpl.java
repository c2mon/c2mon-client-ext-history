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
package cern.c2mon.client.ext.history.data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cern.c2mon.client.common.tag.Tag;
import cern.c2mon.client.ext.history.common.HistoryLoadingManager;
import cern.c2mon.client.ext.history.common.HistoryProvider;
import cern.c2mon.client.ext.history.common.HistoryTagValueUpdate;
import cern.c2mon.client.ext.history.common.SupervisionEventRequest;
import cern.c2mon.client.ext.history.common.Timespan;
import cern.c2mon.client.ext.history.common.event.HistoryLoadingManagerListener;
import cern.c2mon.client.ext.history.common.exception.LoadingParameterException;
import cern.c2mon.client.ext.history.common.id.SupervisionEventId;
import cern.c2mon.client.ext.history.data.filter.DailySnapshotSmartFilter;
import cern.c2mon.client.ext.history.data.utilities.DateUtil;

/**
 * This class implements the {@link HistoryLoadingManager} and can be used to
 * load data from a {@link HistoryProvider}
 * 
 * @see HistoryProvider
 * @see HistoryLoadingManager
 * 
 * @author vdeila
 * 
 */
public class HistoryLoadingManagerImpl extends HistoryLoadingManagerAbs implements HistoryLoadingManager {

  /** Log4j logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(HistoryLoadingManagerImpl.class);

  /** The timeout for waiting for the loading to stop */
  private static final long STOP_LOADING_TIMEOUT = 20000;
  
  /** The history provider to use */
  private final HistoryProvider historyProvider;
  
  /** The filtering of data tags based on daily snapshot data */
  private final DailySnapshotSmartFilter dailySnapshotFilter;

  /** The loading process, if it is running */
  private LoadingThread loadingThread = null;
  
  /** Lock for {@link #loadingThread} */
  private final ReentrantLock loadingThreadLock;

  /** The earliest time loaded (exluding the initial data) */
  private Timestamp earliestTimeLoaded;

  /** The latest time loaded */
  private Timestamp latestTimeLoaded;
  
  /**
   * 
   * @param historyProvider
   *          the history provider to use to get the history data
   */
  public HistoryLoadingManagerImpl(final HistoryProvider historyProvider) {
    super();

    this.earliestTimeLoaded = null;
    this.latestTimeLoaded = null;
    this.dailySnapshotFilter = new DailySnapshotSmartFilter();
    this.historyProvider = historyProvider;
    this.loadingThreadLock = new ReentrantLock();
  }

  @Override
  public void beginLoading(final boolean async) throws LoadingParameterException {
    boolean startThread = false;
    loadingThreadLock.lock();
    try {
      if (this.loadingThread == null) {
        validateConfiguration();
        setLoading(true);
        this.loadingThread = new LoadingThread();
        startThread = true;
      }
    }
    finally {
      loadingThreadLock.unlock();
    }
    if (startThread) {
      if (async) {
        this.loadingThread.start();
      }
      else {
        this.loadingThread.run();
      }
    }
  }
  
  /**
   * Validates the parameters {@link #getConfiguration()}
   * 
   * @throws LoadingParameterException
   *           if any of the parameters is invalid
   */
  private void validateConfiguration() throws LoadingParameterException {
    if (getConfiguration() == null) {
      throw new LoadingParameterException("No loading parameters is set.");
    }
    boolean numberOfDaysSet = getConfiguration().getNumberOfDays() != null;
    boolean maximumRecordsSet = getConfiguration().getMaximumRecords() != null;
    boolean startSet = getConfiguration().getStartTime() != null;
    boolean endSet = getConfiguration().getEndTime() != null;
    
    if (!numberOfDaysSet
        && !maximumRecordsSet
        && (!endSet || !startSet)) {
      throw new LoadingParameterException(
          "Invalid loading parameters set. Must at least have start and end, or number of days, or maximum records set.");
    }
  }
  
  /**
   * Loads the daily snapshots into the {@link #dailySnapshotFilter}
   * 
   * @param start
   *          the start day
   * @param end
   *          the end day
   */
  private void loadDailySnapshots(final Timestamp start, final Timestamp end) {
    this.dailySnapshotFilter.addDailySnapshotValues(
        this.historyProvider.getDailySnapshotRecords(getTagsToLoad().keySet().toArray(new Long[0]), start, end));
  }
  
  /**
   * The thread loading the data
   */
  class LoadingThread extends Thread {
    private boolean allDailySnapshotsIsLoaded = false;
    
    // TODO: For multiple calls of the loadRecordsFromSTL()
    // this value gets overridden. Eventually this might not
    // be wished in the future.
    private boolean maximumRecordsReached = false;
    
    private final Timestamp loadingEndTime = getLoadingEndTime();

    // used to retrieve later the supervision events
    private Timestamp lastestTime;
    private Timestamp earliestTime;
    private Timestamp earliestTimeWithData;
    
    
    /** Constructor */
    public LoadingThread() {
      super("History-Loading-Thread");
    }
    
    @Override
    public void run() {

      this.fireOnLoadingStarting();
      this.initLoading();

      if (getConfiguration().isTimeWindowSet()) {
        this.loadRecordsFromSTL(getTagsToLoad());
      }
      else {
        // Load tags one by one, in order to respect the
        // maximum amount of records per tag
        Map<Long, Tag> entryMap = null;
        for (Entry<Long, Tag> entry : getTagsToLoad().entrySet()) {
          entryMap = new HashMap<>(1);
          entryMap.put(entry.getKey(), entry.getValue());

          this.loadRecordsFromSTL(entryMap);
        }
      }
      
      HistoryLoadingManagerImpl.this.earliestTimeLoaded = earliestTime;
      HistoryLoadingManagerImpl.this.latestTimeLoaded = lastestTime;
      
      loadIntitialValueAndSupervisionEvents();
      
      fireOnLoadingComplete();
      
      finalizeLoadingThread();
    }

    /**
     * In case that the maximum records limit has not been reached we
     * add initial values and initial supervision events to the result.
     */
    private void loadIntitialValueAndSupervisionEvents() {
      
      if (isLoading()) {
        final List <SupervisionEventRequest> supervisionRequests = new ArrayList<SupervisionEventRequest>();
        if (getConfiguration().isLoadSupervisionEvents()) {
          // Gets supervision events
          for (SupervisionEventId id : getSupervisionEventsToLoad()) {
            supervisionRequests.add(new SupervisionEventRequest(id.getEntityId(), id.getEntity()));
          }
        }
        
        if (getConfiguration().isLoadInitialValues() && !maximumRecordsReached) {
          // Gets initial records
          addTagValueUpdates(historyProvider.getInitialValuesForTags(
              getTagIdsToLoad().toArray(new Long[0]), 
              earliestTimeWithData));
          
          addSupervisionEvents(historyProvider.getInitialSupervisionEvents(
              earliestTimeWithData, 
              supervisionRequests));
        }
        
        // Loads the supervision events
        addSupervisionEvents(historyProvider.getSupervisionEvents(earliestTime, lastestTime, supervisionRequests));
      }
    }

    /**
     * Called at the end of the loading process to wakes up 
     * the parent thread that is waiting for the end of the
     * history loading.
     */
    private void finalizeLoadingThread() {
      // Stops loading.
      loadingThreadLock.lock();
      try {
        setLoading(false);
        synchronized (loadingThread) {
          loadingThread.notify();
        }
        loadingThread = null;
      }
      finally {
        loadingThreadLock.unlock();
      }
    }

    /**
     * Notifies all listeners that history loading is now complete
     */
    private void fireOnLoadingComplete() {
      
      for (HistoryLoadingManagerListener listener : getListeners()) {
        try {
          listener.onLoadingComplete();
        }
        catch (Exception e) {
          LOG.error("Error while notifying listener", e);
        }
      }
    }
    
    /**
     * Notifies all listeners that history loading is now starting
     */
    private void fireOnLoadingStarting() {

      for (HistoryLoadingManagerListener listener : getListeners()) {
        try {
          listener.onLoadingStarting();
        }
        catch (Exception e) {
          LOG.error("Error while notifying listener", e);
        }
      }
    }
    
    private Timestamp getLoadingEndTime() {
      Timestamp loadingEndTime = getConfiguration().getEndTime();
      if (loadingEndTime == null) {
        loadingEndTime = DateUtil.latestTimeInDay(System.currentTimeMillis());
      }
      
      return loadingEndTime;
    }
    
    private void initLoading() {
      
      if (getConfiguration().getStartTime() != null
          && getConfiguration().getEndTime() != null) {
        loadDailySnapshots(getConfiguration().getStartTime(), getConfiguration().getEndTime());
        allDailySnapshotsIsLoaded = true;
      }
      
      lastestTime = loadingEndTime;
      earliestTime = loadingEndTime;
      earliestTimeWithData = loadingEndTime;
    }
    
    /**
     * Fetches the historical records of the given list of tags from the
     * database. To optimize the loading performance it does one query per
     * day, taking advantage of the snapshot table entries to sort out tags
     * which have not been updated during the given day.
     *
     * @param tags The tags to load
     */
    private void loadRecordsFromSTL(final Map<Long, Tag> tags) {
      
      boolean loadingTimesReached = false;
      boolean numberOfDaysReached = false;
      boolean startTimeReached = false;
      boolean endTimeReached = false;
      
      Integer numberOfDays = getConfiguration().getNumberOfDays() == null ? null : 0;
      
      Timestamp myLastestTime = loadingEndTime;
      Timestamp myLoadingEndTime = loadingEndTime;
      Timestamp myEarliestTime = loadingEndTime;
      Timestamp myEarliestTimeWithData = loadingEndTime;

      Integer numberOfRecords = getConfiguration().getMaximumRecords() == null ? null : 0;
      maximumRecordsReached = false;
      
      while (!maximumRecordsReached
          && !loadingTimesReached
          && !numberOfDaysReached
          && isLoading()
          && !myEarliestTime.before(getConfiguration().getEarliestTimestamp())) {
        
        // Sets the maxium number of records variable
        Integer maximumNumberOfRecords;
        if (numberOfRecords == null) {
          maximumNumberOfRecords = null;
        }
        else {
          maximumNumberOfRecords = getConfiguration().getMaximumRecords() - numberOfRecords;
        }
        
        // Sets the loading start time
        Timestamp loadingStartTime = getConfiguration().getStartTime();
        
        // If no loading start time is set
        // or there are more than one day to the start time,
        // it is set to the beginning of the day which is now going to be loaded
        if (loadingStartTime == null
            || !DateUtil.isDaysEqual(loadingStartTime.getTime(), myLoadingEndTime.getTime())) {
          loadingStartTime = DateUtil.earliestTimeInDay(myLoadingEndTime.getTime());
        }
        
        if (!allDailySnapshotsIsLoaded) {
          loadDailySnapshots(loadingStartTime, myLoadingEndTime);
        }
        
        // Filters out which tags to load
        final List<Long> tagsToLoad = new ArrayList<Long>();
        for (final Long tagId : tags.keySet()) {
          final Timespan timespan = dailySnapshotFilter.getTimespan(tagId, loadingStartTime);
          // Load the tag if the start or end time is outside of the timespan
          if (timespan == null
              || timespan.getStart().before(loadingStartTime)
              || timespan.getEnd().after(myLoadingEndTime)) {
            tagsToLoad.add(tagId);
          }
        }
        
        if (tagsToLoad.size() > 0) {
          Collection<HistoryTagValueUpdate> result;
          
          // Loads the data from history
          if (maximumNumberOfRecords == null) {
            result = historyProvider.getHistory(tagsToLoad.toArray(new Long[0]), loadingStartTime, myLoadingEndTime);
          }
          else {
            result = historyProvider.getHistory(tagsToLoad.toArray(new Long[0]), loadingStartTime, myLoadingEndTime, (int) maximumNumberOfRecords);
          }
          if (result != null) {
            addTagValueUpdates(result);
            
            if (numberOfRecords != null) {
              numberOfRecords += result.size();
            }
            
            if (result.size() > 0) {
              if (loadingStartTime.compareTo(myEarliestTimeWithData) < 0) {
                myEarliestTimeWithData = loadingStartTime;
              }
            }
          }
        }
        if (numberOfDays != null) {
          numberOfDays++;
        }
        
        if (myLoadingEndTime.compareTo(myLastestTime) > 0) {
          myLastestTime = myLoadingEndTime;
        }
        if (loadingStartTime.compareTo(myEarliestTime) < 0) {
          myEarliestTime = loadingStartTime;
        }
        
        // Checking which criterias is met
        maximumRecordsReached = 
          numberOfRecords != null
          && numberOfRecords >= getConfiguration().getMaximumRecords();
          
        numberOfDaysReached = 
          numberOfDays != null 
          && numberOfDays >= getConfiguration().getNumberOfDays();
          
        startTimeReached = 
          getConfiguration().getStartTime() != null
          && loadingStartTime.compareTo(getConfiguration().getStartTime()) <= 0;

        endTimeReached = 
            getConfiguration().getEndTime() != null
            && myLoadingEndTime.compareTo(getConfiguration().getEndTime()) <= 0;
         
        loadingTimesReached = startTimeReached && endTimeReached;
        
        // Sets new ending day
        myLoadingEndTime = DateUtil.latestTimeInDay(DateUtil.addDays(myLoadingEndTime.getTime(), -1).getTime());
        
      } // End of while
      
      
      // Adjust latest time  
      if (myLastestTime.compareTo(lastestTime) > 0) {
        lastestTime = myLastestTime;
      }
      // Adjust earliest time
      if (myEarliestTime.compareTo(earliestTime) < 0) {
        earliestTime = myEarliestTime;
      }
      if (myEarliestTimeWithData.compareTo(earliestTimeWithData) < 0) {
        earliestTimeWithData = myEarliestTimeWithData;
      }
      
    }
    
  }

  @Override
  public void stopLoading(final boolean wait) {
    if (setLoading(false)) {
      if (wait) {
        synchronized (loadingThread) {
          try {
            loadingThread.wait(STOP_LOADING_TIMEOUT);
          }
          catch (InterruptedException e) { }
        }
      }
    }
  }

  @Override
  public Timestamp getEarliestTimeLoaded() {
    return this.earliestTimeLoaded;
  }

  @Override
  public Timestamp getLatestTimeLoaded() {
    return this.latestTimeLoaded;
  }

}
