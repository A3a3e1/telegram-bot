package name.voropaiev.bot.strategy.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telegram.telegrambots.api.objects.Message;

import name.voropaiev.bot.main.LongPollingBotEntryPoint;
import name.voropaiev.bot.strategy.IInputCommandStrategy;

public class RandomCommandStrategy implements IInputCommandStrategy {

	private Message message;

	public RandomCommandStrategy(Message message) {
		this.message = message;
	}

	@Override
	public void execute() {

		if (LongPollingBotEntryPoint.isBotIsActive() == true) {

			List<Object> numbersArray = getIntArray(message.getText());

			Random r = new Random();
			int low = 1;
			int high = 100;
			
			if (numbersArray.size() == 0) {
				numbersArray.add("орел");
				numbersArray.add("решка");
				
				low = 0;
				high = 1;
				
				Random ran = new Random();
				int result = ran.nextInt(2);
				
				LongPollingBotEntryPoint.sendMsg(message, numbersArray.get(result).toString());
				return;
				
			} else if (numbersArray.size() == 2)  {

				low = (Integer) numbersArray.get(0);
				high = (Integer) numbersArray.get(1);
				
			}

			
			int result = r.nextInt(high - low) + low;

			LongPollingBotEntryPoint.sendMsg(message, Integer.toString(result));
		}
	}

	private List<Object> getIntArray(String requestString) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(requestString);
		List<Object> numbersArray = new LinkedList<>();

		while (matcher.find()) {
			numbersArray.add(Integer.parseInt(matcher.group()));
		}

		return numbersArray;
	}

}
