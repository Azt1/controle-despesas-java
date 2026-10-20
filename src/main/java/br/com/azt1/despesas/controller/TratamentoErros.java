package br.com.azt1.despesas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratamentoErros {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta dadosInvalidos(IllegalArgumentException erro) {
        return new ErroResposta(erro.getMessage());
    }

    public record ErroResposta(String mensagem) {
    }
}
