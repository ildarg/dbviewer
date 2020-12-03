package com.atcproject.dbviewer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "connections")
@Getter
@Setter
@NoArgsConstructor
public class ConnectionDetail extends AbstractSerializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotBlank(message = "hostname is mandatory")
    private String hostname;

    private Integer port;

    @NotBlank(message = "dbname is mandatory")
    private String dbname;

    @NotBlank(message = "username is mandatory")
    private String username;

    @NotBlank(message = "password is mandatory")
    private String password;

    @JsonIgnore
    public String getUrl() {
        return String.format("jdbc:postgresql://%s%s/%s",
                hostname,
                (port == null) ? "" : ":" + port,
                dbname);
    }
}
