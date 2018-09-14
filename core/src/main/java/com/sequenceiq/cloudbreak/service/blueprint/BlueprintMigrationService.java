package com.sequenceiq.cloudbreak.service.blueprint;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.sequenceiq.cloudbreak.blueprint.utils.BlueprintUtils;
import com.sequenceiq.cloudbreak.domain.Blueprint;
import com.sequenceiq.cloudbreak.repository.BlueprintRepository;
import com.sequenceiq.cloudbreak.util.JsonUtil;

@Component
public class BlueprintMigrationService {
    @Inject
    private BlueprintRepository blueprintRepository;

    @Inject
    private BlueprintUtils blueprintUtils;

    public void migrateBlueprints() {
        Iterable<Blueprint> blueprints = blueprintRepository.findAll();
        Set<Blueprint> updatedBlueprints = new HashSet<>();
        for (Blueprint bp : blueprints) {
            if (StringUtils.isEmpty(bp.getStackType()) || StringUtils.isEmpty(bp.getStackVersion())) {
                try {
                    JsonNode root = JsonUtil.readTree(bp.getBlueprintText());
                    bp.setStackType(blueprintUtils.getBlueprintStackName(root));
                    bp.setStackVersion(blueprintUtils.getBlueprintStackVersion(root));
                    updatedBlueprints.add(bp);
                } catch (IOException ex) {
                    bp.setStackType("UNKNOWN");
                    bp.setStackVersion("UNKNOWN");
                }
            }
        }
        if (!updatedBlueprints.isEmpty()) {
            blueprintRepository.saveAll(updatedBlueprints);
        }
    }
}
