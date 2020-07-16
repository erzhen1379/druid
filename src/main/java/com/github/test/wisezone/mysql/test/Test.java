package com.github.test.wisezone.mysql.test;


import com.github.test.wisezone.mysql.bean.ExamStudent;
import com.github.test.wisezone.mysql.biz.ExamStudentBiz;
import com.github.test.wisezone.mysql.biz.impl.ExamStudentBizImpl;

import java.util.List;

public class Test {
    public static ExamStudentBiz biz = new ExamStudentBizImpl();

    public static void queryOne() {
        ExamStudent examStudent = biz.queryOne(6);
        System.out.println(examStudent);

    }

    public static void update() {
        ExamStudent examStudent = biz.queryOne(6);
        examStudent.setStudentName("李麻子");
        examStudent.setLocation("渝中区");
        int update = biz.update(examStudent);
        System.out.println(update > 0 ? "修改成功！" : "修改失败！");
    }

    public static void remove() {
        int remove = biz.remove(6);
        System.out.println(remove > 0 ? "删除成功！" : "删除失败！");
    }

    public static void queryAll() {
        List<ExamStudent> list = biz.queryAll();
        for (ExamStudent student : list) {
            System.out.println(student);
        }
    }

    public static void main(String[] args) {
        //测试添加
        //测试根据id查询
        //queryOne();

        //测试修改的方法
        update();

        //测试根据id删除
        //remove();

        //测试查询全部
        queryAll();
    }
}
