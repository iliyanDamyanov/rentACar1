package org.rentacar1.app.exeptions;

public class DomainExeption extends RuntimeException{

    public DomainExeption(String message) {
        super(message);
    }

    public DomainExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
