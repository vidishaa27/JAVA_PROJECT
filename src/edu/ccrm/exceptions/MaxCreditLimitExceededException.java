package edu.ccrm.exceptions;

public class MaxCreditLimitExceededException extends Exception {
    public MaxCreditLimitExceededException(String message) {
        super(message);
    }
}
