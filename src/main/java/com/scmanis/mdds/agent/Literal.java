package com.scmanis.mdds.agent;

import java.io.Serializable;

public class Literal implements Serializable {
    private String identifier;

    public Literal(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Literal)) {
            return false;
        }
        final Literal other = (Literal) obj;
        return this.identifier.equalsIgnoreCase(other.getIdentifier());
    }

    @Override
    public String toString() {
        return identifier;
    }
}
