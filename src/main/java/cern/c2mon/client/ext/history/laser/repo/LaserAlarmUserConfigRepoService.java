package cern.c2mon.client.ext.history.laser.repo;

import cern.c2mon.client.ext.history.laser.LaserAlarmUserConfig;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Profile("enableLaser")
@Service
public interface LaserAlarmUserConfigRepoService extends JpaRepository<LaserAlarmUserConfig, Long> {
    
    List<LaserAlarmUserConfig> findByConfigId(Long configId);

    @Query("select lauc from LaserAlarmUserConfig lauc " +
            "where lauc.configId = :configId " +
            "and lauc.lastUpdated between :startTime and :endTime " +
            "and (lauc.faultFamily like %:text% or " +
                    "lauc.faultMember like %:text% or " +
                    "lauc.faultCode like %:text% or " +
                    "lauc.problemDescription like %:text%) " +
            "and lauc.priority in :priorities " +
            "order by lauc.lastUpdated asc")
    List<LaserAlarmUserConfig> findAllByConfigIdAndPriorityAndTextBetweenDates(
            @Param("configId") Long configId, @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime, @Param("text") String text, @Param("priorities") List<Integer> priorities);

    @Query("select lauc from LaserAlarmUserConfig lauc " +
            "where lauc.configId = :configId " +
            "and lauc.lastUpdated between :startTime and :endTime " +
            "and lauc.priority in :priorities " +
            "order by lauc.lastUpdated asc")
    List<LaserAlarmUserConfig> findAllByConfigIdAndPriorityBetweenDates(
            @Param("configId") Long configId, @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime, @Param("priorities") List<Integer> priorities);

}