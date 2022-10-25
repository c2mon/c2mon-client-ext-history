package cern.c2mon.client.ext.history.es_publisher.entity;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "shorttermlog")
public class ShortTermLog {
  @Id
  @Column(name = "tagservertime")
  private Instant tagServerTime;

  @Column(name = "tagid")
  private Long id;

  @Column(name = "tagdatatype")
  private Class<?> tagDatatype;

  @Column(name ="tagtime")
  private Instant tagTime;

  @Column(name ="tagdaqtime")
  private Instant tagDaqTime;

  @Column(name = "tagstatusdesc")
  private String tagStatusDesc;

  @Column(name = "tagname")
  private String tagName;

  @Column(name = "tagvaluedesc")
  private String tagValueDesc;

  @Column(name = "tagvalue")
  private String tagValue;
}
