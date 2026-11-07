package com.rocs.rc.ohs.app.model.personnel;

/** Represents the position assigned to personnel. */
public class PersonnelPosition {

    /** The personnel assigned to the position. */
    private Personnel personnel;

    /** The unique ID of the personnel position. */
    private Long id;

    /** The position assigned to the personnel. */
    private Position position;

    /** @return the personnel position ID */
    public Long getId(){
        return id;
    }

    /** @param newId the new personnel position ID */
    public void setId(Long newId){
        this.id = newId;
    }

    /** @return the assigned personnel */
    public Personnel getPersonnel() {
        return personnel;
    }

    /** @param newPersonnel the personnel to assign */
    public void setPersonnel(Personnel newPersonnel) {
        this.personnel = newPersonnel;
    }

    /** @return the assigned position */
    public Position getPosition() {
        return position;
    }

    /** @param newPosition the position to assign */
    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }
}
