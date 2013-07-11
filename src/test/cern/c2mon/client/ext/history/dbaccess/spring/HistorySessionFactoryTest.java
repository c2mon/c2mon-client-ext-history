package cern.c2mon.client.ext.history.dbaccess.spring;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cern.c2mon.client.ext.history.dbaccess.HistorySessionFactory;

/**
 * Tests for the {@link HistorySessionFactoryTest} class
 * 
 * @author ekoufaki
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:cern/c2mon/client/ext/history/dbaccess/spring/config/spring-history-test.xml" })
public class HistorySessionFactoryTest {
  
  @Autowired
  HistorySessionFactory f;

  @Test
  public void testSessionFactory() {

    assertTrue(f.getHistoryMapper() != null);
    assertTrue(f.getSavedHistoryMapper() != null);
    assertTrue(f.getSavedHistoryEventsMapper() != null);
  }
}
