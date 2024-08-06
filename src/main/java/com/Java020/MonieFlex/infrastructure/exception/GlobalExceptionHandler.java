package com.Java020.MonieFlex.infrastructure.exception;

import com.Java020.MonieFlex.domain.entities.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.postgresql.util.PSQLException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleAccountNotFoundException(AccountNotFoundException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Account Not Found")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND) ;
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Customer Not Found")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND) ;
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public final ResponseEntity<ErrorDetails> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Account Balance Insufficient")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND) ;
    }

    @ExceptionHandler(InvalidTransactionException.class)
    public final ResponseEntity<ErrorDetails> handleInvalidTransactionException(InvalidTransactionException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Invalid Transaction")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND) ;
    }

    @ExceptionHandler(PaymentProcessingException.class)
    public final ResponseEntity<ErrorDetails> handlePaymentProcessingException(PaymentProcessingException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Error Processing Payment")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND) ;
    }

    @ExceptionHandler(CardNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleCardNotFoundException(CardNotFoundException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Card Not Found")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND) ;
    }

    @ExceptionHandler(CardExpiredException.class)
    public final ResponseEntity<ErrorDetails> handleCardExpiredException(CardExpiredException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Card Expired")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND) ;
    }

    @ExceptionHandler(InvalidAccountTypeException.class)
    public final ResponseEntity<ErrorDetails> handleInvalidAccountTypeException(InvalidAccountTypeException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Invalid Account")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND) ;
    }
    @ExceptionHandler(PasswordNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handlePasswordNotFoundException(PasswordNotFoundException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Invalid Password")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND) ;
    }
    @ExceptionHandler(FLWException.class)
    public final ResponseEntity<ErrorDetails> handleFLWException(FLWException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Couldn't connect")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST) ;
    }
    @ExceptionHandler(VtpassException.class)
    public final ResponseEntity<ErrorDetails> handleVtpassException(VtpassException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Request failed")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST) ;
    }
    @ExceptionHandler(InvalidPin.class)
    public final ResponseEntity<ErrorDetails> handleInvalidPin(InvalidPin ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage("Incorrect Pin!")
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST) ;
    }


    @ExceptionHandler(PSQLException.class)
    public final ResponseEntity<ErrorDetails> handleDatabaseException(PSQLException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .debugMessage(ex.getMessage())
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.CONFLICT)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT) ;
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<ErrorDetails> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .message("Duplicate data exists")
                .debugMessage(ex.getMessage())
                .dateTime(LocalDateTime.now())
                .status(HttpStatus.CONFLICT)
                .build();
        return  new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT) ;
    }
}

