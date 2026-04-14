package com.leomar.gerenciador_campeonatos.service;

import com.leomar.gerenciador_campeonatos.dto.ClassificacaoDTO;
import com.leomar.gerenciador_campeonatos.model.Bateria;
import com.leomar.gerenciador_campeonatos.model.Categoria;
import com.leomar.gerenciador_campeonatos.model.Piloto;
import com.leomar.gerenciador_campeonatos.model.ResultadoBateria;
import com.leomar.gerenciador_campeonatos.model.TabelaPontuacao;
import com.leomar.gerenciador_campeonatos.repository.BateriaRepository;
import com.leomar.gerenciador_campeonatos.repository.ResultadoBateriaRepository;
import com.leomar.gerenciador_campeonatos.repository.TabelaPontuacaoRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PontuacaoService {

    private final ResultadoBateriaRepository resultadoRepository;
    private final TabelaPontuacaoRepository tabelaPontuacaoRepository;
    private final BateriaRepository bateriaRepository; // <-- O Repositório novo que faltava!

    // Construtor atualizado com a injeção do BateriaRepository
    public PontuacaoService(ResultadoBateriaRepository resultadoRepository,
                            TabelaPontuacaoRepository tabelaPontuacaoRepository,
                            BateriaRepository bateriaRepository) {
        this.resultadoRepository = resultadoRepository;
        this.tabelaPontuacaoRepository = tabelaPontuacaoRepository;
        this.bateriaRepository = bateriaRepository;
    }

    public void processarGridMisto(Long bateriaId, Long tabelaId) {

        TabelaPontuacao tabela = tabelaPontuacaoRepository.findById(tabelaId)
                .orElseThrow(() -> new RuntimeException("Tabela de Pontuação não encontrada!"));

        List<ResultadoBateria> gridGeral = resultadoRepository.findByBateriaIdOrderByPosicaoChegadaAsc(bateriaId);

        Map<Categoria, List<ResultadoBateria>> resultadosPorCategoria = gridGeral.stream()
                .collect(Collectors.groupingBy(resultado -> resultado.getPiloto().getCategoria()));

        for (Map.Entry<Categoria, List<ResultadoBateria>> grupo : resultadosPorCategoria.entrySet()) {

            List<ResultadoBateria> resultadosDaCategoria = grupo.getValue();

            for (int i = 0; i < resultadosDaCategoria.size(); i++) {
                ResultadoBateria resultado = resultadosDaCategoria.get(i);
                int posicaoNaCategoria = i + 1;

                Integer pontos = tabela.getPontosPorPosicao().get(posicaoNaCategoria);

                if (pontos != null) {
                    resultado.setPontos(pontos);
                } else {
                    resultado.setPontos(0);
                }
            }
        }

        resultadoRepository.saveAll(gridGeral);
    }

    public List<ClassificacaoDTO> gerarClassificacaoFinal(Long campeonatoId) {

        // 1. Busca todas as baterias do campeonato para achar a ÚLTIMA
        List<Bateria> baterias = bateriaRepository.findByCampeonatoId(campeonatoId);
        if (baterias.isEmpty()) return Collections.emptyList();

        // Identifica a última bateria pelo maior ID
        Long ultimaBateriaId = baterias.stream()
                .mapToLong(Bateria::getId)
                .max()
                .orElse(0L);

        // 2. Busca todos os resultados do campeonato
        List<ResultadoBateria> todosResultados = resultadoRepository.findByBateriaCampeonatoId(campeonatoId);

        // 3. Agrupa por Piloto e soma os pontos
        Map<Piloto, Integer> pontosPorPiloto = todosResultados.stream()
                .filter(r -> r.getPontos() != null)
                .collect(Collectors.groupingBy(
                        ResultadoBateria::getPiloto,
                        Collectors.summingInt(ResultadoBateria::getPontos)
                ));

        // 4. Cria a lista de DTOs
        List<ClassificacaoDTO> classificacao = pontosPorPiloto.entrySet().stream()
                .map(entry -> new ClassificacaoDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // 5. ORDENAÇÃO COM DESEMPATE TÉCNICO:
        classificacao.sort((a, b) -> {
            // Primeiro critério: Pontos Totais (Decrescente)
            int comp = b.getTotalPontos().compareTo(a.getTotalPontos());

            if (comp == 0) {
                // Empate detectado! Buscar pontos da última bateria para A e B
                Integer pontosUltimaA = todosResultados.stream()
                        .filter(r -> r.getBateria().getId().equals(ultimaBateriaId) && r.getPiloto().getId().equals(a.getPiloto().getId()))
                        .mapToInt(ResultadoBateria::getPontos).findFirst().orElse(0);

                Integer pontosUltimaB = todosResultados.stream()
                        .filter(r -> r.getBateria().getId().equals(ultimaBateriaId) && r.getPiloto().getId().equals(b.getPiloto().getId()))
                        .mapToInt(ResultadoBateria::getPontos).findFirst().orElse(0);

                // Maior pontuação na última bateria ganha
                return pontosUltimaB.compareTo(pontosUltimaA);
            }
            return comp;
        });

        return classificacao;
    }
}