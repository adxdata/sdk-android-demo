package com.meishu.sdkdemo.adid;

public class BDIdProvider implements IAdIdProvider {
    @Override
    public String rewardPortrait() {
        //激励视频-竖版
        return "73646b0799031991";
    }

    @Override
    public String rewardLandscape() {
        //激励视频-横版
        return "73646b0799031991";
    }

    @Override
    public String feedVideo() {
        //未调用
        return "73646b0102031991";
    }

    @Override
    public String feedThreeImgs() {
        //信息流-三图
        return "73646b0104031991";
    }

    @Override
    public String feedImageVertical() {
        //信息流-大图 上文下图
        return "73646b0101031991";
    }

    @Override
    public String feedImageHorizon() {
        //信息流-小图 左图右文
        return "73646b0103031991";
    }

    @Override
    public String feedImageHorizonDesc() {
        //未调用
        return "100424184";
    }

    @Override
    public String feedPreRender() {
        //信息流-模板渲染
        return "73646b0299031991";
    }

    @Override
    public String video() {
        //未调用
        return "100424181";
    }

    @Override
    public String paster() {
        //贴片
        //return "73646b0699031991";  //素材随机
        return "73646b0602031991";  //视频
    }

    @Override
    public String image() {
        //未调用 NativeAdSelectActivity -> ImageNotInRecyclerActivity
        return "100424179";
    }

    @Override
    public String insertScreen() {
        //插屏
        return "73646b0599031991";
    }

    @Override
    public String splash() {
        //开屏
        return "73646b0499031991";
    }

    @Override
    public String banner() {
        //BANNER
        return "73646b0399031991";
    }

    @Override
    public String videoFeed() {
        return "";
    }

    @Override
    public String platformName() {
        return IdProviderFactory.PLATFORM_BD;
    }
}
