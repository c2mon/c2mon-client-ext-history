/******************************************************************************
 * Copyright (C) 2010-2019 CERN. All rights not expressly granted are reserved.
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
package cern.c2mon.client.ext.history.data.repo;

import cern.c2mon.client.ext.history.data.DataTagRecord;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaRepository;
=======
import cern.c2mon.client.ext.history.supervision.ServerSupervisionEvent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
>>>>>>> 998532854423d1175daf0366f62267e70e8955fe
import java.util.List;
import java.util.Optional;


public interface DataTagRepoService extends JpaRepository<DataTagRecord, Long>{

    Optional<DataTagRecord> findById(Long dataTagId);

    List<DataTagRecord> findFirst10ByNameContainingIgnoreCase(String dataTagName);

<<<<<<< HEAD
=======
    @Query("SELECT a FROM DataTagRecord a WHERE "
            + "a.tagServerTimestamp BETWEEN :startTime AND :endTime "
            + "ORDER BY a.tagServerTimestamp ASC")
    List<DataTagRecord> findByTagServerTimestampBetweenByOrderByTagTimeStampDesc(
            @Param("startTime") Instant startTime,
            @Param("endTime") Instant endTime);

    Page<DataTagRecord> findAllByTagServerTimestampAfterOrderByTagTimeStampAsc(
            @Param("startTime") Instant startTime,
            Pageable pageable);

    Page<DataTagRecord> findAllByOrderByTagTimeStampDesc(Pageable pageable);

>>>>>>> 998532854423d1175daf0366f62267e70e8955fe
}
