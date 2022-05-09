package cern.c2mon.client.ext.history.laser.repo;

import cern.c2mon.client.ext.history.laser.LaserUserConfig;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Profile("enableLaser")
@Service
public interface LaserUserConfigRepoService extends JpaRepository<LaserUserConfig, Long> {

    Optional<LaserUserConfig> findByConfigName(String configName);

    List<LaserUserConfig> findAll();
}
