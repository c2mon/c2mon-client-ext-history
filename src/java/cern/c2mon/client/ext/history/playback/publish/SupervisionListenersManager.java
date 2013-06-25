package cern.c2mon.client.ext.history.playback.publish;

import cern.c2mon.client.ext.history.util.KeyForValuesMap;
import cern.c2mon.client.jms.SupervisionListener;

/**
 * This class keeps lists of which listeners is listening on which
 * supervision-id.
 * 
 * @author vdeila
 * 
 */
public class SupervisionListenersManager extends KeyForValuesMap<Long, SupervisionListener> {

  /**
   * Constructor
   */
  public SupervisionListenersManager() {
    super();
  }

}
