package com.github.test.wisezone.mysql.biz;


import com.github.test.wisezone.mysql.bean.ExamStudent;

import java.util.List;

/**
 * ExamStudent业务逻辑层
 */
public interface ExamStudentBiz extends CommonBiz<ExamStudent>{
    /**
     * 全查
     * @return
     */
    List<ExamStudent> queryAll();
}
