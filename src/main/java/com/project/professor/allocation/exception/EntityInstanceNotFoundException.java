package com.project.professor.allocation.exception;

import javax.persistence.EntityNotFoundException;

public class EntityInstanceNotFoundException extends EntityNotFoundException {

    private static final String MESSAGE_BASE = "Instance not found to ID \"%s\" of entity \"%s\"";

    public <T> EntityInstanceNotFoundException(Class<T> entityClass, Object entityId) {
        super(String.format(MESSAGE_BASE, entityClass, entityId));
    }
}
