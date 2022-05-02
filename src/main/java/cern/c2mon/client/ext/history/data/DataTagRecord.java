package cern.c2mon.client.ext.history.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Data
@Table(name = "datatag")
public class DataTagRecord {

    @Id
    @Column(name = "tagid")
    private Long id;

    @Column(name = "tagname")
    private String name;

    @Column(name = "tagdesc")
    private String description;

    @Column(name = "tagcontroltag")
    @Type(type="yes_no")
    private boolean controlTag;
}
