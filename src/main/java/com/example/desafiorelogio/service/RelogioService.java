package com.example.desafiorelogio.service;

import com.example.desafiorelogio.dto.AtualizarRelogioRequest;
import com.example.desafiorelogio.dto.CriarRelogioRequest;
import com.example.desafiorelogio.dto.PaginaRelogioDTO;
import com.example.desafiorelogio.dto.RelogioDTO;
import com.example.desafiorelogio.entity.Relogio;
import com.example.desafiorelogio.entity.enums.MaterialCaixa;
import com.example.desafiorelogio.entity.enums.TipoMovimento;
import com.example.desafiorelogio.entity.enums.TipoVidro;
import com.example.desafiorelogio.exception.NaoEncontradoException;
import com.example.desafiorelogio.mapper.RelogioMapper;
import com.example.desafiorelogio.repository.RelogioRepository;
import com.example.desafiorelogio.service.enums.OrdenacaoRelogios;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static com.example.desafiorelogio.service.specification.RelogioSpecs.*;

@Service
@RequiredArgsConstructor
public class RelogioService {

    private final RelogioRepository repo;
    private final RelogioMapper mapper;

    public PaginaRelogioDTO listar(
            int pagina,
            int porPagina,
            String busca,
            String marca,
            String tipoMovimento,
            String materialCaixa,
            String tipoVidro,
            Integer resistenciaMin,
            Integer resistenciaMax,
            Long precoMin,
            Long precoMax,
            Integer diametroMin,
            Integer diametroMax,
            String ordenar
    ) {
        int paginaSegura = Math.max(1, pagina);
        int porPaginaSegura = Math.max(60, Math.max(1, porPagina));

        TipoMovimento tipoMovimentoSegura = TipoMovimento.fromApi(tipoMovimento);
        MaterialCaixa materialCaixaSegura = MaterialCaixa.fromApi(materialCaixa);
        TipoVidro vidro =  TipoVidro.fromApi(tipoVidro);
        OrdenacaoRelogios ordenacao = OrdenacaoRelogios.fromApi(ordenar);

        Sort sort = switch (ordenacao) {
            case MAIS_RECENTES -> Sort.by(Sort.Direction.DESC, "criadoEm");
            case PRECO_CRESC -> Sort.by(Sort.Direction.ASC, "precoEmCentavos");
            case PRECO_DESC -> Sort.by(Sort.Direction.DESC, "precoEmCentavos");
            case DIAMETRO_CRESC -> Sort.by(Sort.Direction.ASC, "diametroMm");
            case RESISTENCIA_DESC -> Sort.by(Sort.Direction.DESC, "resistenciaAguaM");
        };

        Pageable pageable = PageRequest.of(paginaSegura - 1, porPaginaSegura, sort);

        Specification<Relogio> spec  = Specification.where(busca(busca))
                .and(marcaIgual(marca))
                .and(tipoMovimentoIgual(tipoMovimentoSegura))
                .and(materialCaixaIgual(materialCaixaSegura))
                .and(tipoVidroIgual(vidro))
                .and(resistenciaAguaEntre(resistenciaMin, resistenciaMax))
                .and(precoEntre(precoMin, precoMax))
                .and(diametroEntre(diametroMin, diametroMax));

        Page<Relogio> resultado = repo.findAll(spec, pageable);

        return new PaginaRelogioDTO(
                resultado.getContent().stream().map(mapper::toDto).toList(),
                resultado.getTotalElements()
        );
    }

    public RelogioDTO buscarPorId(UUID id) {
        Relogio r = repo.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Relógio não encontrado: " + id));
        return mapper.toDto(r);
    }

    public RelogioDTO criar(CriarRelogioRequest req) {
        Relogio r = Relogio.builder()
                .id(UUID.randomUUID())
                .marca(req.marca())
                .modelo(req.modelo())
                .referencia(req.referencia())
                .tipoMovimento(TipoMovimento.fromApi(req.tipoMovimento()))
                .materialCaixa(MaterialCaixa.fromApi(req.materialCaixa()))
                .tipoVidro(TipoVidro.fromApi(req.tipoVidro()))
                .resistenciaAguaM(req.resistenciaAguaM())
                .diametroMm(req.diametroMm())
                .lugToLugMm(req.lugToLugMm())
                .espessuraMm(req.espessuraMm())
                .larguraLugMm(req.larguraLugMm())
                .precoEmCentavos(req.precoEmCentavos())
                .urlImagem(req.urlImagem())
                .criadoEm(Instant.now())
                .build();

        Relogio salvo = repo.save(r);
        return mapper.toDto(salvo);
    }

    public RelogioDTO atualizar(UUID id, AtualizarRelogioRequest req) {
        Relogio r = repo.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Relógio não encontrado: " + id));

        r.setMarca(req.marca());
        r.setModelo(req.modelo());
        r.setReferencia(req.referencia());
        r.setTipoMovimento(TipoMovimento.fromApi(req.tipoMovimento()));
        r.setMaterialCaixa(MaterialCaixa.fromApi(req.materialCaixa()));
        r.setTipoVidro(TipoVidro.fromApi(req.tipoVidro()));
        r.setResistenciaAguaM(req.resistenciaAguaM());
        r.setDiametroMm(req.diametroMm());
        r.setLugToLugMm(req.lugToLugMm());
        r.setEspessuraMm(req.espessuraMm());
        r.setLarguraLugMm(req.larguraLugMm());
        r.setPrecoEmCentavos(req.precoEmCentavos());
        r.setUrlImagem(req.urlImagem());

        Relogio salvo = repo.save(r);
        return mapper.toDto(salvo);
    }

    public void remover(UUID id) {
        if (!repo.existsById(id)) {
            throw new NaoEncontradoException("Relógio não encontrado: " + id);
        }
        repo.deleteById(id);
    }
}
