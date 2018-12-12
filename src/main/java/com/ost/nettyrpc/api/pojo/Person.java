package com.ost.nettyrpc.api.pojo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author xyl
 * @Create 2018-11-28 15:35
 * @Desc
 **/
@Data
public class Person{
    private int id;

    private String name;

    private Date birth;

    private boolean man;

    private List<String> list;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birth=" + birth +
                ", man=" + man +
                ", list=" + list +
                '}';
    }
}
