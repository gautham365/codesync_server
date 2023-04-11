package com.example.colabed.api.model;

import lombok.*;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Editor {
    private String sender;
    private String receiver;
    private String body;

    //private Status status;
}
