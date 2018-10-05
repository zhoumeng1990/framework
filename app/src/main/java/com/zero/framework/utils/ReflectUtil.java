package com.zero.framework.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zero on 2017/6/9.
 */

public class ReflectUtil {

    public static List<Type> MethodHandler(Type[] types) {
        List<Type> needTypes = new ArrayList<>();

        for (Type paramType : types) {
            /*
             * ParameterizedType 处理多级泛型
             *
             * GenericArrayType 处理数组泛型
             *
             * TypeVariable 基本数据类型变量
             *
             * WildcardType 通配符类型
             */
            if (paramType instanceof ParameterizedType) {
                /*
                 * getActualTypeArguments()返回表示此类型实际类型参数的 Type 对象的数组
                 *
                 * 实际就是获得超类的泛型参数的实际类型
                 */
                Type[] parenTypes = ((ParameterizedType) paramType).getActualTypeArguments();
                for (Type childType : parenTypes) {
                    needTypes.add(childType);
                    if (childType instanceof ParameterizedType) {
                        Type[] childTypes = ((ParameterizedType) childType).getActualTypeArguments();
                        for (Type type : childTypes) {
                            needTypes.add(type);
                        }
                    }
                }
            }
        }
        return needTypes;
    }
}
