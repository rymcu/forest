package com.rymcu.forest.lucene.model;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "lucene_baike")
public class Baike {
    private Integer id;

    private String title;

    private String summary;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        if(title == null){
            title = "";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getSummary() {
        if(summary == null){
            summary = "";
        }
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary == null ? null : summary.trim();
    }
}
