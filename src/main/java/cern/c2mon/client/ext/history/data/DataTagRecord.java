package cern.c2mon.client.ext.history.data;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import cern.c2mon.client.ext.history.alarm.AlarmRecord;
import cern.c2mon.client.ext.history.equipment.EquipmentRecord;
import cern.c2mon.client.ext.history.data.utilities.MapConverter;
import cern.c2mon.client.ext.history.process.Process;
import cern.c2mon.shared.client.configuration.api.equipment.Equipment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Data
@NoArgsConstructor
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

    @Column(name = "tagmode")
    private Integer tagMode;//TAGMODE             INTEGER NOT NULL,

    @Column(name = "tagdatatype")
    private Class<?> tagDataType;//TAGDATATYPE         VARCHAR(200) NOT NULL,

    @Column(name = "tagvalue")
    private String tagValue;

    @Column(name = "tagvaluedesc")
    private String tagValueDesc;

    @Column(name = "tagtimestamp")
    private Instant tagTimeStamp;//TAGTIMESTAMP        TIMESTAMP(6),

    @Column(name = "tagdaqtimestamp")
    private Instant tagDaqTimestamp;

    @Column(name = "tagsrvtimestamp")
    private Instant tagServerTimestamp;

    @Column(name = "tagmetadata")
    @Convert(converter = MapConverter.class)
    private Map<String, Object> tagMetaData;//TAGMETADATA         VARCHAR(4000),

    @Column(name = "tagqualitycode")
    private Integer tagQualityCode;

    @Column(name = "tagQualityDesc")
    private String tagQualityDesc;

    @Column(name = "tagrule")
    private String tagRule;

    @Column(name = "tagruleids")
    private String tagRuleIds;

    @Column(name = "tagunit")
    private String tagUnit;//TAGUNIT             VARCHAR(50),

    @Column(name = "tagSimulated")
    private Boolean tagSimulated;

    @Column(name = "taglogged")
    private Boolean tagLogged;//TAGLOGGED           INTEGER,

    @OneToMany(mappedBy = "tagId", fetch= FetchType.EAGER)
    private List<AlarmRecord> alarmList;

    @OneToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "tag_eqid", referencedColumnName = "eqid")
    private EquipmentRecord equipment;


}
