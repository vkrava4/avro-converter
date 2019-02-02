package com.vladkrava.converter;

import com.vladkrava.converter.serialization.DataSerializationException;

/**
 * Converting data between 2 types where T is target type and E is a source with providing a target type
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @see DataSerializationException
 * @since 0.1-SNAPSHOT
 */
public interface TargetConverter<T, E> {
    /**
     * Data converting specification
     *
     * @param e      - source data
     * @param aClass - converted data class type
     * @return T - converted data in a target datatype
     * @throws DataSerializationException - on any exception with serialization from source datatype to a byte array
     */
    T convert(final E e, final Class<T> aClass) throws DataSerializationException;
}
