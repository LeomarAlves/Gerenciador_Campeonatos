package com.leomar.gerenciador_campeonatos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Campeonato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // A barreira contra o loop infinito
    @JsonIgnore
    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL)
    private List<Categoria> categorias;

    // A barreira contra o loop infinito
    @JsonIgnore
    @OneToMany(mappedBy = "campeonato", cascade = CascadeType.ALL)
    private List<Bateria> baterias;

    // ==========================================
    // GETTERS E SETTERS (Obrigatórios para o JSON funcionar!)
    // ==========================================

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

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Bateria> getBaterias() {
        return baterias;
    }

    public void setBaterias(List<Bateria> baterias) {
        this.baterias = baterias;
    }
}