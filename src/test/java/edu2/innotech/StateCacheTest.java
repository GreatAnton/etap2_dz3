package edu2.innotech;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StateCacheTest {
    @Test
    @DisplayName("Проверка работы потока очистки кеша")
    public void checkThreadGCCache() throws InterruptedException {
        TestFraction fr = new TestFraction(2, 3);
        Fractionable num = Utils.cache(fr);
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 1);
        Thread.sleep(2000);
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 2);
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 2);
        Thread.sleep(1200);
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 3);
        num.doubleValueInverse();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 4);
        num.doubleValue();
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 4);
        num.setNum(5);
        Assertions.assertEquals(fr.countCachedMethodInvokes, 4);
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 5);
        num.setNum(2);
        num.doubleValue();
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 5);
        Thread.sleep(1500);
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 6);
        num.doubleValue();
        Assertions.assertEquals(fr.countCachedMethodInvokes, 6);
    }
}
