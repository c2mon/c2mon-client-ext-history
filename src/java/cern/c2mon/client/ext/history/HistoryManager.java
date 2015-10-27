/*******************************************************************************
 * This file is part of the Technical Infrastructure Monitoring (TIM) project.
 * See http://ts-project-tim.web.cern.ch
 * 
 * Copyright (C) 2004 - 2011 CERN. This program is free software; you can
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
 ******************************************************************************/
package cern.c2mon.client.ext.history;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cern.c2mon.client.common.listener.TagUpdateListener;
import cern.c2mon.client.common.tag.ClientDataTagValue;
import cern.c2mon.client.common.tag.Tag;
import cern.c2mon.client.core.cache.BasicCacheHandler;
import cern.c2mon.client.core.listener.TagSubscriptionListener;
import cern.c2mon.client.core.manager.CoreSupervisionManager;
import cern.c2mon.client.core.service.AdvancedTagService;
import cern.c2mon.client.core.tag.ClientDataTagImpl;
import cern.c2mon.client.ext.history.common.HistoryLoadingManager;
import cern.c2mon.client.ext.history.common.HistoryPlayer;
import cern.c2mon.client.ext.history.common.HistoryPlayerEvents;
import cern.c2mon.client.ext.history.common.HistoryProvider;
import cern.c2mon.client.ext.history.common.HistoryProviderAvailability;
import cern.c2mon.client.ext.history.common.HistoryProviderFactory;
import cern.c2mon.client.ext.history.common.Timespan;
import cern.c2mon.client.ext.history.common.exception.HistoryPlayerNotActiveException;
import cern.c2mon.client.ext.history.common.tag.HistoryTagManager;
import cern.c2mon.client.ext.history.data.HistoryLoadingManagerImpl;
import cern.c2mon.client.ext.history.playback.HistoryPlayerCoreAccess;
import cern.c2mon.client.ext.history.playback.HistoryPlayerImpl;
import cern.c2mon.client.ext.history.util.KeyForValuesMap;
import cern.c2mon.client.jms.ConnectionListener;
import cern.c2mon.client.jms.SupervisionListener;
import cern.c2mon.shared.common.supervision.SupervisionConstants.SupervisionEntity;

@Service
public class HistoryManager implements C2monHistoryManager, TagSubscriptionListener {

  /** Log4j logger for this class */
  private static final Logger LOG = LoggerFactory.getLogger(HistoryManager.class);
  
  /** Reference to the <code>TagManager</code> singleton */
  private final AdvancedTagService tagService;

  /** Reference to the <code>ClientDataTagCache</code> */
  private final BasicCacheHandler cache;
  
  /** Reference to the <code>C2monSupervisionManager</code> */
  private final CoreSupervisionManager supervisionManager;
  
  /** Reference to the {@link HistoryTagManager} */
  private final HistoryTagManager historyTagManager;
  
  /** the history player */
  private HistoryPlayerCoreAccess historyPlayer = null;
  
  /** Keeps track of which tag id belongs to which {@link SupervisionListener} */
  private final KeyForValuesMap<Long, SupervisionListener> tagToSupervisionListener 
    = new KeyForValuesMap<Long, SupervisionListener>();
  
  /** Interface to keep track of which providers are available */
  private HistoryProviderAvailability historyProviderAvailability = null;
  
  /** A connection listener checking if the connection to the JMS is lost. */
  private ConnectionListener jmsConnectionListener = null;
 
  @Autowired
  protected HistoryManager(final AdvancedTagService tagService, final BasicCacheHandler pCache,
      final CoreSupervisionManager pSupervisionManager, final HistoryTagManager historyTagManager) {
    
    this.tagService = tagService;
    this.cache = pCache;
    this.supervisionManager = pSupervisionManager;
    this.historyTagManager = historyTagManager;
  }

  /**
   * Inner method to initialize the STL database connection.
   */
  @SuppressWarnings("unused")
  @PostConstruct
  private void init() {
    tagService.addTagSubscriptionListener(this);
  }

