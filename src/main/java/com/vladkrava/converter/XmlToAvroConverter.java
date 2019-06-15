package com.vladkrava.converter;

import static com.vladkrava.converter.AvroSchemaProcessor.processValue;

import org.apache.avro.Schema;
import org.apache.avro.specific.SpecificRecordBase;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.vladkrava.converter.serialization.AvroDeserializer;
import com.vladkrava.converter.serialization.DataSerializationException;

/**
 * Converting data from XML string to specific Avro object of a type T
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @see SpecificRecordBase
 * @see TargetConverter
 * @see AvroDeserializer
 * @see AvroSchemaProcessor
 * @since 0.2-SNAPSHOT
 */
public class XmlToAvroConverter<T extends SpecificRecordBase> extends AvroDeserializer<T> implements TargetConverter<T, String> {

    /**
     * Performs converting to specific Avro object
     *
     * @param s      - XML source {@link String}
     * @param aClass - specific Avro object type
     * @return T - specific Avro object
     * @throws DataSerializationException - on exceptional conditions during deserialization
     */
    @Override
    public T convert(final String s, final Class<T> aClass) throws DataSerializationException {
        if (s == null) {
            return null;
        }

        try {
            final JSONObject jsonDataObject = XML.toJSONObject(s).getJSONObject(aClass.getSimpleName());
            final JSONObject schemaJsonObject = new JSONObject();

            if (jsonDataObject == null) {
                throw new DataSerializationException("Bad object format");
            }

            for (final Schema.Field field : aClass.newInstance().getSchema().getFields()) {
                schemaJsonObject.put(field.name(), processValue(jsonDataObject, field.schema(), field.name()));
            }

            return deserialize(schemaJsonObject.toString().getBytes(), aClass);
        } catch (InstantiationException | JSONException | IllegalAccessException e) {
            throw new DataSerializationException("An issue occurred with object deserialization", e);
        }
    }
}
