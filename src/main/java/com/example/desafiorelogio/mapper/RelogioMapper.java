package com.example.desafiorelogio.mapper;

import com.example.desafiorelogio.dto.RelogioDTO;
import com.example.desafiorelogio.entity.Relogio;
import com.example.desafiorelogio.entity.enums.MaterialCaixa;
import com.example.desafiorelogio.entity.enums.TipoMovimento;
import com.example.desafiorelogio.entity.enums.TipoVidro;
import org.springframework.stereotype.Component;

@Component
public class RelogioMapper {
    public RelogioDTO toDto(Relogio r) {
        return RelogioDTO.builder()
                .id(r.getId())
                .marca(r.getMarca())
                .modelo(r.getModelo())
                .referencia(r.getReferencia())
                .tipoMovimento(r.getTipoMovimento().toApi())
                .materialCaixa(r.getMaterialCaixa().toApi())
                .tipoVidro(r.getTipoVidro().toApi())
                .resistenciaAguaM(r.getResistenciaAguaM())
                .diametroMm(r.getDiametroMm())
                .lugToLugMm(r.getLugToLugMm())
                .espessuraMm(r.getEspessuraMm())
                .larguraLugMm(r.getLarguraLugMm())
                .precoEmCentavos(r.getPrecoEmCentavos())
                .urlImagem(r.getUrlImagem())
                .etiquetaResistenciaAgua(etiquetaResistencia(r.getResistenciaAguaM()))
                .pontuacaoColecionador(pontuacaoColecionador(r))
                .build();
    }

    private String etiquetaResistencia(int resistenciaM) {
        if (resistenciaM < 50) return "respingos";
        if (resistenciaM < 100) return "uso_diario";
        if (resistenciaM < 200) return "natacao";
        return "mergulho";
    }

    private int pontuacaoColecionador(Relogio r) {
        int pontos = 0;

        if (r.getTipoVidro() == TipoVidro.SAFIRA) pontos += 25;

        if (r.getResistenciaAguaM() >= 100) pontos += 15;
        if (r.getResistenciaAguaM() >= 200) pontos += 10; //bonus

        if (r.getTipoMovimento() == TipoMovimento.AUTOMATICO) pontos += 20;

        if (r.getMaterialCaixa() == MaterialCaixa.ACO) pontos += 10;
        if (r.getMaterialCaixa() == MaterialCaixa.TITANIO) pontos += 12;

        // "tamanho clássico" 38–42mm
        if (r.getDiametroMm() >= 38 && r.getDiametroMm() <= 42) pontos += 8;

        return pontos;
    }

}
