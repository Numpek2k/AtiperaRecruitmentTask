package com.example.atipera.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GitRepository(String name, Owner owner,
                            List<Branch> branches,
                            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) boolean fork) {
}
