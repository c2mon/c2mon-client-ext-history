package cern.c2mon.client.ext.history.es_publisher.entity.sub;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cern.c2mon.client.ext.history.alarm.AlarmRecord;
import lombok.Data;

public class AlarmData  {
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

  private LocalDateTime timestamp;

  private LocalDateTime sourceTimestamp;

}
