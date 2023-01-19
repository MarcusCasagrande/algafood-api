package com.algaworks.algafood.infrastructure.service.report;

// Não é uma exception de negocio, é uma exception de infra-estrutura
public class ReportException extends RuntimeException{

    public ReportException(String message) {
        super(message);
    }

    public ReportException(String message, Throwable cause) {
        super(message, cause);
    }
}
