public class TestClass{
    private Integer test01;

    public TestClass(){
        test01 = new Integer(5);
        System.out.println(test01.hashCode());
    }

    public Integer getTest01() {
        return test01;
    }
}
