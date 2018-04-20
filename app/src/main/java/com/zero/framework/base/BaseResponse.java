package com.zero.framework.base;

import com.zero.framework.annotation.KeepNotProguard;

import java.io.Serializable;

/**
 * {"code": 0,"message": "ok","result": {}}
 * 由于公司代码最外层都是这个格式，所以采用这个方式包裹
 * 第二版才参与公司项目，这是之前搞Java的人来写的，所以依然存在get/set，如果没有特别需要，可以删掉get/set，字段用public即可
 * @param <T>
 */
@KeepNotProguard
public class BaseResponse<T> implements Serializable {
    public int code;
    public String msg;
    public T result;
}
