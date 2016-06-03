// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Source {
    @JsonProperty("name")
    private String name;

    @JsonProperty("code")
    private String code;

    @JsonCreator
    public Source(@JsonProperty("name") String name, @JsonProperty("code") String code) {
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
