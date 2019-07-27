package com.scmanis.mdds.agent;

public class AtomicSentence extends Sentence {
    public AtomicSentence(Literal consequence) {
        super(null, consequence);
    }

    @Override
    public String toString() {
        return getConsequence().toString();
    }
}
