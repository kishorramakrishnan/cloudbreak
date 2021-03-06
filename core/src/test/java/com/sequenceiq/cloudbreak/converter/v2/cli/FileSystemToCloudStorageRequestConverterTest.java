package com.sequenceiq.cloudbreak.converter.v2.cli;

import static com.sequenceiq.cloudbreak.api.model.filesystem.FileSystemType.ABFS;
import static com.sequenceiq.cloudbreak.api.model.filesystem.FileSystemType.ADLS;
import static com.sequenceiq.cloudbreak.api.model.filesystem.FileSystemType.GCS;
import static com.sequenceiq.cloudbreak.api.model.filesystem.FileSystemType.S3;
import static com.sequenceiq.cloudbreak.api.model.filesystem.FileSystemType.WASB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

import com.sequenceiq.cloudbreak.api.model.filesystem.AbfsFileSystem;
import com.sequenceiq.cloudbreak.api.model.filesystem.AdlsFileSystem;
import com.sequenceiq.cloudbreak.api.model.filesystem.GcsFileSystem;
import com.sequenceiq.cloudbreak.api.model.filesystem.S3FileSystem;
import com.sequenceiq.cloudbreak.api.model.filesystem.WasbFileSystem;
import com.sequenceiq.cloudbreak.api.model.v2.CloudStorageRequest;
import com.sequenceiq.cloudbreak.api.model.v2.StorageLocationRequest;
import com.sequenceiq.cloudbreak.api.model.v2.filesystem.AbfsCloudStorageParameters;
import com.sequenceiq.cloudbreak.api.model.v2.filesystem.AdlsCloudStorageParameters;
import com.sequenceiq.cloudbreak.api.model.v2.filesystem.GcsCloudStorageParameters;
import com.sequenceiq.cloudbreak.api.model.v2.filesystem.S3CloudStorageParameters;
import com.sequenceiq.cloudbreak.api.model.v2.filesystem.WasbCloudStorageParameters;
import com.sequenceiq.cloudbreak.domain.FileSystem;
import com.sequenceiq.cloudbreak.domain.StorageLocation;
import com.sequenceiq.cloudbreak.domain.StorageLocations;
import com.sequenceiq.cloudbreak.domain.json.Json;

public class FileSystemToCloudStorageRequestConverterTest {

    @InjectMocks
    private FileSystemToCloudStorageRequestConverter underTest;

    @Mock
    private ConversionService conversionService;

    @Mock
    private FileSystem fileSystem;

