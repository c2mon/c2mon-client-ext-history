package cern.c2mon.client.ext.history.process;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "process")
public class Process {

    @Id
    @Column(name = "procid")
    private Long id;

    @Column(name = "procname")
    private String name;

    @Column(name = "procstate_tagid")
    private Integer stateTagId;
}
