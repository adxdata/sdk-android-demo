package com.meishu.sdkdemo.adid;

public class MSIdProvider implements IAdIdProvider {
    @Override
    public String rewardPortrait() {
//        return "1007377";
        return "300006";
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
//        return "1007377"; // 测试 logo
//        return "1007610"; // 正式 deeplink + 下载
//        return "1007382"; // 正式 知心天气下载
//        return "1007601"; // 正式 京东视频 横版
//        return "300009"; // 测试
        return "987654001"; // 测试 详情
//        return "100424017";
//        return "100424034";
//        return "100424123"; // 测试 新客户问题
    }

    @Override
    public String feedImageHorizon() {
        return "100424123";
//        return "100424034";
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
    public String videoImg() {
        return "100424034";
//        return "100424018";
    }

    @Override
    public String image() {
        return "100424120";
    }

    @Override
    public String insertScreen() {
//        return "100424020";
        return "1007377";
    }

    @Override
    public String splash() {
//        return "100424024";
//        return "1007377";
        return "100424162"; // 掌通家园
    }

    @Override
    public String banner() {
//        return "100424117";
        return "1007377";
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
