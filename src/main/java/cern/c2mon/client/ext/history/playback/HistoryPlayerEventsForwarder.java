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
package cern.c2mon.client.ext.history.playback;

import java.sql.Timestamp;
import java.util.Collection;

import cern.c2mon.client.ext.history.common.HistoryPlayer;
import cern.c2mon.client.ext.history.common.event.HistoryPlayerListener;
import cern.c2mon.client.ext.history.common.event.HistoryProviderListener;
import cern.c2mon.client.ext.history.common.id.HistoryUpdateId;
import cern.c2mon.client.ext.history.data.event.HistoryLoaderListener;
import cern.c2mon.client.ext.history.data.event.HistoryStoreListener;
import cern.c2mon.client.ext.history.playback.components.ListenersManager;

/**
 * This class forwards events from the {@link HistoryLoaderListener},
 * {@link HistoryStoreListener} and {@link HistoryProviderListener} to the
 * {@link HistoryPlayerListener}s
 * 
 * @author vdeila
 * 
 */
public class HistoryPlayerEventsForwarder implements HistoryLoaderListener, HistoryStoreListener, HistoryProviderListener {

  /**
   * The manager which keeps track of the listeners, should be the same as the
   * {@link HistoryPlayer} instance have
   */
  private final ListenersManager<HistoryPlayerListener> historyPlayerListeners;

  /**
   * Whether or not the history player is initializing, used to decide whether
   * or not to forward messages from the {@link HistoryProviderListener}
   */
  private volatile boolean isInitializing;

  /**
   * 
   * @param historyPlayerListeners
   *          The listeners which will get the events
   */
  public HistoryPlayerEventsForwarder(final ListenersManager<HistoryPlayerListener> historyPlayerListeners) {
    this.historyPlayerListeners = historyPlayerListeners;
    this.isInitializing = false;
  }

  @Override
  public void onInitializingHistoryProgressStatusChanged(final String progressMessage) {
    for (HistoryPlayerListener listener : historyPlayerListeners.getAll()) {
      listener.onInitializingHistoryProgressStatusChanged(progressMessage);
    }
  }

  @Override
  public void onInitializingHistoryStarting() {
    this.isInitializing = true;
    for (HistoryPlayerListener listener : historyPlayerListeners.getAll()) {
      listener.onInitializingHistoryStarted();
    }
  }

  @Override
  public void onInitializingHistoryFinished() {
    this.isInitializing = false;
    for (HistoryPlayerListener listener : historyPlayerListeners.getAll()) {
      listener.onInitializingHistoryFinished();
    }
  }

  @Override
  public void onStoppedLoadingDueToOutOfMemory() {
    for (HistoryPlayerListener listener : historyPlayerListeners.getAll()) {
      listener.onStoppedLoadingDueToOutOfMemory();
    }
  }

  @Override
  public void onPlaybackBufferFullyLoaded() {
    for (HistoryPlayerListener listener : historyPlayerListeners.getAll()) {
      listener.onHistoryIsFullyLoaded();
    }
  }

  @Override
  public void onPlaybackBufferIntervalUpdated(final Timestamp newEndTime) {
    for (HistoryPlayerListener listener : historyPlayerListeners.getAll()) {
      listener.onHistoryDataAvailabilityChanged(newEndTime);
    }
  }
  
  @Override
  public void queryStarting() {
    if (this.isInitializing) {
      for (HistoryPlayerListener listener : historyPlayerListeners.getAll()) {
        listener.onInitializingHistoryProgressChanged(-1);
      }
    }
  }

  @Override
  public void queryProgressChanged(final double percent) {
    if (this.isInitializing) {
      for (HistoryPlayerListener listener : historyPlayerListeners.getAll()) {
        listener.onInitializingHistoryProgressChanged(percent);
      }
    }
  }

  @Override
  public void queryFinished() {
    if (this.isInitializing) {
      for (HistoryPlayerListener listener : historyPlayerListeners.getAll()) {
        listener.onInitializingHistoryProgressChanged(-1);
      }
    }
  }

  @Override
  public void onDataCollectionChanged(final Collection<HistoryUpdateId> historyUpdateIds) {
  }

  @Override
  public void onDataInitialized(final Collection<HistoryUpdateId> historyUpdateIds) {
  }

}
