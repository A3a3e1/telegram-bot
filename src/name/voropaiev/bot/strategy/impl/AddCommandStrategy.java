package name.voropaiev.bot.strategy.impl;

import org.telegram.telegrambots.api.objects.Message;

import name.voropaiev.bot.jpa.service.PhraseService;
import name.voropaiev.bot.main.LongPollingBotEntryPoint;
import name.voropaiev.bot.strategy.IInputCommandStrategy;

public class AddCommandStrategy implements IInputCommandStrategy {

	public static final String ADD_REQUEST_DELIMITER = "::";
	public static final String WRONG_REQUEST_ANSWER_TEXT = "Не понимаю тебя рачка";

	private Message message;

	public AddCommandStrategy(Message message) {
		this.message = message;
	}

	@Override
	public void execute() {

		if (LongPollingBotEntryPoint.isBotIsActive() == true) {
			try {

				if (message.getText().contains(ADD_REQUEST_DELIMITER)) {
					StringBuffer sb1 = new StringBuffer(message.getText().substring("/add ".length()));
					StringBuffer sb2 = new StringBuffer(message.getText().substring("/add ".length()));

					String nickName = sb1.substring(0, sb1.indexOf("::"));
					String phraseString = sb2.substring(sb2.indexOf("::") + 2);

					System.out.println("nickName:" + nickName);
					System.out.println("phraseString:" + phraseString);

					PhraseService.addPhrase(nickName, phraseString);

					LongPollingBotEntryPoint.sendMsg(message, "Добавил");

					return;
				}

				StringBuffer sb1 = new StringBuffer(message.getText().substring("/add ".length()));
				StringBuffer sb2 = new StringBuffer(message.getText().substring("/add ".length()));

				String nickName = sb1.substring(0, sb1.indexOf(" "));
				String phraseString = sb2.substring(sb2.indexOf(" ") + 1);

				System.out.println("nickName:" + nickName);
				System.out.println("phraseString:" + phraseString);

				PhraseService.addPhrase(nickName, phraseString);

				LongPollingBotEntryPoint.sendMsg(message, "Добавил");
			} catch (Exception e) {
				LongPollingBotEntryPoint.sendMsg(message, WRONG_REQUEST_ANSWER_TEXT);
			}
		}

	}

}
