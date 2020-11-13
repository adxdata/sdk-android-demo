package com.didilink.sdkdemo.adid;

public interface IAdIdProvider {
    /** 竖版激励视频 */
    String rewardPortrait();
    /** 横版激励视频 */
    String rewardLandscape();
    /** 信息流视频 */
    String feedVideo();
    /** 信息流三图一文 */
    String feedThreeImgs();
    /** 信息流上图下文 */
    String feedImageVertical();
    /** 信息流左图右文 */
    String feedImageHorizon();
    /** 信息流左图右文图文摘要 */
    String feedImageHorizonDesc();
    /** 信息流预渲染 */
    String feedPreRender();
    /** 纯视频 */
    String video();
    /** 视频暂停贴片 */
    String paster();
    /** 图片 */
    String image();
    /** 插屏 */
    String insertScreen();
    /** 开屏 */
    String splash();
    /** banner */
    String banner();
    /** videoFeed (穿山甲 draw 信息流) */
    String videoFeed();
    /** 全屏视频 */
    String fullScreenVideo();
    String platformName();
}
