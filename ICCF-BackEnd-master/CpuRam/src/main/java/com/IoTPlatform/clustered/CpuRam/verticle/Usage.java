package com.IoTPlatform.clustered.CpuRam.verticle;

import io.vertx.core.json.JsonObject;

public class Usage {

  private String id;

  private String cpu;

  private String ram;

  public Usage(String cpu, String ram) {
    this.cpu = cpu;
    this.ram = ram;
    this.id = "";
  }

  public Usage(JsonObject json) {
    this.cpu = json.getString("cpu");
    this.ram = json.getString("ram");
    this.id = json.getString("_id");
  }

  public Usage() {
    this.id = "";
  }

  public Usage(String id, String cpu, String ram) {
    this.id = id;
    this.cpu = cpu;
    this.ram = ram;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject()
        .put("cpu", cpu)
        .put("ram", ram);
    if (id != null && !id.isEmpty()) {
      json.put("_id", id);
    }
    return json;
  }

  public String getCpu() {
    return cpu;
  }

  public String getRam() {
    return ram;
  }

  public String getId() {
    return id;
  }

  public Usage setCpu(String cpu) {
    this.cpu = cpu;
    return this;
  }

  public Usage setRam(String ram) {
    this.ram = ram;
    return this;
  }

  public Usage setId(String id) {
    this.id = id;
    return this;
  }
}