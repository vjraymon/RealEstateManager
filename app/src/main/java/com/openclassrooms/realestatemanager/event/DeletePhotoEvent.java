package com.openclassrooms.realestatemanager.event;

public class DeletePhotoEvent {

    /**
     * Restaurant to display
     */
    public final int position;

    /**
     * Constructor.
     */
    public DeletePhotoEvent(int position) {
        this.position = position;
    }
}

