package org.reversi;

public class IncorrectInputException extends Exception {
    public IncorrectInputException(String errorMessage) {
        super(errorMessage);
    }
}
