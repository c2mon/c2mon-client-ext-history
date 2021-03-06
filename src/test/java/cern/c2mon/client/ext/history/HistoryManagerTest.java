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
package cern.c2mon.client.ext.history;

import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cern.c2mon.client.common.tag.Tag;
import cern.c2mon.client.core.cache.BasicCacheHandler;
import cern.c2mon.client.core.service.AdvancedTagService;
import cern.c2mon.client.core.service.CoreSupervisionService;
import cern.c2mon.client.core.service.impl.TagServiceImpl;
import cern.c2mon.client.core.tag.TagController;
import cern.c2mon.client.ext.history.common.HistoryPlayer;
import cern.c2mon.client.ext.history.common.HistoryProvider;
import cern.c2mon.client.ext.history.common.HistoryTagValueUpdate;
import cern.c2mon.client.ext.history.common.Timespan;
import cern.c2mon.client.ext.history.common.event.HistoryPlayerAdapter;
import cern.c2mon.client.ext.history.common.event.HistoryPlayerListener;
import cern.c2mon.client.ext.history.common.event.HistoryProviderListener;
import cern.c2mon.client.ext.history.common.exception.HistoryPlayerNotActiveException;
import cern.c2mon.client.ext.history.updates.HistoryTagValueUpdateImpl;
import cern.c2mon.shared.client.tag.TagMode;
import cern.c2mon.shared.client.tag.TransferTagValueImpl;
import cern.c2mon.shared.common.datatag.DataTagQualityImpl;