  @Override
  public void startHistoryPlayerMode(final HistoryProvider provider, final Timespan timespan) {
    if (!supervisionManager.isServerConnectionWorking()) {
      throw new RuntimeException("Cannot go into history mode," +
      		" because the connection to the server is down.");
    }
    
    cache.setHistoryMode(true);
    
    synchronized (cache.getHistoryModeSyncLock()) {
      if (cache.isHistoryModeEnabled()) {
        if (jmsConnectionListener == null) {
          jmsConnectionListener = new ConnectionEvents();
          supervisionManager.addConnectionListener(jmsConnectionListener);
        }
        
        if (historyPlayer == null) {
          historyPlayer = new HistoryPlayerImpl();
        }

        // Configures the history player with the given provider and time span
        historyPlayer.configure(provider, timespan);

        // Activating the history player
        historyPlayer.activateHistoryPlayer();
        try {

          // Subscribes all the current subscribed tags
          subscribeTagsToHistory(cache.getAllSubscribedDataTags());
        }
        catch (RuntimeException e) {
          historyPlayer.deactivateHistoryPlayer();
          cache.setHistoryMode(false);
          throw e;
        }
      }
    }
  }

  @Override
  public void stopHistoryPlayerMode() {
    if (cache.isHistoryModeEnabled()) {
      if (historyPlayer != null) {
        historyPlayer.stopLoading();
      }
      
      synchronized (cache.getHistoryModeSyncLock()) {
        if (historyPlayer != null) {
          historyPlayer.deactivateHistoryPlayer();
        }
      }
      cache.setHistoryMode(false);
    }
  }

  @Override
  public void onNewTagSubscriptions(final Set<Long> tagIds) {
    if (tagIds != null && tagIds.size() > 0) {
      synchronized (cache.getHistoryModeSyncLock()) {
        if (cache.isHistoryModeEnabled()
            && this.historyPlayer != null 
            && this.historyPlayer.isHistoryPlayerActive()) {
          subscribeTagsToHistory(tagIds);
        }
      }
    }
  }

  @Override
  public void onUnsubscribe(final Set<Long> tagIds) {
    if (tagIds != null && tagIds.size() > 0) {
      unsubscribeTagsFromHistory(tagIds);
    }
  }

  /**
   * Subscribes the tags to the history player.<br/>
   * <br/>
   * The caller must hold the {@link BasicCacheHandler#getHistoryModeSyncLock()}
   * when calling this method
   * 
   * @param tagIds
   *          The tags to subscribes
   */
  private void subscribeTagsToHistory(final Set<Long> tagIds) {
    subscribeTagsToHistory(cache.get(tagIds).values());
  }

  /**
   * Subscribes the tags to the history player.<br/>
   * <br/>
   * The caller must hold the {@link BasicCacheHandler#getHistoryModeSyncLock()}
   * when calling this method
   * 
   * @param clientDataTags
   *          The tags to subscribes
   */
  private void subscribeTagsToHistory(final Collection<Tag> clientDataTags) {
    if (cache.isHistoryModeEnabled()
        && this.historyPlayer != null 
        && this.historyPlayer.isHistoryPlayerActive()) {
      
      // Registers all the tag update listeners and supervision listeners  
      for (final Tag cdt : clientDataTags) {
        
        Tag realtimeValue;
        realtimeValue = ((ClientDataTagImpl) cdt).clone();
        
        if (realtimeValue != null) {
          if (realtimeValue.getServerTimestamp().getTime() == 0) {
            realtimeValue = null;
          }
        }
        
        ((ClientDataTagImpl) cdt).clean();
        
        // Registers to tag updates
        this.historyPlayer.registerTagUpdateListener(
            (TagUpdateListener) cdt,
            cdt.getId(),
            realtimeValue);
        
        // Tracks the listener, used when for later when unsubscribing
        tagToSupervisionListener.add(cdt.getId(), (SupervisionListener) cdt);
        
        // Register to supervision events
        this.historyPlayer.registerSupervisionListener(
            SupervisionEntity.PROCESS,
            (SupervisionListener) cdt,
            cdt.getProcessIds());
        
        this.historyPlayer.registerSupervisionListener(
            SupervisionEntity.EQUIPMENT,
            (SupervisionListener) cdt,
            cdt.getEquipmentIds());
        
//          For the day the SupervisionEntity.SUBEQUIPMENT also comes
//          this.historyPlayer.registerSupervisionListener(
//              SupervisionEntity.SUBEQUIPMENT,
//              (SupervisionListener) clientDataTag,
//              clientDataTag.getSubEquipmentIds());
      }
      
      new Thread("History-Player-Begin-Loading-Thread") {
        @Override
        public void run() {
          // Begins the loading process
          historyPlayer.beginLoading();
        }
      }.start();
    }
  }

