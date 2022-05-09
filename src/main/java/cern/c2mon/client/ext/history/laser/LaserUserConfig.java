package cern.c2mon.client.ext.history.laser;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Profile;

@Profile("enableLaser")
@Entity
@Data
@Table(name = "lsr_user_config_uc_v")
public class LaserUserConfig {

    @Id
    @Column(name = "config_id")
    private Long configId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "config_name")
    private String configName;
}
