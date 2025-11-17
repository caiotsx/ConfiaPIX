package com.confiapix.validador_pix.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.confiapix.validador_pix.Model.ComprovantePix;
import com.confiapix.validador_pix.Service.ComprovantePixService;
import com.confiapix.validador_pix.Service.OcrService;

@RestController
@RequestMapping("/api/comprovantes")
public class ComprovantePixController {

    @Autowired
    private ComprovantePixService comprovantePixService;

    @Autowired
    private OcrService ocrService;

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


    // 🆕 OCR: FAZER UPLOAD E EXTRAIR DADOS AUTOMATICAMENTE
@PostMapping("/validar")
public ResponseEntity<?> uploadComprovante(@RequestParam("arquivo") MultipartFile arquivo) {
    try {
        // 1️⃣ Verificar tipo MIME
        String contentType = arquivo.getContentType();
        if (contentType == null) {
            return ResponseEntity.badRequest().body("Tipo de arquivo desconhecido!");
        }

        // 2️⃣ Criar arquivo temporário com extensão original
        String extensao = getFileExtension(arquivo.getOriginalFilename());
        File tempFile = File.createTempFile("comprovante-", "." + extensao);
        arquivo.transferTo(tempFile);

        // 3️⃣ Processar pelo OCR (imagem ou PDF)
        String textoExtraido;
        if (contentType.equals("application/pdf") || extensao.equalsIgnoreCase("pdf")) {
            textoExtraido = ocrService.extrairTextoDePdf(tempFile);
        } else if (contentType.startsWith("image/")
                || List.of("jpg", "jpeg", "png").contains(extensao.toLowerCase())) {

            if (javax.imageio.ImageIO.read(tempFile) == null) {
                return ResponseEntity.badRequest()
                        .body("Não foi possível ler a imagem. Verifique se o arquivo está corrompido.");
            }

            textoExtraido = ocrService.extrairTexto(tempFile);
        } else {
            return ResponseEntity.badRequest().body("Formato não suportado. Envie uma imagem (JPG/PNG) ou PDF.");
        }

        // 4️⃣ Extrair campos do texto OCR via BancoDetectorService
        Map<String, String> dadosExtraidos = ocrService.extrairCampos(textoExtraido);

        // 5️⃣ Validar os campos extraídos no banco da instituição
        Map<String, Object> resultadoValidacao = comprovantePixService.validar(dadosExtraidos);

        // 6️⃣ Deletar arquivo temporário
        tempFile.delete();

        // 7️⃣ Resposta final
        return ResponseEntity.ok(Map.of(
                "mensagem", "OCR processado com sucesso!",
                "textoExtraido", textoExtraido,
                "dadosExtraidos", dadosExtraidos,
                "resultadoValidacao", resultadoValidacao
        ));

    } catch (IOException e) {
        return ResponseEntity.internalServerError().body("Erro ao processar arquivo: " + e.getMessage());
    }
}

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains("."))
            return "png";
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}