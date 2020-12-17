package com.kingja.designpatterns.patterns.structural.proxy;

/**
 * Description:TODO
 * Create Time:2020/12/16 0016 20:57
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class EnglishImageTranslatorStaticProxy implements IImageTranslator{
    private IImageTranslator translator;

    public EnglishImageTranslatorStaticProxy(IImageTranslator translator) {
        this.translator = translator;
    }

    @Override
    public boolean translateImage(byte[] res) {
        System.out.println("==========【动态代理】执行 translateImage ==========");
        System.out.println("【VIP】 验证用户权限->通过");
        translator.translateImage(res);
        System.out.println("【VIP】 进行计费 ¥");
        return false;
    }
}
