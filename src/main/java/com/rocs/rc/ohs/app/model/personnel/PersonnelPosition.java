package com.rocs.rc.ohs.app.model.personnel;

/** Represents the position assigned to personnel. */
public class PersonnelPosition {

    /** The personnel assigned to the position. */
    private Personnel personnel;

    /** The unique ID of the personnel position. */
    private Long id;

    /** The position assigned to the personnel. */
    private Position position;


    public Personnel getPersonnel() {
        return personnel;
    }

    public void setPersonnel(Personnel newPersonnel) {
        this.personnel = newPersonnel;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }
}
