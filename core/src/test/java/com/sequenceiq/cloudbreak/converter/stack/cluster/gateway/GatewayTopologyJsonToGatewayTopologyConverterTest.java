package com.sequenceiq.cloudbreak.converter.stack.cluster.gateway;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;

import com.sequenceiq.cloudbreak.api.model.ExposedService;
import com.sequenceiq.cloudbreak.api.model.stack.cluster.gateway.GatewayTopologyJson;
import com.sequenceiq.cloudbreak.controller.exception.BadRequestException;
import com.sequenceiq.cloudbreak.controller.validation.stack.cluster.gateway.ExposedServiceListValidator;
import com.sequenceiq.cloudbreak.controller.validation.stack.cluster.gateway.GatewayTopologyJsonValidator;
import com.sequenceiq.cloudbreak.domain.stack.cluster.gateway.ExposedServices;
import com.sequenceiq.cloudbreak.domain.stack.cluster.gateway.GatewayTopology;

@RunWith(MockitoJUnitRunner.class)
public class GatewayTopologyJsonToGatewayTopologyConverterTest {

    private static final String TOPOLOGY_NAME = "topologyName";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    private ConversionService conversionService;

    @Spy
    private final GatewayTopologyJsonValidator validator = new GatewayTopologyJsonValidator(new ExposedServiceListValidator());

    @InjectMocks
    private final GatewayTopologyJsonToGatewayTopologyConverter underTest = new GatewayTopologyJsonToGatewayTopologyConverter();

    @Spy
    private final ExposedServiceListValidator exposedServiceListValidator = new ExposedServiceListValidator();

    @InjectMocks
    private final GatewayTopologyJsonToExposedServicesConverter gatewayTopologyJsonToExposedServicesConverter
            = new GatewayTopologyJsonToExposedServicesConverter();

    @Test
    public void testConvertWithNoTopologyName() {
        GatewayTopologyJson gatewayTopologyJson = new GatewayTopologyJson();
        gatewayTopologyJson.setExposedServices(Collections.singletonList(ExposedService.AMBARI.getKnoxService()));
        thrown.expect(BadRequestException.class);

        underTest.convert(gatewayTopologyJson);
    }

    @Test
    public void testConvertWithNoService() {
        GatewayTopologyJson gatewayTopologyJson = new GatewayTopologyJson();
        gatewayTopologyJson.setTopologyName(TOPOLOGY_NAME);

        GatewayTopology result = underTest.convert(gatewayTopologyJson);

        assertEquals(TOPOLOGY_NAME, result.getTopologyName());
        assertNotNull(validator);
    }

    @Test
    public void testConvertWithAllKnoxServices() throws IOException {
        GatewayTopologyJson gatewayTopologyJson = new GatewayTopologyJson();
        gatewayTopologyJson.setTopologyName(TOPOLOGY_NAME);
        gatewayTopologyJson.setExposedServices(Collections.singletonList(ExposedService.ALL.getServiceName()));
        ExposedServices expectedExposedServices = gatewayTopologyJsonToExposedServicesConverter.convert(gatewayTopologyJson);
        when(conversionService.convert(any(GatewayTopologyJson.class), eq(ExposedServices.class))).thenReturn(expectedExposedServices);

        GatewayTopology result = underTest.convert(gatewayTopologyJson);

        assertEquals(TOPOLOGY_NAME, result.getTopologyName());
        assertTrue(result.getExposedServices().get(ExposedServices.class).getServices().containsAll(ExposedService.getAllKnoxExposed()));
    }

    @Test
    public void testConvertWithInvalidService() {
        GatewayTopologyJson gatewayTopologyJson = new GatewayTopologyJson();
        gatewayTopologyJson.setTopologyName(TOPOLOGY_NAME);
        String invalidService = "INVALID_SERVICE";
        gatewayTopologyJson.setExposedServices(Arrays.asList(ExposedService.AMBARI.getKnoxService(), invalidService));
        thrown.expect(BadRequestException.class);
        thrown.expectMessage(invalidService);

        underTest.convert(gatewayTopologyJson);
    }
}