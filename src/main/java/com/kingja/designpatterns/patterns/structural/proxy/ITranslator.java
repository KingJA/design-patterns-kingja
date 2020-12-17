package com.kingja.designpatterns.patterns.structural.proxy;

/**
 * Description:TODO
 * Create Time:2020/12/16 0016 16:54
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public interface ITranslator {
    /**
     *
     * @param article 需要翻译的文章
     * @return boolean 是否翻译成功
     */
    public boolean translateArticle(String article);

    /**
     *
     * @param word 需要翻译的单词
     * @return boolean 是否翻译成功
     */
    public boolean translateWord(String word);
}
