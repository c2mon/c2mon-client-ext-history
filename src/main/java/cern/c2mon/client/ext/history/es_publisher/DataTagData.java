package cern.c2mon.client.ext.history.es_publisher;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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

  @Column(name = "tagmode")
  private Integer tagMode;//TAGMODE             INTEGER NOT NULL,

  @Column(name = "tagunit")
  private String tagUnit;//TAGUNIT             VARCHAR(50),

  @Column(name = "tagmetadata")
  @Convert(converter = MapToStringConverter.class)
  private Map<String, String> tagMetaData;//TAGMETADATA         VARCHAR(4000),

  @Column(name = "tagdatatype")
  private String tagDataType;//TAGDATATYPE         VARCHAR(200) NOT NULL,

  @Column(name = "tag_eqid")
  private Integer tagEqId;//TAG_EQID            INTEGER,

  @Column(name = "taglogged")
  private Integer tagLogged;//TAGLOGGED           INTEGER,

  @Column(name = "tagname")
  private String tagName;//TAGNAME             VARCHAR(255) NOT NULL UNIQUE,

  @Column(name = "tagdesc")
  private String tagDesc;//TAGDESC             VARCHAR(100),

  @Column(name = "tagsrvtimestamp")
  private Instant tagServerTimestamp;

  @Column(name = "tagtimestamp")
  private Instant tagTimeStamp;//TAGTIMESTAMP        TIMESTAMP(6),

  @OneToMany(mappedBy = "alarmTagId", cascade = CascadeType.ALL, fetch= FetchType.EAGER)
  //@JoinColumn(name = "ALARM_TAGID")
  private List<AlarmData> alarmList;
}
