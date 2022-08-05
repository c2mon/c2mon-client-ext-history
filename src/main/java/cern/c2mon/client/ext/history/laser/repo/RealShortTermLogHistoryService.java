package cern.c2mon.client.ext.history.laser.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cern.c2mon.client.ext.history.laser.RealShortTermLog;

public interface RealShortTermLogHistoryService extends JpaRepository<RealShortTermLog, Long> {

  // step three pagination
  @Query("SELECT DISTINCT a FROM RealShortTermLog a WHERE "
          + "a.tagServerTime BETWEEN :startTime AND :endTime")
  Page<RealShortTermLog> findAllByTagTimeBetween(
          @Param("startTime") LocalDateTime from,
          @Param("endTime") LocalDateTime to,
          Pageable pageable);

  // step one unique tags

  @Query("SELECT DISTINCT a.id FROM RealShortTermLog a WHERE "
          + "a.tagServerTime BETWEEN :startTime AND :endTime ")
  List<Long> findDistinctIdByTagTimeBetween(
          @Param("startTime") LocalDateTime startTime,
          @Param("endTime") LocalDateTime endTime);
}
