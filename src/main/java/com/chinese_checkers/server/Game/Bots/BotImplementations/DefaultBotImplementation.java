package com.chinese_checkers.server.Game.Bots.BotImplementations;

import com.chinese_checkers.comms.Message.FromClient.MoveRequestMessage;
import com.chinese_checkers.comms.Pawn;
import com.chinese_checkers.comms.Player;
import com.chinese_checkers.comms.Position;
import com.chinese_checkers.server.Game.Board;
import com.chinese_checkers.server.Game.Move;
import com.chinese_checkers.server.Game.MoveValidator.*;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;

import java.util.*;

public class DefaultBotImplementation implements BotBrain
{
	private final Ruleset ruleset;
	private final Board board;

	private int currentPositionValue = Integer.MAX_VALUE;

	public DefaultBotImplementation(Ruleset ruleset, Board board)
	{
		this.ruleset = ruleset;
		this.board = board;
	}


	@Override
	public MoveRequestMessage move(Player.Corner myCorner)
	{
		ArrayList<Pawn> myPawns = board.getPlayerPawns(myCorner);

		// pawnID, move value
		Map<Pawn, Position> bestMoves = new HashMap<>();
		currentPositionValue = evaluateCurrentPosition(board, myCorner);
		int bestValue = Integer.MAX_VALUE;

		MoveValidator validator =
			new BoundsValidator(
			new OccupiedValidator(
			new MoveOutsideGoalValidator(
			new ReachablePositionValidator(null, ruleset), myCorner, ruleset), board), ruleset);

		for (Pawn pawn : myPawns)
		{
			var possibleMoves = ruleset.getReachableMoves(board.getPositionOf(pawn));
			var possibleJumps = ruleset.getReachableJumps(board.getPositionOf(pawn));

			for (Position position : possibleMoves)
			{
				Move move = new Move(board.getPositionOf(pawn), position);
				validator.validateMove(move);

				if (move.getResult() != Ruleset.MoveResult.SUCCESS && move.getResult() != Ruleset.MoveResult.SUCCESS_JUMP)
				{
					continue;
				}

				int moveValue = evaluateMove(board, pawn, position, myCorner);

				if (moveValue < bestValue)
				{
					bestMoves.clear();
					bestMoves.put(pawn, position);
					bestValue = moveValue;
				}
				else if (moveValue == bestValue)
				{
					bestMoves.put(pawn, position);
				}
			}

			for (Position position : possibleJumps)
			{
				Move move = new Move(board.getPositionOf(pawn), position);
				validator.validateMove(move);

				if (move.getResult() != Ruleset.MoveResult.SUCCESS && move.getResult() != Ruleset.MoveResult.SUCCESS_JUMP)
				{
					continue;
				}

				int moveValue = evaluateMove(board, pawn, position, myCorner);

				// jump only when the value is better
				if (moveValue < bestValue)
				{
					bestMoves.clear();
					bestMoves.put(pawn, position);
					bestValue = moveValue;
				}
			}
		}

		if (bestMoves.isEmpty())
		{
			return null;
		}

		// select random move from best moves
		Pawn chosenPawn = bestMoves.keySet().stream().toList().get(new Random().nextInt(bestMoves.size()));
		Position chosenMove = bestMoves.get(chosenPawn);

		currentPositionValue = bestValue;
		return new MoveRequestMessage(chosenPawn.getId(), chosenMove.getX(), chosenMove.getY());
	}

	@Override
	public MoveRequestMessage jumpPawn(Player.Corner myCorner, int pawnId, Set<Position> prohibitedJumps)
	{
		// don't check if it is my pawn

		ArrayList<Position> bestMoves = new ArrayList<>();
		int bestValue = currentPositionValue;

		MoveValidator validator =
			new BoundsValidator(
			new OccupiedValidator(
			new MoveOutsideGoalValidator(
			new ReachablePositionValidator(null, ruleset), myCorner, ruleset), board), ruleset);

		Pawn pawn = board.getPawnById(pawnId);

		var possibleJumps = ruleset.getReachableJumps(board.getPositionOf(pawn));

		for (Position position : possibleJumps)
		{
			if (prohibitedJumps.contains(position))
				continue;

			Move move = new Move(board.getPositionOf(pawn), position);
			validator.validateMove(move);

			if (move.getResult() != Ruleset.MoveResult.SUCCESS && move.getResult() != Ruleset.MoveResult.SUCCESS_JUMP)
			{
				continue;
			}

			int moveValue = evaluateMove(board, pawn, position, myCorner);

			// jump only when the value is better
			if (moveValue < bestValue)
			{
				bestMoves.clear();
				bestMoves.add(position);
				bestValue = moveValue;
			}
		}

		if (bestMoves.isEmpty())
		{
			return null;
		}

		// select random move from best moves
		Position chosenMove = bestMoves.get(new Random().nextInt(bestMoves.size()));

		currentPositionValue = bestValue;

		return new MoveRequestMessage(pawnId, chosenMove.getX(), chosenMove.getY());
	}


	private int evaluateCurrentPosition(Board board, Player.Corner myCorner)
	{
		int totalDistance = 0;

		var myPawns = board.getPlayerPawns(myCorner);
		for (Pawn p : myPawns)
		{
			totalDistance += board.distance(board.getPositionOf(p), myCorner.getOpposite());
		}

		return totalDistance;
	}


	private int evaluateMove(Board board, Pawn pawn, Position position, Player.Corner myCorner)
	{
		int totalDistance = 0;

		// calculate every other pawn
		var myPawns = board.getPlayerPawns(myCorner);
		for (Pawn p : myPawns)
		{
			if (!Objects.equals(p.getId(), pawn.getId()))
				totalDistance += board.distance(board.getPositionOf(p), myCorner.getOpposite());
		}

		// calculate pawn's new position value
		totalDistance += board.distance(position, myCorner.getOpposite());

		return totalDistance;
	}
}
