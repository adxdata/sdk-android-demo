package com.meishu.sdkdemo.adid;

public class CSJIdProvider implements IAdIdProvider {
    @Override
    public String rewardPortrait() {
        //激励视频-竖版
        return "73646b0799011991";
    }

    @Override
    public String rewardLandscape() {
        //激励视频-横版
        return "73646b0799011991";
    }

    @Override
    public String feedVideo() {
        //未调用
        return "73646b0102011991";
    }

    @Override
    public String feedThreeImgs() {
        //信息流-三图
        return "73646b0104011991";
    }

    @Override
    public String feedImageVertical() {
        //信息流-大图 上文下图
        return "73646b0101011991";
    }

    @Override
    public String feedImageHorizon() {
        //信息流-小图 左图右文
        return "73646b0103011991";
    }

    @Override
    public String feedImageHorizonDesc() {
        //未调用
        return "100424154";
    }

    @Override
    public String feedPreRender() {
        //信息流-模板渲染
        return "73646b0299011991";
    }

    @Override
    public String video() {
        //未调用
        return "100424151";
    }

    @Override
    public String paster() {
        //贴片
        //return "73646b0699011991";  //素材随机
        return "73646b0602011991";  //视频
    }

    @Override
    public String image() {
        //未调用 NativeAdSelectActivity -> ImageNotInRecyclerActivity
        return "100424120";
    }

    @Override
    public String insertScreen() {
        //插屏
        return "73646b0599011991";
    }

    @Override
    public String splash() {
        //开屏
        return "73646b0499011991";
    }

    @Override
    public String banner() {
        //BANNER
        return "73646b0399011991";
    }

    @Override
    public String videoFeed() {
        //DRAW信息流
        return "73646b0899011991";
    }

    @Override
    public String platformName() {
        return "csj";
    }
}
