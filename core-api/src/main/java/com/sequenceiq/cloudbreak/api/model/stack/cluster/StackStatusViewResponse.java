package com.sequenceiq.cloudbreak.api.model.stack.cluster;

import com.sequenceiq.cloudbreak.api.model.Status;

public class StackStatusViewResponse {
    private Long id;

    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
