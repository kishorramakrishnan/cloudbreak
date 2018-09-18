package com.sequenceiq.cloudbreak.converter.stack;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sequenceiq.cloudbreak.api.model.stack.StackViewResponse;
import com.sequenceiq.cloudbreak.api.model.stack.cluster.ClusterViewResponse;
import com.sequenceiq.cloudbreak.api.model.stack.cluster.StackStatusViewResponse;
import com.sequenceiq.cloudbreak.cloud.model.AmbariRepo;
import com.sequenceiq.cloudbreak.cloud.model.component.StackRepoDetails;
import com.sequenceiq.cloudbreak.converter.AbstractConversionServiceAwareConverter;
import com.sequenceiq.cloudbreak.domain.view.InstanceGroupView;
import com.sequenceiq.cloudbreak.domain.view.StackView;
import com.sequenceiq.cloudbreak.service.ClusterComponentConfigProvider;
import com.sequenceiq.cloudbreak.service.ComponentConfigProvider;

@Component
public class StackViewToStackViewResponseConverter extends AbstractConversionServiceAwareConverter<StackView, StackViewResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StackViewToStackViewResponseConverter.class);

    @Inject
    private ComponentConfigProvider componentConfigProvider;

    @Inject
    private ClusterComponentConfigProvider clusterComponentConfigProvider;

    @Inject
    @Qualifier("conversionService")
    private ConversionService conversionService;

    @Override
    public StackViewResponse convert(StackView source) {
        StackViewResponse stackViewResponse = new StackViewResponse();
        stackViewResponse.setId(source.getId());
        stackViewResponse.setName(source.getName());
        stackViewResponse.setOwner(source.getOwner());

        if (source.getClusterView() != null) {
            stackViewResponse.setClusterViewResponse(conversionService.convert(source.getClusterView(), ClusterViewResponse.class));
        }

        convertComponentConfig(stackViewResponse, source);
        addNodeCount(source, stackViewResponse);
        stackViewResponse.setCloudPlatform(source.cloudPlatform());
        stackViewResponse.setPlatformVariant(source.getPlatformVariant());

        StackStatusViewResponse ssvr = new StackStatusViewResponse();
        ssvr.setStatus(source.getStatus());
        stackViewResponse.setStackStatus(ssvr);

        stackViewResponse.setGatewayPort(source.getGatewayPort());
        stackViewResponse.setCreated(source.getCreated());

        return stackViewResponse;

    }

    private void convertComponentConfig(StackViewResponse stackJson, StackView source) {
        try {
            if (source.getClusterView() != null) {
                StackRepoDetails stackRepoDetails = clusterComponentConfigProvider.getHDPRepo(source.getClusterView().getId());
                if (stackRepoDetails != null && stackRepoDetails.getStack() != null) {

                    String repositoryVersion = stackRepoDetails.getStack().get(StackRepoDetails.REPOSITORY_VERSION);
                    if (!StringUtils.isEmpty(repositoryVersion)) {
                        stackJson.setHdpVersion(repositoryVersion);
                    } else {
                        stackJson.setHdpVersion(stackRepoDetails.getHdpVersion());
                    }
                }

                AmbariRepo ambariRepo = clusterComponentConfigProvider.getAmbariRepo(source.getClusterView().getId());
                if (ambariRepo != null) {
                    stackJson.setAmbariVersion(ambariRepo.getVersion());
                }
            }
        } catch (RuntimeException e) {
            LOGGER.error("Failed to convert dynamic component.", e);
        }
    }

    private void addNodeCount(StackView source, StackViewResponse stackViewResponse) {
        int nodeCount = 0;
        for (InstanceGroupView instanceGroupView : source.getInstanceGroups()) {
            nodeCount += instanceGroupView.getNodeCount();
        }
        stackViewResponse.setNodeCount(nodeCount);
    }

}
