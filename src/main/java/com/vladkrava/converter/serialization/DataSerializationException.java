package com.vladkrava.converter.serialization;

/**
 * Signals that some data serialization exception of some sort has occurred.
 * This class is the general exception thrown during the execution of serialization/deserialization operations.
 *
 * @author Vlad Krava - vkrava4@gmail.com
 * @see Serializer
 * @see Deserializer
 * @since 0.1-SNAPSHOT
 */
public class DataSerializationException extends Exception {

    /**
     * @param message text description related to the possible cause of an issue
     * @param cause   info related to the cause saved for later retrieval
     */
    public DataSerializationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message text description related to the possible cause of an issue
     */
    public DataSerializationException(final String message) {
        super(message);
    }
}
