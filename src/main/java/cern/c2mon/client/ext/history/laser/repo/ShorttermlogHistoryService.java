package cern.c2mon.client.ext.history.laser.repo;

import cern.c2mon.client.ext.history.laser.Shorttermlog;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Profile("enableLaser")
@Service
public interface ShorttermlogHistoryService extends JpaRepository<Shorttermlog, Long>{


    Page<Shorttermlog> findAllDistinctByIdAndTagServerTimeBetweenOrderByTagServerTimeDesc(
            Long id, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    List<Shorttermlog> findAllDistinctByIdAndTagServerTimeBetweenOrderByTagServerTimeDesc(
            Long id, LocalDateTime startTime, LocalDateTime endTime);

    Page<Shorttermlog> findAllDistinctByTagServerTimeBetweenOrderByTagServerTimeDesc(
            LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);

    Page<Shorttermlog> findAllDistinctByIdOrderByTagServerTimeDesc(Long id, Pageable pageable);

}
