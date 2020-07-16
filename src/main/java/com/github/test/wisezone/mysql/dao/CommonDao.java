package com.github.test.wisezone.mysql.dao;

/**
 * 通用的接口
 * 每个bean都应该包含增、删、改、根据id查询
 * @param <T>
 */
public interface CommonDao<T> {
    /**
     * 通用的新增的方法
     */
    int add(T t);

    /**
     * 通用的修改方法
     * @param t
     * @return
     */
    int update(T t);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    int remove(int id);

    /**
     * 根据id查询一条数据
     * @param id
     * @return
     */
    T queryOne(int id);
}
