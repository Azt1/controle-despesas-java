package br.com.azt1.despesas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta jsonInvalido() {
        return new ErroResposta("Envie um JSON válido, com data no formato yyyy-MM-dd e tipo RECEITA ou DESPESA.");
    }

    public record ErroResposta(String mensagem) {
    }
}
