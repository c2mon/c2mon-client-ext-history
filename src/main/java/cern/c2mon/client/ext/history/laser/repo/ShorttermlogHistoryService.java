package cern.c2mon.client.ext.history.laser.repo;

import cern.c2mon.client.ext.history.laser.Shorttermlog;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Profile("enableLaser")
@Service
public interface ShorttermlogHistoryService extends JpaRepository<Shorttermlog, Long>{

    @Query("SELECT DISTINCT a FROM Shorttermlog a WHERE "
            + "a.id = :id AND "
            + "a.tagServerTime BETWEEN :startTime AND :endTime "
            + "ORDER BY a.tagServerTime DESC")
    Page<Shorttermlog> findAllDistinctByIdAndTagServerTimeBetweenOrderByTagServerTimeDesc(
            @Param("id") Long id,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

    @Query("SELECT DISTINCT a FROM Shorttermlog a WHERE "
            + "a.id = :id AND "
            + "a.tagServerTime BETWEEN :startTime AND :endTime "
            + "ORDER BY a.tagServerTime DESC")
    List<Shorttermlog> findAllDistinctByIdAndTagServerTimeBetweenOrderByTagServerTimeDesc(
            @Param("id") Long id,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    @Query("SELECT DISTINCT a FROM Shorttermlog a WHERE "
            + "a.tagServerTime BETWEEN :startTime AND :endTime "
            + "ORDER BY a.tagServerTime DESC")
    Page<Shorttermlog> findAllDistinctByTagServerTimeBetweenOrderByTagServerTimeDesc(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

    @Query("SELECT DISTINCT a FROM Shorttermlog a WHERE "
            + "a.id = :id "
            + "ORDER BY a.tagServerTime DESC")
    Page<Shorttermlog> findAllDistinctByIdOrderByTagServerTimeDesc(
            @Param("id") Long id,
            Pageable pageable);

}
