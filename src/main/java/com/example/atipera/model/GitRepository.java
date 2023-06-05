package com.example.atipera.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GitRepository {
    private String name;
    private Owner owner;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean fork;
    private List<Branch> branches;
}