    @Mock
    private Json configurations;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(fileSystem.getConfigurations()).thenReturn(configurations);
    }

    @Test
    public void testConvertWhenLocationIsNullThenEmptySetShouldBeSet() {
        // wasb just for testing reason
        when(fileSystem.getType()).thenReturn(WASB);
        when(fileSystem.getLocations()).thenReturn(null);

        CloudStorageRequest result = underTest.convert(fileSystem);

        assertNotNull(result.getLocations());
        assertTrue(result.getLocations().isEmpty());
    }

    @Test
    public void testConvertWhenLocationNotNullButItsValueNullThenEmptySetShouldBeSet() {
        // wasb just for testing reason
        when(fileSystem.getType()).thenReturn(WASB);
        Json locations = mock(Json.class);
        when(locations.getValue()).thenReturn(null);
        when(fileSystem.getLocations()).thenReturn(locations);

        CloudStorageRequest result = underTest.convert(fileSystem);

        assertNotNull(result.getLocations());
        assertTrue(result.getLocations().isEmpty());
    }

    @Test
    public void testConvertWhenLocationHasValidValueButStorageLocationsIsNullThenLocationsShouldBeEmpty() throws IOException {
        // wasb just for testing reason
        when(fileSystem.getType()).thenReturn(WASB);
        Json locations = mock(Json.class);
        when(locations.getValue()).thenReturn("some value");
        when(locations.get(StorageLocations.class)).thenReturn(null);
        when(fileSystem.getLocations()).thenReturn(locations);

        CloudStorageRequest result = underTest.convert(fileSystem);

        assertNotNull(result.getLocations());
        assertTrue(result.getLocations().isEmpty());
    }

    @Test
    public void testConvertWhelLocationsAreExistsThenTheseLocationsShouldBeStored() throws IOException {
        // wasb just for testing reason
        when(fileSystem.getType()).thenReturn(WASB);
        StorageLocations storageLocations = mock(StorageLocations.class);
        StorageLocation location = new StorageLocation();
        when(storageLocations.getLocations()).thenReturn(Collections.singleton(location));
        Json locations = mock(Json.class);
        when(locations.getValue()).thenReturn("some value");
        when(locations.get(StorageLocations.class)).thenReturn(storageLocations);
        when(fileSystem.getLocations()).thenReturn(locations);
        when(conversionService.convert(location, StorageLocationRequest.class)).thenReturn(new StorageLocationRequest());

        CloudStorageRequest result = underTest.convert(fileSystem);

        assertNotNull(result.getLocations());
        assertEquals(1L, result.getLocations().size());
        verify(conversionService, times(1)).convert(any(StorageLocation.class), eq(StorageLocationRequest.class));
    }

    @Test
    public void testConvertWhenTypeIsAdlsThenExpectedAdlsFileSystemShouldBeSet() throws IOException {
        when(fileSystem.getType()).thenReturn(ADLS);
        AdlsFileSystem adls = mock(AdlsFileSystem.class);
        when(configurations.get(AdlsFileSystem.class)).thenReturn(adls);
        AdlsCloudStorageParameters expected = mock(AdlsCloudStorageParameters.class);
        when(conversionService.convert(adls, AdlsCloudStorageParameters.class)).thenReturn(expected);

        CloudStorageRequest result = underTest.convert(fileSystem);

        assertEquals(expected, result.getAdls());
        verify(conversionService, times(1)).convert(any(AdlsFileSystem.class), eq(AdlsCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(GcsFileSystem.class), eq(GcsCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(S3FileSystem.class), eq(S3CloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(WasbFileSystem.class), eq(WasbCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(AbfsFileSystem.class), eq(AbfsCloudStorageParameters.class));
    }

    @Test
    public void testConvertWhenTypeIsGcsThenExpectedGcsFileSystemShouldBeSet() throws IOException {
        when(fileSystem.getType()).thenReturn(GCS);
        GcsFileSystem gcs = mock(GcsFileSystem.class);
        when(configurations.get(GcsFileSystem.class)).thenReturn(gcs);
        GcsCloudStorageParameters expected = mock(GcsCloudStorageParameters.class);
        when(conversionService.convert(gcs, GcsCloudStorageParameters.class)).thenReturn(expected);

        CloudStorageRequest result = underTest.convert(fileSystem);

        assertEquals(expected, result.getGcs());
        verify(conversionService, times(1)).convert(any(GcsFileSystem.class), eq(GcsCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(AdlsFileSystem.class), eq(AdlsCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(S3FileSystem.class), eq(S3CloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(WasbFileSystem.class), eq(WasbCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(AbfsFileSystem.class), eq(AbfsCloudStorageParameters.class));
    }

    @Test
    public void testConvertWhenTypeIsS3ThenExpectedS3FileSystemShouldBeSet() throws IOException {
        when(fileSystem.getType()).thenReturn(S3);
        S3FileSystem s3 = mock(S3FileSystem.class);
        when(configurations.get(S3FileSystem.class)).thenReturn(s3);
        S3CloudStorageParameters expected = mock(S3CloudStorageParameters.class);
        when(conversionService.convert(s3, S3CloudStorageParameters.class)).thenReturn(expected);

        CloudStorageRequest result = underTest.convert(fileSystem);

        assertEquals(expected, result.getS3());
        verify(conversionService, times(1)).convert(any(S3FileSystem.class), eq(S3CloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(GcsFileSystem.class), eq(GcsCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(AdlsFileSystem.class), eq(AdlsCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(WasbFileSystem.class), eq(WasbCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(AbfsFileSystem.class), eq(AbfsCloudStorageParameters.class));
    }

    @Test
    public void testConvertWhenTypeIsWasbThenExpectedWasbFileSystemShouldBeSet() throws IOException {
        when(fileSystem.getType()).thenReturn(WASB);
        WasbFileSystem wasb = mock(WasbFileSystem.class);
        when(configurations.get(WasbFileSystem.class)).thenReturn(wasb);
        WasbCloudStorageParameters expected = mock(WasbCloudStorageParameters.class);
        when(conversionService.convert(wasb, WasbCloudStorageParameters.class)).thenReturn(expected);

        CloudStorageRequest result = underTest.convert(fileSystem);

        assertEquals(expected, result.getWasb());
        verify(conversionService, times(1)).convert(any(WasbFileSystem.class), eq(WasbCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(S3FileSystem.class), eq(S3CloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(GcsFileSystem.class), eq(GcsCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(AdlsFileSystem.class), eq(AdlsCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(AbfsFileSystem.class), eq(AbfsCloudStorageParameters.class));
    }

    @Test
    public void testConvertWhenTypeIsAbfsThenExpectedAbfsFileSystemShouldBeSet() throws IOException {
        when(fileSystem.getType()).thenReturn(ABFS);
        AbfsFileSystem abfs = mock(AbfsFileSystem.class);
        when(configurations.get(AbfsFileSystem.class)).thenReturn(abfs);
        AbfsCloudStorageParameters expected = mock(AbfsCloudStorageParameters.class);
        when(conversionService.convert(abfs, AbfsCloudStorageParameters.class)).thenReturn(expected);

        CloudStorageRequest result = underTest.convert(fileSystem);

        assertEquals(expected, result.getAbfs());
        verify(conversionService, times(1)).convert(any(AbfsFileSystem.class), eq(AbfsCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(WasbFileSystem.class), eq(WasbCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(S3FileSystem.class), eq(S3CloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(GcsFileSystem.class), eq(GcsCloudStorageParameters.class));
        verify(conversionService, times(0)).convert(any(AdlsFileSystem.class), eq(AdlsCloudStorageParameters.class));
    }

    @Test
    public void testConvertWhenGettingFileSystemFromConfigurationThrowsExceptionThenIWillBeCatchedAndNoFileSystemWillBeSet() throws IOException {
        // adls just for testing reason
        when(fileSystem.getType()).thenReturn(ADLS);
        when(configurations.get(any(Class.class))).thenThrow(new IOException("some message"));

        CloudStorageRequest result = underTest.convert(fileSystem);

        assertNull(result.getAdls());
        assertNull(result.getGcs());
        assertNull(result.getS3());
        assertNull(result.getWasb());
        assertNull(result.getAbfs());
    }

}