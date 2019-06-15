package com.vladkrava.converter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import org.apache.avro.Schema;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The {@code AvroSchemaProcessor} class is used for defining a type of {@link JSONObject}
 * values based on Avro Schema ({@link Schema} class).
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @see Integer
 * @see Boolean
 * @see Long
 * @see Double
 * @see Float
 * @see String
 * @see Schema
 * @see JSONObject
 * @since 0.2-SNAPSHOT
 */
public final class AvroSchemaProcessor {


    /**
     * Utility class constructor
     */
    private AvroSchemaProcessor() {
    }


    /**
     * Processing an Avro Schema ({@link Schema} class) with {@link JSONObject} to define a value and a type of JSON node.
     *
     * @param jsonNode     a node which a processing/traversing will occur
     * @param objectSchema referenced Avro Schema object
     * @param objectKey    a key of JSON node
     * @return {@code Object} processed JSON node value wrapped into corresponding to Avro Schema type
     */
    public static Object processValue(final JSONObject jsonNode, final Schema objectSchema, final String objectKey) {
        switch (objectSchema.getType()) {
            case INT:
                return Integer.valueOf(String.valueOf(jsonNode.get(objectKey)));
            case LONG:
                return Long.valueOf(String.valueOf(jsonNode.get(objectKey)));
            case FLOAT:
                return Float.valueOf(String.valueOf(jsonNode.get(objectKey)));
            case DOUBLE:
                return Double.valueOf(String.valueOf(jsonNode.get(objectKey)));
            case BOOLEAN:
                return Boolean.valueOf(String.valueOf(jsonNode.get(objectKey)));
            case STRING:
            case BYTES:
            case FIXED:
                return String.valueOf(jsonNode.get(objectKey));
            case ARRAY:
                return processArrayValue(jsonNode, objectSchema.getElementType(), objectKey);
            case RECORD:
                return processRecordValue(jsonNode, objectSchema, objectKey);
            case ENUM:
                return objectSchema.getEnumSymbols().get(objectSchema.getEnumOrdinal(String.valueOf(jsonNode.get(objectKey))));
            case UNION:
                return processUnionValue(jsonNode, objectSchema, objectKey);
            case MAP:
                return jsonNode.get(objectKey);
            default:
                return JSONObject.NULL;
        }
    }


    /**
     * Processing complex Avro Schema ({@code Schema.Type.ARRAY}) with {@link JSONObject} to define a value and for
     * corresponding {@link JSONArray} data structure.
     *
     * @param jsonNode     a node which stores values in {@link JSONArray} data structure
     * @param objectSchema referenced Avro Schema object
     * @param objectKey    a key of JSON node by which traversing/searching will occur
     * @return {@code Collection<Object>} processed {@link JSONArray} values
     */
    private static Collection<Object> processArrayValue(final JSONObject jsonNode, final Schema objectSchema, final String objectKey) {
        final Collection<Object> itemsCollection = new LinkedList<>();
        final JSONArray jsonArray = jsonNode.getJSONArray(objectKey);

        for (int i = 0; i < jsonArray.length(); i++) {
            switch (objectSchema.getType()) {
                case INT:
                    itemsCollection.add(Integer.valueOf(String.valueOf(jsonArray.get(i))));
                    break;
                case LONG:
                    itemsCollection.add(Long.valueOf(String.valueOf(jsonArray.get(i))));
                    break;
                case FLOAT:
                    itemsCollection.add(Float.valueOf(String.valueOf(jsonArray.get(i))));
                    break;
                case DOUBLE:
                    itemsCollection.add(Double.valueOf(String.valueOf(jsonArray.get(i))));
                    break;
                case BOOLEAN:
                    itemsCollection.add(Boolean.valueOf(String.valueOf(jsonArray.get(i))));
                    break;
                case STRING:
                case BYTES:
                case FIXED:
                    itemsCollection.add(String.valueOf(jsonArray.get(i)));
                    break;
                case RECORD:
                    itemsCollection.add(processRecordsArray(jsonArray, objectSchema, i));
                    break;
                case ENUM:
                    itemsCollection.add(objectSchema.getEnumSymbols().get(objectSchema.getEnumOrdinal(String.valueOf(jsonArray.get(i)))));
                    break;
                case UNION:
                    itemsCollection.add(processUnionArray(jsonArray, objectSchema, i));
                    break;
                case ARRAY:
                    throw new UnsupportedOperationException("Multi level arrays are not supported yet");
                case MAP:
                    itemsCollection.add(jsonArray.get(i));
                    break;
                default:
                    itemsCollection.add(JSONObject.NULL);
                    break;
            }
        }

        return itemsCollection;
    }


