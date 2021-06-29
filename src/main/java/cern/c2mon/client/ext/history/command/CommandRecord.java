/******************************************************************************
 * Copyright (C) 2010-2021 CERN. All rights not expressly granted are reserved.
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
package cern.c2mon.client.ext.history.command;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity bean for historical command values.
 *
 * @author Tiago Oliveira
 */
@Entity
@IdClass(CommandRecordId.class)
@Data
@Table(name = "commandtaglog")
public class CommandRecord {

    @Id
    @Column(name = "cmdid")
    private Long id;

    @Column(name = "cmdname")
    private String name;

    @Column(name = "cmdmode")
    private Integer mode;

    @Id
    @Column(name = "cmdtime")
    private LocalDateTime time;

    @Column(name = "cmdvalue")
    private String value;

    @Column(name = "cmddatatype")
    private String dataType;

    @Column(name = "cmduser")
    private String user;

    @Column(name = "cmdhost")
    private String host;

    @Column(name = "cmdreporttime")
    private LocalDateTime reportTime;

    @Column(name = "cmdreportstatus")
    private String reportStatus;

    @Column(name = "cmdreportdesc")
    private String reportDesc;

}
