package cern.c2mon.client.ext.history.laser;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Profile;

@Profile("enableLaser")
@Entity
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LaserAlarmLogUserConfig that = (LaserAlarmLogUserConfig) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(faultFamily, that.faultFamily) &&
                Objects.equals(faultMember, that.faultMember) &&
                Objects.equals(faultCode, that.faultCode) &&
                Objects.equals(priority, that.priority) &&
                Objects.equals(active, that.active) &&
                Objects.equals(serverTime, that.serverTime) &&
                Objects.equals(oscillating, that.oscillating) &&
                Objects.equals(configId, that.configId) &&
                Objects.equals(problemDescription, that.problemDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, faultFamily, faultMember, faultCode, priority, active, serverTime, oscillating, configId, problemDescription);
    }

    @Override
    public String toString() {
        return "LaserAlarmLogUserConfig{" +
                "id=" + id +
                ", faultFamily='" + faultFamily + '\'' +
                ", faultMember='" + faultMember + '\'' +
                ", faultCode='" + faultCode + '\'' +
                ", priority=" + priority +
                ", active=" + active +
                ", serverTime=" + serverTime +
                ", oscillating=" + oscillating +
                ", configId=" + configId +
                ", problemDescription='" + problemDescription + '\'' +
                '}';
    }
}
