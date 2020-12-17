package com.kingja.designpatterns.patterns.structural.proxy;

/**
 * Description:TODO
 * Create Time:2020/12/16 0016 20:57
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class EnglishTranslatorStaticProxy implements ITranslator{
    private ITranslator translator;

    public EnglishTranslatorStaticProxy(ITranslator translator) {
        this.translator = translator;
    }

    @Override
    public boolean translateArticle(String article) {
        System.out.println("==========【静态代理】执行 translateArticle ==========");
        System.out.println("【VIP】 验证用户权限->通过");
        translator.translateArticle(article);
        System.out.println("【VIP】 进行计费 ¥");
        return true;
    }

    @Override
    public boolean translateWord(String word) {
        System.out.println("==========【静态代理】执行 translateWord ==========");
        System.out.println("【VIP】 验证用户权限->通过");
        translator.translateWord(word);
        System.out.println("【VIP】 进行计费 ¥");
        return true;
    }
}
