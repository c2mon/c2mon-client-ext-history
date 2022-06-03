package cern.c2mon.client.ext.history.laser.repo;

import cern.c2mon.client.ext.history.laser.LaserAlarmDefinition;

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
public interface LaserAlarmDefinitionRepoService extends JpaRepository<LaserAlarmDefinition, Long> {

    @Query("select lad from LaserAlarmDefinition lad " +
            "where lad.configId = :configId " +
            "and (lad.faultFamily like %:text% or " +
            "lad.faultMember like %:text% or " +
            "lad.faultCode like %:text% or " +
            "lad.problemDescription like %:text%) " +
            "and lad.priority in :priorities ")
    List<LaserAlarmDefinition> findAllByConfigIdAndPriorityAndText(
            @Param("configId") Long configId, @Param("text") String text, @Param("priorities") List<Integer> priorities);

    @Query("select lad from LaserAlarmDefinition lad " +
            "where lad.configId = :configId " +
            "and lad.priority in :priorities ")
    List<LaserAlarmDefinition> findAllByConfigIdAndPriority(
            @Param("configId") Long configId, @Param("priorities") List<Integer> priorities);

    @Query("select lad from LaserAlarmDefinition lad " +
            "where lad.configId = :configId " +
            "and (lad.faultFamily like %:text% or " +
            "lad.faultMember like %:text% or " +
            "lad.faultCode like %:text% or " +
            "lad.problemDescription like %:text%) " +
            "and lad.priority in :priorities ")
    Page<LaserAlarmDefinition> findAllByConfigIdAndPriorityAndText(
            @Param("configId") Long configId, @Param("text") String text, @Param("priorities") List<Integer> priorities, Pageable pageable);

    @Query("select lad from LaserAlarmDefinition lad " +
            "where lad.configId = :configId " +
            "and lad.priority in :priorities ")
    Page<LaserAlarmDefinition> findAllByConfigIdAndPriority(
            @Param("configId") Long configId, @Param("priorities") List<Integer> priorities, Pageable pageable);

}