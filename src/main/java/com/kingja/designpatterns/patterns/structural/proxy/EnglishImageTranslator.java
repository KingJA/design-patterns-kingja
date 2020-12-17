package com.kingja.designpatterns.patterns.structural.proxy;

/**
 * Description:TODO
 * Create Time:2020/12/16 0016 22:49
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class EnglishImageTranslator implements IImageTranslator{
    @Override
    public boolean translateImage(byte[] res) {
        System.out.println("【英语图文翻译引擎】 翻译图文");
        return true;
    }
}
