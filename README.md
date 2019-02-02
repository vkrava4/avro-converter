# Avro Converter

Avro Converter enables conversion of Apache Avro object into popular data formats: JSON, XML and CSV.


### General Info
Avro Converter created for supporting data conversion and serialization based on Apache Avro technology.
Avro is an excellent choice due to message compression mechanisms and Schema Registry support especially for whose who are using Event-Driven architecture with Apache Kafka as an event distribution platform.


### Avro Converter will help you to:

 * Convert Avro object into most popular data formats such as JSON, XML and CSV
 * Integrate Avro with Spring MVC
 * Reuse Avro object from the existing Event-Based systems for defining RESTful interface


### Examples

#### Enable AvroToJsonHttpMessageConverter for Spring MVC
```java
// Spring Web MVC configuration

@Override
public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
    super.configureMessageConverters(converters);
    converters.add(new AvroToJsonHttpMessageConverter<SpecificRecordBase>());
}

@Bean
public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    final RestTemplate restTemplate = builder.build();
    restTemplate.getMessageConverters().add(0, new AvroToJsonHttpMessageConverter<SpecificRecordBase>());
    return restTemplate;
}
```


#### Manual Avro to JSON 
```java
final String jsonString = new AvroToJsonConverter<DummyObject>().convert(dummyObject);
```

 
#### Manual Avro to XML
```java
final String xmlString = new AvroToXmlConverter<DummyObject>().convert(dummyObject);
```

 
#### Manual JSON to Avro
```java
final DummyObject dummyObject = new JsonToAvroConverter<DummyObject>().convert(jsonString, DummyObject.class);
```