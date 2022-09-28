package cern.c2mon.client.ext.history.supervision;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Manuel Bouzas Reguera
 */
public interface SupervisionEventRepository extends JpaRepository<ServerSupervisionEvent, Long>{

    List<ServerSupervisionEvent> findAllDistinctByIdAndEventTimeBetween(Long id, LocalDateTime from, LocalDateTime to);

    List<ServerSupervisionEvent> findAllDistinctByIdAndEventTimeBetweenOrderByEventTimeDesc(Long id, LocalDateTime startTime, LocalDateTime endTime);

    Page<ServerSupervisionEvent> findAllDistinctByIdOrderByEventTimeDesc(Long id, Pageable pageable);

    Set<ServerSupervisionEvent> findByEventInstantBetween(Instant from, Instant to);

}
