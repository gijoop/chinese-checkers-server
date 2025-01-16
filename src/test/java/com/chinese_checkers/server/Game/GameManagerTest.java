// package com.chinese_checkers.server.Game;

// import static org.junit.jupiter.api.Assertions.assertEquals;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import com.chinese_checkers.comms.Player;
// import com.chinese_checkers.comms.Position;
// import com.chinese_checkers.comms.Pawn;
// import com.chinese_checkers.server.Game.Ruleset.PlayerConfig;
// import com.chinese_checkers.server.Game.Ruleset.Ruleset;
// import com.chinese_checkers.server.Game.Ruleset.StandardRuleset;

// public class GameManagerTest {
//     private GameManager gameManager;
//     private StandardBoard board;
//     private StandardRuleset ruleset;
//     private Player player;
//     private Pawn pawn;
//     private Position position;

//     @BeforeEach
//     public void setUp() {
//         board = new StandardBoard(5);
//         ruleset = new StandardRuleset(2, board, new PlayerConfig(2, 10));
//         gameManager = new GameManager(board, ruleset, 10);
//         player = new Player("Player1", 1000);
//         pawn = new Pawn(player);
//         position = new Position(1, 1);
//         board.addPawn(pawn, position);
//     }

//     @Test
//     public void testCheckAndMoveSuccess() {
//         Position newPosition = new Position(2, 2);
//         assertEquals(Ruleset.MoveResult.SUCCESS, gameManager.checkAndMove(pawn.getId(), newPosition, player));
//     }

//     @Test
//     public void testCheckAndMoveOccupied() {
//         assertEquals(Ruleset.MoveResult.OCCUPIED, gameManager.checkAndMove(pawn.getId(), position, player));
//     }
// }
