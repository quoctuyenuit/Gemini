package com.example.a15520824.phanmemchuyensau;

import java.io.Serializable;

/**
 * Created by QuocTuyen on 6/17/2018.
 */

public class ActionModel implements Serializable {
    private String key;
    private String name;
    private String id;

    public ActionModel(String key, String name, String id) {
        this.key = key;
        this.name = name;
        this.id = id;
    }

    public ActionModel(String key, String name){
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    @Override
    public String toString() {
        return this.key + "-" +this.name + "-" + this.id;
    }
}
