package com.temah.jwt.aop;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE}) // //接口、类及方法
@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Inherited // 指定被修饰的Annotation将具有继承性
@Documented // 指定被修饰的该Annotation可以被javadoc工具提取成文档.
public @interface VerifyToken {
}
