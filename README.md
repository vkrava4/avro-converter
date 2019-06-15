# Avro Converter

Avro Converter enables conversion of Apache Avro object into popular data formats: JSON, XML etc.


### General Info
Avro Converter is created for supporting data conversion and serialization based on Apache Avro technology.
Avro is an excellent choice due to message compression mechanisms and Schema Registry support especially for whose who are using Event-Driven architecture with Apache Kafka as an event distribution platform.


### Avro Converter will help you to:

 * Convert Avro object into most popular data formats such as JSON, XML and CSV
 * Integrate Avro with Spring MVC
 * Reuse Avro object from the existing Event-Based systems for defining RESTful interface

## Installing
### Apache Maven
```xml
<dependency>
  <groupId>com.vladkrava</groupId>
  <artifactId>avro-converter</artifactId>
  <version>1.0.1</version>
</dependency>
```

### Gradle
```
compile group: 'com.vladkrava', name: 'avro-converter', version: '1.0.1'
```


#### Hands-On: Avro Converter integration with Spring MVC
```java
// Spring Web MVC configuration

@Override
public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
    converters.add(0, new AvroJsonHttpMessageConverter<>());
    converters.add(1, new AvroXmlHttpMessageConverter<>());
}

@Bean
public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    final RestTemplate restTemplate = builder.build();
    restTemplate.getMessageConverters().add(0, new AvroJsonHttpMessageConverter<>());
    restTemplate.getMessageConverters().add(1, new AvroXmlHttpMessageConverter<>());
    return restTemplate;
}
```

```java
// @Controller class file

// Handling application/avro-xml Media-Type
@PutMapping(produces = AVRO_XML, consumes = AVRO_XML)
public ResponseEntity myXmlMethod(@RequestBody DummyObject dummyObject) {
    // TODO: A MAGIC
}


// Handling application/avro-json Media-Type
@PutMapping(produces = AVRO_JSON, consumes = AVRO_JSON)
public ResponseEntity myJsonMethod(@RequestBody DummyObject dummyObject) {
    // TODO: A MAGIC
}
```


#### Hands-On: Avro Converter manual operations 
```java
// Avro to JSON String
final String jsonString = new AvroToJsonConverter<DummyObject>().convert(dummyObject);

// Avro to XML String
final String xmlString = new AvroToXmlConverter<DummyObject>().convert(dummyObject);

// JSON String to Avro
final DummyObject dummyObject = new JsonToAvroConverter<DummyObject>().convert(jsonString, DummyObject.class);

// XML String to Avro
final DummyObject dummyObject = new XmlToAvroConverter<DummyObject>().convert(xmlString, DummyObject.class);
```

#### Useful information and resources
Feeling difficulties with understanding Apache Avro concepts or onboarding Avro Converter to your project?
Visit useful links which mentioned down below to get explicit information to all your questions:

 * [Avro Converter Article.](https://medium.com/@vkrava4/avro-converter-serialising-apache-avro-objects-via-rest-api-and-other-transformations-3402255e437e) Discussing Avro Converter concepts, setup and features 
 * [Avro Converter Hands-On Project.](https://gitlab.com/vkrava4-hands-on/avro-converter-demo) Quick overview on integration of Spring Boot, Apache Kafka and Avro Converter 
 * [Apache Avro Homepage.](https://avro.apache.org) The place where everything has started
 * [Apache Avro Getting Started Page.](https://avro.apache.org/docs/current/gettingstartedjava.html) A short guide for getting started with Apache Avro using Java


## Contributing
 * Are you interested in this project? 
 * Have any ideas how to improve it?
 * Found a bug?
 
Just open an issue [HERE](https://gitlab.com/vkrava4/avro-converter/issues "Avro Converter's Bug Tracking System") and tell the world about your thoughts and concerns.
If you'd like to contribute, please fork the repository and make a change as you'd like. Pull requests are warmly welcome.
