package com.vladkrava.converter.serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides low level serialization from Avro object to a byte array,
 * which potentially can be converted to any object.
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @see SpecificRecord - Avro object superclass which contains an info related to tbe source and the schema
 * @see ByteArrayOutputStream - byte output stream in which the data is written
 * @see Encoder - interface for encoding Avro schema into {@link ByteArrayOutputStream}
 * @see DatumWriter - Avro schema travercing
 * @see DataSerializationException - checked exception which may occur during the execution
 * @since 0.1-SNAPSHOT
 */
public class AvroSerializer<T extends SpecificRecord> implements Serializer<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvroSerializer.class.getName());

    /**
     * Implements serialization functionality from {@link SpecificRecord} type to a byte array
     *
     * @throws DataSerializationException wrapping any occurrence of {@link IOException} related to
     *                                    I/O operations with data and signals that some other serialization exception
     *                                    has occurred.
     */
    @Override
    public byte[] serialize(final T data) throws DataSerializationException {
        byte[] result = null;

        if (data != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Avro object = {} : {}", data.getSchema().getFullName(), data);
            }

            try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                final Encoder encoder = EncoderFactory.get().jsonEncoder(data.getSchema(), byteArrayOutputStream);
                final DatumWriter<T> datumWriter = new SpecificDatumWriter<>(data.getSchema());

                datumWriter.write(data, encoder);
                encoder.flush();
                result = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                LOGGER.warn("Issue with serialization", e);
                throw new DataSerializationException("Can't serialize the data = '" + data + "'", e);
            }


            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Serialized data = ({})", new String(result));
            }
        } else {
            LOGGER.warn("Can't serialize NULL object. Returning NULL value");
        }

        return result;
    }
}
