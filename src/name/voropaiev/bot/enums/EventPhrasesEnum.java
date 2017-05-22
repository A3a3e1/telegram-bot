package name.voropaiev.bot.enums;

public enum EventPhrasesEnum {
	
	USER_LEFT_CHAT(0),
	USER_JOINED_CHAT(1),
	MESSAGE_EDITED(2),
	MESSAGE_FORWARDED_INSIDE(3),
	MESSAGE_FORWARDED_OUTSIDE(4),
	CHAT_LOGO_CHANGED(5),
	CHAT_TITLE_CHANGED(6),
	
	EVENT_NOT_EXIST(-1);
	
	private final int code;
	
	private EventPhrasesEnum (final int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
}
