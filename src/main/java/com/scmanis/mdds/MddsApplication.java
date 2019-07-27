package com.scmanis.mdds;

import com.scmanis.mdds.Model.DataWrapper;
import com.scmanis.mdds.Redis.JedisFactory;
import com.scmanis.mdds.agent.MDDSAgent;
import com.scmanis.mdds.agent.MDDSAgentMemento;
import com.scmanis.mdds.agent.Sentence;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.scmanis.mdds.Telegram.DrPhiBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import redis.clients.jedis.Jedis;

@SpringBootApplication
public class MddsApplication {

	public static void main(String[] args) throws Exception {
		// Initialize API Context
		ApiContextInitializer.init();

		DrPhiBot drPhiBot = new DrPhiBot();
		System.out.println("Run bot with username " + drPhiBot.getBotUsername() +" and token " + drPhiBot.getBotToken());
		MDDSAgent agent = MDDSAgent.getInstance();
		MDDSAgentMemento data = agent.save();
		Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
		jedis.set("admin", MDDSAgentMemento.toString(data));
		for (String identifier: new String[] {
				"Paranoid",
				"Schizoid",
				"Schizotypal",
				"Antisocial",
				"Borderline",
				"Histrionic",
				"Narcissistic",
				"Avoidant",
				"Dependent",
				"OCD"
		}) {

			agent.choose(identifier);
			jedis.set(identifier.toLowerCase(), MDDSAgentMemento.toString(agent.save()));
			try {
				data = (MDDSAgentMemento) MDDSAgentMemento.fromString(jedis.get("admin"));
			} catch (Exception e) {
				e.printStackTrace();
			};
			agent.restore(data);
		}

		// Instantiate Telegram Bots API
		TelegramBotsApi botsApi = new TelegramBotsApi();

		// Register the bot
		try {
			botsApi.registerBot(drPhiBot);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
}