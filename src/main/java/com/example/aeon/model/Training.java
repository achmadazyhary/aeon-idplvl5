package com.example.aeon.model;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "training")
@Where(clause = "deleted_date is null")
public class Training extends AbstractDate implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tema", length = 90)
    private String tema;

    @Column(name = "nama_pengajar", length = 45)
    private String nama_pengajar;

    @OneToOne(mappedBy = "training")
    private KaryawanTraining karyawanTraining;

    @OneToMany(mappedBy = "training")
    List<KaryawanTraining> karyawanTrainings;


}
