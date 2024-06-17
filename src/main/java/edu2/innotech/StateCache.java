package edu2.innotech;

import java.util.Arrays;

public class StateCache {
    Object[] fieldValuess;
    long timeLife;

    public StateCache(Object[] fieldValuess) {
        this.fieldValuess = fieldValuess;
        this.timeLife = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateCache state = (StateCache) o;
        return Arrays.equals(fieldValuess, state.fieldValuess);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(fieldValuess);
    }
}
