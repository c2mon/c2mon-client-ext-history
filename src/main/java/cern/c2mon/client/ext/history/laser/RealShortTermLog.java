package cern.c2mon.client.ext.history.laser;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Profile;

import lombok.Data;

@Profile("enableLaser")
@Entity
@Data
@Table(name = "shorttermlog")
public class RealShortTermLog {
  @Id
  @Column(name = "tagservertime")
  private LocalDateTime tagServerTime;

  @Column(name = "tagid")
  private Long id;

  @Column(name ="tagtime")
  private LocalDateTime tagTime;

  @Column(name ="tagdaqtime")
  private LocalDateTime tagDaqTime;

  @Column(name = "tagvalue")
  private String tagValue;
}
