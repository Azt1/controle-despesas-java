package br.com.azt1.despesas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ControleDespesasApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void aplicacaoIniciaSemBancoDeDados() {
        assertNotNull(context.getBean(ControleDespesasApplication.class));
    }
}
