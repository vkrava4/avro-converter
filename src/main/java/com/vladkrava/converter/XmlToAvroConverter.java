package com.vladkrava.converter;

import org.apache.avro.specific.SpecificRecordBase;

import com.vladkrava.converter.serialization.AvroDeserializer;

/**
 * @author Vlad Krava - vkrava4@gmail.com
 * @since 0.1-SNAPSHOT
 */
public class XmlToAvroConverter<T extends SpecificRecordBase> extends AvroDeserializer<T> implements TargetConverter<T, String> {
    @Override
    public T convert(final String s, final Class<T> aClass) {
        throw new UnsupportedOperationException("This operation in not supported yet.");
    }
}
