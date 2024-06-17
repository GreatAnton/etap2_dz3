import edu2.innotech.Fraction;
import edu2.innotech.Fractionable;
import edu2.innotech.Utils;

public class Main {
    public static void main(String... args) {
        System.out.println("===================================");
        System.out.println("Запуск основного тела программы");
        System.out.println("===================================");
        Fraction fr = new Fraction(2, 3);
        Fractionable num = Utils.cache(fr);
        System.out.println(num.doubleValue());// sout сработал
        System.out.println(num.doubleValue());// sout молчит
        System.out.println(num.doubleValue());// sout молчит
        num.setNum(5);
        System.out.println(num.doubleValue());// sout сработал
        System.out.println(num.doubleValue());// sout молчит
        System.out.println("===================================");
        System.out.println("Завершение основного тела программы");
        System.out.println("===================================");
        //System.gc();
    }
}

