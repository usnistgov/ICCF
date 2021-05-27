package com.IoTPlatform.clustered.av.verticle;


import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

public class Interaction {
	@JsonProperty
    private long entryId = -1;
    @JsonProperty
    private String ID18B;
    @JsonProperty
    private String DataField;

    public Interaction(String ID18B, String DataField) {
        this.ID18B = ID18B;
        this.DataField = DataField;
    }

    public Interaction(long entryId, String ID18B, String DataField) {
        this.entryId = entryId;
        this.ID18B = ID18B;
        this.DataField = DataField;
    }

    public Interaction() {
        
    }

    public Interaction(JsonObject json) {
        this(
            json.getInteger("entryId", -1),
            json.getString("ID18B"),
            json.getString("DataField")
        );
    }

    public long getEntryId() {
        return entryId;
    }

    public String getID18B() {
        return ID18B;
    }

    public Interaction setID18B(String ID18B) {
        this.ID18B = ID18B;
        return this;
    }

    public String getDataField() {
        return DataField;
    }

    public Interaction setDataField(String DataField) {
        this.DataField = DataField;
        return this;
    }
}
