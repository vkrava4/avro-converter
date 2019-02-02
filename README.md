# Avro Converter v0.1-SNAPSHOT

Avro Converter enables conversion of Apache Avro object into popular data formats: JSON, XML and CSV.


### General Info
Avro Converter created for supporting data conversion and serialization based on Apache Avro technology.
Avro is an excellent choice due to message compression mechanisms and Schema Registry support especially for whose who are using Event-Driven architecture with Apache Kafka as an event distribution platform.


### Avro Converter will help you to:

 * Convert Avro object into most popular data formats such as JSON, XML and CSV
 * Integrate Avro with Spring MVC
 * Reuse Avro object from the existing Event-Based systems for defining RESTful interface

 ### Output Examples

 #### Avro Schema Example
 ```json
[
  {
     "type": "record",
     "namespace": "com.vladkrava.converter.test.domain",
     "name": "DummyObject",
     "doc": "Dummy Object",
     "fields": [
       { "name": "testString1", "type": "string"},
       { "name": "testString2", "type": "string", "default": "TEST_STRING_2"},
       { "name": "testArray", "type": {"type": "array", "items": "string"}},
       { "name": "testInt", "type": "int"}
     ]
   }
]
 ```
 
 #### Avro Generated Class 
 ```java
DummyObject testDummyObject = DummyObject.newBuilder()
    .setTestString1("TEST_STRING")
    .setTestInt(1)
    .setTestArray(Arrays.asList("1", "2", "3"))
    .build();
 ```

 #### Converted JSON Object 
 ```json
 {
   "testString1": "TEST_STRING",
   "testString2": "TEST_STRING_2",
   "testArray": [
     "1",
     "2",
     "3"
   ],
   "testInt": 1
 }
 ```
 
  #### Converted XML 
  ```xml
  <DummyObject>
      <testArray>1</testArray>
      <testArray>2</testArray>
      <testArray>3</testArray>
      <testInt>1</testInt>
      <testString1>TEST_STRING</testString1>
      <testString2>TEST_STRING_2</testString2>
  </DummyObject>
  ```