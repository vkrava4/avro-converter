package com.vladkrava.converter.http;

import java.io.IOException;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

import com.vladkrava.converter.serialization.AvroDeserializer;
import com.vladkrava.converter.serialization.AvroSerializer;
import com.vladkrava.converter.serialization.DataSerializationException;

/**
 * Implementation of {@link AbstractHttpMessageConverter} which can be used for reading and writing specific
 * Avro object
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @see AvroSerializer
 * @see AvroDeserializer
 * @see org.springframework.http.converter.HttpMessageConverter
 * @since 0.1-SNAPSHOT
 */
public abstract class AbstractAvroHttpMessageConverter<T extends SpecificRecordBase> extends AbstractHttpMessageConverter<T> {

    private final AvroSerializer<SpecificRecordBase> avroSerializer;
    private final AvroDeserializer<SpecificRecordBase> avroDeserializer;

    public AbstractAvroHttpMessageConverter(final MediaType supportedMediaType) {
        super(supportedMediaType);

        this.avroSerializer = new AvroSerializer<>();
        this.avroDeserializer = new AvroDeserializer<>();
    }

    /**
     * Indicates whether the given converter is suitable for a class type
     *
     * @param aClass - class type
     * @return {@code boolean} value indicating whether objects of the type {@code SpecificRecordBase}
     * can be assigned to objects of {@code aClass}
     */
    @Override
    protected boolean supports(final Class<?> aClass) {
        return SpecificRecordBase.class.isAssignableFrom(aClass);
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
