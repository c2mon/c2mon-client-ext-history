package cern.c2mon.client.ext.history.es_publisher;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

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
  private LocalDateTime tagServerTime;

  @Column(name = "tagid")
  private Long id;

  @Column(name = "tagdatatype")
  private Class<?> tagDatatype;

  @Column(name ="tagtime")
  private ZonedDateTime tagTime;

  @Column(name ="tagdaqtime")
  private ZonedDateTime tagDaqTime;

  @Column(name = "tagstatusdesc")
  private String tagStatusDesc;

  @Column(name = "tagname")
  private String tagName;

  @Column(name = "tagvaluedesc")
  private String tagValueDesc;

  @Column(name = "tagvalue")
  private String tagValue;
}
