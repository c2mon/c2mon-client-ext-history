package cern.c2mon.client.ext.history.es_publisher.repo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cern.c2mon.client.ext.history.es_publisher.entity.DataTagData;

public interface TagConfigHistoryService extends JpaRepository<DataTagData, Long>{

    @Query("SELECT a FROM DataTagData a WHERE "
            + "a.tagServerTimestamp BETWEEN :startTime AND :endTime "
            + "ORDER BY a.tagServerTimestamp ASC")
    List<DataTagData> findByTagServerTimestampBetweenByOrderByTagTimeStampDesc(
            @Param("startTime") ZonedDateTime startTime,
            @Param("endTime") ZonedDateTime endTime);

    Page<DataTagData> findAllAfterByOrderByTagTimeStampAsc(
            @Param("startTime") Instant startTime,
            Pageable pageable);

    Page<DataTagData> findAllByOrderByTagTimeStampDesc(Pageable pageable);
}