/**
 * This tests the {@link HistoryManager} class for the following functions:<br/>
 * {@link HistoryManager#startHistoryPlayerMode(HistoryProvider, Timespan)},<br/>
 * {@link HistoryManager#getHistoryPlayer()},<br/>
 * {@link HistoryManager#isHistoryModeEnabled()},<br/>
 * {@link HistoryManager#onNewTagSubscriptions(Set)},<br/>
 * {@link HistoryManager#onUnsubscribe(Set)},<br/>
 * {@link HistoryManager#stopHistoryPlayerMode()}<br/>
 *
 * @author vdeila
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    "classpath:test-config/c2mon-historymanager-test.xml",
    "classpath:config/c2mon-client-ext-history.xml" })
@ActiveProfiles("test")
public class HistoryManagerTest {

  /*
   * Component to test
   */
  @Autowired
  private HistoryManager historyManager;

  /*
   * Mocked components
   */
  @Autowired
  private BasicCacheHandler cacheMock;

  @Autowired
  private HistoryProvider historyProviderMock;

  @Autowired
  private AdvancedTagService tagServiceMock;

  @Autowired
  private CoreSupervisionService coreSupervisionService;

  /*
   * Test variables
   */

  private static final Timespan timespan =
    new Timespan(
        new GregorianCalendar(2011, 06, 05, 12, 00).getTime(),
        new GregorianCalendar(2011, 06, 07, 12, 00).getTime());

  /**
   * The maximum number of milliseconds to wait for the loading to finish, or
   * <code>null</code> to disable timeout
   */
  private static final Long LOADING_TIMEOUT = 10000L;

  /**
   * The number of tags which were subscribed to before the history player were
   * started
   */
  private static final int NUMBER_OF_INITIAL_TAGS = 8;

  /** The number of tags which will be added later */
  private static final int NUMBER_OF_ADDED_TAGS = 8;

  /** The number of milliseconds to wait for the initialization */
  private static final int INITIALIZE_TIMEOUT = 10000;


  /** Exception handler for multi threaded tests */
  private Exception uncaughtException = null;

  /** The handler handeling uncaught exceptions */
  private UncaughtExceptionHandler uncaughtExceptionHandler = null;

  @Before
  public void setUp() throws Exception {
    if (uncaughtExceptionHandler == null) {
      uncaughtExceptionHandler = new UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(final Thread t, final Throwable e) {
          if (e instanceof Exception && uncaughtException == null) {
            uncaughtException = (Exception) e;
          }
        }
      };
    }
    uncaughtException = null;
    Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
  }

  @After
  public void tearDown() throws Exception {
    if (uncaughtException != null) {
      throw uncaughtException;
    }
  }


  @Test
  public void testStartHistoryPlayerMode() throws HistoryPlayerNotActiveException, InterruptedException {

    final HistoryPlayerListener historyPlayerListenerMock = EasyMock.createNiceMock(HistoryPlayerListener.class);

    // The list of history provider listeners which will be added
    final Capture<HistoryProviderListener> historyProviderListeners = new Capture<>(CaptureType.ALL);

    // Captures the last call for getting the initial records
    final Capture<Long[]> initalRecordRequest = new Capture<>(CaptureType.LAST);

    // Always contains the parameter of the last call to cacheMock.get(Set<Long>)
    final Capture<Set<Long>> cacheGetParameter = new Capture<>(CaptureType.LAST);

    final Object historyModeSyncLock = new Object();

    // The "real time" tags which is already subscribed to when starting the history player
    final List<TagController> subscribedTags = new ArrayList<>();

    // The "real time" tags which is subscribed to AFTER starting the history player
    final List<TagController> subscribedTagsAddedLater = new ArrayList<>();

    // The tag ids of the tags which already subscribed to when starting the history player,
    // exluding the tag ids which are filtered because of the server time of the real time value
    final List<Long> firstInitialTagIds = new ArrayList<>();

    // The tag ids of the tags which is subscribed to after starting the history player.
    final List<Long> secondInitialTagIds = new ArrayList<>();

    // All initial records
    final List<HistoryTagValueUpdate> initialRecords = new ArrayList<>();

    // All history records
    final List<HistoryTagValueUpdate> historyRecords = new ArrayList<>();

    // Generating data for the HistoryProvider
    for (int i = 0; i < NUMBER_OF_INITIAL_TAGS + NUMBER_OF_ADDED_TAGS; i++) {
      final TagController tagController = new TagController(100000L + i);

      final Timestamp sourceTimestamp;
      final Timestamp daqTimestamp;
      final Timestamp serverTimestamp;

      final boolean isAddedLater = i >= NUMBER_OF_INITIAL_TAGS;

      sourceTimestamp = new Timestamp(timespan.getStart().getTime() + 2 * 60 * 60 * 1000);
      daqTimestamp = new Timestamp(timespan.getStart().getTime() + 2 * 60 * 60 * 1000);
      serverTimestamp = new Timestamp(timespan.getStart().getTime() + 2 * 60 * 60 * 1000);

      // Creating "current real time" values
      final TransferTagValueImpl value =
        new TransferTagValueImpl(
            tagController.getTagImpl().getId(),
            Integer.valueOf(i),
            "Test tag value description",
            new DataTagQualityImpl(),
            TagMode.OPERATIONAL,
            sourceTimestamp,
            daqTimestamp,
            serverTimestamp,
            "Test tag");
      value.getDataTagQuality().validate();
      tagController.update(value);

      if (!isAddedLater) {
        // Adds it to the subscribed tags
        subscribedTags.add(tagController);
      }
      else {
        // Adds it to the list of tags which will be subscribed to later
        subscribedTagsAddedLater.add(tagController);
      }

      if (!isAddedLater) {
        // Adds the id to the initialization tag list
        firstInitialTagIds.add(tagController.getTagImpl().getId());
      }
      else {
        secondInitialTagIds.add(tagController.getTagImpl().getId());
      }


      // Creates a TagValueUpdate record
      final HistoryTagValueUpdateImpl initialHistoryRecord =
          new HistoryTagValueUpdateImpl(
              tagController.getTagImpl().getId(),
              new DataTagQualityImpl(),
              Integer.valueOf(i+40000),
              new Timestamp(timespan.getStart().getTime() - 1 * 60 * 60 * 1000),
              new Timestamp(timespan.getStart().getTime() - 1 * 60 * 60 * 1000),
              new Timestamp(timespan.getStart().getTime() - 1 * 60 * 60 * 1000),
              null,
              "Test tag",
              TagMode.OPERATIONAL);
        initialHistoryRecord.setValueClassName("Integer");

      // Adds the initial record to the list of initialization records
      initialRecords.add(initialHistoryRecord);

      // Creates loading records
      final Random random = new Random(i);
      long currentTime = timespan.getStart().getTime();

      while (true) {
        // Adds a time periode of between one minute and one day.
        currentTime += 60*1000 + (long)(random.nextDouble() * 60.0 * 60.0 * 1000.0);
        if (currentTime > timespan.getEnd().getTime()) {
          break;
        }

        // Creates a TagValueUpdate record
        final HistoryTagValueUpdateImpl historyRecord =
            new HistoryTagValueUpdateImpl(
                tagController.getTagImpl().getId(),
                new DataTagQualityImpl(),
                Integer.valueOf((int)(currentTime % 100000)),
                new Timestamp(currentTime - 2 * 60 * 60 * 1000),
                new Timestamp(currentTime - 2 * 60 * 60 * 1000),
                new Timestamp(currentTime),
                null,
                "Test tag",
                TagMode.OPERATIONAL);
          historyRecord.setValueClassName("Integer");

        // Adds it to the list of records
        historyRecords.add(historyRecord);
      }
    }

    // Creates a history provider that returns the given records
    final HistoryProvider historyProvider = createHistoryProvider(historyRecords, initialRecords, historyProviderListeners);

    //
    // Record
    //

    EasyMock.expect(
        historyProviderMock.getDailySnapshotRecords(EasyMock.<Long[]>anyObject(), EasyMock.<Timestamp>anyObject(), EasyMock.<Timestamp>anyObject())
        ).andReturn(new ArrayList<HistoryTagValueUpdate>()).anyTimes();

    historyProviderMock.resetProgress();
    EasyMock.expectLastCall().asStub();

    historyProviderMock.disableProvider();
    EasyMock.expectLastCall().asStub();

    historyProviderMock.enableProvider();
    EasyMock.expectLastCall().asStub();

    // startHistoryPlayerMode
    historyProviderMock.addHistoryProviderListener(EasyMock.<HistoryProviderListener>capture(historyProviderListeners));
    EasyMock.expectLastCall().anyTimes();

    cacheMock.setHistoryMode(true);

    // historyPlayer.configure
    historyPlayerListenerMock.onHistoryProviderChanged(historyProviderMock);

    // startHistoryPlayerMode
    historyPlayerListenerMock.onActivatedHistoryPlayer();

    // HistoryPlayer#beginLoading()
    // HistoryStore#registerTags(Long[])

    // HistoryLoader#beginLoading()
    historyPlayerListenerMock.onInitializingHistoryStarted();

    EasyMock.expect(historyProviderMock.getInitialValuesForTags(
        EasyMock.capture(initalRecordRequest),
        EasyMock.eq(timespan.getStart()))).andDelegateTo(historyProvider);

    historyPlayerListenerMock.onInitializingHistoryFinished();

    historyPlayerListenerMock.onHistoryIsFullyLoaded();


    // Phase 2
    // Subscribing to more data tags
//    historyPlayerListenerMock.onHistoryDataAvailabilityChanged(timespan.getStart());


    // HistoryPlayer#beginLoading()
    // HistoryStore#registerTags(Long[])
    //

    // HistoryLoader#beginLoading()
    // Loading initial values
    historyPlayerListenerMock.onInitializingHistoryStarted();

    EasyMock.expect(historyProviderMock.getInitialValuesForTags(
        EasyMock.capture(initalRecordRequest),
        EasyMock.eq(timespan.getStart()))).andDelegateTo(historyProvider);

    historyPlayerListenerMock.onInitializingHistoryFinished();

    // Loading history
    historyPlayerListenerMock.onHistoryIsFullyLoaded();


    // Phase 3
    // Deactivating history mode
    cacheMock.setHistoryMode(false);
    historyPlayerListenerMock.onDeactivatingHistoryPlayer();

    //
    // Sets stub methods
    //

    EasyMock.expect(coreSupervisionService.isServerConnectionWorking()).andStubReturn(Boolean.TRUE);

    EasyMock.expect(cacheMock.getTagControllers(EasyMock.capture(cacheGetParameter))).andReturn(subscribedTagsAddedLater).atLeastOnce();
    EasyMock.expect(cacheMock.getAllTagControllers()).andReturn(subscribedTags).atLeastOnce();

    historyPlayerListenerMock.onInitializingHistoryProgressStatusChanged(EasyMock.<String>anyObject());
    EasyMock.expectLastCall().atLeastOnce().asStub();

    historyPlayerListenerMock.onInitializingHistoryProgressChanged(EasyMock.anyDouble());
    EasyMock.expectLastCall().atLeastOnce().asStub();

    historyPlayerListenerMock.onHistoryDataAvailabilityChanged(EasyMock.<Timestamp>anyObject());//EasyMock.not(EasyMock.eq(timespan.getStart())));
    EasyMock.expectLastCall().atLeastOnce().asStub();

    EasyMock.expect(historyProviderMock.getHistory(EasyMock.<Long[]>anyObject(), EasyMock.<Timestamp>anyObject(), EasyMock.<Timestamp>anyObject()))
      .andDelegateTo(historyProvider).atLeastOnce();

    EasyMock.expect(cacheMock.getHistoryModeSyncLock()).andStubReturn(historyModeSyncLock);
    EasyMock.expect(cacheMock.isHistoryModeEnabled()).andStubReturn(true);

    EasyMock.expect(tagServiceMock.get(EasyMock.<Collection<Long>>anyObject()))
      .andDelegateTo(new TagServiceImpl(null, null, null, null) {
        @Override
        public Collection<Tag> get(final Collection<Long> tagIds) {
          final List<Tag> result = new ArrayList<>();
          for (TagController tagController : subscribedTags) {
            if (tagIds.contains(tagController.getTagImpl().getId())) {
              result.add(tagController.getTagImpl());
            }
          }
          return result;
        }
      }).anyTimes();

    //Arrays.asList(subscribedTags.toArray(new Tag[0]))

    //
    // Replay
    //

    final AtomicBoolean finishedLoading = new AtomicBoolean(false);
    final CountDownLatch initializingCountDownLatch = new CountDownLatch(1);

    EasyMock.replay(cacheMock, historyProviderMock, historyPlayerListenerMock, tagServiceMock, coreSupervisionService);
    historyManager.getHistoryPlayerEvents().addHistoryPlayerListener(historyPlayerListenerMock);

    // Event to know when the history is finish loading
    historyManager.getHistoryPlayerEvents().addHistoryPlayerListener(new HistoryPlayerAdapter() {
      @Override
      public void onHistoryIsFullyLoaded() {
        finishedLoading.set(true);
      }

      @Override
      public void onInitializingHistoryFinished() {
        initializingCountDownLatch.countDown();
      }
    });

    historyManager.startHistoryPlayerMode(historyProviderMock, timespan);

    initializingCountDownLatch.await(INITIALIZE_TIMEOUT, TimeUnit.MILLISECONDS);

    // Checks that the correct initial tag ids have been requested
    final Collection<Long> requestedInitialValues = Arrays.asList(initalRecordRequest.getValue());
    Assert.assertTrue("When initializing, it didn't ask for the expected list of tag ids.",
        requestedInitialValues.containsAll(firstInitialTagIds)
        && firstInitialTagIds.containsAll(requestedInitialValues));

    // Wait for the loading to finish
    Long timeout = null;
    if (LOADING_TIMEOUT != null) {
      timeout = System.currentTimeMillis() + LOADING_TIMEOUT;
    }
    while (!finishedLoading.get()
        && (timeout == null || timeout > System.currentTimeMillis())
        ) {
      try {
        Thread.sleep(50);
      }
      catch (InterruptedException e) { }
    }

    final HistoryPlayer historyPlayer = historyManager.getHistoryPlayer();
    Assert.assertTrue("The history is not fully loaded..", historyPlayer.getHistoryLoadedUntil().compareTo(historyPlayer.getEnd()) >= 0);


    // Phase 2
    // Subscribing to more data tags

    initalRecordRequest.reset();
    finishedLoading.set(false);

    historyManager.onNewTagSubscriptions(new HashSet<>(secondInitialTagIds));

    // Wait for the loading to finish
    if (LOADING_TIMEOUT != null) {
      timeout = System.currentTimeMillis() + LOADING_TIMEOUT;
    }
    while (!finishedLoading.get()
        && (timeout == null || timeout > System.currentTimeMillis())) {
      try {
        Thread.sleep(50);
      }
      catch (InterruptedException e) { }
    }

    Assert.assertTrue("The history is not fully loaded..", historyPlayer.getHistoryLoadedUntil().compareTo(historyPlayer.getEnd()) >= 0);

    // Checks that it asked for the correct tag ids from the cache.get(Set<Long>)
    final Collection<Long> cacheGetParameterValues = Arrays.asList(initalRecordRequest.getValue());
    Assert.assertTrue("When requesting the new client data tags from the cache, it didn't ask for the expected list of tag ids.",
        cacheGetParameterValues.containsAll(secondInitialTagIds)
        && secondInitialTagIds.containsAll(cacheGetParameterValues));

    // Phase 3
    // Unsubscribe from data tags
    historyManager.onUnsubscribe(new HashSet<>(firstInitialTagIds));
    Assert.assertTrue("The history is not fully loaded..", historyPlayer.getHistoryLoadedUntil().compareTo(historyPlayer.getEnd()) >= 0);

    // Phase 4
    // Deactivating the history player
    historyManager.stopHistoryPlayerMode();


    // Verifing
    EasyMock.verify(cacheMock, historyProviderMock, historyPlayerListenerMock);
  }

  /**
   * Creates a history provider which implements the methods:
   * {@link HistoryProvider#getInitialValuesForTags(Long[], Timestamp)}
   * and {@link HistoryProvider#getHistory(Long[], Timestamp, Timestamp)}
   *
   * @param historyRecords
   *          All the history records
   * @param initialRecords
   *          All the inital values
   * @param historyProviderListeners
   *          The history provider listeners
   * @return an instance of a history provider
   */
  private HistoryProvider createHistoryProvider(final List<HistoryTagValueUpdate> historyRecords, final List<HistoryTagValueUpdate> initialRecords, final Capture<HistoryProviderListener> historyProviderListeners) {
    return new HistoryProviderDummy() {
      @Override
      public Collection<HistoryTagValueUpdate> getInitialValuesForTags(final Long[] pTagIds, final Timestamp before) {
        final List<HistoryTagValueUpdate> result = new ArrayList<>();

        // Simulates the events which would happend in the history provider
        for (HistoryProviderListener listener : historyProviderListeners.getValues()) {
          listener.queryStarting();
        }

        for (int i = 0; i < pTagIds.length; i++) {
          final Long searchingTagId = pTagIds[i];
          for (final HistoryTagValueUpdate record : initialRecords) {
            if (searchingTagId.equals(record.getId())
                && before.compareTo(record.getServerTimestamp()) > 0) {
              result.add(record);
              break;
            }
          }

          for (HistoryProviderListener listener : historyProviderListeners.getValues()) {
            listener.queryProgressChanged(i / (double) pTagIds.length);
          }
        }

        for (HistoryProviderListener listener : historyProviderListeners.getValues()) {
          listener.queryFinished();
        }

        return result;
      }

      @Override
      public Collection<HistoryTagValueUpdate> getHistory(final Long[] tagIds, final Timestamp from, final Timestamp to) {
        for (HistoryProviderListener listener : historyProviderListeners.getValues()) {
          listener.queryStarting();
        }

        for (HistoryProviderListener listener : historyProviderListeners.getValues()) {
          listener.queryProgressChanged(0.0);
        }

        // Filters out the tags and timespan that is asked for.
        final List<HistoryTagValueUpdate> result = new ArrayList<>();
        final List<Long> tagIdList = Arrays.asList(tagIds);
        for (final HistoryTagValueUpdate value : historyRecords) {
          if (tagIdList.contains(value.getId())) {
            if (from.compareTo(value.getServerTimestamp()) <= 0
                || to.compareTo(value.getServerTimestamp()) >= 0) {
              result.add(value);
            }
          }
        }

        for (HistoryProviderListener listener : historyProviderListeners.getValues()) {
          listener.queryProgressChanged(1.0);
        }

        for (HistoryProviderListener listener : historyProviderListeners.getValues()) {
          listener.queryFinished();
        }
        return result;
      }
    };
  }

}
