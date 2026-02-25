package com.polaris.HS.Code.Validator.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChatRequestDto implements Serializable {
    private String sessionId;
    private String message;
}
