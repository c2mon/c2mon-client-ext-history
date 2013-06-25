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
package cern.c2mon.client.ext.history.common.event;

/**
 * Is used by the {@link PlaybackControl} to inform about events
 * 
 * @author vdeila
 *
 */
public interface PlaybackControlListener {

  /**
   * Invoked when the playback is starting
   */
  void onPlaybackStarting();

  /**
   * Invoked when the playback is stopped or paused
   */
  void onPlaybackStopped();
  
  /**
   * Invoked when the clock is going to be manually changed, is NOT called
   * continuously when the playback is running
   * 
   * @param newTime
   *          The new time that it is being set to.
   */
  void onClockTimeChanging(final long newTime);

  /**
   * Invoked when the clock have been manually changed, is NOT called
   * continuously when the playback is running
   * 
   * @param newTime
   *          The new time that it have been set to
   */
  void onClockTimeChanged(final long newTime);

  /**
   * Invoked when the playback speed is changed
   * 
   * @param newMultiplier
   *          The new multiplier that is set
   */
  void onClockPlaybackSpeedChanged(final double newMultiplier);
}
