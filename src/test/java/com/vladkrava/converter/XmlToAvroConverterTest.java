package com.vladkrava.converter;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;
import org.junit.Before;
import org.junit.Test;

import com.vladkrava.converter.serialization.DataSerializationException;
import com.vladkrava.converter.test.domain.DummyArrays;
import com.vladkrava.converter.test.domain.DummyObject;
import com.vladkrava.converter.test.domain.DummyObjectComplex;
import com.vladkrava.converter.test.domain.DummyObjectPrimitives;
import com.vladkrava.converter.test.domain.InnerClass;
import com.vladkrava.converter.test.domain.TestEnum;

/**
 * @author Vlad Krava - vkrava4@gmail.com
 * @since 0.2-SNAPSHOT
 */
public class XmlToAvroConverterTest {
    private static final String TEST_ENCODING = "UTF-8";

    private static final String DUMMY_FILE_NAME = "xml/given-dummy.xml";
    private static final String DUMMY_PRIMITIVES_FILE_NAME = "xml/dummy-primitives.xml";
    private static final String DUMMY_COMPLEX_FILE_NAME = "xml/dummy-complex.xml";
    private static final String DUMMY_FORMATTED_FILE_NAME = "xml/given-formatted-dummy.xml";
    private static final String BROKEN_DUMMY_FILE_NAME = "xml/broken-dummy.xml";
    private static final String BROKEN_XML_FILE_NAME = "xml/broken.xml";

    //    DUMMY
    private DummyObject expectedDummyObject;
    private XmlToAvroConverter<DummyObject> xmlToDummyAvro;

    //    PRIMITIVES
    private DummyObjectPrimitives expectedDummyPrimitivesObject;
    private XmlToAvroConverter<DummyObjectPrimitives> xmlToDummyPrimitivesAvro;

    //    COMPLEX
    private DummyObjectComplex expectedDummyComplexObject;
    private XmlToAvroConverter<DummyObjectComplex> xmlToDummyComplexAvro;


    @Before
    public void setup() {
        setupDummyTestData();
        setupDummyPrimitivesTestData();
        setupDummyComplexTestData();
    }


    @Test
    public void convertDummyTest() throws Exception {
        final String givenXml = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(DUMMY_FILE_NAME)), TEST_ENCODING);

        assertEquals(this.expectedDummyObject, xmlToDummyAvro.convert(givenXml, DummyObject.class));
    }

    @Test
    public void convertPrimitivesRecordTest() throws Exception {
        final String givenXml = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(DUMMY_PRIMITIVES_FILE_NAME)), TEST_ENCODING);

        assertEquals(this.expectedDummyPrimitivesObject, xmlToDummyPrimitivesAvro.convert(givenXml, DummyObjectPrimitives.class));
    }


    @Test
    public void convertFormattedDummyTest() throws Exception {
        final String givenXml = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(DUMMY_FORMATTED_FILE_NAME)), TEST_ENCODING);

        assertEquals(this.expectedDummyObject, xmlToDummyAvro.convert(givenXml, DummyObject.class));
    }

    @Test
    public void convertComplexDummyTest() throws Exception {
        final String givenXml = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(DUMMY_COMPLEX_FILE_NAME)), TEST_ENCODING);

        assertEquals(this.expectedDummyComplexObject, xmlToDummyComplexAvro.convert(givenXml, DummyObjectComplex.class));
    }


    @Test(expected = DataSerializationException.class)
    public void convertBrokenDummyTest() throws Exception {
        final String givenXml = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(BROKEN_DUMMY_FILE_NAME)), TEST_ENCODING);

        assertEquals(this.expectedDummyObject, xmlToDummyAvro.convert(givenXml, DummyObject.class));
    }

    @Test(expected = DataSerializationException.class)
    public void convertBrokenJsonTest() throws Exception {
        final String givenXml = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(BROKEN_XML_FILE_NAME)), TEST_ENCODING);

        assertEquals(this.expectedDummyObject, xmlToDummyAvro.convert(givenXml, DummyObject.class));
    }

    private void setupDummyTestData() {
        this.xmlToDummyAvro = new XmlToAvroConverter<>();
        this.expectedDummyObject = DummyObject.newBuilder()
                .setTestInt(1)
                .setTestString1("TEST_STRING")
                .setTestString2("TEST_STRING_2")
                .setTestArray(Arrays.asList("1", "2", "3"))
                .build();
    }

    private void setupDummyPrimitivesTestData() {
        this.xmlToDummyPrimitivesAvro = new XmlToAvroConverter<>();
        this.expectedDummyPrimitivesObject = DummyObjectPrimitives.newBuilder()
                .setTestInt(1)
                .setTestDouble(2d)
                .setTestFloat(3f)
                .setTestLong(4L)
                .setTestBoolean(true)
                .setTestString("TEST")
                .setTestBytes(ByteBuffer.wrap("TEST_BYTES".getBytes()))
                .build();
    }


    private void setupDummyComplexTestData() {
        final Map<String, Float> testMap = new LinkedHashMap<>();
        testMap.put("TEST_MAP_VALUE1", 99.99f);
        testMap.put("TEST_MAP_VALUE2", 299.99f);

        this.xmlToDummyComplexAvro = new XmlToAvroConverter<>();
        final DummyObjectPrimitives.Builder primitivesBuilder = DummyObjectPrimitives.newBuilder()
                .setTestInt(1)
                .setTestDouble(2d)
                .setTestFloat(3f)
                .setTestLong(4L)
                .setTestBoolean(true)
                .setTestString("TEST_STRING_PRIMITIVES")
                .setTestBytes(ByteBuffer.wrap("TEST_BYTES".getBytes()));

        this.expectedDummyComplexObject = DummyObjectComplex.newBuilder()
                .setTestString("TEST_STRING_1")
                .setTestEnum(TestEnum.YES)
                .setTestList(Arrays.asList(InnerClass.newBuilder().setTestId(10L).setTestName("TEST_STRING_INNER_CLASS_1").build(),
                        InnerClass.newBuilder().setTestId(20L).setTestName("TEST_STRING_INNER_CLASS_2").build()))
                .setTestMap(testMap)
                .setTestPrimitivesBuilder(primitivesBuilder)
                .setTestUnion(666.66d)
                .build();
    }
}
