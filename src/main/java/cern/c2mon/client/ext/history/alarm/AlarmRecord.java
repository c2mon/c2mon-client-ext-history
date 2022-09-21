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


import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "alarm")
public class AlarmRecord {

    @Id
    @Column(name = "alarmid")
    private Long id;

    @Column(name = "alarm_tagid")
    private Long tagId;

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

    @Column(name = "alarminfo")
    private String info;

    @Column(name = "alarmmetadata")
    private String metadata;
}