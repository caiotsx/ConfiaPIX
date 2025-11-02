package com.confiapix.validador_pix.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.confiapix.validador_pix.Model.ComprovantePix;
import com.confiapix.validador_pix.Service.ComprovantePixService;

@RestController
@RequestMapping("/api/comprovantes")
public class ComprovantePixController {

    @Autowired
    private ComprovantePixService comprovantePixService;

    // ✅ LISTAR TODOS (GET)
    @GetMapping
    public List<ComprovantePix> listarTodos() {
        return comprovantePixService.findAll();
    }

    // ✅ BUSCAR POR ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<ComprovantePix> buscarPorId(@PathVariable Long id) {
        Optional<ComprovantePix> comprovante = comprovantePixService.findById(id);
        return comprovante.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ CRIAR NOVO COMPROVANTE (POST)
    @PostMapping
    public ResponseEntity<ComprovantePix> criarComprovante(@RequestBody ComprovantePix comprovante) {
        ComprovantePix salvo = comprovantePixService.save(comprovante);
        return ResponseEntity.ok(salvo);
    }

    // ✅ ATUALIZAR COMPROVANTE (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<ComprovantePix> atualizarComprovante(
            @PathVariable Long id,
            @RequestBody ComprovantePix comprovanteAtualizado) {

        Optional<ComprovantePix> atualizado = comprovantePixService.update(id, comprovanteAtualizado);
        return atualizado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ✅ DELETAR COMPROVANTE (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarComprovante(@PathVariable Long id) {
        boolean deletado = comprovantePixService.deleteById(id);
        if (deletado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

     // ✅ VALIDAR COMPROVANTE (POST /validar)
    @PostMapping("/validar")
    public ResponseEntity<String> validarComprovante(@RequestBody ComprovantePix comprovante) {
        String resultado = comprovantePixService.validarComprovante(comprovante);
        return ResponseEntity.ok("Status da validação: " + resultado);
    }
}