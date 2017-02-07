package com.example.carlos.appcurso.Domain;

import java.util.ArrayList;

/**
 * Created by Carlos on 25/01/2017.
 */

public class TokenStack {

    /** Member variables **/
    private ArrayList<Token> tokens;

    /** Constructors **/
    public TokenStack() {
        tokens = new ArrayList<Token>();
    }

    /** Accessor methods **/
    public boolean isEmpty() {
        return tokens.size() == 0;
    }
    public Token top() {
      if(!tokens.isEmpty()) return tokens.get(tokens.size()-1);
        else return new Token();
    }

    /** Mutator methods **/
    public void push(Token t) {
        tokens.add(t);
    }
    public void pop() {

        if(!tokens.isEmpty())tokens.remove(tokens.size()-1);
    }

}
