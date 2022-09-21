package cern.c2mon.client.ext.history.es_publisher.repo;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cern.c2mon.client.ext.history.es_publisher.entity.ShortTermLog;

public interface ShortTermLogHistoryService extends JpaRepository<ShortTermLog, Long> {

  // step three pagination
  @Query("SELECT a FROM ShortTermLog a WHERE "
          + "a.tagServerTime BETWEEN :startTime AND :endTime "
          + "ORDER BY a.tagServerTime ASC")
  Page<ShortTermLog> findAllByTagServerTimeBetweenOrderByTagServerTimeDesc(
          @Param("startTime") ZonedDateTime from,
          @Param("endTime") ZonedDateTime to,
          Pageable pageable);

  // step one unique tags

  @Query("SELECT DISTINCT a.id FROM ShortTermLog a WHERE "
          + "a.tagServerTime BETWEEN :startTime AND :endTime ")
  List<Long> findDistinctIdByTagTimeBetween(
          @Param("startTime") ZonedDateTime startTime,
          @Param("endTime") ZonedDateTime endTime);
}