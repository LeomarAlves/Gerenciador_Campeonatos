package com.leomar.gerenciador_campeonatos.controller;

import com.leomar.gerenciador_campeonatos.model.ResultadoBateria;
import com.leomar.gerenciador_campeonatos.repository.ResultadoBateriaRepository;
import com.leomar.gerenciador_campeonatos.service.PontuacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resultados")
@CrossOrigin(origins = "*")
public class ResultadoBateriaController {

    private final ResultadoBateriaRepository resultadoRepository;
    private final PontuacaoService pontuacaoService;

    public ResultadoBateriaController(ResultadoBateriaRepository resultadoRepository, PontuacaoService pontuacaoService) {
        this.resultadoRepository = resultadoRepository;
        this.pontuacaoService = pontuacaoService;
    }

    @GetMapping
    public List<ResultadoBateria> listarTodos() {
        return resultadoRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<ResultadoBateria> registrarResultado(@RequestBody ResultadoBateria novoResultado) {

        ResultadoBateria existente = resultadoRepository.findByBateriaIdAndPilotoId(
                novoResultado.getBateria().getId(),
                novoResultado.getPiloto().getId()
        );

        if (existente != null) {
            // Atualiza os dados do registro existente para evitar duplicatas
            existente.setPosicaoChegada(novoResultado.getPosicaoChegada());
            existente.setNc(novoResultado.isNc());
            existente.setPolePosition(novoResultado.isPolePosition());

            existente.setPontosExtras(novoResultado.getPontosExtras());

            ResultadoBateria atualizado = resultadoRepository.save(existente);
            return ResponseEntity.ok(atualizado);
        }

        // Se não existe, cria um novo
        ResultadoBateria salvo = resultadoRepository.save(novoResultado);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PostMapping("/calcular/{bateriaId}")
    public ResponseEntity<String> calcularPontosDaBateria(
            @PathVariable Long bateriaId,
            @RequestParam Long tabelaId) {

        pontuacaoService.calcularPontosDaBateria(bateriaId, tabelaId);
        return ResponseEntity.ok("Pontos calculados com sucesso!");
    }
}