    /**
     * Processing a value for Avro Schema ({@code Schema.Type.UNION}) type
     *
     * @param jsonNode     a node which stores values in {@link JSONObject} data structure
     * @param objectSchema referenced Avro Schema object
     * @param objectKey    a key of JSON node by which traversing/searching will occur
     * @return {@code Object} processed JSON node value wrapped into corresponding to Avro Schema type
     */
    private static Object processUnionValue(final JSONObject jsonNode, final Schema objectSchema, final String objectKey) {
        if (jsonNode.get(objectKey) == null || jsonNode.get(objectKey).equals(JSONObject.NULL)) {
            return JSONObject.NULL;
        } else {
            Optional<Schema> optionalSchema = getUnionNonNullSchema(objectSchema);

            if (optionalSchema.isPresent()) {
                return makeUnionObject(optionalSchema.get(), jsonNode, objectKey);
            } else {
                return JSONObject.NULL;
            }
        }
    }


    /**
     * Processing a value for Avro Schema ({@code Schema.Type.UNION}) type
     *
     * @param jsonArray    a node which stores values in {@link JSONArray} data structure
     * @param objectSchema referenced Avro Schema object
     * @param index        an array index by which traversing/searching will occur
     * @return {@code Object} processed JSON node value wrapped into corresponding to Avro Schema type
     */
    private static Object processUnionArray(final JSONArray jsonArray, final Schema objectSchema, final int index) {
        if (jsonArray.get(index) == null || jsonArray.get(index).equals(JSONObject.NULL)) {
            return JSONObject.NULL;
        } else {
            Optional<Schema> optionalSchema = getUnionNonNullSchema(objectSchema);
            if (optionalSchema.isPresent()) {
                return makeUnionArrayObject(optionalSchema.get(), jsonArray, index);
            } else {
                return JSONObject.NULL;
            }
        }
    }


    /**
     * Generating {@link JSONObject} for Avro Schema ({@code Schema.Type.UNION}) type
     *
     * @param jsonArray    a node which stores values in {@link JSONArray} data structure
     * @param objectSchema referenced Avro Schema object
     * @param index        an array index by which traversing/searching will occur
     * @return Generated {@link JSONObject} with ({@code Schema.Type.UNION}) type value
     */
    private static JSONObject makeUnionArrayObject(final Schema objectSchema, final JSONArray jsonArray, final int index) {
        final JSONObject unionJsonObject = new JSONObject();
        unionJsonObject.put(objectSchema.getType().getName(), processValue(jsonArray.getJSONObject(index), objectSchema,
                objectSchema.getType().getName()));
        return unionJsonObject;
    }


    /**
     * Generating {@link JSONObject} for Avro Schema ({@code Schema.Type.UNION}) type
     *
     * @param jsonObject   a node which stores values in {@link JSONObject} data structure
     * @param objectSchema referenced Avro Schema object
     * @param objectKey    a key of JSON node by which traversing/searching will occur
     * @return Generated {@link JSONObject} with ({@code Schema.Type.UNION}) type value
     */
    private static JSONObject makeUnionObject(final Schema objectSchema, final JSONObject jsonObject, final String objectKey) {
        final JSONObject unionJsonObject = new JSONObject();
        unionJsonObject.put(objectSchema.getType().getName(), processValue(jsonObject.getJSONObject(objectKey),
                objectSchema, objectSchema.getType().getName()));
        return unionJsonObject;
    }


    /**
     * Generating {@link JSONObject} for Avro Schema ({@code Schema.Type.RECORD}) type
     *
     * @param jsonObject   a node which stores values in {@link JSONObject} data structure
     * @param objectSchema referenced Avro Schema object
     * @param objectKey    a key of JSON node by which traversing/searching will occur
     * @return Generated {@link JSONObject} with ({@code Schema.Type.RECORD}) type value
     */
    private static JSONObject processRecordValue(final JSONObject jsonObject, final Schema objectSchema, final String objectKey) {
        final JSONObject complexJsonStructure = new JSONObject();
        for (final Schema.Field field : objectSchema.getFields()) {
            complexJsonStructure.put(field.name(), processValue(jsonObject.getJSONObject(objectKey), field.schema(), field.name()));
        }
        return complexJsonStructure;
    }


    /**
     * Generating {@link JSONObject} for Avro Schema ({@code Schema.Type.RECORD}) type
     *
     * @param jsonArray    a node which stores values in {@link JSONArray} data structure
     * @param objectSchema referenced Avro Schema object
     * @param index        an array index by which traversing/searching will occur
     * @return Generated {@link JSONObject} with ({@code Schema.Type.RECORD}) type value
     */
    private static JSONObject processRecordsArray(final JSONArray jsonArray, final Schema objectSchema, final int index) {
        final JSONObject jsonRecords = new JSONObject();
        for (final Schema.Field field : objectSchema.getFields()) {
            jsonRecords.put(field.name(), processValue(jsonArray.getJSONObject(index), field.schema(), field.name()));
        }

        return jsonRecords;
    }


    /**
     * Retrieving first non null schema type from {@code Schema.Type.UNION}
     *
     * @param objectSchema referenced Avro Schema object of {@code Schema.Type.UNION} type
     * @return {@code Optional<Schema>} first non null schema if such applicable
     */
    private static Optional<Schema> getUnionNonNullSchema(final Schema objectSchema) {
        return objectSchema.getTypes()
                .stream()
                .filter(t -> t.getType() != Schema.Type.NULL)
                .findFirst();
    }
}
