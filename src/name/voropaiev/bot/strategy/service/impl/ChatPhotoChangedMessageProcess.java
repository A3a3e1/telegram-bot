package name.voropaiev.bot.strategy.service.impl;

import java.util.List;
import java.util.Random;

import org.telegram.telegrambots.api.objects.Message;

import lombok.Data;
import name.voropaiev.bot.jpa.service.EventPhraseService;
import name.voropaiev.bot.main.LongPollingBotEntryPoint;
import name.voropaiev.bot.strategy.service.IMessageProcess;

@Data
public class ChatPhotoChangedMessageProcess implements IMessageProcess {

	List<String> phrases;
	private Message chatPhotoChangedMessage;

	public ChatPhotoChangedMessageProcess(Message message, int eventType) {
		this.chatPhotoChangedMessage = message;
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

			LongPollingBotEntryPoint.sendMsg(chatPhotoChangedMessage, phrases.get(result));
		}
		return;

	}

}
