package ex01E;

class C extends B
{
  int x=3, a;

  void m()
  {
    System.out.println("Je suis dans la méthode m d'une instance de C");
  }

  void test()
  {
    a = super.x;
    System.out.println(a);
    // Super.super impossible
    //a = super.super.x;
    a = ((B)this).x;
    System.out.println(a);
    a = ((A)this).x;
    System.out.println(a);
    super.m();
    // Super.super impossible
    //super.super.m();
    ((B)this).m(); // (1)
    A aclass = new A();
    aclass.m();
    aclass = new B();
    aclass.m();
  }

  public static void main(String[] args)
  {
    C c = new C();
    c.test();
  }
}
