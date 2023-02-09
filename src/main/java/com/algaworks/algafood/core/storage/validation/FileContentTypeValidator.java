package com.algaworks.algafood.core.storage.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class FileContentTypeValidator implements ConstraintValidator<FileContentType, MultipartFile> {

    List<String> tipos;

    @Override // nao obrigatorio implementar
    public void initialize(FileContentType constraintAnnotation) {
        this.tipos = Arrays.asList(constraintAnnotation.allowed());
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        return multipartFile == null || tipos.contains(multipartFile.getContentType());
    }
}
