package com.example.desafiorelogio.entity;

import com.example.desafiorelogio.entity.enums.MaterialCaixa;
import com.example.desafiorelogio.entity.enums.TipoMovimento;
import com.example.desafiorelogio.entity.enums.TipoVidro;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "relogios", indexes = {
        @Index(name = "idx_relogio_marca", columnList = "marca"),
        @Index(name = "idx_relogio_criado_em", columnList = "criadoEm"),
        @Index(name = "idx_relogio_preco", columnList = "precoEmCentavos")
})
public class Relogio {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 80)
    private String marca;

    @Column(nullable = false, length = 120)
    private String modelo;

    @Column(nullable = false, length = 80)
    private String referencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimento tipoMovimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MaterialCaixa materialCaixa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoVidro tipoVidro;

    @Column(nullable = false)
    private int resistenciaAguaM;

    @Column(nullable = false)
    private int diametroMm;

    @Column(nullable = false)
    private int lugToLugMm;

    @Column(nullable = false)
    private int espessuraMm;

    @Column(nullable = false)
    private int larguraLugMm;

    @Column(nullable = false)
    private long precoEmCentavos;

    @Column(nullable = false, length = 600)
    private String urlImagem;

    @Column(nullable = false)
    private Instant criadoEm;


    //Para montar o seed inicial
    @PrePersist
    void prePersist() {
        if (id == null) id = UUID.randomUUID();
        if (criadoEm == null) criadoEm = Instant.now();
        normalizar();
    }

    @PreUpdate
    void preUpdate() {
        normalizar();
    }

    private void normalizar() {
        if (marca != null) marca = marca.trim();
        if (modelo != null) modelo = modelo.trim();
        if (referencia != null) referencia = referencia.trim();
        if (urlImagem != null) urlImagem = urlImagem.trim();
    }

}