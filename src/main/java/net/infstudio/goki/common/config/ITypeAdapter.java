package net.infstudio.goki.common.config;

import net.minecraftforge.common.config.Property;

/**
 * Abstracts the types of properties away. Higher level logic must prevent invalid data types.
 */
public interface ITypeAdapter
{
    /**
     * Assigns the default value to the property
     * @param property the property whose default value will be assigned
     * @param value the default value
     */
    void setDefaultValue(Property property, Object value);

    /**
     * Sets the properties value.
     * @param property the property whose value will be set
     * @param value the set value
     */
    void setValue(Property property, Object value);

    /**
     * Retrieves the properties value
     * @param prop the property whose value will be retrieved
     * @return the properties value
     */
    Object getValue(Property prop);

    Property.Type getType();

    boolean isArrayAdapter();
}
