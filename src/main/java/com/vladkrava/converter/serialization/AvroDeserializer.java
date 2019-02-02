package com.vladkrava.converter.serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides deserialization from the byte array to Avro object
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @since 0.1-SNAPSHOT
 */
public class AvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvroSerializer.class.getName());

    @Override
    public T deserialize(final byte[] data, final Class<? extends T> aClass) throws DataSerializationException {
        try {
            T result = null;
            if (data != null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("data = ({})", new String(data));
                }
                final Schema schema = aClass.newInstance().getSchema();
                final DatumReader<T> datumReader = new SpecificDatumReader<>(schema);
                final Decoder decoder = DecoderFactory.get().jsonDecoder(schema, new ByteArrayInputStream(data));

                result = datumReader.read(null, decoder);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Avro object = {} : {}", aClass.getName(), result);
                }
            }
            return result;
        } catch (InstantiationException | IllegalAccessException | IOException e) {
            throw new DataSerializationException("Can't deserialize data '" + new String(data) + "'", e);
        }
    }
}
