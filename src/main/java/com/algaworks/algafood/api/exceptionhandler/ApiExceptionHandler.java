package com.algaworks.algafood.api.exceptionhandler;

import com.algaworks.algafood.core.validation.ValidacaoException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice //armazena excecoes globais
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String MSG_ERRO_GENERICO_USUARIO_FINAL = "Ocorreu um erro interno inesperado no sistema. tente novamente e se o problema persistir, entre em contato com o administrador do sistema.";

    @Autowired //source de msgs de campos escritos errados no front-end. pega o que está em "messages.properties". Usando no metodo handleMethodArgumentNotValid
    private MessageSource messageSource;

    /*
    // quando gerar excecao nessa classe, essa anotacao define que vai chamar isso e o return diz o que deve ser retornado
    @ExceptionHandler(EntidadeNaoEncontradaException.class) // entre {chaves} caso quiser tratar mais de uma
    public ResponseEntity<?> tratarEntidadeNaoEncontradaException(EntidadeNaoEncontradaException e){
        Problema problema = Problema.builder().dataHora(LocalDateTime.now()).msg(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problema);
     }
     //OLD STYLE
     */

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.status(status).headers(headers).build(); // perceba que nao tem body. Pois o front-end nao vai ta esperando /json. Ver RestauranteProdutoFotoController.servirFoto
    }

    //NOVO STYLE: tocando tudo pra handleExceptionInternal
    @ExceptionHandler(EntidadeNaoEncontradaException.class) // entre {chaves} caso quiser tratar mais de uma
    public ResponseEntity<?> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = ex.getMessage();
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<?> handleEntidadeEmUsoExceptionException(EntidadeEmUsoException ex, WebRequest request){
        HttpStatus status = HttpStatus.CONFLICT;
        ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
        String detail = ex.getMessage();
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NegocioException.class)
    public ResponseEntity<?> handleNegocioException(NegocioException ex, WebRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemType problemType = ProblemType.ERRO_NEGOCIO;
        String detail = ex.getMessage();
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex, WebRequest request){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
        String detail = MSG_ERRO_GENERICO_USUARIO_FINAL;

        // Importante colocar o printStackTrace (pelo menos por enquanto, que não estamos
        // fazendo logging) para mostrar a stacktrace no console
        // Se não fizer isso, você não vai ver a stacktrace de exceptions que seriam importantes
        // para você durante, especialmente na fase de desenvolvimento
        //ex.printStackTrace();

        // com logging:
        log.error(ex.getMessage(), ex);
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause instanceof InvalidFormatException){
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException){
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        }
        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
                .build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        //Throwable rootCause = ExceptionUtils.getRootCause(ex);
        ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
        String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', que é de um tipo inválido. "
                        + "Corrija e informe um valor compatível com o tipo %s",
                        ((MethodArgumentTypeMismatchException) ex).getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
       // Throwable rootCause = ExceptionUtils.getRootCause(ex);
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());
        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail;
        if (ex instanceof IgnoredPropertyException){ // esse if nao devia fazer, pois o consumidor nao deve saber esse tipo de detalhe. Tem que dizer só que nao existe e pronto.
            detail = String.format("A propriedade '%s' não é relevante para essa requisição. Remova esse campo.", path);
        } else if (ex instanceof UnrecognizedPropertyException){
            detail = String.format("A propriedade '%s' é desconhecida. Remova esse campo.", path);
        } else {
            detail = String.format("Erro de propriedade não esperado. Favor remover campos inválidos.");
        }
        Problem problem = createProblemBuilder(status, problemType, detail).build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
                + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                path, ex.getValue(), ex.getTargetType().getSimpleName()); // como saber cada tipo aqui? Fuçando os metodos da ex.
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

/* //pode tirar, pois o extends já diz que trata exceptions do spring naturalmente
    @ExceptionHandler(HttpMediaTypeException.class)
    public ResponseEntity<?> tratarHttpMediaTypeExceptionException(){
        Problema problema = Problema.builder().dataHora(LocalDateTime.now()).msg("O tipo de midia não é aceito").build();
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(problema);
    }
     */

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<?> handleValidacaoException(ValidacaoException ex, WebRequest request){
        return handleValidationInternal(ex, ex.getBindingResult(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override // capturado quando nao passa no @Valid do metodo do controller
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleValidationInternal(ex, ex.getBindingResult(), headers, status, request);
    }

    private ResponseEntity<Object> handleValidationInternal(Exception ex, BindingResult bindingResult, HttpHeaders headers, HttpStatus status, WebRequest request){
        ProblemType problemType = ProblemType.DADOS_INVALIDOS;
        String detail = String.format("Um ou mais campos estão inválidos.");
        List<Problem.Objects> problemFields = bindingResult.getAllErrors().stream()// passou de getFieldErrors() para getAllErrors()
                .map(objError -> {
                    String msg = messageSource.getMessage(objError, LocaleContextHolder.getLocale());
                    String name = objError.getObjectName();
                    if (objError instanceof FieldError){
                        name = ((FieldError) objError).getField();
                    }
                    return Problem.Objects.builder()
                        .name(name)
                        //.userMessage(fieldError.getDefaultMessage())
                        .userMessage(msg)
                        .build();
                })
                .collect(Collectors.toList());
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .objects(problemFields)
                .build();
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override // override pra poder dar um body proprio pra todas as chamadas com body vazio
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null){
            body = Problem.builder()
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
                    .timestamp(OffsetDateTime.now())
                    .build();
        } else if (body instanceof String){
            body = Problem.builder()
                    .title((String) body)
                    .status(status.value())
                    .userMessage(MSG_ERRO_GENERICO_USUARIO_FINAL)
                    .timestamp(OffsetDateTime.now())
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    // cada field fica com um "." no meio: cozinha.id
    private String joinPath(List<Reference> references) {
        return references.stream()
                .map(ref -> ref.getFieldName())
                .collect(Collectors.joining("."));
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail){
        return Problem.builder()
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail) // msg pro front-ender dev
                //.userMessage(msg) // msg pro usuario (pro front-ender mostrar direto)
                .timestamp(OffsetDateTime.now());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedExceptionException(AccessDeniedException ex, WebRequest request){
        HttpStatus status = HttpStatus.FORBIDDEN;
        ProblemType problemType = ProblemType.ERRO_DE_ACESSO;
        String detail = "Acesso negado";
        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage("Você não possui permissão para executar essa operação.")
                .build();
        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }
}
