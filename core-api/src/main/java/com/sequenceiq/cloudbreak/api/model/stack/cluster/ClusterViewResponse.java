package com.sequenceiq.cloudbreak.api.model.stack.cluster;

import java.util.HashSet;
import java.util.Set;

import com.sequenceiq.cloudbreak.api.model.Status;

public class ClusterViewResponse {
    private Long id;

    private boolean secure;

    private String name;

    private String owner;

    private String ambariIp;

    private Boolean emailNeeded;

    private String emailTo;

    private Status status;

    private Set<HostGroupViewResponse> hostGroups = new HashSet<>();

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAmbariIp() {
        return ambariIp;
    }

    public void setAmbariIp(String ambariIp) {
        this.ambariIp = ambariIp;
    }

    public Boolean getEmailNeeded() {
        return emailNeeded;
    }

    public void setEmailNeeded(Boolean emailNeeded) {
        this.emailNeeded = emailNeeded;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public Set<HostGroupViewResponse> getHostGroups() {
        return hostGroups;
    }

    public void setHostGroups(Set<HostGroupViewResponse> hostGroups) {
        this.hostGroups = hostGroups;
    }
}
