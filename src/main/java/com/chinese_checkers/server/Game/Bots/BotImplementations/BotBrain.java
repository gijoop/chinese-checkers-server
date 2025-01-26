package com.chinese_checkers.server.Game.Bots.BotImplementations;


import com.chinese_checkers.comms.Message.FromClient.MoveRequestMessage;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Position;

import java.util.Set;

public interface BotBrain
{
	public MoveRequestMessage move(Player.Corner myCorner);
	public MoveRequestMessage jumpPawn(Player.Corner myCorner, int pawnId, Set<Position> prohibitedJumps);
}
