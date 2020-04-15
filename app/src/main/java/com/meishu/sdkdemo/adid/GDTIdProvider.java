package com.meishu.sdkdemo.adid;

public class GDTIdProvider implements IAdIdProvider {
    @Override
    public String rewardPortrait() {
        //激励视频-竖版
        return "73646b0799021991";
    }

    @Override
    public String rewardLandscape() {
        //激励视频-横版
        return "73646b0799021991";
    }

    @Override
    public String feedVideo() {
        //未调用
        return "73646b0102021991";
    }

    @Override
    public String feedThreeImgs() {
        //信息流-三图
        return "73646b0104021991";
    }

    @Override
    public String feedImageVertical() {
        //信息流-大图 上文下图
        return "73646b0101021991";
    }

    @Override
    public String feedImageHorizon() {
        //信息流-小图 左图右文
        return "73646b0103021991";
    }

    @Override
    public String feedImageHorizonDesc() {
        //未调用
        return "100424169";
    }

    @Override
    public String feedPreRender() {
        //信息流-模板渲染
        return "73646b0299021991";
    }

    @Override
    public String video() {
        //未调用
        return "100424166";
    }

    @Override
    public String paster() {
        //贴片
        //return "73646b0699021991";  //素材随机
        return "73646b0602021991";  //视频
    }

    @Override
    public String image() {
        //未调用 NativeAdSelectActivity -> ImageNotInRecyclerActivity
        return "100424120";
    }

    @Override
    public String insertScreen() {
        //插屏
        return "73646b0599021991";
    }

    @Override
    public String splash() {
        //开屏
        return "73646b0499021991";
    }

    @Override
    public String banner() {
        //BANNER
        return "73646b0399021991";
    }

    @Override
    public String videoFeed() {
        return "";
    }

    @Override
    public String platformName() {
        return "gdt";
    }
}
