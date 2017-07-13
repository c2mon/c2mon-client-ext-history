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
package cern.c2mon.client.ext.history.alarm.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cern.c2mon.client.ext.history.alarm.Alarm;
import cern.c2mon.client.ext.history.alarm.AlarmHistoryService;
import cern.c2mon.client.ext.history.alarm.HistoricAlarmQuery;
import cern.c2mon.client.ext.history.alarm.repository.AlarmRepository;
import cern.c2mon.client.ext.history.dbaccess.util.TimeZoneUtil;

/**
 * @author Justin Lewis Salmon
 */
@Service
public class AlarmHistoryServiceImpl implements AlarmHistoryService {

  @Autowired
  private AlarmRepository alarmRepository;

  @Override
  public List<Alarm> findBy(HistoricAlarmQuery query) {
    return toList(localise(alarmRepository.findAll(query.getPredicate())));
  }

  @Override
  public Page<Alarm> findBy(HistoricAlarmQuery query, Pageable page) {
    return (Page<Alarm>) localise(alarmRepository.findAll(query.getPredicate(), page));
  }

  /**
   * Modify the timestamps of the given {@link Iterable<Alarm>} list to match the current local timezone.
   * Relies on the fact that the {@link Alarm} timestamps are stored as UTC.
   *
   * @param alarms the list of alarms to modify
   * @return the same list, with localised timestamps (modified in-place)
   */
  private Iterable<Alarm> localise(Iterable<Alarm> alarms) {
    for (Alarm alarm : alarms) {
      alarm.setTimestamp(TimeZoneUtil.convertDateTimezone(TimeZone.getDefault(), alarm.getTimestamp(), TimeZone.getTimeZone("UTC")));
    }

    return alarms;
  }

  /**
   * Convert an {@link Iterable} to an {@link ArrayList}.
   *
   * @param iterable
   * @param <E>
   * @return
   */
  private static <E> List<E> toList(Iterable<E> iterable) {
    List<E> list = new ArrayList<>();
    for (E item : iterable) {
      list.add(item);
    }
    return list;
  }
}
