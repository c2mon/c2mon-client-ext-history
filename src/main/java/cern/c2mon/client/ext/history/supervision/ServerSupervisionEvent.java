package cern.c2mon.client.ext.history.supervision;

import cern.c2mon.client.ext.history.equipment.EquipmentRecord;
import cern.c2mon.client.ext.history.process.Process;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author Manuel Bouzas Reguera
 */
@Entity
@Table(name = "SUPERVISION_LOG",indexes = {@Index(name="SUL_ID_IDX",columnList = "SUL_ID"), @Index(name = "SUL_DATE_IDX", columnList="SUL_DATE")})
@Data
public class ServerSupervisionEvent {

    @Column(name = "SUL_ID")
    private Long id;


    @Column(name = "SUL_ENTITY")
    private String sul_entity;

    @Id
    @Column(name = "SUL_DATE")
    private LocalDateTime eventTime;

    @Column(name = "SUL_DATE", insertable = false, updatable = false)
    private Instant eventInstant;

    @Column(name = "SUL_STATUS")
    private String status;


    @Column(name = "SUL_MESSAGE")
    private String sul_message;

    @ManyToOne
    @JoinColumn(name = "sul_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private EquipmentRecord equipmentRecord;

    @ManyToOne
    @JoinColumn(name = "sul_id", insertable = false, updatable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private Process process;
}
