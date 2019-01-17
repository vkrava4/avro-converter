package com.vladkrava.converter;

import org.apache.avro.specific.SpecificRecord;

import com.vladkrava.converter.serialization.AvroSerializer;
import com.vladkrava.converter.serialization.DataSerializationException;

/**
 * Converting data from Avro's SpecificRecord object to JSON string
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @see SpecificRecord
 * @see Converter
 * @see AvroSerializer
 * @since 0.1-SNAPSHOT
 */
public class AvroToJsonConverter extends AvroSerializer<SpecificRecord> implements Converter<String, SpecificRecord> {

    /**
     * Performs converting to JSON string
     *
     * @param specificRecord - Avro's source object
     * @return JSON {@link String} on successful data serialization or null if Avro's source object is null
     * @throws DataSerializationException - on any exception with serialization from source datatype to a byte array
     */
    @Override
    public String convert(final SpecificRecord specificRecord) throws DataSerializationException {
        final byte[] json = serialize(specificRecord);
        return json == null ? null : new String(json);
    }
}
