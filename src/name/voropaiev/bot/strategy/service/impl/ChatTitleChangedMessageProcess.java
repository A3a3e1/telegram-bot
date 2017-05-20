package name.voropaiev.bot.strategy.service.impl;

import java.util.List;
import java.util.Random;

import org.telegram.telegrambots.api.objects.Message;

import lombok.Data;
import name.voropaiev.bot.jpa.entities.EventPhrase;
import name.voropaiev.bot.jpa.entities.Phrase;
import name.voropaiev.bot.jpa.service.EventPhraseService;
import name.voropaiev.bot.jpa.service.PhraseService;
import name.voropaiev.bot.main.LongPollingBotEntryPoint;
import name.voropaiev.bot.strategy.service.IMessageProcess;

@Data
public class ChatTitleChangedMessageProcess implements IMessageProcess {

	List<String> phrases;
	private Message chatTitleChangedMessage;

	public ChatTitleChangedMessageProcess(Message message,int eventType) {
		this.chatTitleChangedMessage = message;
		this.phrases = EventPhraseService.getPhraseByEvent(eventType);
		for (String s : phrases) {
			System.out.println(s);
		}
	}

	public void process() {

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
