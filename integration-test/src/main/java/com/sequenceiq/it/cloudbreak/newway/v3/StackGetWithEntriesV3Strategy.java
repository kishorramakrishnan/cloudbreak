package com.sequenceiq.it.cloudbreak.newway.v3;

import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.sequenceiq.cloudbreak.api.model.stack.StackResponseEntries;
import com.sequenceiq.it.IntegrationTestContext;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakClient;
import com.sequenceiq.it.cloudbreak.newway.CloudbreakTest;
import com.sequenceiq.it.cloudbreak.newway.Entity;
import com.sequenceiq.it.cloudbreak.newway.StackEntity;
import com.sequenceiq.it.cloudbreak.newway.Strategy;
import com.sequenceiq.it.cloudbreak.newway.log.Log;

public class StackGetWithEntriesV3Strategy implements Strategy {
    private final Set<StackResponseEntries> entries;

    private StackGetWithEntriesV3Strategy(Set<StackResponseEntries> entries) {
        this.entries = entries;
    }

    public static StackGetWithEntriesV3Strategy create(Set<StackResponseEntries> entries) {
        Preconditions.checkNotNull(entries, "entries must be set");
        return new StackGetWithEntriesV3Strategy(entries);
    }

    @Override
    public void doAction(IntegrationTestContext integrationTestContext, Entity entity) throws Exception {
        StackEntity stackEntity = (StackEntity) entity;
        CloudbreakClient client;
        client = integrationTestContext.getContextParam(CloudbreakClient.CLOUDBREAK_CLIENT, CloudbreakClient.class);
        Long workspaceId = integrationTestContext.getContextParam(CloudbreakTest.WORKSPACE_ID, Long.class);
        Log.log(" get stack " + stackEntity.getName());
        stackEntity.setResponse(client.getCloudbreakClient().stackV3Endpoint().getByNameInWorkspace(workspaceId, stackEntity.getName(),
                entries.stream().map(StackResponseEntries::getEntryName).collect(Collectors.toSet())));
        Log.logJSON(" stack get response: ", stackEntity.getResponse());
    }
}
