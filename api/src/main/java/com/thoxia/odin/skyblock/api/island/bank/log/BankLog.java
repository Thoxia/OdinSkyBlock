package com.thoxia.odin.skyblock.api.island.bank.log;

import com.thoxia.odin.skyblock.api.player.SPlayer;

import java.math.BigDecimal;

public record BankLog(SPlayer player, BankAction action, BigDecimal amount, long timestamp) {

}
