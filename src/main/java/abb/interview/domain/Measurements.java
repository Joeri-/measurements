package abb.interview.domain;

import java.util.HashMap;

public class Measurements extends HashMap<Key, Measurement>{

    public Measurement find(Key key) {
        return get(key);
    }
}
