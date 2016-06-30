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
package cern.c2mon.client.ext.history.playback.components;

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import cern.c2mon.client.ext.history.common.Timespan;
import cern.c2mon.client.ext.history.playback.player.Clock;
import cern.c2mon.client.ext.history.playback.player.event.ClockListener;

/**
 * Tests the {@link Clock} component
 * 
 * @author vdeila
 * 
 */
public class ClockTest {

  /** Component under test */
  private Clock clock;

  /** The timespan which will be used for the clock */
  private static final Timespan TIMESPAN = 
    new Timespan(
        new GregorianCalendar(2011, 06, 03, 00, 00).getTime(), 
        new GregorianCalendar(2011, 06, 10, 0, 00).getTime());

  /** The second timespan used */
  private static final Timespan TIMESPAN2 = 
    new Timespan(
        new GregorianCalendar(2011, 06, 02, 00, 00).getTime(), 
        new GregorianCalendar(2011, 06, 12, 00, 00).getTime());
  
  /** The time to sleep */
  private static final long RUN_TIME = 2000;

  /** How many milliseconds that the measurement can miss with */
  private static final long ACCEPTED_TIME_ERROR = 1000;

  
  @Before
  public void setUp() throws Exception {
    clock = new Clock(TIMESPAN.getStart(), TIMESPAN.getEnd());
  }

  /**
   * Checks that the measurement is close enough to be accepted. test fails if
   * it is not accepted
   * 
   * @param expectedTime
   *          the expected time
   * @param actualTime
   *          the actual time
   */
  private void checkTime(final long expectedTime, final long actualTime) {
    boolean accepted = expectedTime + ACCEPTED_TIME_ERROR >= actualTime 
                       && expectedTime - ACCEPTED_TIME_ERROR <= actualTime;
    if (!accepted) {
      Assert.fail(String.format("Expected: %s (%d ms), actual: %s (%d ms)", 
          new Timestamp(expectedTime), expectedTime,
          new Timestamp(actualTime), actualTime));
    }
  }

  /**
   * Tests normal start and pause behavior.
   * 
   * @see Clock#start()
   * @see Clock#pause()
   */
  @Test
  public void testNormalStartPause() throws InterruptedException {
    long calculatedTime = this.clock.getTime() + RUN_TIME;
    clock.setSpeedMultiplier(1.0);
    clock.start();
    Thread.sleep(RUN_TIME);
    clock.pause();
    checkTime(calculatedTime, clock.getTime());
    Assert.assertFalse("The clock shouldn't be running", clock.isRunning());
    Assert.assertTrue("The clock should be paused", clock.isPaused());
    
    clock.start();
    Assert.assertTrue("The clock should be running", clock.isRunning());
    Assert.assertFalse("The clock shouldn't be paused", clock.isPaused());
  }
  
  /**
   * Tests normal speed multiplier behavior.
   * 
   * @see Clock#setSpeedMultiplier(double)
   */
  @Test
  public void testSpeedMultiplier() throws InterruptedException {
    final double speedMultiplier = 3.0;
    long calculatedTime = this.clock.getTime() + (long) (RUN_TIME * speedMultiplier);
    clock.setSpeedMultiplier(speedMultiplier);
    clock.start();
    Thread.sleep(RUN_TIME);
    clock.pause();
    checkTime(calculatedTime, clock.getTime());
  }
  
  /**
   * Tests that the speed multiplier works when setting it while running.
   * 
   * @see Clock#setSpeedMultiplier(double)
   * @see Clock#getSpeedMultiplier()
   */
  @Test
  public void testSpeedMultiplier2() throws InterruptedException {
    final double speedMultiplier = 3.0;
    long calculatedTime = this.clock.getTime() + RUN_TIME + (long) (RUN_TIME * speedMultiplier);
    
    clock.setSpeedMultiplier(1.0);
    clock.start();
    Thread.sleep(RUN_TIME);
    clock.setSpeedMultiplier(speedMultiplier);
    Thread.sleep(RUN_TIME);
    clock.pause();
    checkTime(calculatedTime, clock.getTime());
    
    Assert.assertEquals(speedMultiplier, clock.getSpeedMultiplier());
  }
  
