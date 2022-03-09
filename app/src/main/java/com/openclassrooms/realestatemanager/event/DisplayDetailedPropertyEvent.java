package com.openclassrooms.realestatemanager.event;

import com.openclassrooms.realestatemanager.model.Property;

public class DisplayDetailedPropertyEvent {

    /**
     * Restaurant to display
     */
    public final Property property;

    /**
     * Constructor.
     */
    public DisplayDetailedPropertyEvent(Property property) {
        this.property = property;
    }
}