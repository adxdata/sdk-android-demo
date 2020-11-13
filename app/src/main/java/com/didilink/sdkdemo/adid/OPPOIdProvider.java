package com.didilink.sdkdemo.adid;

class OPPOIdProvider implements IAdIdProvider {

    @Override
    public String rewardPortrait() {
        return "73646b0799061991";
    }

    @Override
    public String rewardLandscape() {
        return "73646b0799061991";
    }

    @Override
    public String feedVideo() {
        return "73646b0102061991";
    }

    @Override
    public String feedThreeImgs() {
        return "73646b0104061991";
    }

    @Override
    public String feedImageVertical() {
        return "73646b0101061991";
    }

    @Override
    public String feedImageHorizon() {
        return "73646b0103061991";
    }

    @Override
    public String feedImageHorizonDesc() {
        return "73646b0103061991";
    }

    @Override
    public String feedPreRender() {
        return "73646b0203061991";
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
        return "73646b0599061991";
    }

    @Override
    public String splash() {
        return "73646b0499061991";
    }

    @Override
    public String banner() {
        return "73646b0399061991";
    }

    @Override
    public String videoFeed() {
        return "73646b0102061991";
    }

    @Override
    public String fullScreenVideo() {
        return "73646b0999061991";
    }

    @Override
    public String platformName() {
        return IdProviderFactory.PLATFORM_OPPO;
    }
}
