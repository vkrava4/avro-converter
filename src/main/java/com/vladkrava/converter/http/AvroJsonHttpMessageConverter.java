package com.vladkrava.converter.http;

import java.io.IOException;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;

import com.vladkrava.converter.serialization.AvroDeserializer;
import com.vladkrava.converter.serialization.AvroSerializer;
import com.vladkrava.converter.serialization.DataSerializationException;

/**
 * This converter supports application/avro-json format with {@code DEFAULT_CHARSET} character set.
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @see AbstractAvroHttpMessageConverter
 * @see SpecificRecordBase
 * @since 1.0.0-SNAPSHOT
 */
public class AvroJsonHttpMessageConverter<T extends SpecificRecordBase> extends AbstractAvroHttpMessageConverter<T> {

    private final AvroSerializer<SpecificRecordBase> avroSerializer;
    private final AvroDeserializer<SpecificRecordBase> avroDeserializer;

    public AvroJsonHttpMessageConverter() {
        super(AVRO_JSON_MEDIA_TYPE);

        this.avroSerializer = new AvroSerializer<>();
        this.avroDeserializer = new AvroDeserializer<>();
    }

    /**
     * Reading accepted message and providing deserialization
     *
     * @param aClass       - target Avro object type
     * @param inputMessage - accepted message
     * @return {@code T} - converted Avro object
     * @throws IOException - on read/write issues
     */
    @SuppressWarnings("unchecked")
    @Override
    protected T readInternal(final Class<? extends T> aClass, HttpInputMessage inputMessage) throws IOException {
        T result = null;
        final byte[] data = IOUtils.toByteArray(inputMessage.getBody());
        if (data != null && data.length > 0) {
            try {
                result = (T) avroDeserializer.deserialize(data, aClass);
            } catch (DataSerializationException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * Serializing and writing Avro object into outgoing message
     *
     * @param t                 - Avro object
     * @param httpOutputMessage - outgoing message
     * @throws IOException - on read/write issues
     */
    @Override
    protected void writeInternal(final T t, final HttpOutputMessage httpOutputMessage) throws IOException {
        try {
            final byte[] data = avroSerializer.serialize(t);
            httpOutputMessage.getBody().write(data);
        } catch (DataSerializationException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
