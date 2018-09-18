package com.sequenceiq.cloudbreak.domain.view;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sequenceiq.cloudbreak.api.model.stack.instance.InstanceStatus;

@Entity
@Table(name = "InstanceMetaData")
public class InstanceMetaDataView {

    @Id
    private Long id;

    @ManyToOne
    private InstanceGroupView instanceGroup;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InstanceStatus instanceStatus;

    public boolean isTerminated() {
        return InstanceStatus.TERMINATED.equals(instanceStatus);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InstanceStatus getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(InstanceStatus instanceStatus) {
        this.instanceStatus = instanceStatus;
    }
}
