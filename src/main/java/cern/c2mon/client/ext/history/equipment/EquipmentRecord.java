package cern.c2mon.client.ext.history.equipment;

import java.util.Set;

import cern.c2mon.client.ext.history.process.Process;
import cern.c2mon.client.ext.history.supervision.ServerSupervisionEvent;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "equipment")
public class EquipmentRecord {

    @Id
    @Column(name = "eqid")
    private Long id;

    @Column(name = "eqname")
    private String name;

    @Column(name = "eqdesc")
    private String description;

    @Column(name = "eq_procid")
    private Long processId;

    @Column(name = "eqstate_tagid")
    private Integer stateTagId;

    @OneToOne(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
    @JoinColumn(name = "eq_procid", referencedColumnName = "procid", insertable = false, updatable = false)
    private Process processData;

    /*
    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ServerSupervisionEvent> supervisionEvent;

    @Column(name = "eqaddress")
    private String address;

    @Column(name = "eqalive_tagid")
    private Long aliveTagId;

    @Column(name = "eqcommfault_tagid")
    private Long commFaultTagId;


    @Column(name = "eqstate")
    private String state;

    @Column(name = "eqstatustime")
    private LocalDateTime statusTime;

    @Column(name = "eqstatusdesc")
    private String statusdesc;*/

}
