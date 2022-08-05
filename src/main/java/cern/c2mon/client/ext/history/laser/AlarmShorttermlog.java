/******************************************************************************
 * Copyright (C) 2010-2016 CERN. All rights not expressly granted are reserved.
 *
 * This file is part of the CERN Control and Monitoring Platform 'C2MON'.
 * C2MON is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the license.
 *
 * C2MON is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with C2MON. If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************/
package cern.c2mon.client.ext.history.laser;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;

@Profile("enableLaser")
@Entity
@Data
@Table(name = "c2mon_alarm_shorttermlog_v")

/*-
 * create view c2mon_alarm_shorttermlog_v as
select
    LOGDATE, 
    TAGID    ,     
    TAGNAME   ,  
    TAGVALUE   , 
    TAGVALUEDESC, 
    TAGDATATYPE  ,  
    TAGTIME       ,   
    TAGSERVERTIME ,   
    TAGDAQTIME     ,   
    TAGSTATUS       ,     
    TAGSTATUSDESC    , 
    TAGMODE,     
    TAGDIR,
    a.alarmmetadata
from 
    shorttermlog st,
    alarm a
where
    st.tagid = a.alarm_tagid;

 */
public class AlarmShorttermlog {
    

  @Id
  @Column(name = "tagservertime")
  private LocalDateTime tagServerTime;

  @Column(name = "tagid")
  private Long id;

  @Column(name = "tagname")
  private String tagName;

  @Column(name = "tagvalue")
  private String tagValue;

  @Column(name = "tagvaluedesc")
  private String tagValueDesc;

  @Column(name = "tagdatatype")
  private String tagDatatype;

  @Column(name ="tagtime")
  private LocalDateTime tagTime;

  @Column(name ="tagdaqtime")
  private LocalDateTime tagDaqTime;

  @Column(name ="alarmmetadata")
  private String alarmMetaData;
}
