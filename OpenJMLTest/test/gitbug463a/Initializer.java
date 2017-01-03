public class Initializer {
    public int a;

    /*@ public normal_behavior
      @   ensures this.a == a;
      @ pure */
    public Initializer(int a) {
        this.a = a;
    }

    /*@ public normal_behavior
      @   assignable a;
      @   ensures this.a == \old(this.a) + 1;
      @   ensures \fresh(\result);
      @   ensures \result.equals(new Initializer(\old(a)));
      @*/
    public Initializer dupe() {
        Initializer other = new Initializer(a);
        a++;
        return other;
    }

    /*@ public normal_behavior
      @   assignable \nothing;
      @   ensures \result <==> obj instanceof Initializer && ((Initializer) obj).a == a;
      @*/
    public /*@ pure @*/ boolean equals(Object obj) {
        return obj instanceof Initializer && ((Initializer) obj).a == a;
    }
}