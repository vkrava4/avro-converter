package com.vladkrava.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
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
 * @since 0.1-SNAPSHOT
 */
public class AvroToXmlConverterTest {

    private static final String TEST_ENCODING = "UTF-8";

    private static final String DUMMY_FILE_NAME = "xml/expected-dummy.xml";
    private static final String DUMMY_PRIMITIVES_FILE_NAME = "xml/dummy-primitives.xml";
    private static final String DUMMY_COMPLEX_FILE_NAME = "xml/dummy-complex.xml";
    private static final String DUMMY_ARRAYS_FILE_NAME = "xml/dummy-arrays.xml";

    //    DUMMY
    private AvroToXmlConverter<DummyObject> avroToXml;
    private DummyObject testDummyObject;

    //    PRIMITIVES
    private DummyObjectPrimitives testDummyPrimitivesObject;
    private AvroToXmlConverter<DummyObjectPrimitives> avroToPrimitivesXml;

    //    COMPLEX
    private AvroToXmlConverter<DummyObjectComplex> avroToComplexXml;
    private DummyObjectComplex testDummyComplexObject;

    //    COMPLEX
    private AvroToXmlConverter<DummyArrays> avroToDummyArraysXml;
    private DummyArrays testDummyArraysObject;

    @Before
    public void setup() {
        setupDummyTestData();
        setupDummyPrimitivesTestData();
        setupDummyComplexTestData();
        setupDummyArraysTestData();
    }


    @Test
    public void convertDummyToXmlStringTest() throws DataSerializationException, IOException {
        final String expectedXml = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(DUMMY_FILE_NAME)), TEST_ENCODING);

        assertEquals(expectedXml, this.avroToXml.convert(this.testDummyObject));
    }

    @Test
    public void convertDummyPrimitivesToXmlStringTest() throws DataSerializationException, IOException {
        final String expectedXml = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(DUMMY_PRIMITIVES_FILE_NAME)), TEST_ENCODING);

        assertEquals(expectedXml, this.avroToPrimitivesXml.convert(this.testDummyPrimitivesObject));
    }


    @Test
    public void convertComplexToXmlStringTest() throws DataSerializationException, IOException {
        final String expectedXml = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(DUMMY_COMPLEX_FILE_NAME)), TEST_ENCODING);

        assertEquals(expectedXml, this.avroToComplexXml.convert(testDummyComplexObject));
    }


    @Test
    public void convertArraysToXmlStringTest() throws DataSerializationException, IOException {
        final String expectedXml = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(DUMMY_ARRAYS_FILE_NAME)), TEST_ENCODING);

        assertEquals(expectedXml, this.avroToDummyArraysXml.convert(testDummyArraysObject));
    }

    @Test
    public void convertNullToXmlStringTest() throws DataSerializationException {
        assertNull(this.avroToXml.convert(null));
    }


    private void setupDummyTestData() {
        this.avroToXml = new AvroToXmlConverter<>();
        this.testDummyObject = DummyObject.newBuilder()
                .setTestString1("TEST_STRING")
                .setTestInt(1)
                .setTestArray(Arrays.asList("1", "2", "3"))
                .build();
    }

    private void setupDummyPrimitivesTestData() {
        this.avroToPrimitivesXml = new AvroToXmlConverter<>();
        this.testDummyPrimitivesObject = DummyObjectPrimitives.newBuilder()
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

        this.avroToComplexXml = new AvroToXmlConverter<>();
        final DummyObjectPrimitives.Builder primitivesBuilder = DummyObjectPrimitives.newBuilder()
                .setTestInt(1)
                .setTestDouble(2d)
                .setTestFloat(3f)
                .setTestLong(4L)
                .setTestBoolean(true)
                .setTestString("TEST_STRING_PRIMITIVES")
                .setTestBytes(ByteBuffer.wrap("TEST_BYTES".getBytes()));

        this.testDummyComplexObject = DummyObjectComplex.newBuilder()
                .setTestString("TEST_STRING_1")
                .setTestEnum(TestEnum.YES)
                .setTestList(Arrays.asList(InnerClass.newBuilder().setTestId(10L).setTestName("TEST_STRING_INNER_CLASS_1").build(),
                        InnerClass.newBuilder().setTestId(20L).setTestName("TEST_STRING_INNER_CLASS_2").build()))
                .setTestMap(testMap)
                .setTestPrimitivesBuilder(primitivesBuilder)
                .setTestUnion(666.66d)
                .build();
    }

    private void setupDummyArraysTestData() {
        this.avroToDummyArraysXml = new AvroToXmlConverter<>();

        final Map<String, Long> testMap1 = new LinkedHashMap<>();
        testMap1.put("VLAD", 1993L);
        testMap1.put("KRAVA", 1111993L);


        final Map<String, Long> testMap2 = new LinkedHashMap<>();
        testMap2.put("vkrava4", 1L);
        testMap2.put("@gmail.com", 10000000000L);

        final Map<String, Long> testEmptyMap3 = new LinkedHashMap<>();

        final List<List<List<String>>> superComplexDataList = new ArrayList<>(2);
        final List<List<String>> complexDataList = new ArrayList<>(2);
        final List<List<String>> complexDataList2 = new ArrayList<>(2);
        final List<String> dataList = new ArrayList<>(2);
        final List<String> dataList_1 = new ArrayList<>(2);
        final List<String> dataList2 = new ArrayList<>(2);
        final List<String> dataList2_1 = new ArrayList<>(2);

        dataList.addAll(Arrays.asList("HI", "ALL"));
        dataList_1.addAll(Arrays.asList("This", "is"));
        dataList2.addAll(Arrays.asList("ME", "GOING"));
        dataList2_1.addAll(Arrays.asList("HOME", "!"));

        complexDataList.addAll(Arrays.asList(dataList, dataList_1));
        complexDataList2.addAll(Arrays.asList(dataList2, dataList2_1));

        superComplexDataList.addAll(Arrays.asList(complexDataList, complexDataList2));

        this.testDummyArraysObject = DummyArrays.newBuilder()
                .setTestArrayLevel1(superComplexDataList)
                .setTestArrayMap(Arrays.asList(testMap1, testMap2, testEmptyMap3))
                .build();
    }
}