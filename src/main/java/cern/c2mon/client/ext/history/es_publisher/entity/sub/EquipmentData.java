package cern.c2mon.client.ext.history.es_publisher.entity.sub;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "equipment")
public class EquipmentData {
  @Id
  @Column(name = "eqid")
  private Long equipmentId;

  @Column(name = "eqname")
  private String equipmentName;

  @OneToOne(cascade = CascadeType.ALL, fetch= FetchType.EAGER)
  @JoinColumn(name = "eq_procid", referencedColumnName = "procid")
  private ProcessData processData;
}
