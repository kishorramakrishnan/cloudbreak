package com.sequenceiq.cloudbreak.api.endpoint.v3;

import java.util.Collection;
import java.util.Set;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sequenceiq.cloudbreak.api.model.VersionCheckResult;
import com.sequenceiq.cloudbreak.api.model.filesystem.CloudStorageSupportedResponse;
import com.sequenceiq.cloudbreak.api.model.rds.RDSBuildRequest;
import com.sequenceiq.cloudbreak.api.model.rds.RdsBuildResult;
import com.sequenceiq.cloudbreak.api.model.stack.StackMatrix;
import com.sequenceiq.cloudbreak.doc.ContentType;
import com.sequenceiq.cloudbreak.doc.ControllerDescription;
import com.sequenceiq.cloudbreak.doc.OperationDescriptions.UtilityOpDescription;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/v3/utils")
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/v3/utils", description = ControllerDescription.UTIL_DESCRIPTION, protocols = "http,https")
public interface UtilV3Endpoint {

    @GET
    @Path("client/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = UtilityOpDescription.CHECK_CLIENT_VERSION, produces = ContentType.JSON,
            nickname = "checkClientVersionV3")
    VersionCheckResult checkClientVersion(@PathParam("version") String version);

    @POST
    @Path("rds-database")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = UtilityOpDescription.CREATE_DATABASE, produces = ContentType.JSON, nickname = "createRDSDatabaseUtilV3")
    RdsBuildResult buildRdsConnection(@Valid RDSBuildRequest rdsBuildRequest, @QueryParam("target") Set<String> targets);

    @GET
    @Path("stackmatrix")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = UtilityOpDescription.STACK_MATRIX, produces = ContentType.JSON, nickname = "getStackMatrixUtilV3")
    StackMatrix getStackMatrix();

    @GET
    @Path("cloudstoragematrix")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = UtilityOpDescription.CLOUD_STORAGE_MATRIX, produces = ContentType.JSON, nickname = "getCloudStorageMatrixV3",
            notes = "Define stack version at least at patch level eg. 2.6.0")
    Collection<CloudStorageSupportedResponse> getCloudStorageMatrix(@QueryParam("stackVersion") String stackVersion);
}
