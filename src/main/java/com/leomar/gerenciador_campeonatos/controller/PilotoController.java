package com.leomar.gerenciador_campeonatos.controller;

import com.leomar.gerenciador_campeonatos.model.Bateria;
import com.leomar.gerenciador_campeonatos.model.Piloto;
import com.leomar.gerenciador_campeonatos.repository.BateriaRepository;
import com.leomar.gerenciador_campeonatos.repository.PilotoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/pilotos")
@CrossOrigin(origins = "*")
public class PilotoController {

    private final PilotoRepository pilotoRepository;
    private final BateriaRepository bateriaRepository;

    public PilotoController(PilotoRepository pilotoRepository, BateriaRepository bateriaRepository) {
        this.pilotoRepository = pilotoRepository;
        this.bateriaRepository = bateriaRepository;
    }

    // Listagem de todos os pilotos cadastrados
    @GetMapping
    public List<Piloto> listarTodos() {
        return pilotoRepository.findAll();
    }

    // Listagem de pilotos aptos para uma bateria específica (baseado nas categorias da bateria)
    @GetMapping("/bateria/{bateriaId}")
    public List<Piloto> listarPorBateria(@PathVariable Long bateriaId) {
        return bateriaRepository.findById(bateriaId)
                .map(bateria -> pilotoRepository.findByCategoriaIn(bateria.getCategorias()))
                .orElse(Collections.emptyList());
    }

    // Cadastro de um novo piloto
    @PostMapping
    public ResponseEntity<Piloto> criarPiloto(@RequestBody Piloto novoPiloto) {
        Piloto pilotoSalvo = pilotoRepository.save(novoPiloto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pilotoSalvo);
    }

    // Busca detalhada de um piloto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Piloto> buscarPorId(@PathVariable Long id) {
        return pilotoRepository.findById(id)
                .map(piloto -> ResponseEntity.ok().body(piloto))
                .orElse(ResponseEntity.notFound().build());
    }
    // Atualização de dados de um piloto existente
    @PutMapping("/{id}")
    public ResponseEntity<Piloto> atualizarPiloto(@PathVariable Long id, @RequestBody Piloto pilotoAtualizado) {
        return pilotoRepository.findById(id)
                .map(pilotoExistente -> {
                    pilotoExistente.setNome(pilotoAtualizado.getNome());
                    pilotoExistente.setNumeroKart(pilotoAtualizado.getNumeroKart());
                    pilotoExistente.setCategoria(pilotoAtualizado.getCategoria());

                    Piloto pilotoSalvo = pilotoRepository.save(pilotoExistente);
                    return ResponseEntity.ok().body(pilotoSalvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pilotoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}