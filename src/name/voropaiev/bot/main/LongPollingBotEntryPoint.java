package name.voropaiev.bot.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.PhotoSize;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
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
import name.voropaiev.bot.strategy.service.impl.ChatPhotoChangedMessageProcess;
import name.voropaiev.bot.strategy.service.impl.ChatTitleChangedMessageProcess;
import name.voropaiev.bot.strategy.service.impl.CommonMessageProcess;
import name.voropaiev.bot.strategy.service.impl.EditedMessageProcess;
import name.voropaiev.bot.strategy.service.impl.ForwardedInsideMessageProcess;
import name.voropaiev.bot.strategy.service.impl.UserJoinedChatMessageProcess;
import name.voropaiev.bot.strategy.service.impl.UserLeftChatMessageProcess;

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
		Message editedMessage = update.getEditedMessage();
		
		if (message != null && message.getText() != null) {
			String inputTextExtractedCommand = inputTextExtractCommand(message.getText());
			
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
				
				CommonMessageProcess messageProcess = new CommonMessageProcess();
				messageProcess.setKeywordList(keywordList);
				messageProcess.setMessage(message);
				messageProcess.process();
				
			}
		}
		
		if (message != null && message.getForwardDate() != null) {
			ForwardedInsideMessageProcess forwardedInsideMessageProcess = new ForwardedInsideMessageProcess(3);
			forwardedInsideMessageProcess.setForwardedInsideMessage(message);
			forwardedInsideMessageProcess.process();
		}
		
		if (editedMessage != null) {
			EditedMessageProcess editedMessageProcess = new EditedMessageProcess(2);
			editedMessageProcess.setEditedMessage(editedMessage);
			editedMessageProcess.process();
		}
		
		if (message != null && message.getLeftChatMember() != null) {
			UserLeftChatMessageProcess userLeftChatMessageProcess = new UserLeftChatMessageProcess(0);
			userLeftChatMessageProcess.setUserLeftChatMessage(message);
			userLeftChatMessageProcess.process();
		}
		
		if (message != null && message.getNewChatMember() != null) {
			UserJoinedChatMessageProcess userJoinedChatMessageProcess = new UserJoinedChatMessageProcess(1);
			userJoinedChatMessageProcess.setUserJoinedChatMessage(message);
			userJoinedChatMessageProcess.process();
		}
		
		if (message != null && message.getNewChatTitle() != null) {
			ChatTitleChangedMessageProcess chatTitleChangedMessageProcess = new ChatTitleChangedMessageProcess(6);
			chatTitleChangedMessageProcess.setChatTitleChangedMessage(message);
			chatTitleChangedMessageProcess.process();
		}
		
		if (message != null && message.getNewChatPhoto() != null) {
			ChatPhotoChangedMessageProcess chatPhotoChangedMessageProcess = new ChatPhotoChangedMessageProcess(5);
			chatPhotoChangedMessageProcess.setChatPhotoChangedMessage(message);
			chatPhotoChangedMessageProcess.process();
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