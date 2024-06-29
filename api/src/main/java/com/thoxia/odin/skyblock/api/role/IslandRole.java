package com.thoxia.odin.skyblock.api.role;

import lombok.Getter;

@Getter
public enum IslandRole {

    BANNED(-999),
    VISITOR(0),
    COOP(100),
    MEMBER(200),
    ADMIN(500),
    OWNER(1000);

    private final int weight;

    IslandRole(int i) {
        this.weight = i;
    }

}
