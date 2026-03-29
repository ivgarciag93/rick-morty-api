package org.ivione93.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "character")
public class Character extends PanacheEntityBase {

  @Id
  public Integer id;
  public String name;
  public String status;
  public String specie;
  public String gender;
  public String image;

}
