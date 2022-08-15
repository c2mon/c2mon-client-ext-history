package cern.c2mon.client.ext.history.laser.repo;

import cern.c2mon.client.ext.history.laser.LaserUserConfig;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Profile("enableLaser")
@Service
public interface LaserUserConfigRepoService extends JpaRepository<LaserUserConfig, Long> {

    @Query("select luc from LaserUserConfig luc where luc.configName = :configName")
    List<LaserUserConfig> findAllByConfigName(@Param("configName") String configName);

    Optional<LaserUserConfig> findByConfigName(String configName);

    @Query("select luc from LaserUserConfig luc order by upper(luc.configName)")
    List<LaserUserConfig> findAllByOrderByConfigName();
}
