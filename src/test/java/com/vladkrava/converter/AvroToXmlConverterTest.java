package com.vladkrava.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.vladkrava.converter.serialization.DataSerializationException;
import com.vladkrava.converter.test.domain.DummyObject;

/**
 * @author Vlad Krava - vkrava4@gmail.com
 * @since 0.1-SNAPSHOT
 */
public class AvroToXmlConverterTest {

    private static final String EXPECTED_RECORD1_FILE_NAME = "xml/expected-test-record1.xml";

    private AvroToXmlConverter<DummyObject> avroToXml;
    private DummyObject testDummyObject;

    @Before
    public void setup() {
        avroToXml = new AvroToXmlConverter<>();
        testDummyObject = DummyObject.newBuilder()
                .setTestString1("TEST_STRING")
                .setTestInt(1)
                .setTestArray(Arrays.asList("1", "2", "3"))
                .build();

    }


    @Test
    public void convertToXmlStringTest() throws DataSerializationException, IOException {
        final String expectedXml = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(EXPECTED_RECORD1_FILE_NAME)), "UTF-8");

        assertEquals(expectedXml, this.avroToXml.convert(this.testDummyObject));
    }

    @Test
    public void convertNullToXmlStringTest() throws DataSerializationException {
        assertNull(this.avroToXml.convert(null));
    }
}