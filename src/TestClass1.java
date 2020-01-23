public class TestClass1 extends TestClass {

    public static void main(String[] args) {
        TestClass test = new TestClass();

        Integer test01 = test.getTest01();
        test01 = test01 - 2;
        System.out.println(test01.hashCode());
        System.out.println("Object int: " + test.getTest01());
        System.out.println("Gotten int: " + test01);
    }

}
