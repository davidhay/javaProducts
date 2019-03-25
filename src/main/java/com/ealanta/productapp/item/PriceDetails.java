package com.ealanta.productapp.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceDetails {

    private String was;
    private String then1;
    private String then2;

    //we use a JsonNode for 'now' because the JSON value is either simple text or a nested JSON object.
    private JsonNode now;

    private String currency;

}

