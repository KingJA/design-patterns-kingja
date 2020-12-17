package com.kingja.designpatterns.patterns.structural.proxy;

/**
 * Description:TODO
 * Create Time:2020/12/16 0016 15:59
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class ProxyTest {
    public static void main(String[] args) {
        /*=========静态代理=========*/
        EnglishTranslatorStaticProxy englishTranslatorStaticProxy =
                new EnglishTranslatorStaticProxy(new EnglishTranslator());
        englishTranslatorStaticProxy.translateArticle("《我有一个梦想》");
        englishTranslatorStaticProxy.translateWord("梦想");

        /*=========动态代理=========*/
        ITranslator englishTranslatorDynamicProxy =
                (ITranslator) new EnglishTranslatorDynamicProxy(new EnglishTranslator()).bind();
        englishTranslatorDynamicProxy.translateArticle("《我有一个梦想》");
        englishTranslatorDynamicProxy.translateWord("梦想");

        IImageTranslator englishImageTranslatorDynamicProxy =
                (IImageTranslator) new EnglishTranslatorDynamicProxy(new EnglishImageTranslator()).bind();
        englishImageTranslatorDynamicProxy.translateImage(new byte[]{});
        englishImageTranslatorDynamicProxy.toString();




//        ITranslator frenchTranslatorDynamicProxy =
//                (ITranslator) new EnglishTranslatorDynamicProxy(new FrenchTranslator()).bind();
//        frenchTranslatorDynamicProxy.translateArticle("《基督山伯爵》");
//        frenchTranslatorDynamicProxy.translateWord("希望");
    }
}
