package edu2.innotech;

public class TestFraction implements Fractionable {
    private int num;
    private int denum;
    @NoCache
    public int countCachedMethodInvokes = 0;

    public TestFraction(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Override
    @Cache(timeLyfe = 1000)
    public double doubleValue() {
        countCachedMethodInvokes++;
        System.out.println("invoke double value");
        return num/denum;
    }

    @Override
    @Cache(timeLyfe = 500)
    public double doubleValueInverse() {
        countCachedMethodInvokes++;
        System.out.println("invoke double value inverse");
        return (double) denum/num;
    }

    @Override
    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Override
    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache
    public String toString() {
        countCachedMethodInvokes++;
        System.out.println("invoke toString");
        return "TestFraction{" +
                "num=" + num +
                ", denum=" + denum +
                ", count=" + countCachedMethodInvokes +
                '}';
    }
}
