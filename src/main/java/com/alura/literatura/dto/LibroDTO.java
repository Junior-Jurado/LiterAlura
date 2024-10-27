package com.alura.literatura.dto;

public record LibroDTO(
        long id,
        String titulo,
        String autor,
        String idioma,
        Integer numeroDeDescargas
) {
}
