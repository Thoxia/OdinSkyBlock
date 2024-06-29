package com.thoxia.odin.skyblock.api.island.bank;

import com.thoxia.odin.skyblock.api.island.Island;
import com.thoxia.odin.skyblock.api.island.bank.log.BankLog;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

@RequiredArgsConstructor
@Getter
@Setter
public class IslandBank {

    private final Island island;
    private final Deque<BankLog> logs = new ArrayDeque<>();
    private BigDecimal balance = BigDecimal.ZERO;

    public void addLog(BankLog log) {
        if (logs.size() >= 5)
            logs.removeLast();

        logs.addFirst(log);
    }

}
