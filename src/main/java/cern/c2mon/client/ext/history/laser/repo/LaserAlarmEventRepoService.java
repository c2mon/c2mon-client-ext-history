package cern.c2mon.client.ext.history.laser.repo;

import cern.c2mon.client.ext.history.laser.LaserAlarmLogUserConfig;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Profile("enableLaser")
@Service
public interface LaserAlarmEventRepoService extends JpaRepository<LaserAlarmLogUserConfig, Long> {

    /**
     * Fetch active alarms state at given time
     * Filter by configId, text search and priority
     */
    @Query("select lagc from LaserAlarmLogUserConfig lagc " +
            "where lagc.serverTime = (select max(lagc2.serverTime) from LaserAlarmLogUserConfig lagc2 " +
            "where lagc2.serverTime < to_timestamp(:time, 'YYYY-MM-DD-HH24:MI') " +
            "and lagc.id = lagc2.id) " +
            "and lagc.active='Y' " +
            "and exists (select lagc3 from LaserAlarmLogUserConfig lagc3 " +
            "where lagc3.configId = :configId " +
            "and lagc.id = lagc3.id) " +
            "and (lagc.faultFamily like %:text% or " +
            "lagc.faultMember like %:text% or " +
            "lagc.faultCode like %:text% or " +
            "lagc.problemDescription like %:text%) " +
            "and lagc.priority in :priorities " +
            "order by lagc.serverTime asc")
    List<LaserAlarmLogUserConfig> findAllActiveAlarmsByConfigIdAndPriorityAndTextAtGivenTime(
            @Param("configId") Long configId, @Param("time") String time, @Param("text") String text, @Param("priorities") List<Integer> priorities);

    /**
     * Fetch active alarms state at given time
     * Filter by configId and priority
     */
    @Query("select lagc from LaserAlarmLogUserConfig lagc " +
            "where lagc.serverTime = (select max(lagc2.serverTime) from LaserAlarmLogUserConfig lagc2 " +
            "where lagc2.serverTime < to_timestamp(:time, 'YYYY-MM-DD-HH24:MI') " +
            "and lagc.id = lagc2.id) " +
            "and lagc.active='Y' " +
            "and exists (select lagc3 from LaserAlarmLogUserConfig lagc3 " +
            "where lagc3.configId = :configId " +
            "and lagc.id = lagc3.id) " +
            "and lagc.priority in :priorities " +
            "order by lagc.serverTime asc")
    List<LaserAlarmLogUserConfig> findAllActiveAlarmsByConfigIdAndPriorityAtGivenTime(
            @Param("configId") Long configId, @Param("time") String time, @Param("priorities") List<Integer> priorities);
}
