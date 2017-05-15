package name.voropaiev.bot.strategy.impl;

import org.telegram.telegrambots.api.objects.Message;

import name.voropaiev.bot.LongPollingBotEntryPoint;
import name.voropaiev.bot.strategy.IInputCommandStrategy;

public class StopCommandStrategy implements IInputCommandStrategy {

	public static final String STOP_ANSWER_TEXT = "Ебалось";
	
	private Message message;
	
	public StopCommandStrategy(Message message) {
		this.message = message;
	}
	
	@Override
	public void execute() {
		
		LongPollingBotEntryPoint.sendMsgWithReply(message, STOP_ANSWER_TEXT);
		LongPollingBotEntryPoint.setBotIsActive(false);
		
	}

}
