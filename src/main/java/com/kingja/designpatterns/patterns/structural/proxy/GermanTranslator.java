package com.kingja.designpatterns.patterns.structural.proxy;

/**
 * Description:TODO
 * Create Time:2020/12/16 0016 16:55
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class GermanTranslator implements ITranslator {
    @Override
    public boolean translateArticle(String article) {
        System.out.println("【德语翻译引擎】 翻译文章：" + article);
        return true;
    }

    @Override
    public boolean translateWord(String word) {
        System.out.println("【德语翻译引擎】 翻译单词：" + word);
        return false;
    }
}
