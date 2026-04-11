package com.leomar.gerenciador_campeonatos.service;

import com.leomar.gerenciador_campeonatos.model.*;
import com.leomar.gerenciador_campeonatos.repository.ResultadoBateriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PontuacaoService {

    private final ResultadoBateriaRepository resultadoRepository;

    // Injeção de dependência via construtor (Boa prática do Spring)
    public PontuacaoService(ResultadoBateriaRepository resultadoRepository) {
        this.resultadoRepository = resultadoRepository;
    }

    public void processarGridMisto(Long bateriaId) {
        // 1. Busca todos os resultados da corrida já ordenados pela posição geral na pista
        List<ResultadoBateria> gridGeral = resultadoRepository
                .findByBateriaIdOrderByPosicaoChegadaAsc(bateriaId);

        // 2. Agrupa os resultados em um Map, separando-os por Categoria
        Map<Categoria, List<ResultadoBateria>> resultadosPorCategoria = gridGeral.stream()
                .collect(Collectors.groupingBy(resultado -> resultado.getPiloto().getCategoria()));

        // O próximo passo acontece aqui!
    }
}
