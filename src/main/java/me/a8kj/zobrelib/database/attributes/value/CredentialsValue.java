package me.a8kj.zobrelib.database.attributes.value;

/**
 * Represents the value of a credential in the database.
 * <p>
 * This interface provides methods to retrieve the type and value of a specific
 * credential.
 * </p>
 * 
 * @param <U> the type of the credential's value
 * @author a8kj7sea
 */
public interface CredentialsValue<U> {

    /**
     * Returns the type of the credential's value.
     * 
     * @return the type of the value
     */
    Class<U> getType();

    /**
     * Returns the actual value of the credential.
     * 
     * @return the value of the credential
     */
    U getValue();
}
