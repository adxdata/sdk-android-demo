package com.meishu.sdkdemo.adid;

/**
 * 功能描述
 *
 * @author Lalala
 * @date 2020-09-17 20:41
 */
public class IqiyiIdProvider implements IAdIdProvider {
    @Override
    public String rewardPortrait() {
        //激励视频-竖版
        return "";
    }

    @Override
    public String rewardLandscape() {
        //激励视频-横版
        return "";
    }

    @Override
    public String feedVideo() {
        //未调用
        return "";
    }

    @Override
    public String feedThreeImgs() {
        //信息流-三图
        return "";
    }

    @Override
    public String feedImageVertical() {
        //信息流-大图 上文下图
        return "";
    }

    @Override
    public String feedImageHorizon() {
        //信息流-小图 左图右文
        return "";
    }

    @Override
    public String feedImageHorizonDesc() {
        //未调用
        return "";
    }

    @Override
    public String feedPreRender() {
        //信息流-模板渲染
        return "73646b0399080991";
    }

    @Override
    public String video() {
        //未调用
        return "";
    }

    @Override
    public String paster() {
        //贴片
        return "73646b0699080991";  //视频
    }

    @Override
    public String image() {
        //未调用 NativeAdSelectActivity -> ImageNotInRecyclerActivity
        return "";
    }

    @Override
    public String insertScreen() {
        //插屏
        return "";
    }

    @Override
    public String splash() {
        //开屏
        return "73646b0499080991";
    }

    @Override
    public String banner() {
        //BANNER
        return "73646b0399080991";
    }

    @Override
    public String videoFeed() {
        return "";
    }

    @Override
    public String fullScreenVideo() {
        return "";
    }

    @Override
    public String platformName() {
        return IdProviderFactory.PLATFORM_IQIYI;
    }
}
