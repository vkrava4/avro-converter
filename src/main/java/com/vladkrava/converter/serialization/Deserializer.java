package com.vladkrava.converter.serialization;

/**
 * Provides low level interface for deserialization from the byte array
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @since 0.1-SNAPSHOT
 */
public interface Deserializer<T> {

    /**
     * Object deserialization
     *
     * @param aClass - target class
     * @param data   - source of the data
     * @return T generic object
     * @throws DataSerializationException - signals that some exception of some sort has occurred
     *                                    during the data deserialization
     */
    T deserialize(Class<? extends T> aClass, byte[] data) throws DataSerializationException;

}
