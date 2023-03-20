package com.bluespoon.searchpostcode.aspect;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bluespoon.searchpostcode.model.GeneralErrorResponse;
import com.bluespoon.searchpostcode.model.GeneralErrorResponseDetail;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path.Node;

@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class); 

    /**
     * ここで定義しない不特定の例外を捕捉して、その他のエラーとしてステータスコード、レスポンスヘッダ、ボディを生成する。
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<GeneralErrorResponse> handleAllException(Exception ex) {
        GeneralErrorResponse ger = new GeneralErrorResponse();
        return new ResponseEntity<GeneralErrorResponse>(ger, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * ConstraintViolationExceptionを捕捉して、エラーとしてステータスコード、レスポンスヘッダ、ボディを生成する。
     * コンパイルオプションを指定しないと、パラメタ名が取得できない。arg0となってしまう。
     * -parametersをbuild.gradleへ設定。
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<GeneralErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        GeneralErrorResponse ger = new GeneralErrorResponse();
        List<GeneralErrorResponseDetail> gerdList = new ArrayList<>();

        ex.getConstraintViolations().forEach(error -> {
            String field = null;
            for (Node node : error.getPropertyPath()) {
                field = node.getName();
            }
            gerdList.add(new GeneralErrorResponseDetail(field, error.getMessage()));
            LOGGER.info("field : {}, message : {}", field, error.getMessage());
        });
        ger.setDetails(gerdList);

        return new ResponseEntity<GeneralErrorResponse>(ger, HttpStatus.BAD_REQUEST);
    }

    /**
     * handleExceptionInternalのオーバライド。
     * ResponseEntityExceptionHandlerクラスで定義された例外をすべて捕捉して、ステータスコード、レスポンスヘッダ、ボディを上書きする。
     * 
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        GeneralErrorResponse ger;
        HttpStatus httpStatus = (HttpStatus)statusCode;

        //呼び出し元でレスポンスボディを生成済みでない場合、このメソッド内で生成する。
        if(!(body instanceof GeneralErrorResponse)){
            ger = new GeneralErrorResponse();

            //4xx系エラーをまとめる。
            if(httpStatus.is4xxClientError()){
                httpStatus = HttpStatus.BAD_REQUEST;
            //5xx系エラーをまとめる。
            }else if(httpStatus.is5xxServerError()){
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }else{
            ger = (GeneralErrorResponse)body;
        }

        return new ResponseEntity<>(ger, httpStatus);
    }

    /**
     * handleHttpMessageNotReadableのオーバライド。
     * レスポンスボディを設定して、本クラスにあるhandleExceptionInternalメソッドを呼び出す。
     * 
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        GeneralErrorResponse ger = new GeneralErrorResponse();
        return handleExceptionInternal(ex, ger, headers, HttpStatus.BAD_REQUEST, request);
    }

    /**
     * handleNoHandlerFoundException
     * レスポンスボディを設定して、本クラスにあるhandleExceptionInternalメソッドを呼び出す。
     * また、application.propertyniの以下の設定も必要。
     *   spring.mvc.throw-exception-if-no-handler-found=true
     * 
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        GeneralErrorResponse ger = new GeneralErrorResponse();
        return handleExceptionInternal(ex, ger, headers, HttpStatus.NOT_FOUND, request);
    }

    /**
     * handleMethodArgumentNotValidのオーバライド。
     * レスポンスボディを設定して、本クラスにあるhandleExceptionInternalメソッドを呼び出す。
     * 
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        GeneralErrorResponse ger = new GeneralErrorResponse();
        List<GeneralErrorResponseDetail> gerdList = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->{
            gerdList.add(new GeneralErrorResponseDetail(error.getField(), error.getDefaultMessage()));
            LOGGER.info("field : {}, message : {}", error.getField(), error.getDefaultMessage());
        });
        ger.setDetails(gerdList);
        return handleExceptionInternal(ex, ger, headers, HttpStatus.BAD_REQUEST, request);
    }

}
