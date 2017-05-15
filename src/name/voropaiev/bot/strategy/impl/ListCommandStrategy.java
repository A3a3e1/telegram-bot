package name.voropaiev.bot.strategy.impl;

import java.util.List;

import org.telegram.telegrambots.api.objects.Message;

import name.voropaiev.bot.LongPollingBotEntryPoint;
import name.voropaiev.bot.jpa.entities.Phrase;
import name.voropaiev.bot.jpa.service.PhraseService;
import name.voropaiev.bot.strategy.IInputCommandStrategy;

public class ListCommandStrategy implements IInputCommandStrategy {

	private Message message;

	public ListCommandStrategy(Message message) {
		this.message = message;
	}

	@Override
	public void execute() {

		if (LongPollingBotEntryPoint.isBotIsActive() == true) {

			String msg = message.getText().substring("/list ".length());
			List<Phrase> list = PhraseService.getPhraseByNickname(msg);
			for (Phrase phrase : list) {
				LongPollingBotEntryPoint.sendMsg(message, phrase.getPhrase());
			}
		}
	}

}
