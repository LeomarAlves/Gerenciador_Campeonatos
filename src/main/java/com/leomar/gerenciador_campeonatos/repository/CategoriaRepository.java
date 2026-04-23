package com.leomar.gerenciador_campeonatos.repository;

import com.leomar.gerenciador_campeonatos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Busca categorias associadas a um campeonato
    List<Categoria> findByCampeonatoId(Long campeonatoId);
}