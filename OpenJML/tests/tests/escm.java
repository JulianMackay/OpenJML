package tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class escm extends EscBase {
    
    public escm(String option, String solver) {
        super(option,solver);
    }
    

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }
    
    /** This test checks that nested, local and anonymous classes are handled */
    @Test
    public void testNestedClass() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  public void m1(TestJava o) {\n"
                +"       class C { void m() { /*@ assert false; */ }};\n"
                +"       C x;\n"
                +"       C y = new C() { void m() {/*@ assert false; */}};\n"
                +"       //@ assert false;\n"
                +"  }\n"
                
                +"  public static class A {\n"
                +"     public void m2() {\n"
                +"       //@ assert false;\n"
                +"     }\n"
                +"  }\n"
                

                
                +"}"
                , !option.equals("-custom") ?
                new Object[] {"/tt/TestJava.java:8: warning: The prover cannot establish an assertion (Assert) in method m",33
                ,"/tt/TestJava.java:10: warning: The prover cannot establish an assertion (Assert) in method m",38
                ,"/tt/TestJava.java:11: warning: The prover cannot establish an assertion (Assert) in method m1",12
                ,"/tt/TestJava.java:15: warning: The prover cannot establish an assertion (Assert) in method m2",12 }
                :  new Object[] {"/tt/TestJava.java:8: warning: The prover cannot establish an assertion (Assert) in method m",33
                        ,"/tt/TestJava.java:10: warning: The prover cannot establish an assertion (Assert) in method m",38
                        // FIXME _ why duplicated?
                        ,"/tt/TestJava.java:8: warning: The prover cannot establish an assertion (Assert) in method m",33
                        ,"/tt/TestJava.java:10: warning: The prover cannot establish an assertion (Assert) in method m",38
                        ,"/tt/TestJava.java:11: warning: The prover cannot establish an assertion (Assert) in method m1",12
                        ,"/tt/TestJava.java:15: warning: The prover cannot establish an assertion (Assert) in method m2",12 }

                );
    }
   
    /** This test checks that the specs of methods in nested, local and anonymous classes are used */
    @Test
    public void testNestedMethodSpecs() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  public void me(TestJava o) {\n"
                +"       class C { /*@ ensures false; */ void mc() {  }};\n"
                +"       C x;\n"
                +"       class D { void md() {  }};\n" // Line 10
                +"       D y = new D() { /*@ ensures false; */ void md() {}};\n"
                +"       class E { /*@ ensures false; */void me() {  }};\n"
                +"       E z = new E() {  void me() {}};\n"
                +"  }\n"
                
                +"  public static class A {\n"
                +"       //@ ensures false;\n"
                +"     public void m2() {\n"
                +"     }\n"
                +"  }\n"
                

                
                +"}"
                , !option.equals("-custom") ?
                new Object[] {"/tt/TestJava.java:8: warning: The prover cannot establish an assertion (Postcondition) in method mc",45
                ,"/tt/TestJava.java:8: warning: Associated declaration",22
                ,"/tt/TestJava.java:11: warning: The prover cannot establish an assertion (Postcondition) in method md",51
                ,"/tt/TestJava.java:11: warning: Associated declaration",28
                ,"/tt/TestJava.java:12: warning: The prover cannot establish an assertion (Postcondition) in method me",44
                ,"/tt/TestJava.java:12: warning: Associated declaration",22
                ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Postcondition) in method me",30
                ,"/tt/TestJava.java:12: warning: Associated declaration",22
                ,"/tt/TestJava.java:17: warning: The prover cannot establish an assertion (Postcondition) in method m2",18
                ,"/tt/TestJava.java:16: warning: Associated declaration",12 }
                : new Object[] {"/tt/TestJava.java:8: warning: The prover cannot establish an assertion (Postcondition) in method mc",45
                        ,"/tt/TestJava.java:8: warning: Associated declaration",22
                        ,"/tt/TestJava.java:11: warning: The prover cannot establish an assertion (Postcondition) in method md",51
                        ,"/tt/TestJava.java:11: warning: Associated declaration",28
                        ,"/tt/TestJava.java:12: warning: The prover cannot establish an assertion (Postcondition) in method me",44
                        ,"/tt/TestJava.java:12: warning: Associated declaration",22
                        ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Postcondition) in method me",30
                        ,"/tt/TestJava.java:12: warning: Associated declaration",22
                        // FIXME - why duplicated
                        ,"/tt/TestJava.java:8: warning: The prover cannot establish an assertion (Postcondition) in method mc",45
                        ,"/tt/TestJava.java:8: warning: Associated declaration",22
                        ,"/tt/TestJava.java:11: warning: The prover cannot establish an assertion (Postcondition) in method md",51
                        ,"/tt/TestJava.java:11: warning: Associated declaration",28
                        ,"/tt/TestJava.java:12: warning: The prover cannot establish an assertion (Postcondition) in method me",44
                        ,"/tt/TestJava.java:12: warning: Associated declaration",22
                        ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Postcondition) in method me",30
                        ,"/tt/TestJava.java:12: warning: Associated declaration",22
                        ,"/tt/TestJava.java:17: warning: The prover cannot establish an assertion (Postcondition) in method m2",18
                        ,"/tt/TestJava.java:16: warning: Associated declaration",12 }
                
                );
    }
   
    /** This test checks that the specs of nested, local and anonymous classes are used */
    @Test
    public void testNestedClassSpecs() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"
                
                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"
                
                +"  public void m1(TestJava o) {\n"
                +"       class C {  \n"
                +"           //@ invariant false;\n" 
                +"           void m() {  }};\n"  // Line 10
                +"       C x;\n"
                +"       class D { void m() {  }};\n"
                +"       D y = new D() { /*@ invariant false;*/ void m() {}};\n"
                +"       class E { /*@ invariant false;*/void m() {  }};\n"
                +"       E z = new E() {  void m() {}};\n"
                +"  }\n"
                
                +"  public static class A {\n"
                +"     //@ invariant false;\n"
                +"     public void m2() {\n"
                +"     }\n"
                +"  }\n"
                

                
                +"}"
                , !option.equals("-custom") ?
                new Object[]{"/tt/TestJava.java:8: warning: The prover cannot establish an assertion (InvariantExit) in method <init>",8
                ,"/tt/TestJava.java:9: warning: Associated declaration",16
                ,"/tt/TestJava.java:10: warning: Invariants+Preconditions appear to be contradictory in method C.m()",17
                ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (InvariantExit) in method <init>",22
                ,"/tt/TestJava.java:13: warning: Associated declaration",28
                ,"/tt/TestJava.java:13: warning: Invariants+Preconditions appear to be contradictory in method .m()",52
                ,"/tt/TestJava.java:14: warning: The prover cannot establish an assertion (InvariantExit) in method <init>",8
                ,"/tt/TestJava.java:14: warning: Associated declaration",22
                ,"/tt/TestJava.java:14: warning: Invariants+Preconditions appear to be contradictory in method E.m()",45
                ,"/tt/TestJava.java:15: warning: The prover cannot establish an assertion (InvariantExit) in method <init>",22
                ,"/tt/TestJava.java:14: warning: Associated declaration",22
                ,"/tt/TestJava.java:15: warning: Invariants+Preconditions appear to be contradictory in method .m()",30
                ,"/tt/TestJava.java:17: warning: The prover cannot establish an assertion (InvariantExit) in method <init>",17
                ,"/tt/TestJava.java:18: warning: Associated declaration",10
                ,"/tt/TestJava.java:19: warning: Invariants+Preconditions appear to be contradictory in method tt.TestJava.A.m2()",18 }
        :       new Object[]{"/tt/TestJava.java:8: warning: The prover cannot establish an assertion (Invariant) in method <init>",8
                        ,"/tt/TestJava.java:9: warning: Associated declaration",26
                        ,"/tt/TestJava.java:10: warning: Invariants+Preconditions appear to be contradictory in method C.m()",17
                        ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Invariant) in method <init>",22
                        ,"/tt/TestJava.java:13: warning: Associated declaration",38
                        ,"/tt/TestJava.java:13: warning: Invariants+Preconditions appear to be contradictory in method .m()",52
                        ,"/tt/TestJava.java:14: warning: The prover cannot establish an assertion (Invariant) in method <init>",8
                        ,"/tt/TestJava.java:14: warning: Associated declaration",32
                        ,"/tt/TestJava.java:14: warning: Invariants+Preconditions appear to be contradictory in method E.m()",45
                        ,"/tt/TestJava.java:15: warning: Invariants+Preconditions appear to be contradictory in method .m()",30 // FIXME - this one is extra
                        // FIXME - why duplicated
                        ,"/tt/TestJava.java:8: warning: The prover cannot establish an assertion (Invariant) in method <init>",8
                        ,"/tt/TestJava.java:9: warning: Associated declaration",26
                        ,"/tt/TestJava.java:10: warning: Invariants+Preconditions appear to be contradictory in method C.m()",17
                        ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Invariant) in method <init>",22
                        ,"/tt/TestJava.java:13: warning: Associated declaration",38
                        ,"/tt/TestJava.java:13: warning: Invariants+Preconditions appear to be contradictory in method .m()",52
                        ,"/tt/TestJava.java:14: warning: The prover cannot establish an assertion (Invariant) in method <init>",8
                        ,"/tt/TestJava.java:14: warning: Associated declaration",32
                        ,"/tt/TestJava.java:14: warning: Invariants+Preconditions appear to be contradictory in method E.m()",45
                        ,"/tt/TestJava.java:15: warning: Invariants+Preconditions appear to be contradictory in method .m()",30 // FIXME - this one is extra

                        ,"/tt/TestJava.java:17: warning: The prover cannot establish an assertion (Invariant) in method <init>",17
                        ,"/tt/TestJava.java:18: warning: Associated declaration",20
                        ,"/tt/TestJava.java:19: warning: Invariants+Preconditions appear to be contradictory in method tt.TestJava.A.m2()",18 }

                );
    }
    
    /** This tests that the specs of model classes and methods are checked */
    @Test
    public void testModelSpecs() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"

                +"  public TestJava t;\n"
                +"  public int a;\n"
                +"  public static int b;\n"

                +"  public int m1(TestJava o) {\n"
                +"       /*@ model class C {  \n"
                +"           invariant false;\n" 
                +"           void mc() {  }};*/ \n"  // Line 10
                +"       /*@ model class D {  \n"
                +"           ensures false;\n" 
                +"           void md() {  }};*/ \n" 
                +"       /*@ model class E {  \n"
                +"           void me() {  assert false; }};*/ \n"
                +"       //@ ghost E e;\n"
                +"       return 0;\n"
                +"  }\n"

                +"  /*@ ensures false;\n"
                +"      model void mm() {}*/\n"

                +"  /*@ model void mn() {  assert false;  }*/\n"

                +"  /*@ model public static class A {\n"
                +"     invariant false;\n"
                +"     public void m2() {\n"
                +"     }*/\n"
                +"  }\n"

                +"}"
                +"  /*@ model class B {\n"
                +"     invariant false;\n"
                +"     public void mb() {\n"
                +"     }*/\n"
                +"  }\n"

                +"  /*@ model class BB {\n"
                +"     ensures false;\n"
                +"     public void mbb() {\n"
                +"     }*/\n"
                +"  }\n"


                ,!option.equals("-custom") ?
                new Object[]{"/tt/TestJava.java:8: warning: The prover cannot establish an assertion (InvariantExit) in method <init>",18
                ,"/tt/TestJava.java:9: warning: Associated declaration",12
                ,"/tt/TestJava.java:10: warning: Invariants+Preconditions appear to be contradictory in method C.mc()",17
                ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Postcondition) in method md",17
                ,"/tt/TestJava.java:12: warning: Associated declaration",12
                ,"/tt/TestJava.java:15: warning: The prover cannot establish an assertion (Assert) in method me",25
                ,"/tt/TestJava.java:20: warning: The prover cannot establish an assertion (Postcondition) in method mm",18
                ,"/tt/TestJava.java:19: warning: Associated declaration",7
                ,"/tt/TestJava.java:21: warning: The prover cannot establish an assertion (Assert) in method mn",26
                ,"/tt/TestJava.java:27: warning: The prover cannot establish an assertion (InvariantExit) in method <init>",14
                ,"/tt/TestJava.java:28: warning: Associated declaration",6
                ,"/tt/TestJava.java:29: warning: Invariants+Preconditions appear to be contradictory in method tt.B.mb()",18
                ,"/tt/TestJava.java:34: warning: The prover cannot establish an assertion (Postcondition) in method mbb",18
                ,"/tt/TestJava.java:33: warning: Associated declaration",6}
        :       new Object[]{"/tt/TestJava.java:8: warning: The prover cannot establish an assertion (Invariant) in method <init>",18
                        ,"/tt/TestJava.java:9: warning: Associated declaration",22
                        ,"/tt/TestJava.java:10: warning: Invariants+Preconditions appear to be contradictory in method C.mc()",17
                        ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Postcondition) in method md",17
                        ,"/tt/TestJava.java:12: warning: Associated declaration",12
                        ,"/tt/TestJava.java:15: warning: The prover cannot establish an assertion (Assert) in method me",25
                        // FIXME - why the duplication?
                        ,"/tt/TestJava.java:8: warning: The prover cannot establish an assertion (Invariant) in method <init>",18
                        ,"/tt/TestJava.java:9: warning: Associated declaration",22
                        ,"/tt/TestJava.java:10: warning: Invariants+Preconditions appear to be contradictory in method C.mc()",17
                        ,"/tt/TestJava.java:13: warning: The prover cannot establish an assertion (Postcondition) in method md",17
                        ,"/tt/TestJava.java:12: warning: Associated declaration",12
                        ,"/tt/TestJava.java:15: warning: The prover cannot establish an assertion (Assert) in method me",25,"/tt/TestJava.java:20: warning: The prover cannot establish an assertion (Postcondition) in method mm",18
                        ,"/tt/TestJava.java:19: warning: Associated declaration",7
                        ,"/tt/TestJava.java:21: warning: The prover cannot establish an assertion (Assert) in method mn",26
                        ,"/tt/TestJava.java:27: warning: The prover cannot establish an assertion (Invariant) in method <init>",14
                        ,"/tt/TestJava.java:28: warning: Associated declaration",16
                        ,"/tt/TestJava.java:29: warning: Invariants+Preconditions appear to be contradictory in method tt.B.mb()",18
                        ,"/tt/TestJava.java:34: warning: The prover cannot establish an assertion (Postcondition) in method mbb",18
                        ,"/tt/TestJava.java:33: warning: Associated declaration",6}

        );
    }

    @Test
    public void testAnon() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { \n"

                +"  public int m1(TestJava o) {\n"
                +"       //@ assert new TestJava() {  invariant false; int i; } == null; \n"  // Line 7
                +"       return 0;\n"
                +"  }\n"
                +"}\n"
                , !option.equals("-custom") ?
                new Object[]{"/tt/TestJava.java:5: warning: The prover cannot establish an assertion (InvariantExit) in method <init>",34
                ,"/tt/TestJava.java:5: warning: Associated declaration",37
                ,"/tt/TestJava.java:5: warning: The prover cannot establish an assertion (Assert) in method m1",12 }
                : new Object[]{"/tt/TestJava.java:5: warning: The prover cannot establish an assertion (Invariant) in method <init>",34
                        ,"/tt/TestJava.java:5: warning: Associated declaration",47
                        ,"/tt/TestJava.java:5: warning: The prover cannot establish an assertion (Invariant) in method <init>",34
                        ,"/tt/TestJava.java:5: warning: Associated declaration",47 // FIXME - why is the warning duplicated?
                        ,"/tt/TestJava.java:5: warning: The prover cannot establish an assertion (Assert) in method m1",12 }


        );
    }


    @Test
    public void testMethodsInSpecs() {
        helpTCX("tt.TestJava","package tt; \n"
                +" import org.jmlspecs.annotation.*; \n"
                +"@NonNullByDefault public class TestJava { static boolean b; \n"

                    +"  //@ normal_behavior\n"
                    +"  //@   ensures \\result == k+1;\n"
                    +"  //@ pure\n"
                    +"  public int m(int k) {\n"
                    +"       return k+1;\n"
                    +"  }\n"

                    +"  //@ ensures \\result == 2 + m(j+1) - 3; \n"
                    +"  public int m1(int j) {\n"
                    +"       return j+1;\n"
                    +"  }\n"

                    +"  //@ ensures \\result == 2 + m(j+1) - 2; \n"
                    +"  public int m1bad(int j) {\n"
                    +"       return j+1;\n"
                    +"  }\n"

                    +"  //@ requires m(j) == 3; \n"
                    +"  //@ ensures \\result == 3; \n"
                    +"  public int m3b(int j) {\n"
                    +"       return j+1;\n"
                    +"  }\n"  // Line 22

                    +"  public void m2(int j) {\n"
                    +"       j = j+1;\n"
                    +"       //@ assert m(j) == \\old(j) + 2;\n"
                    +"  }\n"
                    
                    +"  //@ normal_behavior\n"
                    +"  //@   requires b;\n"
                    +"  //@   ensures \\result == k+1;\n"
                    +"  //@ pure\n"
                    +"  public int mm(int k) {\n"
                    +"       return k+1;\n"
                    +"  }\n"

                    +"  //@ ensures \\result == mm(j); \n" // Postcondition error - undefined precondition for mm
                    +"  public int m4bad(int j) {\n"
                    +"       return j+1;\n"
                    +"  }\n"

                    +"  //@ requires b; \n"
                    +"  //@ ensures \\result == mm(j); \n"
                    +"  public int m4(int j) {\n"
                    +"       return j+1;\n"
                    +"  }\n"

                    +"  //@ ensures b ==> \\result == mm(j); \n"
                    +"  public int m4a(int j) {\n"
                    +"       return j+1;\n"
                    +"  }\n"


                    +"}\n"
                    ,!option.equals("-custom") ?
                    		new Object[]{"/tt/TestJava.java:16: warning: The prover cannot establish an assertion (Postcondition) in method m1bad",8
                                        ,"/tt/TestJava.java:14: warning: Associated declaration",7
                                        ,"/tt/TestJava.java:34: warning: The prover cannot establish an assertion (UndefinedCalledMethodPrecondition) in method m4bad",28
                                        ,"/tt/TestJava.java:28: warning: Associated declaration",9 }
                    :       new Object[]{"/tt/TestJava.java:16: warning: The prover cannot establish an assertion (Postcondition) in method m1bad",8
                                    ,"/tt/TestJava.java:14: warning: Associated declaration",7
                                    ,"/tt/TestJava.java:36: warning: The prover cannot establish an assertion (Postcondition) in method m4bad",8
                                    ,"/tt/TestJava.java:34: warning: Associated declaration",7 }
                );
    }

        // TODO
        // Need to check anonymous classes within specs
        // Need to check non-static inner classes 
        // Need to check anonymous classes for non-static classes
   

}