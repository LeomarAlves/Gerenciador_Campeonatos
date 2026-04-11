package com.leomar.gerenciador_campeonatos.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Campeonato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // Um Campeonato tem Muitas Baterias
    // O 'mappedBy' avisa o JPA que a chave estrangeira está na classe Bateria
    // CascadeType.ALL significa que se apagarmos o campeonato, as baterias somem junto
    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL)
    private List<Bateria> baterias = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Bateria> getBaterias() {
        return baterias;
    }

    public void setBaterias(List<Bateria> baterias) {
        this.baterias = baterias;
    }
}