package com.yh.cn.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author 16051
 */
public class Main {


    public static void main(String[] args) {

        Student student = new Student(1, "张三", 20,"2019-05-17 12:00:19");
        Student student2 = new Student(1, "张三2", 25,"2019-05-18 11:56:23");
        Student student3 = new Student(1, "张三3", 27,"2019-05-17 16:00:09");
        List<Student> list = new ArrayList<Student>() {{
            add(student);
            add(student2);
            add(student3);
        }};
        try {
            Collections.sort(list, new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    try {
                        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o2.getCreateTime()).
                                compareTo(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(o1.getCreateTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
            System.out.println(list);
        }catch (Exception e){

        }

    }


}
