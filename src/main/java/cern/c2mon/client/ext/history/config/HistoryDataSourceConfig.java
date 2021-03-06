/******************************************************************************
 * Copyright (C) 2010-2019 CERN. All rights not expressly granted are reserved.
 * <p/>
 * This file is part of the CERN Control and Monitoring Platform 'C2MON'.
 * C2MON is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the license.
 * <p/>
 * C2MON is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with C2MON. If not, see <http://www.gnu.org/licenses/>.
 *****************************************************************************/
package cern.c2mon.client.ext.history.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;

/**
 * @author Justin Lewis Salmon
 */
@Configuration
@Slf4j
public class HistoryDataSourceConfig {

  @Bean
  @Primary
  @ConfigurationProperties("c2mon.client.history.jdbc")
  public DataSourceProperties historyDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean
  @Primary
  @Profile("!test")
  public DataSource historyDataSource(@Autowired DataSourceProperties historyDataSourceProperties) {
	log.debug("Preparing client history JDBC datasource");
    return historyDataSourceProperties.initializeDataSourceBuilder().build();
  }
}
