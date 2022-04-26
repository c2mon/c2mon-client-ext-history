package cern.c2mon.client.ext.history.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

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

    /*@Column(name = "tagvalue")
    private String value;

    @Column(name = "tagunit")
    private String unit;

    @Column(name = "tagvaluedesc")
    private String valueDescription;

    @Column(name = "tagtimestamp")
    private LocalDateTime timestamp;

    @Column(name = "tagdaqtimestamp")
    private LocalDateTime daqTimestamp;

    @Column(name = "tagsrvtimestamp")
    private LocalDateTime serverTimestamp;

    @Column(name = "tagmode")
    private Integer mode;

    @Column(name = "tagsimulated")
    private Integer simulated;

    @Column(name = "tagmetadata")
    private String metadata;*/
}
