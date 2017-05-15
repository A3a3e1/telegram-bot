package name.voropaiev.bot.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import name.voropaiev.bot.command.listenum.Command;
import name.voropaiev.bot.jpa.service.PhraseService;
import name.voropaiev.bot.strategy.IInputCommandStrategy;
import name.voropaiev.bot.strategy.impl.AddCommandStrategy;
import name.voropaiev.bot.strategy.impl.ListCommandStrategy;
import name.voropaiev.bot.strategy.impl.PingCommandStrategy;
import name.voropaiev.bot.strategy.impl.RandomCommandStrategy;
import name.voropaiev.bot.strategy.impl.StartCommandStrategy;
import name.voropaiev.bot.strategy.impl.StopCommandStrategy;
import name.voropaiev.bot.strategy.service.MessageProcess;

public class LongPollingBotEntryPoint extends TelegramLongPollingBot {

	public static final String BOT_NAME = "rachok_bot";
	public static final String BOT_TOKEN = "332133890:AAFRAymIQzDHSMWXDYXUYlz3R5hXVWQKtr0";
	
	public static final String BOT_START_MESSAGE = "Bot started.....";

	private static boolean botIsActive = true;
	private static List<String> keywordList;

	public static void main(String[] args) {
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			System.out.println(BOT_START_MESSAGE);
			telegramBotsApi.registerBot(new LongPollingBotEntryPoint());

			keywordList = PhraseService.getKeywordList();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpdateReceived(Update update) {
		
		// for debug purposes:
		System.out.println(update.toString());
		
		Message message = update.getMessage();
		String inputTextExtractedCommand = inputTextExtractCommand(message.getText());
		
		if (message != null) {
			
			//Design Pattern 'Command'
			Map<String, IInputCommandStrategy> commandMap = new HashMap<>();
			commandMap.put("/add", new AddCommandStrategy(message));
			commandMap.put("/list", new ListCommandStrategy(message));
			commandMap.put("/ping", new PingCommandStrategy(message));
			commandMap.put("/random", new RandomCommandStrategy(message));
			commandMap.put("/start", new StartCommandStrategy(message));
			commandMap.put("/stop", new StopCommandStrategy(message));
			
			if (!"-1".equals(inputTextExtractedCommand)) {
				commandMap.get(inputTextExtractedCommand).execute();
			}
			
			if (message.hasText() && isBotIsActive() == true) {
				
				MessageProcess messageProcess = new MessageProcess();
				messageProcess.setKeywordList(keywordList);
				messageProcess.setMessage(message);
				messageProcess.process();
				
			}
		}
	}
	
	private String inputTextExtractCommand(String inputString) {
		for (Command command : Command.values()) {
			if (inputString.contains(command.toString())) {
				return command.toString();
			}
		}
		return Command.NOT_A_COMMAND.isNotCommand();
	}

	public static void sendMsg(Message message, String text) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.enableMarkdown(true);

		sendMessage.setChatId(message.getChatId().toString());
		sendMessage.setText(text);
		sendMessage.enableHtml(true);
		try {
			new LongPollingBotEntryPoint().sendMessage(sendMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendMsgWithReply(Message message, String text) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.enableMarkdown(true);

		sendMessage.setChatId(message.getChatId().toString());
		sendMessage.setReplyToMessageId(message.getMessageId());
		sendMessage.setText(text);
		sendMessage.enableHtml(true);
		try {
			new LongPollingBotEntryPoint().sendMessage(sendMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getBotUsername() {
		return BOT_NAME;
	}

	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}
	
	public static boolean isBotIsActive() {
		return botIsActive;
	}

	public static void setBotIsActive(boolean botIsActive) {
		LongPollingBotEntryPoint.botIsActive = botIsActive;
	}

}