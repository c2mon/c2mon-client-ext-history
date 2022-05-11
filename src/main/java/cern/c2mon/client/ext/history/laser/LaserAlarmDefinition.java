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
@Table(name = "lsr_alarm_definition_v")
public class LaserAlarmDefinition {

    @Id
    @Column(name = "alarm_id")
    private Long alarmId;

    @Column(name = "fault_family")
    private String faultFamily;

    @Column(name = "fault_member")
    private String faultMember;

    @Column(name = "fault_code")
    private String faultCode;

    @Column(name = "system_name")
    private String systemName;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "problem_description")
    private String problemDescription;

    @Column(name = "cause")
    private String cause;

    @Column(name = "action")
    private String action;

    @Column(name = "consequence")
    private String consequence;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "help_url")
    private String helpUrl;

    @Column(name = "responsible_id")
    private Long responsibleId;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name = "itn")
    private String itn;

    @Column(name = "location")
    private String location;

    @Column(name = "enabled")
    @Type(type="yes_no")
    private Boolean enabled;

    @Column(name = "ppm")
    @Type(type="yes_no")
    private Boolean ppm;

    @Column(name = "accelerator")
    private String accelerator;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "config_id")
    private Long configId;
}
