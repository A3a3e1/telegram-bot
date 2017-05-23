package name.voropaiev.bot.main;

import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import com.google.inject.Guice;
import com.google.inject.Injector;

import name.voropaiev.bot.enums.CommandEnum;
import name.voropaiev.bot.enums.EventPhrasesEnum;
import name.voropaiev.bot.guice.modules.CommonMessageProcessorModule;
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
	
	public IMessageProcess commonMessageProcessorInstance;
	public IMessageProcess editedMessageProcessorInstance;
	public IMessageProcess userLeftChatMessageProcessorInstance;
	public IMessageProcess userJoinedChatMessageProcessorInstance;
	public IMessageProcess forwardedInsideMessageProcessorInstance;
	public IMessageProcess chatTitleChangedMessageProcessorInstance;
	public IMessageProcess chatPhotoChangedMessageProcessorInstance;
	
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
		
		Injector injector = Guice.createInjector(new CommonMessageProcessorModule());
		commonMessageProcessorInstance = injector.getInstance(CommonMessageProcess.class);
		editedMessageProcessorInstance = injector.getInstance(EditedMessageProcess.class);
		userLeftChatMessageProcessorInstance = injector.getInstance(UserLeftChatMessageProcess.class);
		userJoinedChatMessageProcessorInstance = injector.getInstance(UserJoinedChatMessageProcess.class);
		forwardedInsideMessageProcessorInstance = injector.getInstance(ForwardedInsideMessageProcess.class);
		chatTitleChangedMessageProcessorInstance = injector.getInstance(ChatTitleChangedMessageProcess.class);
		chatPhotoChangedMessageProcessorInstance = injector.getInstance(ChatPhotoChangedMessageProcess.class);
		
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
				commonMessageProcessorInstance.process(message, EventPhrasesEnum.DUMMY_EVENT.getCode());
			}
		}
		
		if (message != null && message.getLeftChatMember() != null) {
			userLeftChatMessageProcessorInstance.process(message, EventPhrasesEnum.USER_LEFT_CHAT.getCode());
		}
			
		if (message != null && message.getNewChatMember() != null) {
			userJoinedChatMessageProcessorInstance.process(message, EventPhrasesEnum.USER_JOINED_CHAT.getCode());
		}

		if (editedMessage != null) {
			editedMessageProcessorInstance.process(editedMessage, EventPhrasesEnum.MESSAGE_EDITED.getCode());
		}
		
		if (message != null && message.getForwardDate() != null) {
			forwardedInsideMessageProcessorInstance.process(message, EventPhrasesEnum.MESSAGE_FORWARDED_INSIDE.getCode());
		}
		
		if (message != null && message.getNewChatPhoto() != null) {
			chatPhotoChangedMessageProcessorInstance.process(message, EventPhrasesEnum.CHAT_LOGO_CHANGED.getCode());
		}
		
		if (message != null && message.getNewChatTitle() != null) {
			chatTitleChangedMessageProcessorInstance.process(message, EventPhrasesEnum.CHAT_TITLE_CHANGED.getCode());
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