package cern.c2mon.client.ext.history.laser;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Profile;

import lombok.Data;

@Profile("enableLaser")
@Entity
@Data
@Table(name = "alarm")
public class AlarmData {
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
  @Column(name = "ALARMID")
  private int alarmId;        //  INTEGER NOT NULL PRIMARY KEY,

  @Column(name = "ALARM_TAGID")
  private Integer alarmTagId;

  @Column(name = "ALARMPRIORITY")
  private Integer alarmPriority; //    INTEGER,

  @Column(name = "ALARMFFAMILY")
  private String alarmFamily; //ALARMFFAMILY     VARCHAR(64) NOT NULL,

  @Column(name = "ALARMFMEMBER")
  private String alarmFMember; //ALARMFMEMBER     VARCHAR(64) NOT NULL,

  @Column(name = "ALARMFCODE")
  private Integer alarmFCode; //ALARMFCODE       INTEGER NOT NULL,

  @Column(name = "ALARMSTATE")
  private String alarmState;//      VARCHAR(10),

  @Column(name = "ALARMTIME")
  private LocalDateTime alarmTime;//        TIMESTAMP(6),

  @Column(name = "ALARMSOURCETIME")
  private LocalDateTime alarmSourceTime;// TIMESTAMP(6),

  @Column(name = "ALARMINFO")
  private String alarmInfo; //ALARMINFO        VARCHAR(100),

  @Column(name = "ALARMCONDITION")
  private String alarmCondition;//   VARCHAR(500),

  @Column(name = "ALARMMETADATA")
  private String alarmMetadata; //ALARMMETADATA    VARCHAR(4000),

  @Column(name = "ALARMOSCILLATION")
  private Integer alarmOscillation;// INTEGER
}
