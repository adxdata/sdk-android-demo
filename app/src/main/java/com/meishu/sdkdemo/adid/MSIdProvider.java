package com.meishu.sdkdemo.adid;

public class MSIdProvider implements IAdIdProvider {
    @Override
    public String rewardPortrait() {
//        return "100424034";
        return "987654902"; // 曝光上报测试
    }

    @Override
    public String rewardLandscape() {
        return "1004468";
    }

    @Override
    public String feedVideo() {
        return "100424126";
    }

    @Override
    public String feedThreeImgs() {
            return "100424039";
    }

    @Override
    public String feedImageVertical() {
        return "100424034";
    }

    @Override
    public String feedImageHorizon() {
        return "100424123";
    }

    @Override
    public String feedImageHorizonDesc() {
        return "100424125";
    }

    @Override
    public String feedPreRender() {
        return "100424017";
    }

    @Override
    public String video() {
        return "100424122";
    }

    @Override
    public String paster() {
        return "100424034";
    }

    @Override
    public String image() {
        return "100424120";
    }

    @Override
    public String insertScreen() {
        return "100424020";
    }

    @Override
    public String splash() {
       return "100424024";
    }

    @Override
    public String banner() {
        return "100424117";
    }

    @Override
    public String videoFeed() {
        return "";
    }

    @Override
    public String platformName() {
        return "meishu";
    }
}
