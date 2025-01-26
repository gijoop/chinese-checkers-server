package com.chinese_checkers.server.Game.Bots;

import com.chinese_checkers.comms.Message.FromClient.EndTurnMessage;
import com.chinese_checkers.comms.Message.FromServer.*;
import com.chinese_checkers.comms.Message.Message;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Connection.Server;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.Bots.BotImplementations.BotBrain;
import com.chinese_checkers.server.Game.Bots.BotImplementations.DefaultBotImplementation;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Bot extends Player
{
	private BotBrain brain;

	private final Server server;
	private final Board board;
	private final Ruleset ruleset;

	private final Map<String, Consumer<Message>> messageHandlers;

	public Bot(int id, String name, Corner corner, Server server)
	{
		super(name, id, corner);
		this.server = server;
		this.board = server.getGameManager().getBoard();
		this.ruleset = server.getGameManager().getRuleset();

		this.brain = new DefaultBotImplementation(ruleset, board);

		messageHandlers = new HashMap<>();
		messageHandlers.put("game_start", msg -> onGameStart((GameStartMessage) msg));
		messageHandlers.put("game_end", msg -> onGameEnd((GameEndMessage) msg));
		messageHandlers.put("next_round", msg -> onNextRound((NextRoundMessage) msg));
		messageHandlers.put("response", msg -> onResponse((ResponseMessage) msg));
		messageHandlers.put("move_player", msg -> onMovePlayer((MovePlayerMessage) msg));
		messageHandlers.put("self_data", msg -> onSelfDataReceived((SelfDataMessage) msg));
		messageHandlers.put("announce_winner", msg -> onWinnerAnnounced((AnnounceWinnerMessage) msg));
	}


	public void receiveMessage(Message message)
	{
		String type = message.getType();

		if (messageHandlers.containsKey(type))
		{
			messageHandlers.get(type).accept(message);
		}
		else
		{
			System.out.println("Bot " + name + " received unknown message type: " + type);
		}
	}


	private Void onGameStart(GameStartMessage message)
	{
		// bot has full access to the board
		return null;
	}

	private Void onGameEnd(GameEndMessage message)
	{
		// do nothing
		return null;
	}

	private Void onNextRound(NextRoundMessage message)
	{
		if (message.getCurrentPlayerID() != this.id)
		{
			// not my turn
			return null;
		}

		var requestMessage = brain.move(this.getCorner());


		if (requestMessage == null)
		{
			System.out.println("Bot " + name + " failed to make a move");
			server.endTurnCallback(new EndTurnMessage(), this);
			return null;
		}

		var movedPawn = board.getPawnById(requestMessage.pawnID);
		Position oldPosition = board.getPositionOf(movedPawn);
		Position newPosition = new Position(requestMessage.x, requestMessage.y);

		boolean isJump = board.distance(oldPosition, newPosition) > 1;

		// send the move request to the server
		server.moveCallback(requestMessage, this);

		if (!isJump)
			return null;

		// check for chained jumps
		Set<Position> prohibitedJumps = new HashSet<>();
		prohibitedJumps.add(oldPosition);
		prohibitedJumps.add(newPosition);

		var chainedJump = brain.jumpPawn(this.corner, movedPawn.getId(), prohibitedJumps);
		while (chainedJump != null)
		{
			prohibitedJumps.add(new Position(chainedJump.x, chainedJump.y));
			server.moveCallback(requestMessage, this);
			chainedJump = brain.jumpPawn(this.corner, movedPawn.getId(), prohibitedJumps);
		}

		// jumped -> end turn manually
		server.endTurnCallback(new EndTurnMessage(), this);

		return null;
	}

	private Void onResponse(ResponseMessage message)
	{
		return null;
	}

	private Void onMovePlayer(MovePlayerMessage message)
	{
		return null;
	}

	private Void onSelfDataReceived(SelfDataMessage message)
	{
		return null;
	}

	private Void onWinnerAnnounced(AnnounceWinnerMessage message)
	{
		return null;
	}


}
