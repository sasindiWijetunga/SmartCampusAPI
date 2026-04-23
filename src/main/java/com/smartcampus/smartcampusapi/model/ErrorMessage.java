/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi.model;

/**
 *
 * @author sasin
 */
public class ErrorMessage {
    private String errorMessage;
    private int errorCode;
    private String documentation;
    
    //Constructors
    public ErrorMessage() {
        
    }
    
    public ErrorMessage(String errorMessage,int errorCode,String error){
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.documentation = error;
    }
    //Getters
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public int getErrorCode() {
        return errorCode;
    }
    
    public String getDocumentation() {
        return documentation;
    }
    
    //Setters
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public void setError(String documentation) {
        this.documentation = documentation;
    }

}
