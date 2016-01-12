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
package cern.c2mon.client.ext.history.common.tag;

/**
 * Describes methods that can be used for handling {@link HistoryTag}s.
 * 
 * @author vdeila
 */
public interface HistoryTagManager {

  /**
   * Creates a history tag based on the <code>expression</code>. Note that
   * {@link #subscribeToConfiguration(HistoryTagConfiguration, HistoryTagManagerListener)}
   * should be called after creating it. (Else it won't do much)
   * 
   * @param expression
   *          an expression created with the
   *          {@link HistoryTagConfiguration#createExpression()}
   * @return a history tag with the given <code>expression</code> as the
   *         configuration.
   */
  HistoryTag createHistoryTag(String expression);
  
  /**
   * Creates a history tag based on the <code>expression</code>. Note that
   * {@link #subscribeToConfiguration(HistoryTagConfiguration, HistoryTagManagerListener)}
   * should be called after creating it. (Else it won't do much)
   * 
   * @param expression
   *          an expression created with the
   *          {@link HistoryTagConfiguration#createExpression()}
   *          
   * @param allowNullValues 
   *  Whether null values are allowed or not. In case this is false,
   *  null values will be removed.
   * 
   * @return a history tag with the given <code>expression</code> as the
   *         configuration.
   */
  HistoryTag createHistoryTag(String expression, boolean allowNullValues);

  /**
   * Creates a history tag based on the <code>configuration</code>. Note that
   * {@link #subscribeToConfiguration(HistoryTagConfiguration, HistoryTagManagerListener)}
   * should be called after creating it. (Else it won't do much)
   * 
   * @param configuration
   *          the configuration to use for the history tag
   * @return a history tag with the given configuration
   */
  HistoryTag createHistoryTag(HistoryTagConfiguration configuration);

  /**
   * @return an empty configuration with the default values
   */
  HistoryTagConfiguration createEmptyConfiguration();

  /**
   * @param expression
   *          the expression to initiate the configuration with
   * @return a HistoryTagConfiguration with the given <code>expression</code> as
   *         the initial configuration
   * @throws HistoryTagExpressionException
   *           if any of the arguments in the expression is invalid
   */
  HistoryTagConfiguration createConfiguration(String expression) throws HistoryTagExpressionException;

  /**
   * Subscribes the <code>listener</code> for the given
   * <code>configuration</code>. When the historical data for the given
   * <code>configuration</code> are retrieved it will call the listener.
   * 
   * The
   * 
   * @param configuration
   *          the configuration to subscribe to
   * @param listener
   *          the listener that subscribes to the data
   */
  void subscribeToConfiguration(HistoryTagConfiguration configuration, HistoryTagManagerListener listener);

  /**
   * @param configuration
   *          the configuration to subscribe to
   * @param listener
   *          the listener that subscribes to the data
   */
  void unsubscribeFromConfiguration(HistoryTagConfiguration configuration, HistoryTagManagerListener listener);

}
