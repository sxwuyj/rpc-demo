package com.abc.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by v_yanjunwu on 2020/4/15
 */
@Data
public class Invocation implements Serializable {

    /**
     * 接口名即服务名
     */
    private String className;
    /**
     * 需要远程调用的方法名
     */
    private String methodName;
    /**
     *参数类型列表
     */
    private Class<?>[] paramTypes;
    /**
     * 参数值列表
     */
    private Object[] paramValues;
}
