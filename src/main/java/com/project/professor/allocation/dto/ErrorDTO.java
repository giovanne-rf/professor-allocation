package com.project.professor.allocation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class ErrorDTO {

    private Date timestamp;

    private Integer statusCode;

    private String statusReason;

    private String path;

    private String exception;

    private String message;

    private String stack;
}
