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
package cern.c2mon.client.ext.history.alarm;


import cern.c2mon.client.ext.history.es_publisher.entity.sub.MapToStringConverter;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Data
@Table(name = "alarm")
public class AlarmRecord {

    @Id
    @Column(name = "alarmid")
    private Long id;

    @Column(name = "alarm_tagid")
    private Long tagId;

    @Column(name = "alarmpriority")
    private Integer alarmPriority;

    @Column(name = "alarmffamily")
    private String faultFamily;

    @Column(name = "alarmfmember")
    private String faultMember;

    @Column(name = "alarmfcode")
    private Integer faultCode;

    @Column(name = "alarmstate")
    @Type(type="yes_no")
    private Boolean active;

    @Column(name ="alarmtime")
    private LocalDateTime timestamp;

    @Column(name ="alarmsourcetime")
    private LocalDateTime sourceTimestamp;

    @Column(name ="alarmtime", insertable = false, updatable = false)
    private Instant instantTimestamp;

    @Column(name ="alarmsourcetime", insertable = false, updatable = false)
    private Instant instantSourceTimestamp;

    @Column(name = "alarminfo")
    private String info;

    @Column(name = "alarmmetadata")
    private String metadata;


    @Column(name = "alarmmetadata", insertable = false, updatable = false)
    @Convert(converter = MapToStringConverter.class)
    private Map<String, String> metadataMap;//TAGMETADATA         VARCHAR(4000),

    @Column(name = "alarmoscillation")
    private Integer alarmOscillation;
}
