package name.voropaiev.bot.main;

import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import name.voropaiev.bot.enums.CommandEnum;
import name.voropaiev.bot.enums.EventPhrasesEnum;
import name.voropaiev.bot.strategy.IInputCommandStrategy;
import name.voropaiev.bot.strategy.impl.AddCommandStrategy;
import name.voropaiev.bot.strategy.impl.ListCommandStrategy;
import name.voropaiev.bot.strategy.impl.PingCommandStrategy;
import name.voropaiev.bot.strategy.impl.RandomCommandStrategy;
import name.voropaiev.bot.strategy.impl.StartCommandStrategy;
import name.voropaiev.bot.strategy.impl.StopCommandStrategy;
import name.voropaiev.bot.strategy.service.IMessageProcess;
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

	public static void main(String[] args) {
		ApiContextInitializer.init();
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
		try {
			System.out.println(BOT_START_MESSAGE);
			telegramBotsApi.registerBot(new LongPollingBotEntryPoint());

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
			commandMap.put(CommandEnum.ADD.toString(), new AddCommandStrategy(message));
			commandMap.put(CommandEnum.LIST.toString(), new ListCommandStrategy(message));
			commandMap.put(CommandEnum.PING.toString(), new PingCommandStrategy(message));
			commandMap.put(CommandEnum.RANDOM.toString(), new RandomCommandStrategy(message));
			commandMap.put(CommandEnum.START.toString(), new StartCommandStrategy(message));
			commandMap.put(CommandEnum.STOP.toString(), new StopCommandStrategy(message));
			
			if (!CommandEnum.NOT_A_COMMAND.toString()
					.equals(inputTextExtractedCommand)) {
				commandMap.get(inputTextExtractedCommand).execute();
			}
			
			if (message.hasText() && isBotIsActive() == true) {
				
				IMessageProcess messageProcess = new CommonMessageProcess(message);
				messageProcess.process();
				
			}
		}
		
		if (message != null && message.getLeftChatMember() != null) {
			IMessageProcess userLeftChatMessageProcess = 
					new UserLeftChatMessageProcess(message, EventPhrasesEnum.USER_LEFT_CHAT.getCode());
			userLeftChatMessageProcess.process();
		}
			
		if (message != null && message.getNewChatMember() != null) {
			IMessageProcess userJoinedChatMessageProcess = 
					new UserJoinedChatMessageProcess(message, EventPhrasesEnum.USER_JOINED_CHAT.getCode());
			userJoinedChatMessageProcess.process();
		}

		if (editedMessage != null) {
			IMessageProcess editedMessageProcess = 
					new EditedMessageProcess(editedMessage, EventPhrasesEnum.MESSAGE_EDITED.getCode());
			editedMessageProcess.process();
		}
		
		if (message != null && message.getForwardDate() != null) {
			IMessageProcess forwardedInsideMessageProcess = 
					new ForwardedInsideMessageProcess(message, EventPhrasesEnum.MESSAGE_FORWARDED_INSIDE.getCode());
			forwardedInsideMessageProcess.process();
		}
		
		if (message != null && message.getNewChatPhoto() != null) {
			IMessageProcess chatPhotoChangedMessageProcess = 
					new ChatPhotoChangedMessageProcess(message, EventPhrasesEnum.CHAT_LOGO_CHANGED.getCode());
			chatPhotoChangedMessageProcess.process();
		}
		
		if (message != null && message.getNewChatTitle() != null) {
			IMessageProcess chatTitleChangedMessageProcess = 
					new ChatTitleChangedMessageProcess(message, EventPhrasesEnum.CHAT_TITLE_CHANGED.getCode());
			chatTitleChangedMessageProcess.process();
		}
	}

	private String inputTextExtractCommand(String inputString) {
		for (CommandEnum command : CommandEnum.values()) {
			if (inputString.contains(command.toString())) {
				return command.toString();
			}
		}
		return CommandEnum.NOT_A_COMMAND.isNotCommand();
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