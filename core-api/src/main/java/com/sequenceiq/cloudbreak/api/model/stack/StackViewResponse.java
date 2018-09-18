package com.sequenceiq.cloudbreak.api.model.stack;

import static com.sequenceiq.cloudbreak.doc.ModelDescriptions.OWNER;
import static com.sequenceiq.cloudbreak.doc.ModelDescriptions.StackModelDescription.AMBARI_VERSION;
import static com.sequenceiq.cloudbreak.doc.ModelDescriptions.StackModelDescription.HDP_VERSION;
import static com.sequenceiq.cloudbreak.doc.ModelDescriptions.StackModelDescription.NODE_COUNT;
import static com.sequenceiq.cloudbreak.doc.ModelDescriptions.StackModelDescription.STACK_ID;

import com.sequenceiq.cloudbreak.api.model.stack.cluster.ClusterViewResponse;
import com.sequenceiq.cloudbreak.api.model.stack.cluster.StackStatusViewResponse;

import io.swagger.annotations.ApiModelProperty;

public class StackViewResponse {
    @ApiModelProperty(STACK_ID)
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    @ApiModelProperty(OWNER)
    private String owner;

    private String cloudPlatform;

    private String platformVariant;

    @ApiModelProperty(HDP_VERSION)
    private String hdpVersion;

    @ApiModelProperty(AMBARI_VERSION)
    private String ambariVersion;

    private ClusterViewResponse cluster;

    private StackStatusViewResponse stackStatus;

    private Integer gatewayPort;

    @ApiModelProperty(NODE_COUNT)
    private Integer nodeCount;

    private Long created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ClusterViewResponse getCluster() {
        return cluster;
    }

    public void setClusterViewResponse(ClusterViewResponse cluster) {
        this.cluster = cluster;
    }

    public String getCloudPlatform() {
        return cloudPlatform;
    }

    public void setCloudPlatform(String cloudPlatform) {
        this.cloudPlatform = cloudPlatform;
    }

    public String getPlatformVariant() {
        return platformVariant;
    }

    public void setPlatformVariant(String platformVariant) {
        this.platformVariant = platformVariant;
    }

    public StackStatusViewResponse getStackStatus() {
        return stackStatus;
    }

    public void setStackStatus(StackStatusViewResponse stackStatus) {
        this.stackStatus = stackStatus;
    }

    public Integer getGatewayPort() {
        return gatewayPort;
    }

    public void setGatewayPort(Integer gatewayPort) {
        this.gatewayPort = gatewayPort;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getHdpVersion() {
        return hdpVersion;
    }

    public void setHdpVersion(String hdpVersion) {
        this.hdpVersion = hdpVersion;
    }

    public String getAmbariVersion() {
        return ambariVersion;
    }

    public void setAmbariVersion(String ambariVersion) {
        this.ambariVersion = ambariVersion;
    }

    public Integer getNodeCount() {
        return nodeCount;
    }

    public void setNodeCount(Integer nodeCount) {
        this.nodeCount = nodeCount;
    }

}
