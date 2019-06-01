package com.vladkrava.converter;

import org.apache.avro.AvroTypeException;
import org.apache.avro.specific.SpecificRecordBase;

import com.vladkrava.converter.serialization.AvroDeserializer;
import com.vladkrava.converter.serialization.DataSerializationException;

/**
 * Converting data from JSON string to specific Avro object of a type T
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @see SpecificRecordBase
 * @see TargetConverter
 * @see AvroDeserializer
 * @since 0.1-SNAPSHOT
 */
public class JsonToAvroConverter<T extends SpecificRecordBase> extends AvroDeserializer<T> implements TargetConverter<T, String> {

    /**
     * Performs converting to specific Avro object
     *
     * @param s      - JSON source {@link String}
     * @param aClass - specific Avro object type
     * @return T - specific Avro object
     * @throws DataSerializationException - on exceptional conditions during deserialization
     */
    @Override
    public T convert(final String s, final Class<T> aClass) throws DataSerializationException {
        try {
            return s == null ? null : deserialize(s.getBytes(), aClass);
        } catch (AvroTypeException e) {
            throw new DataSerializationException("An issue occurred with object deserialization", e);
        }
    }
}
