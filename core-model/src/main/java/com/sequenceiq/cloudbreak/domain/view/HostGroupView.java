package com.sequenceiq.cloudbreak.domain.view;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sequenceiq.cloudbreak.domain.ProvisionEntity;

@Entity
@Table(name = "HostGroup")
public class HostGroupView implements ProvisionEntity {

    @Id
    private Long id;

    private String name;

    @ManyToOne
    private ClusterView cluster;

    @OneToMany(mappedBy = "hostGroup")
    private Set<HostMetadataView> hostMetadata = new HashSet<>();

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

    public Set<HostMetadataView> getHostMetadata() {
        return hostMetadata;
    }

    public void setHostMetadata(Set<HostMetadataView> hostMetadata) {
        this.hostMetadata = hostMetadata;
    }
}
