package com.openclassrooms.realestatemanager;

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