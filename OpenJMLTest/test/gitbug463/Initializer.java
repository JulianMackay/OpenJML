public class Initializer {
    public int a;

    /*@ public normal_behavior
      @   assignable this.a;
      @   ensures this.a == a;
      @*/
    public Initializer(int a) {
        this.a = a;
    }

    /*@ public normal_behavior
      @   assignable a;
      @   ensures this.a == \old(this.a) + 1;
      @   ensures \fresh(\result);
      @   ensures \result.equals(\old(new Initializer(a)));
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