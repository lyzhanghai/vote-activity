package com.lyl.outsourcing.activity.dao

/**
 * dao的基类，封装了常用的一些 dao 方法
 * @author liuwei22
 * @date 2015-12-25
 */
abstract class BaseDao<T> {
    Class<T> entityClaz;            //当前 entity 的 class 类型

    /**
     * dao 对应的 entity class
     * @param claz
     */
    BaseDao(Class claz) {
        entityClaz=claz
    }
}
