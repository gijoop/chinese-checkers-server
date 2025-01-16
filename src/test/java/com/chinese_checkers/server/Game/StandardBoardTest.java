// package com.chinese_checkers.server.Game;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNull;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import com.chinese_checkers.comms.Pawn;
// import com.chinese_checkers.comms.Position;
// import com.chinese_checkers.server.Move;

// public class StandardBoardTest {
//     private StandardBoard board;
//     private Pawn pawn;
//     private Position position;

//     @BeforeEach
//     public void setUp() {
//         board = new StandardBoard(10);
//         pawn = new Pawn(null);
//         position = new Position(1, 1);
//         board.addPawn(pawn, position);
//     }

//     @Test
//     public void testAddPawn() {
//         assertEquals(pawn, board.getPawnAt(position));
//     }

//     @Test
//     public void testMovePawn() {
//         Position newPosition = new Position(2, 2);
//         board.movePawn(new Move(pawn, newPosition));
//         assertEquals(pawn, board.getPawnAt(newPosition));
//         assertNull(board.getPawnAt(position));
//     }

//     @Test
//     public void testIsOccupied() {
//         assertEquals(true, board.isOccupied(position));
//         assertEquals(false, board.isOccupied(new Position(2, 2)));
//     }
// }
