package com.fxj.simpleRouterAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*元注解@Target用于定义该注解用于修饰哪些程序元素*/
@Target(ElementType.TYPE)
/*元注解@Retention用于定义该注解保留多长时间，即声明这个注解的生命周期*/
@Retention(RetentionPolicy.CLASS)
public @interface Router {
    String path();
}
