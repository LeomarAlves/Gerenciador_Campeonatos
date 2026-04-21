package com.leomar.gerenciador_campeonatos.controller;

import com.leomar.gerenciador_campeonatos.dto.ClassificacaoDTO;
import com.leomar.gerenciador_campeonatos.service.PontuacaoService;
import com.leomar.gerenciador_campeonatos.service.RelatorioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/relatorios")
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final PontuacaoService pontuacaoService;

    public RelatorioController(RelatorioService relatorioService, PontuacaoService pontuacaoService) {
        this.relatorioService = relatorioService;
        this.pontuacaoService = pontuacaoService;
    }

    // 1. Rota para gerar a tabela na tela
    @GetMapping("/etapa")
    public ResponseEntity<Map<String, List<ClassificacaoDTO>>> gerarRelatorioEtapa(
            @RequestParam List<Long> bateriasIds) {

        if (bateriasIds == null || bateriasIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Map<String, List<ClassificacaoDTO>> resultado = pontuacaoService.gerarRelatorioFinalSelecionado(bateriasIds);
        return ResponseEntity.ok(resultado);
    }

    // 2. Rota para gerar o PDF Oficial a partir dos dados ordenados no Frontend
    @PostMapping("/etapa/pdf")
    public ResponseEntity<byte[]> baixarRelatorioPdf(
            @RequestBody Map<String, List<ClassificacaoDTO>> dadosOrdenados,
            @RequestParam String nomeCampeonato) {

        if (dadosOrdenados == null || dadosOrdenados.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        byte[] arquivoPdf = relatorioService.gerarRelatorioFinalPdf(nomeCampeonato, dadosOrdenados);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"classificacao.pdf\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(arquivoPdf);
    }
}