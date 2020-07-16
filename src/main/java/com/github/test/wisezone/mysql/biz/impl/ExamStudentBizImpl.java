package com.github.test.wisezone.mysql.biz.impl;




import com.github.test.wisezone.mysql.bean.ExamStudent;
import com.github.test.wisezone.mysql.biz.ExamStudentBiz;
import com.github.test.wisezone.mysql.dao.ExamStudentDao;
import com.github.test.wisezone.mysql.dao.impl.ExamStudentDaoImpl;

import java.util.List;

public class ExamStudentBizImpl implements ExamStudentBiz {
    //调用数据访问层的对象
    private ExamStudentDao dao = new ExamStudentDaoImpl();


    public int add(ExamStudent student) {
        return dao.add(student);
    }


    public int update(ExamStudent student) {
        return dao.update(student);
    }

    public int remove(int flowId) {
        return dao.remove(flowId);
    }


    public List<ExamStudent> queryAll() {
        return dao.queryAll();
    }

    public ExamStudent queryOne(int flowId) {
        return dao.queryOne(flowId);
    }
}
