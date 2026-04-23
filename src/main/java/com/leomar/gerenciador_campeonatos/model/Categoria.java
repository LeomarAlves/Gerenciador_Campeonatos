package com.leomar.gerenciador_campeonatos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    // Relacionamento com o campeonato ao qual a categoria pertence
    @ManyToOne
    @JoinColumn(name = "campeonato_id", nullable = false)
    private Campeonato campeonato;

    // Associação com o GrupoGrid para agrupamento de categorias em pista
    @ManyToOne
    @JoinColumn(name = "grupo_grid_id")
    @JsonIgnore // Evita recursividade no mapeamento JSON
    private GrupoGrid grupoGrid;

    // Regra de pontuação vinculada à categoria
    @ManyToOne
    @JoinColumn(name = "tabela_pontuacao_id")
    private TabelaPontuacao tabelaPontuacao;

    @JsonIgnore
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Piloto> pilotos;

    // ==========================================
    // GETTERS E SETTERS OBRIGATÓRIOS
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

    public Campeonato getCampeonato() {
        return campeonato;
    }

    public void setCampeonato(Campeonato campeonato) {
        this.campeonato = campeonato;
    }

    public GrupoGrid getGrupoGrid() {
        return grupoGrid;
    }

    public void setGrupoGrid(GrupoGrid grupoGrid) {
        this.grupoGrid = grupoGrid;
    }

    public TabelaPontuacao getTabelaPontuacao() {
        return tabelaPontuacao;
    }

    public void setTabelaPontuacao(TabelaPontuacao tabelaPontuacao) {
        this.tabelaPontuacao = tabelaPontuacao;
    }

    public List<Piloto> getPilotos() {
        return pilotos;
    }

    public void setPilotos(List<Piloto> pilotos) {
        this.pilotos = pilotos;
    }
}