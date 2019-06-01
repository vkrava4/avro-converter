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
import org.json.JSONObject;
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
public class AvroToJsonConverterTest {

    private static final String TEST_ENCODING = "UTF-8";

    private static final String EXPECTED_DUMMY_FILE_NAME = "json/expected-dummy.json";
    private static final String EXPECTED_DUMMY_PRIMITIVES_FILE_NAME = "json/dummy-primitives.json";
    private static final String EXPECTED_DUMMY_COMPLEX_FILE_NAME = "json/dummy-complex.json";
    private static final String EXPECTED_DUMMY_ARRAYS_FILE_NAME = "json/dummy-arrays.json";

    //    DUMMY
    private AvroToJsonConverter<DummyObject> avroToJson;
    private DummyObject testDummyObject;

    //    PRIMITIVES
    private AvroToJsonConverter<DummyObjectPrimitives> avroToPrimitivesJson;
    private DummyObjectPrimitives testDummyPrimitivesObject;

    //    COMPLEX
    private AvroToJsonConverter<DummyObjectComplex> avroToComplexJson;
    private DummyObjectComplex testDummyComplexObject;


    //    ARRAYS
    private AvroToJsonConverter<DummyArrays> avroToDummyArraysJson;
    private DummyArrays testDummyArraysObject;

    @Before
    public void setup() {
        setupDummyTestData();
        setupDummyPrimitivesTestData();
        setupDummyComplexTestData();
        setupDummyArraysTestData();
    }

    @Test
    public void convertToJsonStringTest() throws DataSerializationException, IOException {
        final String expectedJson = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(EXPECTED_DUMMY_FILE_NAME)), TEST_ENCODING);

        assertEquals(expectedJson, this.avroToJson.convert(this.testDummyObject));
    }

    @Test
    public void convertPrimitivesToJsonStringTest() throws DataSerializationException, IOException {
        final String expectedJson = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(EXPECTED_DUMMY_PRIMITIVES_FILE_NAME)), TEST_ENCODING);

        assertEquals(expectedJson, this.avroToPrimitivesJson.convert(this.testDummyPrimitivesObject));
    }

    @Test
    public void convertArraysToJsonStringTest() throws DataSerializationException, IOException {
        final String expectedJson = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(EXPECTED_DUMMY_ARRAYS_FILE_NAME)), TEST_ENCODING);

        assertEquals(expectedJson, this.avroToDummyArraysJson.convert(this.testDummyArraysObject));
    }

    @Test
    public void convertComplexToJsonStringTest() throws DataSerializationException, IOException {
        final String expectedJson = IOUtils.toString(Objects.requireNonNull(getClass().getClassLoader()
                .getResourceAsStream(EXPECTED_DUMMY_COMPLEX_FILE_NAME)), TEST_ENCODING);

        assertEquals(new JSONObject(expectedJson).toString(), new JSONObject(avroToComplexJson.convert(testDummyComplexObject)).toString());
    }

    @Test
    public void convertNullToJsonStringTest() throws DataSerializationException {
        assertNull(this.avroToJson.convert(null));
    }

    private void setupDummyTestData() {
        this.avroToJson = new AvroToJsonConverter<>();
        this.testDummyObject = DummyObject.newBuilder()
                .setTestString1("TEST_STRING")
                .setTestInt(1)
                .setTestArray(Arrays.asList("1", "2", "3"))
                .build();
    }

    private void setupDummyPrimitivesTestData() {
        this.avroToPrimitivesJson = new AvroToJsonConverter<>();
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
        this.avroToComplexJson = new AvroToJsonConverter<>();

        final Map<String, Float> testMap = new LinkedHashMap<>();
        testMap.put("TEST_MAP_VALUE1", 99.99f);
        testMap.put("TEST_MAP_VALUE2", 299.99f);

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
                .build();
    }

    private void setupDummyArraysTestData() {
        this.avroToDummyArraysJson = new AvroToJsonConverter<>();

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