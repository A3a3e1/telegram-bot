package name.voropaiev.bot.guice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import name.voropaiev.bot.strategy.service.IMessageProcess;
import name.voropaiev.bot.strategy.service.impl.CommonMessageProcess;

public class CommonMessageProcessorModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(IMessageProcess.class)
		.to(CommonMessageProcess.class)
		.in(Scopes.SINGLETON);

	}

}
