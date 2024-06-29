package com.thoxia.odin.skyblock.api.upgrade;

public interface IUpgradeManager {

    void onEnable();

    Upgrade getUpgrade(Upgrade.Type type, int level);

}
