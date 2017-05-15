package name.voropaiev.bot.strategy.impl;

import org.telegram.telegrambots.api.objects.Message;

import name.voropaiev.bot.LongPollingBotEntryPoint;
import name.voropaiev.bot.strategy.IInputCommandStrategy;

public class StartCommandStrategy implements IInputCommandStrategy {

	public static final String START_ANSWER_TEXT = "Привет рачки";
	
	private Message message;
	
	public StartCommandStrategy(Message message) {
		this.message = message;
	}
	
	@Override
	public void execute() {
		
		LongPollingBotEntryPoint.sendMsgWithReply(message, START_ANSWER_TEXT);
		LongPollingBotEntryPoint.setBotIsActive(true);
		
	}

}
