package cern.c2mon.client.ext.history.es_publisher.entity.sub;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import cern.c2mon.client.ext.history.supervision.ServerSupervisionEvent;
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

  @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  //@LazyCollection(LazyCollectionOption.FALSE)
  private Set<ServerSupervisionEvent> supervisionEvent;
}
