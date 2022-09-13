package cern.c2mon.client.ext.history.es_publisher.repo;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cern.c2mon.client.ext.history.es_publisher.DataTagData;

public interface TagConfigHistoryService extends JpaRepository<DataTagData, Long>{

    Page<DataTagData> findAllBetweenByOrderByTagTimeStampDesc(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

    @Query("SELECT a FROM DataTagData a WHERE "
            + "a.tagTimeStamp > :startTime "
            + "ORDER BY a.tagTimeStamp ASC")
    Page<DataTagData> findAllAfterByOrderByTagTimeStampAsc(
            @Param("startTime") LocalDateTime startTime,
            Pageable pageable);

    Page<DataTagData> findAllByOrderByTagTimeStampDesc(Pageable pageable);
}
