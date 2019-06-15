package com.vladkrava.converter.http;

import static com.vladkrava.converter.AvroSchemaProcessor.processValue;

import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.compress.utils.IOUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;

import com.vladkrava.converter.serialization.AvroDeserializer;
import com.vladkrava.converter.serialization.AvroSerializer;
import com.vladkrava.converter.serialization.DataSerializationException;

/**
 * This converter supports application/avro-xml format with {@code DEFAULT_CHARSET} character set.
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @see AbstractAvroHttpMessageConverter
 * @see SpecificRecordBase
 * @since 1.0.0-SNAPSHOT
 */
public class AvroXmlHttpMessageConverter<T extends SpecificRecordBase> extends AbstractAvroHttpMessageConverter<T> {

    private final AvroSerializer<SpecificRecordBase> avroSerializer;
    private final AvroDeserializer<SpecificRecordBase> avroDeserializer;

    public AvroXmlHttpMessageConverter() {
        super(AVRO_XML_MEDIA_TYPE);

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
    protected T readInternal(final Class<? extends T> aClass, final HttpInputMessage inputMessage) throws IOException {
        T result = null;
        final byte[] data = IOUtils.toByteArray(inputMessage.getBody());
        if (data != null && data.length > 0) {
            try {
                final JSONObject jsonDataObject = XML.toJSONObject(new String(data)).getJSONObject(aClass.getSimpleName());
                final JSONObject schemaJsonObject = new JSONObject();

                if (jsonDataObject == null) {
                    logger.warn("HttpInputMessage has invalid body: " + inputMessage.getBody().toString());
                    throw new DataSerializationException("Can't handle message body");
                }

                for (final Schema.Field field : aClass.newInstance().getSchema().getFields()) {
                    schemaJsonObject.put(field.name(), processValue(jsonDataObject, field.schema(), field.name()));
                }

                result = (T) avroDeserializer.deserialize(schemaJsonObject.toString().getBytes(), aClass);
            } catch (DataSerializationException | InstantiationException | IllegalAccessException e) {
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
            final byte[] json = avroSerializer.serialize(t);
            httpOutputMessage.getBody().write(XML.toString(new JSONObject(new String(json)), t.getSchema().getName()).getBytes());
        } catch (DataSerializationException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
