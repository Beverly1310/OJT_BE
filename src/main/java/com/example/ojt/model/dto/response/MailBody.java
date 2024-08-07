package com.example.ojt.model.dto.response;

import lombok.Builder;

@Builder
public record MailBody (String to, String subject, String text){
}
