package com.IoTPlatform.clustered.capmanager.verticle;

import java.util.concurrent.atomic.AtomicInteger;

public class Capability {

  private static final AtomicInteger COUNTER = new AtomicInteger();
 
  private final int id;
  private String    name;
  private String    location;
  private String    metric;
  private String    url;
  private String    timestamp;
  
  public String getMetric() {
	return metric;
}

public void setMetric(String metric) {
	this.metric = metric;
}

public String getUrl() {
	return url;
}

public void setUrl(String url) {
	this.url = url;
}

public String getTimestamp() {
	return timestamp;
}

public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
}

public Capability(String name, String location,String url, String metric) {
    this.id = COUNTER.getAndIncrement();
    this.name = name;
    this.location = location;
    this.url=url;
    this.metric=metric;
  }

  public Capability() {
    this.id = COUNTER.getAndIncrement();
  }

  public String getName() {
    return name;
  }

  public String getLocation() {
    return location;
  }

  public int getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
