
package com.rocs.rc.ohs.app.model.personnel;

/**
 * Represents the position assigned to personnel.
 */
public class PersonnelPosition {

    /** The personnel assigned to the position. */
    private Personnel personnel;

    /** The position assigned to the personnel. */
    private Position position;

    /**
     * Retrieves the personnel.
     *
     * @return The personnel.
     */
    public Personnel getPersonnel() {
        return personnel;
    }

    /**
     * Sets the personnel.
     *
     * @param newPersonnel The personnel.
     */
    public void setPersonnel(Personnel newPersonnel) {
        this.personnel = newPersonnel;
    }

    /**
     * Retrieves the position.
     *
     * @return The position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position.
     *
     * @param newPosition The position.
     */
    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }
}
