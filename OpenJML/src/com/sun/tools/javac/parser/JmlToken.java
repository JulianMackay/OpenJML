/*
 * This file is part of the OpenJML project. 
 * Author: David R. Cok
 */
package com.sun.tools.javac.parser;

import org.jmlspecs.openjml.JmlTokenKind;

import com.sun.tools.javac.parser.Tokens.Token;
import com.sun.tools.javac.parser.Tokens.TokenKind;

/**
 * This class is an extension of the JDK Token class so that we can represent JML tokens
 * as a JDK Token.
 * 
 * @author David Cok
 */
public class JmlToken extends Token {

    public JmlTokenKind jmlkind;
    
    /** Creates a JmlToken object, as either, if jmlkind is null, a Java token in a JMLToken wrapper or, 
      * if jmlkind is not null, a JMlToken object for a JML construct.
      */
    public JmlToken(/*@ nullable */JmlTokenKind jmlkind, TokenKind tk, int pos, int endPos) {
        super(jmlkind != null ? jmlkind : tk, pos, endPos, null); // FIXME - do we ever need to add in a List<Comment>
        this.jmlkind = jmlkind;
    }
    
    public JmlToken(Token javaToken) {
        this(null, javaToken);
    }
    
    public JmlToken(JmlTokenKind jmlkind, Token javaToken) {
        super(jmlkind != null ? jmlkind : javaToken.kind, javaToken.pos, javaToken.endPos, javaToken.comments);
        this.jmlkind = jmlkind;
    }

    protected void checkKind() { // FIXME - is it OK to override completely the baehavior in the super class?
//        if (kind.tag != Tag.DEFAULT) {
//            throw new AssertionError("Bad token kind - expected " + Tag.STRING);
//        }
    }

    public String toString() {
        return ikind.toString();
    }
    
}
