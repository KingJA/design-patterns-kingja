package com.kingja.designpatterns.patterns.structural.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Description:TODO
 * Create Time:2020/12/16 0016 16:57
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class EnglishTranslatorDynamicProxy implements InvocationHandler {
    private Object targetObject;

    public EnglishTranslatorDynamicProxy(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("==========【动态代理】执行 "+method.getName()+" ==========");
        preInvoke();
        Object result = method.invoke(targetObject, args);
        postInvoke();
        return result;
    }

    private void preInvoke() {
        System.out.println("【VIP】 验证用户权限->通过");
    }

    private void postInvoke() {
        System.out.println("【VIP】 进行计费 ¥");
    }

    /**
     * 生成代理对象
     * @return 代理对象
     */
    public Object bind() {
        Class<?> clazz = targetObject.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }
}
