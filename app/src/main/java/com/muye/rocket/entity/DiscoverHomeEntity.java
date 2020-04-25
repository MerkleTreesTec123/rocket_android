package com.muye.rocket.entity;

import java.util.List;

public class DiscoverHomeEntity {

    private String id;
    private String name;
    private List<DiscoverEntity> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DiscoverEntity> getList() {
        return list;
    }

    public void setList(List<DiscoverEntity> list) {
        this.list = list;
    }

}
