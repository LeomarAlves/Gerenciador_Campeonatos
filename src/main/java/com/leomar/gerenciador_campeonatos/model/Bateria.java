package com.leomar.gerenciador_campeonatos.model;

import jakarta.persistence.*;

@Entity
public class Bateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; // Ex: "Bateria 1", "Bateria 2"

    // Muitas Baterias pertencem a Um Campeonato
    @ManyToOne
    @JoinColumn(name = "campeonato_id") // Cria a coluna da chave estrangeira no banco
    private Campeonato campeonato;

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

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }
}