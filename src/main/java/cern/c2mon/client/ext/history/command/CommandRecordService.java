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
package cern.c2mon.client.ext.history.command;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This service allows querying {@link CommandRecord} history from the c2mon history database.
 *
 */
public interface CommandRecordService extends JpaRepository<CommandRecord, CommandRecordId> {
    
    /**
     * Find all historical command records for the given time span and the given command id in descending time order
     * @param id command id
     * @param startTime start time to search for an command entry
     * @param endTime end time to search for an command entry
     * @param pageable The requested page
     * @return The requested page
     * @see #findAllByIdAndTimeBetweenOrderByTimeDesc(Long, LocalDateTime, LocalDateTime)
     */
    Page<CommandRecord> findAllByIdAndTimeBetweenOrderByTimeDesc(
            Long id, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    /**
     * Find all historical command records for the given time span and the given command id in descending time order
     * @param id command id
     * @param startTime start time to search for an command entry
     * @param endTime end time to search for an command entry
     * @return The resulting list
     * @see #findAllByIdAndTimeBetweenOrderByTimeDesc(Long, LocalDateTime, LocalDateTime, Pageable)
     */
    List<CommandRecord> findAllByIdAndTimeBetweenOrderByTimeDesc(
            Long id, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Returns the last N records for a given command id in descending time order
     * @param id command id
     * @param pageable Use e.g. <code>new PageResult(0, 100)</code> to retrieve the last 100 historical records for the given command
     * @return The page of requested commands
     */
    Page<CommandRecord> findAllByIdOrderByTimeDesc(Long id, Pageable pageable);


}
