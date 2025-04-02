package org.rentacar1.app.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Обработчик за невалидни аргументи (включително за грешки в полетата)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors); // Връщаме 400
    }

    // Обработчик за IllegalArgumentException (например отрицателна сума)
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", ex.getMessage());  // Показваме съобщението за грешка
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);  // Връщаме 400
        return modelAndView;
    }

    // Обработчик за всички RuntimeException (неправилно поведение на приложението)
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", "Resource not found or invalid request.");  // Генерален съобщение за грешка
        modelAndView.setStatus(HttpStatus.NOT_FOUND);  // Връщаме 404 за не намерени ресурси
        return modelAndView;
    }

    // Обработчик за всички други изключения (неочаквани)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", "Something went wrong. Please try again later.");
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);  // Връщаме 500 за вътрешна грешка
        ex.printStackTrace();  // Показване на грешката в конзолата (по желание)
        return modelAndView;
    }
}
