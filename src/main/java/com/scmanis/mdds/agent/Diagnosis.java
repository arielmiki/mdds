package com.scmanis.mdds.agent;

public class Diagnosis extends Literal {
    private String description;

    public Diagnosis(String identifier, String description) {
        super(identifier);
        this.description = description;
    }

    public String getDiagnosis() {
        return getIdentifier() + "\n" + description;
    }

}
