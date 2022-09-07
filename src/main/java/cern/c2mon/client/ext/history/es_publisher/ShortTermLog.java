package cern.c2mon.client.ext.history.es_publisher;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Profile;

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
  private LocalDateTime tagTime;

  @Column(name ="tagdaqtime")
  private LocalDateTime tagDaqTime;

  @Column(name = "tagstatusdesc")
  private String tagStatusDesc;

  @Column(name = "tagname")
  private String tagName;

  @Column(name = "tagvaluedesc")
  private String tagValueDesc;

  @Column(name = "tagvalue")
  private String tagValue;
}
