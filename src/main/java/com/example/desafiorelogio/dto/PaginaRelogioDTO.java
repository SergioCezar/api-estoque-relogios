package com.example.desafiorelogio.dto;

import java.util.List;

public record PaginaRelogioDTO (
    List<RelogioDTO> itens,
    long total
){}
