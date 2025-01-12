package com.example.Apitrain.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AthleteNotFoundException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(AthleteNotFoundException.class);

    public AthleteNotFoundException(String message) {
        super(message);
        logger.error("AthleteNotFoundException lanzada: {}", message);
    }
}
