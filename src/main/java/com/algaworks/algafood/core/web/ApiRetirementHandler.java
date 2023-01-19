package com.algaworks.algafood.core.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// clkasse criada na aula 20.18, sobre deprecated
@Component
public class ApiRetirementHandler implements HandlerInterceptor {

    @Override // método a ser chamado ANTES dos metodos de cada controlador
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (request.getRequestURI().startsWith("/v1/")) {
            /** usado pra avisar que ta deprecated (20.18) */
            response.addHeader("X-AlgaFood-Deprecated",
                    "Essa versão da API está depreciada e deixará de existir a partir de 01/01/2021."
                            + "Use a versão mais atual da API.");

            /** usado pra avisar que ta desativado (20.19) */
//            response.setStatus(HttpStatus.GONE.value());
//            return false; // se for pra retornar false, interrompe a executacao do metodo
        }
        return true;

    }
}
