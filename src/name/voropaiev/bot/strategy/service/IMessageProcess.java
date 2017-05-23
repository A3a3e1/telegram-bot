package name.voropaiev.bot.strategy.service;

import org.telegram.telegrambots.api.objects.Message;

public interface IMessageProcess {
	
	public void process(Message message, int eventType);

}
