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
package cern.c2mon.client.ext.history.common.exception;

/**
 * Exception is threw if something goes wrong in fetching an instance of a
 * {@link HistoryProvider}
 * 
 * @author vdeila
 * 
 */
public class NoHistoryProviderException extends Exception {

  /** serialVersionUID */
  private static final long serialVersionUID = 4579433171420826894L;

  /** Empty constructor */
  public NoHistoryProviderException() {
  }

  /**
   * 
   * @param message
   *          an description of why the exception occurred
   */
  public NoHistoryProviderException(final String message) {
    super(message);
  }

  /**
   * 
   * @param cause
   *          the original exception
   */
  public NoHistoryProviderException(final Throwable cause) {
    super(cause);
  }

  /**
   * 
   * @param message
   *          an description of why the exception occurred
   * @param cause
   *          the original exception
   */
  public NoHistoryProviderException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
