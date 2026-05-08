package com.example.desafiorelogio.entity.enums;

public enum TipoMovimento {
    QUARTZ,
    AUTOMATICO,
    MANUAL;


    //Normaliza o texto do client-side pro backend (evita problemas ingles -> ptbr e vice-versa)
    public static TipoMovimento fromApi(String valor) {
        if (valor == null || valor.isBlank()) return null;
        return switch(valor) {
            case "quartz" -> QUARTZ;
            case "automatic" -> AUTOMATICO;
            case "manual" -> MANUAL;
            default -> throw new IllegalArgumentException("Tipo de movimento inválido: " + valor);
        };
    }

    public String toApi() {
        return switch(this) {
            case QUARTZ -> "quartz";
            case AUTOMATICO -> "automatic";
            case MANUAL -> "manual";
        };
    }

}