  /**
   * Tests setting start and end time
   * 
   * @see Clock#setStartTime(long)
   * @see Clock#setEndTime(long) 
   */
  @Test
  public void testSetDates() throws InterruptedException {
    final double speedMultiplier = 3.0;
    long calculatedTime = this.clock.getTime() + RUN_TIME + (long) (RUN_TIME * speedMultiplier);
    
    clock.setSpeedMultiplier(1.0);
    clock.start();
    Thread.sleep(RUN_TIME);
    clock.setSpeedMultiplier(speedMultiplier);
    Thread.sleep(RUN_TIME);
    checkTime(calculatedTime, clock.getTime());
    
    calculatedTime = clock.getTime();
    clock.setStartTime(TIMESPAN2.getStart().getTime());
    clock.setEndTime(TIMESPAN2.getEnd().getTime());
    
    // Checks that the time is still the same
    checkTime(calculatedTime, clock.getTime());

    calculatedTime = this.clock.getTime() + (long) (RUN_TIME * speedMultiplier);
    Thread.sleep(RUN_TIME);
    checkTime(calculatedTime, clock.getTime());
    
    Assert.assertTrue("The stop should have been running!", clock.isRunning());
  }
  
  /**
   * Tests hasReachedEndTime function
   * 
   * @see Clock#hasReachedEndTime()
   */
  @Test
  public void testHasReachedEndTime() throws InterruptedException {
    final long totalTime = this.clock.getEndDate().getTime() - this.clock.getStartDate().getTime();
    final double speedMultiplier = totalTime / (double) (RUN_TIME);
    
    long runTimePart1 = RUN_TIME / 2;
    long runTimePart2 = RUN_TIME - runTimePart1 + ACCEPTED_TIME_ERROR;
    
    clock.setSpeedMultiplier(speedMultiplier);
    clock.start();
    
    Thread.sleep(runTimePart1);
    Assert.assertFalse("It shouldn't have reached the end time yet", clock.hasReachedEndTime());
    
    Thread.sleep(runTimePart2);
    Assert.assertTrue("It should have reached the end time", clock.hasReachedEndTime());
    checkTime(clock.getEndTime(), clock.getTime());
  }
  
  /**
   * Tests hasReachedEndTime function
   * 
   * @see Clock#hasReachedEndTime()
   */
  @Test
  public void testClockListener() throws InterruptedException {
    final long totalTime = this.clock.getEndDate().getTime() - this.clock.getStartDate().getTime();
    final double speedMultiplier = totalTime / (double) (RUN_TIME);
    
    long runTimePart1 = RUN_TIME / 2;
    long runTimePart2 = RUN_TIME - runTimePart1;
    
    final long endTimeReachesAfter = RUN_TIME - ACCEPTED_TIME_ERROR;
    final CountDownLatch listenerCall = new CountDownLatch(1);
    
    final ClockListener listener = new ClockListener() {
      @Override
      public void onEndTimeReached() {
        if (System.currentTimeMillis() < endTimeReachesAfter) {
          Assert.fail("The end time event were called too early.");
        }
        listenerCall.countDown();
      }
    };
    clock.addClockListener(listener);
    
    clock.setSpeedMultiplier(speedMultiplier);
    clock.start();
    
    Thread.sleep(runTimePart1);
    Assert.assertFalse("It shouldn't have reached the end time yet", clock.hasReachedEndTime());
    
    Assert.assertTrue("The onEndTimeReached() were not called as expected.",
        listenerCall.await(runTimePart2 + ACCEPTED_TIME_ERROR, TimeUnit.MILLISECONDS));
    
    Assert.assertTrue("It should have reached the end time", clock.hasReachedEndTime());
    checkTime(clock.getEndTime(), clock.getTime());
  }
}
