package cern.c2mon.client.ext.history.es_publisher.entity.sub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "process")
public class ProcessData {
  @Id
  @Column(name = "procid")
  private Long ProcessId;

  @Column(name = "procname")
  private String processName;
}
