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
package cern.c2mon.client.ext.history.common.tag;


/**
 * This enum defines all the parameters that can be set in a
 * {@link HistoryTagConfiguration}.
 * 
 * @author vdeila
 */
public enum HistoryTagParameter {
  
  // If changing the class type of an existing parameters, you should also change the HistoryTagConfiguration
  
  /** The type of result */
  Result(false, HistoryTagResultType.class, HistoryTagResultType.Values),
  /** The tag id */
  TagId(true, "tagId", Long.class),
  /** The number of records */
  Records(true, "records", Integer.class),
  /** The number of days */
  Days(true, "days", Double.class),
  /** The number of hours */
  Hours(true, "hours", Double.class),
  /** Whether or not to request supervision events */
  Supervision(true, "supervision", Boolean.class, true),
  /** Whether or not to request initial records */
  InitialRecord(true, "initial", Boolean.class, new InitialRecordsConditionalDefaultValue()),
  
  /** The value while loading */
  LoadingValue(false, "loading", Object.class),
  /** The value when the data is loaded */
  ActiveValue(false, "active", Object.class),
  /** The value if it failed to load the data */
  FailedValue(false, "failed", Object.class),
  
  /** Any changes to the result */
  ResultChange(false, "change", String.class);
  
  /** The argument. Can be <code>null</code> if {@link #type} is an enum */
  private final String argument;
  
  /** The type */
  private final Class< ? > type;
  
  /** The enum type */
  private Class< ? extends Enum< ? >> enumType;
  
  /** The default value */
  private final Object defaultValue;
  
  /** The default value based on the given configurations */
  private ConditionalDefaultValue< ? > conditionalDefaultValue;
  
  /**
   * <code>true</code> if this parameter changes the request that will be sent
   * to the data provider.
   */
  private final boolean affectingQuery;
  
  private <T extends Enum<?>> HistoryTagParameter(final boolean affectingQuery, final Class<T> enumType, final T defaultValue) {
    this(affectingQuery, null, enumType, defaultValue);
    this.enumType = enumType;
  }
  
  private <T> HistoryTagParameter(final boolean affectingQuery, final String argument, final Class<T> type, final ConditionalDefaultValue<T> conditionalDefaultValue) {
    this(affectingQuery, argument, type);
    this.conditionalDefaultValue = conditionalDefaultValue;
  }
  
  private <T> HistoryTagParameter(final boolean affectingQuery, final String argument, final Class<T> type) {
    this(affectingQuery, argument, type, (T) null);
  }
  
  private <T> HistoryTagParameter(final boolean affectingQuery, final String argument, final Class<T> type, final T defaultValue) {
    this.affectingQuery = affectingQuery;
    this.argument = argument;
    this.type = type;
    this.defaultValue = defaultValue;
    this.enumType = null;
    this.conditionalDefaultValue = null;
  }
  
  /**
   * @return the value type
   */
  public Class< ? > getType() {
    return type;
  }
  
  /**
   * Finds the history tag parameter by the given <code>argument</code>.
   * 
   * @param argument
   *          the {@link #getArgument()} to find.
   * @return the {@link HistoryTagParameter} with the given
   *         <code>argument</code>.
   */
  public static HistoryTagParameter findByArgument(final String argument) {
    for (HistoryTagParameter parameter : values()) {
      if (parameter.getArgument() != null && argument.compareToIgnoreCase(parameter.getArgument()) == 0) {
        return parameter;
      }
    }
    return null;
  }
  
  /**
   * Finds the history tag parameter by the given enum value.
   * 
   * @param value the value to search for
   * @return the history tag parameter where this value is accepted.
   */
  public static HistoryTagParameter findByEnumValue(final String value) {
    for (HistoryTagParameter parameter : values()) {
      if (parameter.isEnum()) {
        for (Enum< ? > constant : parameter.enumType.getEnumConstants()) {
          if (constant.toString().compareToIgnoreCase(value) == 0) {
            return parameter;
          }
        }
      }
    }
    return null;
  }

  /**
   * @return The argument. Can be <code>null</code> if {@link #type} is an enum.
   */
  public String getArgument() {
    return argument;
  }

  /**
   * @param configuration
   *          the configuration to get the default value for. The value might
   *          change depending on the configuration.
   * @return the default value
   */
  public Object getDefaultValue(final HistoryTagConfiguration configuration) {
    if (conditionalDefaultValue == null) {
      return defaultValue;
    }
    else {
      return conditionalDefaultValue.getDefault(configuration);
    }
  }

  /**
   * @return whether or the parameter is an enum type
   */
  public boolean isEnum() {
    return enumType != null;
  }

  /**
   * @return <code>true</code> if this parameter changes the request that will be sent
   * to the data provider.
   */
  public boolean isAffectingQuery() {
    return affectingQuery;
  }
  
  /** The conditional default value for the {@link HistoryTagParameter#InitialRecord} */
  private static class InitialRecordsConditionalDefaultValue implements ConditionalDefaultValue<Boolean> {
    @Override
    public Boolean getDefault(final HistoryTagConfiguration configuration) {
      return Boolean.valueOf(
          configuration.getTotalMilliseconds() != null
          );
    }
  }
}
