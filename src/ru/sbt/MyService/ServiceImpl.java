package ru.sbt.MyService;

import ru.sbt.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acer on 02.12.2016.
 */
public class ServiceImpl implements Service {

    private void process(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int doHardWork(String s, int i) {
        process(2000);
        return s.hashCode() + i;
    }

    @Override
    public List<Object> doSuperWork(int i) {
        process(1500);
        ArrayList<Object> objects = new ArrayList<>();
        for (int j = 0; j < i; j++) {
            objects.add(j);
        }
        return objects;
    }


    @Override
    public String doMagicWork(String s, boolean b) {
        process(2000);
        return b ? s : s.length() + "";
    }

    @Override
    public Person doPersonWork(String name) {
        process(500);
        return new Person(name,name.hashCode()%100);
    }
}
