package com.leomar.gerenciador_campeonatos.model;

import jakarta.persistence.*;

@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // Muitas Categorias usam Uma Tabela de Pontuação
    @ManyToOne
    @JoinColumn(name = "tabela_pontuacao_id")
    private TabelaPontuacao tabelaPontuacao;

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

    public TabelaPontuacao getTabelaPontuacao() {
        return tabelaPontuacao;
    }

    public void setTabelaPontuacao(TabelaPontuacao tabelaPontuacao) {
        this.tabelaPontuacao = tabelaPontuacao;
    }
}
