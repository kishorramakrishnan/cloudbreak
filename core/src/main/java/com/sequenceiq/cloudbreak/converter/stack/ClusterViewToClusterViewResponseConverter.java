package com.sequenceiq.cloudbreak.converter.stack;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.api.model.stack.cluster.ClusterViewResponse;
import com.sequenceiq.cloudbreak.api.model.stack.cluster.HostGroupViewResponse;
import com.sequenceiq.cloudbreak.converter.AbstractConversionServiceAwareConverter;
import com.sequenceiq.cloudbreak.domain.KerberosConfig;
import com.sequenceiq.cloudbreak.domain.view.ClusterView;
import com.sequenceiq.cloudbreak.domain.view.HostGroupView;

@Component
public class ClusterViewToClusterViewResponseConverter extends AbstractConversionServiceAwareConverter<ClusterView, ClusterViewResponse> {

    @Override
    public ClusterViewResponse convert(ClusterView source) {
        ClusterViewResponse clusterViewResponse = new ClusterViewResponse();
        clusterViewResponse.setId(source.getId());
        clusterViewResponse.setName(source.getName());
        clusterViewResponse.setOwner(source.getOwner());
        clusterViewResponse.setAmbariIp(source.getAmbariIp());
        clusterViewResponse.setEmailNeeded(source.getEmailNeeded());
        clusterViewResponse.setEmailTo(source.getEmailTo());
        clusterViewResponse.setStatus(source.getStatus());
        convertKerberosConfig(source, clusterViewResponse);
        clusterViewResponse.setHostGroups(convertHostGroupsToJson(source.getHostGroupViews()));
        return clusterViewResponse;
    }

    private void convertKerberosConfig(ClusterView source, ClusterViewResponse clusterViewResponse) {
        KerberosConfig kerberosConfig = source.getKerberosConfig();
        if (source.isSecure() && kerberosConfig != null) {
            clusterViewResponse.setSecure(source.isSecure());
        }
    }

    private Set<HostGroupViewResponse> convertHostGroupsToJson(Iterable<HostGroupView> hostGroups) {
        Set<HostGroupViewResponse> jsons = new HashSet<>();
        for (HostGroupView hostGroup : hostGroups) {
            jsons.add(getConversionService().convert(hostGroup, HostGroupViewResponse.class));
        }
        return jsons;
    }
}
