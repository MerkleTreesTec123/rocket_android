package com.muye.rocket.entity;

public class BannerEntity {

    private String id;
    private String name;
    private String thumb;
    private String url;

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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "BannerEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", thumb='" + thumb + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
