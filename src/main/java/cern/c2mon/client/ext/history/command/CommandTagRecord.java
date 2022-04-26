package cern.c2mon.client.ext.history.command;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "commandtag")
public class CommandTagRecord {

    @Id
    @Column(name = "cmdid")
    private Long id;

    @Column(name = "cmdname")
    private String name;

    @Column(name = "cmddesc")
    private String description;
}
