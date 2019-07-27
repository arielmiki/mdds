package com.scmanis.mdds.agent;

public class Symptom extends Literal {
    private String question;

    public Symptom(String identifier, String question) {
        super(identifier);
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

}
