package com.kingja.designpatterns.patterns.structural.proxy;

/**
 * Description:TODO
 * Create Time:2020/12/16 0016 16:54
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public interface IImageTranslator {
    /**
     *
     * @param res 图文资源
     * @return boolean 是否翻译成功
     */
    public boolean translateImage(byte[] res);
}
