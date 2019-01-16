package com.vladkrava.converter.serialization;

/**
 * Provides low level interface for serialization from object of a generic type into the byte array.
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @since 0.1-SNAPSHOT
 */
public interface Serializer<T> {

    /**
     * Object serialization
     *
     * @param data - source of the data
     * @return byte[] - serialized object
     * @throws DataSerializationException - signals that some exception of some sort has occurred
     *                                    during the data serialization
     */
    byte[] serialize(T data) throws DataSerializationException;
}
