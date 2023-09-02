package com.group9project.portfoliomanager.exception;

public class PortfolioNotFoundException extends RuntimeException{
    public PortfolioNotFoundException(long id){
        super("PortFolio not avialable with portfolio id: " + id);
    }
}
