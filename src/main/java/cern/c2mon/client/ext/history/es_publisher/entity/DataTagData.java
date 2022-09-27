package cern.c2mon.client.ext.history.es_publisher.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

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

import cern.c2mon.client.ext.history.alarm.AlarmRecord;
import cern.c2mon.client.ext.history.es_publisher.entity.sub.AlarmData;
import cern.c2mon.client.ext.history.es_publisher.entity.sub.EquipmentData;
import cern.c2mon.client.ext.history.es_publisher.entity.sub.MapToStringConverter;
import cern.c2mon.client.ext.history.supervision.ServerSupervisionEvent;
import lombok.Data;

@Entity
@Data
@Table(name = "datatag")
public class DataTagData {
  /*
                      "mode": 0,
                    "unit": null,
                    "metadata": {
                        "responsible": "Jon Doe",
                        "building": 200
                    },
                    "c2mon": {
                        "process": "P_daqrest1",
                        "logged": true,
                        "dataType": "java.lang.Double",
                        "equipment": "E_daqrest1"
                    },
                    "alarms": [],
                    "name": "rack/XYZ/temperature",
                    "description": "<no description provided>",
                    "id": 1000004,
                    "timestamp": 1662039161805
   */

  @Id
  @Column(name = "tagId")
  private Long tagId;        //  INTEGER NOT NULL PRIMARY KEY,

  @Column(name = "tagname")
  private String tagName;//TAGNAME             VARCHAR(255) NOT NULL UNIQUE,

  @Column(name = "tagdesc")
  private String tagDesc;//TAGDESC             VARCHAR(100),

  @Column(name = "tagmode")
  private Integer tagMode;//TAGMODE             INTEGER NOT NULL,

  @Column(name = "tagdatatype")
  private Class<?> tagDataType;//TAGDATATYPE         VARCHAR(200) NOT NULL,

  @Column(name = "tagcontroltag")
  private Boolean tagControlTag;

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
  @Convert(converter = MapToStringConverter.class)
  private Map<String, String> tagMetaData;//TAGMETADATA         VARCHAR(4000),

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

  @OneToMany(mappedBy = "tagId", cascade = CascadeType.ALL, fetch= FetchType.EAGER)
  private List<AlarmRecord> alarmList;

  @OneToOne(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
  @JoinColumn(name = "tag_eqid", referencedColumnName = "eqid")
  private EquipmentData equipment;

}
