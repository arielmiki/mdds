package com.scmanis.mdds.Telegram;

import com.scmanis.mdds.Model.DataWrapper;
import com.scmanis.mdds.Redis.JedisFactory;
import com.scmanis.mdds.agent.MDDSAgent;
import com.scmanis.mdds.agent.MDDSAgentMemento;
import com.scmanis.mdds.agent.Symptom;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DrPhiBot extends TelegramLongPollingBot {
    Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
    MDDSAgent agent = MDDSAgent.getInstance();

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        String response = "";
        if ( update.hasCallbackQuery() || (update.hasMessage() && update.getMessage().hasText())) {
            String chatId = "";
            String text = "";
            String param = "";
            String username = "";
            if (update.hasCallbackQuery()) {
                chatId = update.getCallbackQuery().getMessage().getChatId().toString();
                text = update.getCallbackQuery().getData();
                username = update.getCallbackQuery().getFrom().getUserName();
            } else {
                chatId = update.getMessage().getChatId().toString();
                String[] temp = update.getMessage().getText().split(" ");
                text = temp[0];
                username = update.getMessage().getFrom().getUserName();
                if (temp.length > 1){
                    param = temp[1];
                }

            }
            MDDSAgentMemento data;
            try {
                data = (MDDSAgentMemento) MDDSAgentMemento.fromString(jedis.get(chatId));
            } catch (Exception e) {
                data = null;
            }
            if (data == null && text.equalsIgnoreCase("/help")) {
                response = "Gunakan:\n/start untuk memulai diagnosis\n/start [NAMA PENYAKIT] untuk menguji terhadap penyakit tertentu\n/help untuk menampilkan bantuan\n/stop untuk berhenti\nPenyakit yang di support: Paranoid, Schizoid, Schizotypal, Antisocial, Borderline, Histrionic, Narcissistic, Avoidant, Dependent, OCD";
            } else if (text.equalsIgnoreCase("/start")) {
                if (param.equalsIgnoreCase("")) {
                    jedis.set(chatId, jedis.get("admin"));
                } else {
                    if (jedis.exists(param.toLowerCase())) {
                        response = "Anda hanya akan diuji terhadap penyakit " + param.toUpperCase() + ".\n\n";
                        jedis.set(chatId, jedis.get(param.toLowerCase()));
                    } else {
                        response = "Penyakit " + param.toUpperCase() + " belum terdaftar:(\nOleh karena itu agent akan menggunakan mode pengetesan ke SEMUA penyakit :)\n\n";
                        jedis.set(chatId, jedis.get("admin"));
                    }
                }

                response += "Jawab dengan benar atau salah.\n";
                try {
                    data = (MDDSAgentMemento) MDDSAgentMemento.fromString(jedis.get(chatId));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (data == null) {
                response = "Gunakan:\n/start untuk memulai diagnosis\n/start [NAMA PENYAKIT] untuk menguji terhadap penyakit tertentu\n/help untuk menampilkan bantuan\n/stop untuk berhenti\nPenyakit yang di support: Paranoid, Schizoid, Schizotypal, Antisocial, Borderline, Histrionic, Narcissistic, Avoidant, Dependent, OCD";
            }
            if (data != null && text.equalsIgnoreCase("/start")) {
                agent.restore(data);;
                Symptom temp = ((Symptom) agent.getAskedLiteral());
                response += temp.getQuestion();
                try {
                    jedis.set(chatId, MDDSAgentMemento.toString(agent.save()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (data != null && text.equalsIgnoreCase("/stop")) {
                jedis.del(chatId);
                response = "Thank you for your time :)";
            } else if (data != null) {
                if (text.equalsIgnoreCase("benar")) {
                    agent.restore(data);
                    String answer = agent.answerLiteral(agent.getCurrentLiteral(), true);
                    if (!answer.equalsIgnoreCase("sehat")) {
                        jedis.set("USER-" + username, answer);
                        response = "Anda memiliki kecenderungan untuk memiliki penyakit mental:\n" + answer;
                        jedis.del(chatId);
                    } else {
                        if (agent.getAskedLiteral() == null) {
                            response = "Selamat anda tidak mengidap penyakit mental";
                            jedis.del(chatId);
                        } else {
                            response = ((Symptom) agent.getCurrentLiteral()).getQuestion();
                            try {
                                jedis.set(chatId, MDDSAgentMemento.toString(agent.save()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                } else if (text.equalsIgnoreCase("salah")) {
                    agent.restore(data);
                    String answer = agent.answerLiteral(agent.getCurrentLiteral(), false);

                    if (agent.getAskedLiteral() == null) {
                        response = "Selamat anda tidak mengidap penyakit mental";
                        jedis.del(chatId);
                    } else {
                        response = ((Symptom) agent.getCurrentLiteral()).getQuestion();
                        try {
                            jedis.set(chatId, MDDSAgentMemento.toString(agent.save()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } else {
                    response = "Anda hanya diperkenankan menjawab dengan benar atau salah";
                }

            }

            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(chatId)
                    .setText(response);

            if (jedis.exists(chatId)) {
                InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                List<InlineKeyboardButton> buttons = new ArrayList<>();
                KeyboardButton keyboardButton = new KeyboardButton();
                buttons.add(new InlineKeyboardButton().setText("Benar").setCallbackData("benar"));
                buttons.add(new InlineKeyboardButton().setText("Salah").setCallbackData("salah"));
                List<List<InlineKeyboardButton>> buttonInLines = new ArrayList<>();
                buttonInLines.add(buttons);
                keyboardMarkup.setKeyboard(buttonInLines);
                message.setReplyMarkup(keyboardMarkup);
            }

            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return System.getenv("BOT_USERNAME");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }
}
