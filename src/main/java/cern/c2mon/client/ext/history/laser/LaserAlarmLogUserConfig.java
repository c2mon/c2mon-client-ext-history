package cern.c2mon.client.ext.history.laser;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDateTime;

import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Profile;

@Profile("enableLaser")
@Entity
@Data
@Table(name = "lsr_alarm_log_user_config_v")
public class LaserAlarmLogUserConfig {

    @Id
    @Column(name = "alarmId")
    private Long id;

    @Column(name = "faultfamily")
    private String faultFamily;

    @Column(name = "faultmember")
    private String faultMember;

    @Column(name = "fault_code")
    private String faultCode;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "active")
    @Type(type="yes_no")
    private Boolean active;

    @Column(name ="servertime")
    private LocalDateTime serverTime;

    @Column(name = "oscillating")
    @Type(type="yes_no")
    private Boolean oscillating;

    @Column(name = "config_id")
    private Long configId;

    @Column(name = "problem_description")
    private String problemDescription;


}
