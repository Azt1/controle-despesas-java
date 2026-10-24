package br.com.azt1.despesas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResposta mesInvalido() {
        return new ErroResposta("Informe o mês no formato yyyy-MM, por exemplo 2026-10.");
    }

    public record ErroResposta(String mensagem) {
    }
}
