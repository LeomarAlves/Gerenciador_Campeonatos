package com.leomar.gerenciador_campeonatos.controller;

import com.leomar.gerenciador_campeonatos.dto.ClassificacaoDTO;
import com.leomar.gerenciador_campeonatos.model.Campeonato;
import com.leomar.gerenciador_campeonatos.repository.CampeonatoRepository;
import com.leomar.gerenciador_campeonatos.service.PontuacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campeonatos")
@CrossOrigin(origins = "*")
public class CampeonatoController {

    private final CampeonatoRepository campeonatoRepository;
    private final PontuacaoService pontuacaoService;

    public CampeonatoController(CampeonatoRepository campeonatoRepository, PontuacaoService pontuacaoService) {
        this.campeonatoRepository = campeonatoRepository;
        this.pontuacaoService = pontuacaoService;
    }

    // Listagem de todos os campeonatos cadastrados
    @GetMapping
    public List<Campeonato> listarTodos() {
        return campeonatoRepository.findAll();
    }

    // Cadastro de um novo campeonato
    @PostMapping
    public ResponseEntity<Campeonato> criarCampeonato(@RequestBody Campeonato novoCampeonato) {
        Campeonato salvo = campeonatoRepository.save(novoCampeonato);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    // Endpoint para recuperação da classificação geral (Pódio)
    @GetMapping("/{id}/classificacao")
    public ResponseEntity<List<ClassificacaoDTO>> obterClassificacao(@PathVariable Long id) {
        return ResponseEntity.ok(pontuacaoService.gerarClassificacaoFinal(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        campeonatoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}