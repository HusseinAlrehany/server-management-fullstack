package com.iogetArray.server.models;

import com.iogetArray.server.enumeration.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Ip Address Can not Be Empty Or Null")
    private String ipAddress;
    private String name;
    private String memory;
    private String type;
    private String imageUrl;
    @Enumerated
    private Status status;


}
