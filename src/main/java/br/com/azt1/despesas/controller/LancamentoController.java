package br.com.azt1.despesas.controller;

import java.util.List;
import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.azt1.despesas.model.Lancamento;
import br.com.azt1.despesas.dto.NovoLancamento;
import br.com.azt1.despesas.dto.SaldoMensal;
import br.com.azt1.despesas.service.LancamentoService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoController {

    private final LancamentoService service;

    public LancamentoController(LancamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Lancamento> listar() {
        return service.listar();
    }

    @GetMapping("/saldo")
    public SaldoMensal consultarSaldo(
            @RequestParam @DateTimeFormat(pattern = "uuuu-MM") YearMonth mes) {
        return new SaldoMensal(mes, service.calcularSaldo(mes));
    }

    @PostMapping
    public ResponseEntity<Lancamento> cadastrar(@RequestBody NovoLancamento dados) {
        Lancamento lancamento = service.cadastrar(dados.descricao(), dados.valor(), dados.data(), dados.tipo());
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamento);
    }
}
