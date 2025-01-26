package com.chinese_checkers.server.Connection;

import com.chinese_checkers.comms.Message.FromClient.RequestJoinMessage;
import com.chinese_checkers.comms.Message.Message;
import com.chinese_checkers.server.Game.Bots.Bot;

import javax.swing.plaf.IconUIResource;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The BotConnection class mocks a player connection for a bot.
 */
public class BotConnection extends PlayerConnection
{
	public BotConnection(ServerSocket listener, ReentrantLock socketLock, Server server, int playerID)
	{
		super(null, null, server, playerID);

		Random random = new Random();
		String name = "Bot " + possibleNames.get(random.nextInt(possibleNames.size()));
		this.player = new Bot(playerID, name, null, server);
	}

	@Override
	public void send(Message message)
	{
		((Bot)player).receiveMessage(message);
	}


	@Override
	protected void establishConnection() throws IOException
	{
		// Do nothing
	}

	@Override
	protected void startListener()
	{
		// Do nothing
	}


	@Override
	protected void joinCallback(RequestJoinMessage message)
	{

	}


	// https://pl.wiktionary.org/wiki/Kategoria:J%C4%99zyk_polski_-_imiona_m%C4%99skie
	// i tylko te fajne
	private static final List<String> possibleNames = Arrays.asList(
			"Baltazar",
			"Barabasz",
			"Barnaba",
			"Bartłomiej",
			"Bartosz",
			"Baruch",
			"Baszar",
			"Baszszar",
			"Bazyli",
			"Benedykt",
			"Beniamin",
			"Benicjusz",
			"Benon",
			"Bernard",
			"Bernardyn",
			"Biernat",
			"Błażej",
			"Bogdan",
			"Bogdar",
			"Boguchwał",
			"Bogumił",
			"Bogumir",
			"Bogusław",
			"Bohdan",
			"Bolesław",
			"Bonawentura",
			"Bonifacy",
			"Boromeusz",
			"Borys",
			"Borysek",
			"Bożydar",
			"Bronisław",
			"Brunon",
			"Brutus"
	);
}
