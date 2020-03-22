package com.meishu.sdkdemo.adid;

public class MSIdProvider implements IAdIdProvider {
    @Override
    public String rewardPortrait() {
        //激励视频-竖版
        return "73646b0799001991";
    }

    @Override
    public String rewardLandscape() {
        //激励视频-横版
        return "73646b0799001991";
    }

    @Override
    public String feedVideo() {
        //未调用
        return "100424126";
    }

    @Override
    public String feedThreeImgs() {
        //信息流-三图
        return "73646b0104001991";
    }

    @Override
    public String feedImageVertical() {
        //信息流-大图 上文下图
        return "73646b0101001991";
    }

    @Override
    public String feedImageHorizon() {
        //信息流-小图 左图右文
        return "73646b0103001991";
    }

    @Override
    public String feedImageHorizonDesc() {
        //未调用
        return "100424125";
    }

    @Override
    public String feedPreRender() {
        //信息流-模板渲染
        return "73646b0299001991";
    }

    @Override
    public String video() {
        //未调用
        return "100424122";
    }

    @Override
    public String videoImg() {
        //贴片
        //return "73646b0699001991";  //素材随机
        return "73646b0602001991";  //视频
    }

    @Override
    public String image() {
        //未调用 NativeAdSelectActivity -> ImageNotInRecyclerActivity
        return "100424120";
    }

    @Override
    public String insertScreen() {
        //插屏
        return "73646b0599001991";
    }

    @Override
    public String splash() {
        //开屏
        return "73646b0499001991";
    }

    @Override
    public String banner() {
        //BANNER
        return "73646b0399001991";
    }

    @Override
    public String videoFeed() {
        //DRAW信息流
        return "";
    }

    @Override
    public String platformName() {
        return "meishu";
    }
}
