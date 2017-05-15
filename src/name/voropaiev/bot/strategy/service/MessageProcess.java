package name.voropaiev.bot.strategy.service;

import java.util.List;
import java.util.Random;

import org.telegram.telegrambots.api.objects.Message;

import name.voropaiev.bot.LongPollingBotEntryPoint;
import name.voropaiev.bot.jpa.entities.Phrase;
import name.voropaiev.bot.jpa.service.PhraseService;

public class MessageProcess {
	
	private Message message;
	private List<String> keywordList;

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public List<String> getKeywordList() {
		return keywordList;
	}

	public void setKeywordList(List<String> keywordList) {
		this.keywordList = keywordList;
	}

	public void process() {
		// Generate random number. Bot will reply if weight>15 (random
		// from 1 to 20) - TODO tune?
		Random weightRandom = new Random();
		int first = 1;
		int second = 20;

		int weight = weightRandom.nextInt(second) + first;

		// System.out.println("weight:" + weight);

		if (message.getText().toLowerCase().contains("rachok_bot")) {
			weight = 14;
		}

		if ("-174469227".equals(Long.toString((message.getChatId())))
				|| "248533466".equals(Long.toString((message.getChatId())))) {
			weight = 14;
		}

		if (weight > 13) {
			for (String key : keywordList) {

				if (message.getText().toLowerCase().contains(key)) {

					List<Phrase> matchedKeywords = PhraseService.getPhraseByNickname(key);

					if (matchedKeywords.size() > 0) {
						Random r1 = new Random();
						int f1 = 0;
						int s1 = matchedKeywords.size();
						int w1 = r1.nextInt(s1) + f1;

						LongPollingBotEntryPoint.sendMsgWithReply(message, matchedKeywords.get(w1).getPhrase());
					}
					return;

				}
			}
		}

		else
			return;
	}

}
