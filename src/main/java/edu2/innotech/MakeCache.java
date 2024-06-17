package edu2.innotech;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class MakeCache<T> implements InvocationHandler {
    private T obj;
    private volatile ConcurrentHashMap<Method, ConcurrentHashMap<StateCache, Object>> hashMapMethodsValues = new ConcurrentHashMap<>();

    Thread myGC;

    public MakeCache(T obj) {
        this.obj = obj;

        this.myGC = new Thread(() -> {
            boolean inter = false;
            while (true) {
                if (Thread.currentThread().isInterrupted() || inter) {
                    break;
                }

                if (!hashMapMethodsValues.isEmpty()) {
                    long curTime = System.currentTimeMillis();
                    for (ConcurrentHashMap<StateCache, Object> so : hashMapMethodsValues.values()) {
                        if (!(so.isEmpty())) {
                            for (StateCache s : so.keySet()) {
                                if ((s.timeLife != 0) && (s.timeLife < curTime)) {
                                    so.remove(s);
                                }
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    inter = true;
                }
            }
        });
        this.myGC.setDaemon(true);
        this.myGC.start();
    }

    private StateCache getObjState() throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();

        Object[] fv = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].isAnnotationPresent(NoCache.class)) {
                fields[i].setAccessible(true);
                fv[i] = fields[i].get(obj);
            }
        }

        return new StateCache(fv);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class myObjClass = obj.getClass();
        Method m = myObjClass.getDeclaredMethod(method.getName(), method.getParameterTypes());

        if (m.isAnnotationPresent(Mutator.class)) {
            return method.invoke(obj, args);
        }

        StateCache st = getObjState();

        Object methodValue;

        if (m.isAnnotationPresent(Cache.class)
                && hashMapMethodsValues.containsKey(method) && hashMapMethodsValues.get(method).containsKey(st)) {
            methodValue = hashMapMethodsValues.get(method).get(st);
            Cache annotation = m.getAnnotation(Cache.class);
            if (annotation.timeLyfe() != 0)
                st.timeLife = annotation.timeLyfe() + System.currentTimeMillis();
            else st.timeLife = annotation.timeLyfe();
            hashMapMethodsValues.get(method).replace(st, methodValue);
        } else {
            methodValue = method.invoke(obj, args);

            if (m.isAnnotationPresent(Cache.class)) {
                st = getObjState();
                Cache annotation = m.getAnnotation(Cache.class);
                if (annotation.timeLyfe() != 0)
                    st.timeLife = annotation.timeLyfe() + System.currentTimeMillis();
                else st.timeLife = annotation.timeLyfe();

                ConcurrentHashMap<StateCache, Object> hmVal = new ConcurrentHashMap<>();
                hmVal.put(st, methodValue);

                if (hashMapMethodsValues.containsKey(method))
                    hashMapMethodsValues.get(method).put(st, methodValue);
                else
                    hashMapMethodsValues.put(method, hmVal);
            }
        };
        return methodValue;
    }
}
