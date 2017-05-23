package name.voropaiev.bot.strategy.service.impl;

import java.util.List;
import java.util.Random;

import org.telegram.telegrambots.api.objects.Message;

import lombok.Data;
import name.voropaiev.bot.jpa.service.EventPhraseService;
import name.voropaiev.bot.main.LongPollingBotEntryPoint;
import name.voropaiev.bot.strategy.service.IMessageProcess;

@Data
public class ChatTitleChangedMessageProcess implements IMessageProcess {

	List<String> phrases;

	public ChatTitleChangedMessageProcess() {

	}

	public void process(Message chatTitleChangedMessage,int eventType) {
		this.phrases = EventPhraseService.getPhraseByEvent(eventType);

		if (phrases.size() > 0) {
			Random random = new Random();
			int low = 0;
			int high = phrases.size();
			int result = random.nextInt(high - low) + low;

			LongPollingBotEntryPoint.sendMsg(chatTitleChangedMessage, phrases.get(result));
		}
		return;

	}

}
