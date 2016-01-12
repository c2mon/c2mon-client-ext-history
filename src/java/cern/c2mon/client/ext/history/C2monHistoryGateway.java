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

import cern.c2mon.client.ext.history.alarm.AlarmHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cern.c2mon.client.core.C2monServiceGateway;

public class C2monHistoryGateway {

  /** Class logger */
  private static final Logger LOG = LoggerFactory.getLogger(C2monHistoryGateway.class);

  /** The path to the core Spring XML */
  private static final String APPLICATION_SPRING_XML_PATH
    = "classpath:cern/c2mon/client/ext/history/springConfig/spring-history.xml";

  /** The extended SPRING application context for this gateway */
  public static ApplicationContext context;

  /** Static reference to the <code>C2monHistoryManager</code> singleton instance */
  private static C2monHistoryManager historyManager = null;

  /** Static reference to the <code>AlarmHistoryService</code> singleton instance */
  private static AlarmHistoryService alarmHistoryService = null;

  /**
   * Hidden default constructor
   */
  private C2monHistoryGateway() {
    // Do nothing
  }

  /**
   * Initializes the C2monHistoryManager. Must be called before using for the
   * first time {@link #getTagSimulator()}.
   * @see #startC2monClientSynchronous(Module...)
   */
  public static synchronized void initialize() {
    if (C2monServiceGateway.getApplicationContext() == null) {
      C2monServiceGateway.startC2monClientSynchronous();
    }
    initiateHistoryManager();
  }

  /**
   * Private method which initiates the static field(s) of this gateway by retrieving
   * it from the extended gateway {@link #context}.
   */
  private static void initiateHistoryManager() {
    if (context == null) {
      context = new ClassPathXmlApplicationContext
          (new String[]{APPLICATION_SPRING_XML_PATH}, C2monServiceGateway.getApplicationContext());
      historyManager = context.getBean(C2monHistoryManager.class);
    }
    else {
      LOG.warn("C2monHistoryManager is already initialized.");
    }
  }

 /**
  * @return The C2MON history manager which allows
  *         switching data into history mode.
  */
  public static synchronized C2monHistoryManager getHistoryManager() {

    if (historyManager == null) {
      initialize();
    }
    return historyManager;
  }

  /**
   * @return the {@link AlarmHistoryService} instance
   */
  public static synchronized AlarmHistoryService getAlarmHistoryService() {
    if (alarmHistoryService == null) {
      initialize();
      alarmHistoryService = context.getBean(AlarmHistoryService.class);
    }
    return alarmHistoryService;
  }
}
