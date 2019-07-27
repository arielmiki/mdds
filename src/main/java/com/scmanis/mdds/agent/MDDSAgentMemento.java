package com.scmanis.mdds.agent;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Set;

public class MDDSAgentMemento implements Serializable{
    private List<Sentence> sentences;
    private Set<Literal> inferred;
    private boolean isShuffle;
    private int questionCounter;

    public Set<Literal> getInferred() {
        return inferred;
    }

    public List<Sentence> getSentences() {

        return sentences;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public int getQuestionCounter() {
        return questionCounter;
    }

    public MDDSAgentMemento(List<Sentence> sentences, Set<Literal> inferred, boolean isShuffle, int questionCounter) {
        this.isShuffle = isShuffle;
        this.sentences = sentences;
        this.inferred = inferred;
        this.questionCounter = questionCounter;
    }

    public static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    public static Object fromString( String s ) throws IOException ,
            ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

}
