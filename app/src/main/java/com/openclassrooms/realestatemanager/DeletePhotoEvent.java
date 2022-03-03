package com.openclassrooms.realestatemanager;

public class DeletePhotoEvent {

    /**
     * Restaurant to display
     */
    public int position;

    /**
     * Constructor.
     */
    public DeletePhotoEvent(int position) {
        this.position = position;
    }
}