  /**
   * Unsubscribes the tags to the history player
   * 
   * @param tagIds
   *          The tags to unsubscribe
   */
  private void unsubscribeTagsFromHistory(final Set<Long> tagIds) {
    synchronized (cache.getHistoryModeSyncLock()) {
      if (cache.isHistoryModeEnabled()
          && this.historyPlayer != null 
          && this.historyPlayer.isHistoryPlayerActive()) {
        
        this.historyPlayer.unregisterTags(tagIds);

        // Unregistering the supervision listeners
        for (final Long tagId : tagIds) {
          for (final SupervisionListener listener : tagToSupervisionListener.getValues(tagId)) {
            this.historyPlayer.unregisterSupervisionListener(SupervisionEntity.PROCESS, listener);
            this.historyPlayer.unregisterSupervisionListener(SupervisionEntity.EQUIPMENT, listener);
            this.historyPlayer.unregisterSupervisionListener(SupervisionEntity.SUBEQUIPMENT, listener);
            tagToSupervisionListener.removeValue(listener);
          }
        }
      }
    }
    
  }

  @Override
  public HistoryPlayer getHistoryPlayer() throws HistoryPlayerNotActiveException {
    if (isHistoryModeEnabled()) {
      return historyPlayer;
    }
    else {
      throw new HistoryPlayerNotActiveException("The history player is not active, and can therefore not be retrieved");
    }
  }
  
  @Override
  public HistoryPlayerEvents getHistoryPlayerEvents() {
    synchronized (cache.getHistoryModeSyncLock()) {
      if (this.historyPlayer == null) {
        this.historyPlayer = new HistoryPlayerImpl(); 
      }
      return this.historyPlayer;
    }
  }

  @Override
  public HistoryProviderFactory getHistoryProviderFactory() {
    return new HistoryProviderFactoryImpl(new ClientDataTagRequester());
  }

  @Override
  public boolean isHistoryModeEnabled() {
    return cache.isHistoryModeEnabled();
  }

  class ConnectionEvents implements ConnectionListener {
    @Override
    public void onConnection() {
      
    }

    @Override
    public void onDisconnection() {
      if (isHistoryModeEnabled()) {
        stopHistoryPlayerMode();
        LOG.info("The history mode is stopped, because the connection to the server is lost.");
      }
    }
  }

  @Override
  public HistoryLoadingManager createHistoryLoadingManager(final HistoryProvider historyProvider, final Collection<Long> tagIds) {
    final HistoryLoadingManager manager = new HistoryLoadingManagerImpl(historyProvider);
    final Collection<Tag> cdtValues = this.tagService.get(tagIds);
    final List<Tag> cdts = new ArrayList<Tag>();
    for (final Tag cdtValue : cdtValues) {
      if (cdtValue instanceof Tag) {
        cdts.add((Tag) cdtValue);
      }
      else {
        throw new RuntimeException(String.format("The '%s' must be of type '%s'", Tag.class.getName(), Tag.class.getName()));
      }
    }
    manager.addClientDataTagsForLoading(cdts);
    return manager;
  }

  /** Used by the {@link HistoryProvider} to get access to ClientDataTagValues */
  class ClientDataTagRequester implements ClientDataTagRequestCallback {
    @Override
    public ClientDataTagValue getClientDataTagValue(final long tagId) {
      final Collection<Tag> tagValues = tagService.get(Arrays.asList(tagId));
      if (tagValues == null || tagValues.size() == 0) {
        throw new RuntimeException("Cannot get the client data tag value for the tag id " + tagId);
      }
      return (ClientDataTagValue) tagValues.iterator().next();
    }
  }

  @Override
  public HistoryTagManager getHistoryTagManager() {
    return this.historyTagManager;
  }
  
}
