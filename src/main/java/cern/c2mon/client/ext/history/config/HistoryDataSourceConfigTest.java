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

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * @author Justin Lewis Salmon
 */
@Configuration
public class HistoryDataSourceConfigTest {

  @Bean
  @Profile("test")
  @ConfigurationProperties(prefix = "c2mon.client.history.jdbc")
  public DataSource historyDataSource(Environment environment) {

    /**
     * HSQL only allows other JVMs to connect, if data is persisted on disk.<br/>
     * By default C2MON server is only storing data In-Memory.
     * Therefore please change accordingly the following c2mon server properties to the same url:
     * <li>c2mon.server.cachedbaccess.jdbc.url</li>
     * <li>c2mon.server.history.jdbc.url</li>
     */
    String url = "jdbc:hsqldb:hsql://localhost/c2mondb;sql.syntax_ora=true";
    String username = "sa";
    String password = "";

    DataSourceBuilder<?> dataSource = DataSourceBuilder.create().url(url).username(username).password(password);

    if (url.contains("hsql")) {
      dataSource.driverClassName("org.hsqldb.jdbcDriver");
    }
    else if (url.contains("oracle")) {
      dataSource.driverClassName("oracle.jdbc.OracleDriver");
    }
    else if (url.contains("mysql")) {
      dataSource.driverClassName("com.mysql.jdbc.Driver");
    }

    return dataSource.build();
  }
}
