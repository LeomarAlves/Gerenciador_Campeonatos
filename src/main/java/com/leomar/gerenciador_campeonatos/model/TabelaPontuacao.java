package com.leomar.gerenciador_campeonatos.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class TabelaPontuacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome; // Ex: "Padrão", "Pontuação Dobrada (Final)"

    // Cria uma tabela no SQLite apenas para mapear: Posição -> Pontos
    @ElementCollection
    @CollectionTable(name = "pontuacao_posicao", joinColumns = @JoinColumn(name = "tabela_id"))
    @MapKeyColumn(name = "posicao")
    @Column(name = "pontos")
    private Map<Integer, Integer> pontosPorPosicao = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Integer, Integer> getPontosPorPosicao() {
        return pontosPorPosicao;
    }

    public void setPontosPorPosicao(Map<Integer, Integer> pontosPorPosicao) {
        this.pontosPorPosicao = pontosPorPosicao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
