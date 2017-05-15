package name.voropaiev.bot.strategy.impl;

import org.telegram.telegrambots.api.objects.Message;

import name.voropaiev.bot.LongPollingBotEntryPoint;
import name.voropaiev.bot.strategy.IInputCommandStrategy;

public class PingCommandStrategy implements IInputCommandStrategy {

	public static final String PING_PONG_ANSWER_TEXT = "Понг епта";

	private Message message;

	public PingCommandStrategy(Message message) {
		this.message = message;
	}

	@Override
	public void execute() {

		if (LongPollingBotEntryPoint.isBotIsActive() == true) {
			LongPollingBotEntryPoint.sendMsgWithReply(message, PING_PONG_ANSWER_TEXT);
		}
	}

}
