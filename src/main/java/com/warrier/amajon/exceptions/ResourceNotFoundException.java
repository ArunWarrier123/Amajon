package com.warrier.amajon.exceptions;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String fieldName;
    String field;
    Integer fieldId;

    //resource name is entity name field name  is col name and field Id is primary key
    public ResourceNotFoundException(String resourceName, String fieldName, Integer fieldId) {
        super(String.format("%s with %s not found: %s", resourceName, fieldName, fieldId ));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldId = fieldId;
    }
}
