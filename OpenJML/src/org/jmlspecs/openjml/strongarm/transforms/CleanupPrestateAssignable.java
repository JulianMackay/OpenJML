package org.jmlspecs.openjml.strongarm.transforms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.lang.model.type.TypeKind;

import org.jmlspecs.openjml.JmlOption;
import org.jmlspecs.openjml.JmlToken;
import org.jmlspecs.openjml.JmlTokenKind;
import org.jmlspecs.openjml.JmlTree;
import org.jmlspecs.openjml.JmlTreeScanner;
import org.jmlspecs.openjml.JmlTreeUtils;
import org.jmlspecs.openjml.Strings;
import org.jmlspecs.openjml.Utils;
import org.jmlspecs.openjml.JmlTree.JmlMethodClause;
import org.jmlspecs.openjml.JmlTree.JmlMethodClauseExpr;
import org.jmlspecs.openjml.JmlTree.JmlMethodClauseStoreRef;
import org.jmlspecs.openjml.JmlTree.JmlMethodDecl;
import org.jmlspecs.openjml.JmlTree.JmlSpecificationCase;

import com.sun.tools.javac.code.Symtab;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Log;
import com.sun.tools.javac.util.Name;

public class CleanupPrestateAssignable extends JmlTreeScanner {
    
    final protected Log                    log;
    final protected Utils                  utils;
    final protected JmlTreeUtils           treeutils;
    final protected JmlTree.Maker          M;
    final protected Context                context;
    final Symtab syms;
    public static boolean inferdebug = false; 
    public static boolean verbose = false; 
    public static CleanupPrestateAssignable instance;

    
    public CleanupPrestateAssignable(Context context){
        
        this.context    = context;
        this.log        = Log.instance(context);
        this.utils      = Utils.instance(context);
        this.treeutils  = JmlTreeUtils.instance(context);
        this.M          = JmlTree.Maker.instance(context);
        this.syms       = Symtab.instance(context);
        
        this.inferdebug = JmlOption.isOption(context, JmlOption.INFER_DEBUG);           

        this.verbose = inferdebug || JmlOption.isOption(context,"-verbose") // The Java verbose option
            || utils.jmlverbose >= Utils.JMLVERBOSE;
            
    }
    
    public static void cache(Context context){
        if(instance==null){
            instance = new CleanupPrestateAssignable(context);
        }
    }
    
    
    /**
     * Locals removed if they are a formal and primative OR if they are just local. Fields stay.  
     */
    public boolean shouldRemove(JmlMethodClause clause){
        
        if(clause instanceof JmlMethodClauseStoreRef){
            JmlMethodClauseStoreRef mExpr = (JmlMethodClauseStoreRef)clause;
            
            if(mExpr.token == JmlTokenKind.ASSIGNABLE && mExpr.toString().startsWith("assignable \\result.")){
                    return true;
            }
            
            if(mExpr.token == JmlTokenKind.ASSIGNABLE && mExpr.toString().startsWith("assignable " + Strings.newArrayVarString)){
                return true;
            }
            
            if(mExpr.token == JmlTokenKind.ASSIGNABLE && mExpr.toString().startsWith("assignable " + Strings.newObjectVarString)){
                return true;
            }

        }
        
        // filter any junk here 
        if((clause instanceof JmlMethodClauseExpr  || clause instanceof JmlMethodClauseStoreRef) && (clause.toString().contains(Strings.allocName) || clause.toString().contains(Strings.isAllocName))){
            return true;
        }
        
        if(clause instanceof JmlMethodClauseExpr  && (((JmlMethodClauseExpr)clause).expression.toString().equals("true"))){
            return true;
        }
        
        if(clause instanceof JmlMethodClauseExpr  && (((JmlMethodClauseExpr)clause).expression.toString().contains(Strings.tmpVarString))){
            return true;
        }
        
        if(clause instanceof JmlMethodClauseExpr  && (((JmlMethodClauseExpr)clause).expression.toString().contains("_switchExpression"))){
            return true;
        }
        
        return false;
    }
    
    public void filterBlock(JmlSpecificationCase block){
        
        List<JmlMethodClause> replacedClauses = null;
        
        if(block.clauses==null){return;}
        
        for(List<JmlMethodClause> clauses = block.clauses; clauses.nonEmpty(); clauses = clauses.tail){
                        
            if(shouldRemove(clauses.head) == false){
                if(replacedClauses == null){
                    replacedClauses = List.of(clauses.head);
                }else{
                    replacedClauses = replacedClauses.append(clauses.head);
                }
            }
        }
        
        block.clauses = replacedClauses;
    }

    @Override
    public void visitJmlSpecificationCase(JmlSpecificationCase tree) {

        filterBlock(tree);
       
        super.visitJmlSpecificationCase(tree);
    }
    
        
    public void scan(JCTree node) {        
        super.scan(node);
    }
    
    public static void simplify(JCTree node){
        instance.scan(node);
    }
    
    
}
