package com.scmanis.mdds.agent;

import java.io.Serializable;
import java.util.List;
import java.util.ListIterator;

public class Sentence implements Comparable<Sentence>, Serializable{
    private List<Literal> antecedent;
    private Literal consequence;
    private int threshold;

    public Sentence(List<Literal> antecedent, Literal consequence, int threshold) {
        this.antecedent = antecedent;
        this.consequence = consequence;
        this.threshold = Math.min(antecedent != null?antecedent.size():0, threshold);

    }
    public Sentence(List<Literal> antecedent, Literal consequence) {
        this(antecedent, consequence, Integer.MAX_VALUE);

    }

    public boolean containSymbol(Literal literal) {
        return antecedent.stream().anyMatch(o -> o.equals(literal));
    }

    public Literal getConsequence() {
        return consequence;
    }

    public int getThreshold() {
        return threshold;
    }

    @Override
    public int compareTo(Sentence o) {
        return threshold - o.getThreshold();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ListIterator<Literal> it = antecedent.listIterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) {
                sb.append('^');
            }
        }
        sb.append("=>");
        sb.append(consequence.toString());
        return sb.toString();
    }

    public boolean wrongLiteral(Literal literal) {
        antecedent.remove(literal);
        return antecedent.size() < threshold;

    }

    public Literal getAskedLiteral() {
        return antecedent.get(0);
    }

    public boolean inferred() {
        return threshold <= 0;
    }

    public void proof(Literal literal) {
        antecedent.remove(literal);
        this.threshold -=1;
    }
}
