package com.rocs.rc.ohs.app.model.personnel;

/** Represents the position assigned to personnel. */
public class PersonnelPosition {

    /** The unique ID of the personnel position. */
    private Long id;

    /** The personnel assigned to the position. */
    private Personnel personnel;

    /** The position assigned to the personnel. */
    private Position position;
    /**
     * Returns the unique ID of this personnel position.
     *
     * @return the personnel position ID
     */
    public Long getId() {
        return id;
    }

    /**
     * Updates the unique ID of this personnel position.
     *
     * @param newId the new personnel position ID
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     * Returns the personnel assigned to this position.
     *
     * @return the assigned personnel
     */
    public Personnel getPersonnel() {
        return personnel;
    }

    /**
     * Assigns the given personnel to this position.
     *
     * @param newPersonnel the personnel to assign
     */
    public void setPersonnel(Personnel newPersonnel) {
        this.personnel = newPersonnel;
    }

    /**
     * Returns the position assigned to the personnel.
     *
     * @return the assigned position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Assigns the given position to the personnel.
     *
     * @param newPosition the position to assign
     */
    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }
}
