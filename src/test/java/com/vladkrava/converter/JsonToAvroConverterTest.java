package com.vladkrava.converter;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Objects;

import org.apache.avro.AvroTypeException;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.vladkrava.converter.serialization.DataSerializationException;
import com.vladkrava.converter.test.domain.DummyObject;

/**
 * @author Vlad Krava - vkrava4@gmail.com
 * @since 0.1-SNAPSHOT
 */
public class JsonToAvroConverterTest {

    private static final String RECORD1_FILE_NAME = "json/given-test-record1.json";
    private static final String RECORD1_FORMATTED_FILE_NAME = "json/given-formatted-test-record1.json";
    private static final String BROKEN_RECORD1_FILE_NAME = "json/broken-test-record1.json";
    private static final String BROKEN_JSON_FILE_NAME = "json/broken.json";

    private DummyObject expectedDummyObject;
    private JsonToAvroConverter<DummyObject> jsonToAvro;


    @Before
    public void setup() {
        this.jsonToAvro = new JsonToAvroConverter<>();
        this.expectedDummyObject = DummyObject.newBuilder()
                .setTestInt(1)
                .setTestString1("TEST_STRING")
                .setTestString2("TEST_STRING_2")
                .setTestArray(Arrays.asList("1", "2", "3"))
                .build();
    }


    @Test
    public void convertRecord1Test() throws Exception {
        final String givenJson = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(RECORD1_FILE_NAME)), "UTF-8");

        assertEquals(this.expectedDummyObject, jsonToAvro.convert(givenJson, DummyObject.class));
    }

    @Test
    public void convertFormattedRecord1Test() throws Exception {
        final String givenJson = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(RECORD1_FORMATTED_FILE_NAME)), "UTF-8");

        assertEquals(this.expectedDummyObject, jsonToAvro.convert(givenJson, DummyObject.class));
    }


    @Test(expected = AvroTypeException.class)
    public void convertBrokenRecord1Test() throws Exception {
        final String givenJson = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(BROKEN_RECORD1_FILE_NAME)), "UTF-8");

        assertEquals(this.expectedDummyObject, jsonToAvro.convert(givenJson, DummyObject.class));
    }

    @Test(expected = DataSerializationException.class)
    public void convertBrokenJsonTest() throws Exception {
        final String givenJson = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(BROKEN_JSON_FILE_NAME)), "UTF-8");

        assertEquals(this.expectedDummyObject, jsonToAvro.convert(givenJson, DummyObject.class));
    }
}