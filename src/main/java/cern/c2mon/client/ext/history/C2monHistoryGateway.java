/******************************************************************************
 * Copyright (C) 2010-2021 CERN. All rights not expressly granted are reserved.
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

import cern.c2mon.client.ext.history.alarm.AlarmHistoryService;
import cern.c2mon.client.ext.history.command.CommandRecordService;
import org.springframework.context.ApplicationContext;

import cern.c2mon.client.core.C2monServiceGateway;

/**
 * Gateway for acquiring references to the various services available in the
 * history extension.
 *
 * This gateway is designed to be used from a non Spring environment. If your
 * application already starts a Spring context, you should not use this, but
 * directly {@link org.springframework.beans.factory.annotation.Autowire}
 * the services that you need.
 *
 * @author Justin Lewis Salmon
 */
public class C2monHistoryGateway {

  private static C2monHistoryManager historyManager = null;

  private static AlarmHistoryService alarmHistoryService = null;

  private static CommandRecordService commandRecordService = null;

  public static ApplicationContext context;

  private C2monHistoryGateway() {}

  /**
   * Initializes the C2monHistoryManager.
   */
  public static synchronized void initialize() {
    if (C2monServiceGateway.getApplicationContext() == null) {
      C2monServiceGateway.startC2monClientSynchronous();
    }
    
    if (context == null) {
      context = C2monServiceGateway.getApplicationContext();
      historyManager = context.getBean(C2monHistoryManager.class);
      alarmHistoryService = context.getBean(AlarmHistoryService.class);
      commandRecordService = context.getBean(CommandRecordService.class);
    }
  }

 /**
  * @return the {@link C2monHistoryManager} instance
  */
  public static synchronized C2monHistoryManager getHistoryManager() {
    if (context == null) {
      initialize();
    }

    return historyManager;
  }

  /**
   * @return the {@link AlarmHistoryService} instance
   */
  public static synchronized AlarmHistoryService getAlarmHistoryService() {
    if (context == null) {
      initialize();
    }

    return alarmHistoryService;
  }

  /**
   * @return the {@link CommandRecordService} instance
   */
  public static synchronized CommandRecordService getCommandRecordService() {
    if (context == null) {
      initialize();
    }

    return commandRecordService;
  }
}
