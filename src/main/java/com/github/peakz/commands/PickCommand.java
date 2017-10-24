package com.github.peakz.commands;

import com.darichey.discord.CommandContext;
import com.github.peakz.queues.QueueManager;
import com.github.peakz.queues.QueuePug;

public class PickCommand {
	private CommandContext ctx;
	private QueueManager queueManager;

	public PickCommand(CommandContext ctx, QueueManager queueManager) {
		this.ctx = ctx;
		this.queueManager = queueManager;
	}

	public void pickPlayer(String number) {
		QueuePug qpug = queueManager.getQueuePug("RANKS");
		qpug.setCtx(ctx);
		int n = qpug.getRanksPool();
		// If it's red's turn
		// else if it's blue's turn
		if(qpug.getRanksPool() == 1) {

		} else {
			if (n % 2 == 1) qpug.pickPlayer("RED", number);
			else qpug.pickPlayer("BLUE", number);
		}
	}
}
