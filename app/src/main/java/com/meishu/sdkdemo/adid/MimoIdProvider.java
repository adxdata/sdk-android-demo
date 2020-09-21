package com.meishu.sdkdemo.adid;

class MimoIdProvider implements IAdIdProvider {

    @Override
    public String rewardPortrait() {
        return "73646b0799071991";
    }

    @Override
    public String rewardLandscape() {
        return "73646b0799071991";
    }

    @Override
    public String feedVideo() {
        return "73646b0102071991";
    }

    @Override
    public String feedThreeImgs() {
        return "73646b0104071991";
    }

    @Override
    public String feedImageVertical() {
        return "73646b0101071991";
    }

    @Override
    public String feedImageHorizon() {
        return "73646b0103071991";
    }

    @Override
    public String feedImageHorizonDesc() {
        return "73646b0103071991";
    }

    @Override
    public String feedPreRender() {
        return "73646b0203071991";
    }

    @Override
    public String video() {
        return null;
    }

    @Override
    public String paster() {
        return null;
    }

    @Override
    public String image() {
        return null;
    }

    @Override
    public String insertScreen() {
        return "73646b0599071991";
    }

    @Override
    public String splash() {
        return "73646b0499071991";
    }

    @Override
    public String banner() {
        return "73646b0399071991";
    }

    @Override
    public String videoFeed() {
        return "73646b0102071991";
    }

    @Override
    public String fullScreenVideo() {
        return "73646b0999071991";
    }

    @Override
    public String platformName() {
        return IdProviderFactory.PLATFORM_MIMO;
    }
}
