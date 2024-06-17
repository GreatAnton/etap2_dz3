package edu2.innotech;

import org.junit.jupiter.api.*;

import java.lang.reflect.Method;

public class MakeCacheTest {
    @Test
    @DisplayName("Создание Proxy")
    public void createProxy() {
        Fraction fr = new Fraction(2, 3);
        Fractionable num = Utils.cache(fr);

        Class classProxy = num.getClass();
        Assertions.assertTrue(classProxy.getName().indexOf("Proxy") > 0);
    }

    @Test
    @DisplayName("Проверка наличия аннотаций @Mutator и @Cache")
    public void checkAnnotations() throws NoSuchMethodException {
        TestFraction fr = new TestFraction(2, 3);
        Fractionable num = Utils.cache(fr);
        num.doubleValue();
        Class objClass = fr.getClass();
        Method m = objClass.getDeclaredMethod("doubleValue");
        Assertions.assertTrue(m.isAnnotationPresent(Cache.class));
        m = objClass.getDeclaredMethod("setNum", int.class);
        Assertions.assertTrue(m.isAnnotationPresent(Mutator.class));
        m = objClass.getDeclaredMethod("setDenum", int.class);
        Assertions.assertTrue(m.isAnnotationPresent(Mutator.class));
        m = objClass.getDeclaredMethod("doubleValueInverse");
        Assertions.assertTrue(m.isAnnotationPresent(Cache.class));
    }

    @Test
    @DisplayName("Проверка быстрого вызова Proxy методов")
    public void createCache() {
        TestFraction fr = new TestFraction(2, 3);
        Fractionable num = Utils.cache(fr);
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 1);
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 1);
        num.doubleValue();
        num.toString();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 2);
        num.setNum(5);
        Assertions.assertEquals(fr.countCachedMethodInvokes, 2);
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 3);
        num.setDenum(6);
        Assertions.assertEquals(fr.countCachedMethodInvokes, 3);
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 4);
        num.doubleValueInverse();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 5);
        num.doubleValueInverse();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 5);
    }

}
