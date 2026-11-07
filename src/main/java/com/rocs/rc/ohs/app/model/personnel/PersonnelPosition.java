package com.rocs.rc.ohs.app.model.personnel;

/** Represents the position assigned to personnel. */
public class PersonnelPosition {

    /** The personnel assigned to the position. */
    private Personnel personnel;

    /** The unique ID of the personnel position. */
    private Long id;

    /** The position assigned to the personnel. */
    private Position position;

    /** Returns the unique ID of this personnel position. */
    public Long getId(){
        return id;
    }

    /** Sets the unique ID of this personnel position. */
    public void setId(Long newId){
        this.id = newId;
    }

    /** Returns the personnel assigned to this position. */
    public Personnel getPersonnel() {
        return personnel;
    }

    /** Sets the personnel assigned to this position. */
    public void setPersonnel(Personnel newPersonnel) {
        this.personnel = newPersonnel;
    }

    /** Returns the position assigned to the personnel. */
    public Position getPosition() {
        return position;
    }

    /** Sets the position assigned to the personnel. */
    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }
}
