package cern.c2mon.client.ext.history.es_publisher.repo;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import cern.c2mon.client.ext.history.es_publisher.DataTagData;
import cern.c2mon.client.ext.history.laser.Shorttermlog;

public interface TagConfigHistoryService extends JpaRepository<DataTagData, Long>{

    Page<Shorttermlog> findAllBetweenByOrderByTagTimeStampDesc(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);

    Page<DataTagData> findAllByOrderByTagTimeStampDesc(Pageable pageable);
}
