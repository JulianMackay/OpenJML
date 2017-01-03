package com.sun.tools.javac.code;

import org.jmlspecs.openjml.JmlTokenKind;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.code.TypeTag;

/** This class extends Type in order to implement new JML primitive types. */
public class JmlType extends Type {

    /** The token defining the primitive type - 
     * is immutable after construction.
     */
    final protected JmlTokenKind jmlTypeTag;
    
    /** The fully-qualified name of the class used as the runtime
     * representation of this type.
     */
    final public String fqName;
    
    /** The Class used to represent this type in RAC - do not use this
     * value directly since it is lazily initialized in JmlTypes.
     */
    protected Symbol.ClassSymbol repSym;
    
    /** Creates a new primitive type with the given token - should be a 
     * singleton for each new JML type */
    public JmlType(JmlTokenKind token, String fullyQualifiedClassName) {
        super(null);
        jmlTypeTag = token;
        fqName = fullyQualifiedClassName;
    }
    
    /** The JmlToken that designates this type */
    public JmlTokenKind jmlTypeTag() {
        return jmlTypeTag;
    }
    
    /** Returns the public name of the type token */
    @Override public String toString() {
        return jmlTypeTag.internedName();
    }
    
    // returns true for these new JML primitive types
    @Override public boolean isPrimitive() {
        return true;
    }
    
    // returns true for these new JML primitive types
    @Override public boolean isPrimitiveOrVoid() {
        return true;
    }



    @Override
    public TypeTag getTag() {
        return TypeTag.NONE;
    }
    
    @Override
    public boolean hasTag(TypeTag t) {
        if (t != TypeTag.NONE) return super.hasTag(t);
        return false;
    }



}
