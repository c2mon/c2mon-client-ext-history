package cern.c2mon.client.ext.history.laser.repo;

import cern.c2mon.client.ext.history.laser.LaserAlarmLogUserConfig;

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
public interface LaserAlarmEventRepoService extends JpaRepository<LaserAlarmLogUserConfig, Long> {

    @Query("select lagc from LaserAlarmLogUserConfig lagc " +
            "where lagc.configId = :configId " +
            "and lagc.serverTime between to_timestamp(:startTime, 'YYYY-MM-DD-HH24:MI') and to_timestamp(:endTime, 'YYYY-MM-DD-HH24:MI') " +
            "and (lagc.faultFamily like %:text% or " +
            "lagc.faultMember like %:text% or " +
            "lagc.faultCode like %:text% or " +
            "lagc.problemDescription like %:text%) " +
            "and lagc.priority in :priorities " +
            "order by lagc.serverTime asc")
    List<LaserAlarmLogUserConfig> findAllAlarmsByConfigIdAndPriorityAndTextBetweenDates(
            @Param("configId") Long configId, @Param("startTime") String startTime,
            @Param("endTime") String endTime, @Param("text") String text, @Param("priorities") List<Integer> priorities);


    @Query("select lagc from LaserAlarmLogUserConfig lagc " +
            "where lagc.configId = :configId " +
            "and lagc.serverTime between to_timestamp(:startTime, 'YYYY-MM-DD-HH24:MI') and to_timestamp(:endTime, 'YYYY-MM-DD-HH24:MI') " +
            "and lagc.priority in :priorities " +
            "order by lagc.serverTime asc")
    List<LaserAlarmLogUserConfig> findAllAlarmsByConfigIdAndPriorityBetweenDates(
            @Param("configId") Long configId, @Param("startTime") String startTime,
            @Param("endTime") String endTime, @Param("priorities") List<Integer> priorities);


    @Query("select lagc from LaserAlarmLogUserConfig lagc " +
            "where lagc.configId = :configId " +
            "and lagc.serverTime between to_timestamp(:startTime, 'YYYY-MM-DD-HH24:MI') and to_timestamp(:endTime, 'YYYY-MM-DD-HH24:MI') " +
            "and (lagc.faultFamily like %:text% or " +
            "lagc.faultMember like %:text% or " +
            "lagc.faultCode like %:text% or " +
            "lagc.problemDescription like %:text%) " +
            "and lagc.priority in :priorities " +
            "order by lagc.serverTime asc")
    Page<LaserAlarmLogUserConfig> findAllAlarmsByConfigIdAndPriorityAndTextBetweenDates(
            @Param("configId") Long configId, @Param("startTime") String startTime,
            @Param("endTime") String endTime, @Param("text") String text, @Param("priorities") List<Integer> priorities,
            Pageable pageable);


    @Query("select lagc from LaserAlarmLogUserConfig lagc " +
            "where lagc.configId = :configId " +
            "and lagc.serverTime between to_timestamp(:startTime, 'YYYY-MM-DD-HH24:MI') and to_timestamp(:endTime, 'YYYY-MM-DD-HH24:MI') " +
            "and lagc.priority in :priorities " +
            "order by lagc.serverTime asc")
    Page<LaserAlarmLogUserConfig> findAllAlarmsByConfigIdAndPriorityBetweenDates(
            @Param("configId") Long configId, @Param("startTime") String startTime,
            @Param("endTime") String endTime, @Param("priorities") List<Integer> priorities, Pageable pageable);


    /**
     * Fetch active alarms state at given time
     * Filter by configId, text search and priority
     */
    @Query("select lagc from LaserAlarmLogUserConfig lagc " +
            "where lagc.serverTime = (" +
            "select max(lagc2.serverTime) from LaserAlarmLogUserConfig lagc2 " +
            "where lagc2.serverTime < to_timestamp(:time, 'YYYY-MM-DD-HH24:MI') " +
            "and lagc2.configId = :configId " +
            "and lagc2.priority in :priorities " +
            "and (lagc.faultFamily like %:text% or " +
            "lagc.faultMember like %:text% or " +
            "lagc.faultCode like %:text% or " +
            "lagc.problemDescription like %:text%) " +
            "and lagc.id = lagc2.id) " +
            "and lagc.active='Y' " +
            "order by lagc.serverTime asc")
    List<LaserAlarmLogUserConfig> findAllActiveAlarmsByConfigIdAndPriorityAndTextAtGivenTime(
            @Param("configId") Long configId, @Param("time") String time, @Param("text") String text,
            @Param("priorities") List<Integer> priorities);

    /**
     * Fetch active alarms state at given time
     * Filter by configId and priority
     */
    @Query("select lagc from LaserAlarmLogUserConfig lagc " +
            "where lagc.serverTime = (" +
            "select max(lagc2.serverTime) from LaserAlarmLogUserConfig lagc2 " +
            "where lagc2.serverTime < to_timestamp(:time, 'YYYY-MM-DD-HH24:MI') " +
            "and lagc2.configId = :configId " +
            "and lagc2.priority in :priorities " +
            "and lagc.id = lagc2.id) " +
            "and lagc.active='Y' " +
            "order by lagc.serverTime asc")
    List<LaserAlarmLogUserConfig> findAllActiveAlarmsByConfigIdAndPriorityAtGivenTime(
            @Param("configId") Long configId, @Param("time") String time, @Param("priorities") List<Integer> priorities);

    /**
     * Fetch active alarms state at given time
     * Filter by configId, text search and priority
     */
    @Query("select lagc from LaserAlarmLogUserConfig lagc " +
            "where lagc.serverTime = (" +
            "select max(lagc2.serverTime) from LaserAlarmLogUserConfig lagc2 " +
            "where lagc2.serverTime < to_timestamp(:time, 'YYYY-MM-DD-HH24:MI') " +
            "and lagc2.configId = :configId " +
            "and lagc2.priority in :priorities " +
            "and (lagc.faultFamily like %:text% or " +
            "lagc.faultMember like %:text% or " +
            "lagc.faultCode like %:text% or " +
            "lagc.problemDescription like %:text%) " +
            "and lagc.id = lagc2.id) " +
            "and lagc.active='Y' " +
            "order by lagc.serverTime asc")
    Page<LaserAlarmLogUserConfig> findAllActiveAlarmsByConfigIdAndPriorityAndTextAtGivenTime(
            @Param("configId") Long configId, @Param("time") String time, @Param("text") String text,
            @Param("priorities") List<Integer> priorities, Pageable pageable);

    /**
     * Fetch active alarms state at given time
     * Filter by configId and priority
     */
    @Query("select lagc from LaserAlarmLogUserConfig lagc " +
            "where lagc.serverTime = (" +
            "select max(lagc2.serverTime) from LaserAlarmLogUserConfig lagc2 " +
            "where lagc2.serverTime < to_timestamp(:time, 'YYYY-MM-DD-HH24:MI') " +
            "and lagc2.configId = :configId " +
            "and lagc2.priority in :priorities " +
            "and lagc.id = lagc2.id) " +
            "and lagc.active='Y' " +
            "order by lagc.serverTime asc")
    Page<LaserAlarmLogUserConfig> findAllActiveAlarmsByConfigIdAndPriorityAtGivenTime(
            @Param("configId") Long configId, @Param("time") String time, @Param("priorities") List<Integer> priorities,
            Pageable pageable);
}
