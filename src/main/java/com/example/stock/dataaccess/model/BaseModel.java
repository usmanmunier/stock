package com.example.stock.dataaccess.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class BaseModel {

    @Id
    @GeneratedValue
    private int id;

    public int getId() {
        return this.id;
    }
    public void setId(int id) { this.id = id; }
}
