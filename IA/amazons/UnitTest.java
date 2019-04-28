package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static org.junit.Assert.*;
import ucb.junit.textui;

/** The suite of all JUnit tests for the amazons package.
 *  @author Ziyue Meng
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }

    /** Tests basic correctness of put and get on the initialized board. */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
    }

    /** Tests proper identification of legal/illegal queen moves. */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   B - - - - - - - - B\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   W - - - - - - - - W\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - S - S - - S - S -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - W - - - - W - -\n"
                    + "   - - - W W W W - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n";
    /** Test isLegal of board **/
    @Test
    public void testisLegal() {
        Board b = new Board();
        assertTrue(b.isLegal(Square.sq(3, 0), Square.sq(3, 1)));
        assertFalse(b.isLegal(Square.sq(3, 9), Square.sq(6, 9)));
        assertFalse(b.isLegal(Square.sq(6, 9),
                Square.sq(6, 6), Square.sq(6, 0)));
        assertTrue(b.isLegal(Square.sq(3, 0),
                Square.sq(3, 1), Square.sq(3, 2)));
    }

    /** Test makeMove Method of board **/
    @Test
    public void testmakeMove() {
        Board b = new Board();
        b.makeMove(Square.sq(3, 0), Square.sq(3, 1), Square.sq(3, 2));
        assertEquals(WHITE, Square.sq(3, 1).getContent());
        assertEquals(SPEAR, Square.sq(3, 2).getContent());
        assertEquals(1, b.numMoves());
        b.undo();
        assertEquals(WHITE, Square.sq(3, 0).getContent());
        assertEquals(EMPTY, Square.sq(3, 2).getContent());
        assertEquals(EMPTY, Square.sq(3, 1).getContent());
        assertEquals(0, b.numMoves());
    }

    @Test
    public void testDirection() {
        Board board  = new Board();
        Square a = Square.sq(55);
        Square b = Square.sq(77);
        Square c = Square.sq(33);
        Square d = Square.sq(58);
        Square e = Square.sq(51);
        Square f = Square.sq(85);
        Square g = Square.sq(15);
        Square h = Square.sq(37);
        Square i = Square.sq(73);
        assertEquals(a.direction(b), 1);
        assertEquals(a.direction(c), 5);
        assertEquals(a.direction(d), 2);
        assertEquals(a.direction(e), 6);
        assertEquals(a.direction(f), 0);
        assertEquals(a.direction(g), 4);
        assertEquals(a.direction(h), 3);
        assertEquals(a.direction(i), 7);
    }
    @Test
    public void testQueenMove() {
        Board board  = new Board();
        Square a = Square.sq(55);
        Square b = Square.sq(77);
        Square c = Square.sq(33);
        Square d = Square.sq(58);
        Square e = Square.sq(51);
        Square f = Square.sq(85);
        Square g = Square.sq(15);
        Square h = Square.sq(37);
        Square i = Square.sq(73);
        Square irrelevent = Square.sq(1);
        assertEquals(a.queenMove(0, 3), f);
        assertEquals(a.queenMove(1, 2), b);
        assertEquals(a.queenMove(2, 3), d);
        assertEquals(a.queenMove(3, 2), h);
        assertEquals(a.queenMove(4, 4), g);
        assertEquals(a.queenMove(5, 2), c);
        assertEquals(a.queenMove(6, 4), e);
        assertEquals(a.queenMove(7, 2), i);
        assertEquals(a.queenMove(0, 5), null);
    }

    @Test
    public void testisUnbloackedMove() {
        Board board = new Board();
        Square whiteone = Square.sq(30);
        Square whitetwo = Square.sq(3);
        Square whitethree = Square.sq(6);
        Square whitefour = Square.sq(39);
        Square blackone = Square.sq(60);
        Square blacktwo = Square.sq(93);
        Square blackthree = Square.sq(96);
        Square blackfour = Square.sq(69);
        Square a = Square.sq(55);
        Square b = Square.sq(77);
        Square c = Square.sq(33);
        Square d = Square.sq(58);
        Square e = Square.sq(51);
        Square f = Square.sq(85);
        Square g = Square.sq(15);
        Square h = Square.sq(37);
        Square i = Square.sq(73);
        Square irrelevent = Square.sq(1);
        assertFalse(board.isUnblockedMove(whitefour, Square.sq(99), null));
        assertFalse(board.isUnblockedMove(whiteone, Square.sq(60), null));
        assertTrue(board.isUnblockedMove(whiteone, Square.sq(50), null));
    }
    @Test
    public void testisLegal1() {
        Board board = new Board();
        Square whiteone = Square.sq(30);
        Square whitetwo = Square.sq(3);
        Square whitethree = Square.sq(6);
        Square whitefour = Square.sq(39);
        Square blackone = Square.sq(60);
        Square blacktwo = Square.sq(93);
        Square blackthree = Square.sq(96);
        Square blackfour = Square.sq(69);
        board.put(SPEAR, Square.sq(40));
        board.put(SPEAR, Square.sq(31));
        board.put(SPEAR, Square.sq(20));
        board.put(SPEAR, Square.sq(41));
        board.put(SPEAR, Square.sq(21));
        assertTrue(Square.sq(30).getContent() == WHITE);
        assertTrue(Square.sq(93).getContent() == BLACK);
        assertFalse(Square.sq(93).getContent() == WHITE);
        assertFalse(board.isLegal(whiteone));
        assertTrue(board.isLegal(whitefour));
        assertFalse(board.isLegal(whiteone, blackone));
        assertEquals(board.isLegal(blackone, Square.sq(63)), false);
        assertFalse(board.isLegal(blackfour, Square.sq(88)));
        assertFalse(board.isLegal(blackone, Square.sq(63), Square.sq(84)));
        assertFalse(board.isLegal(Square.sq(0), Square.sq(90)));
        assertFalse(board.isLegal(Square.sq(0), Square.sq(30)));
        assertTrue(board.isLegal(Square.sq(3), Square.sq(4), Square.sq(24)));
    }

    @Test
    public void testmakeMove1() {
        Board board = new Board();
        Square whiteone = Square.sq(30);
        Square whitetwo = Square.sq(3);
        Square whitethree = Square.sq(6);
        Square whitefour = Square.sq(39);
        Square blackone = Square.sq(60);
        Square blacktwo = Square.sq(93);
        Square blackthree = Square.sq(96);
        Square blackfour = Square.sq(69);
        board.makeMove(Move.mv(whiteone, Square.sq(34), Square.sq(44)));
        assertEquals(board.turn(), BLACK);
        assertEquals(board.get(0, 3), EMPTY);
        assertEquals(board.get(4, 3), WHITE);
        assertEquals(board.get(Square.sq(44)), SPEAR);
        assertEquals(Square.sq('a', '4'), Square.sq(30));
        board.makeMove(Move.mv(blackone, Square.sq(64), Square.sq(65)));
        board.makeMove(Move.mv(whitefour, Square.sq(49), Square.sq(59)));
        board.makeMove(Move.mv(Square.sq(64), Square.sq(54), Square.sq(55)));
    }

    @Test
    public void testlastMoveandundo() {
        Board board = new Board();
        Square whiteone = Square.sq(30);
        Square whitetwo = Square.sq(3);
        Square whitethree = Square.sq(6);
        Square whitefour = Square.sq(39);
        Square blackone = Square.sq(60);
        Square blacktwo = Square.sq(93);
        Square blackthree = Square.sq(96);
        Square blackfour = Square.sq(69);
        board.makeMove(Move.mv(whiteone, Square.sq(34), Square.sq(44)));
        assertEquals(board.turn(), BLACK);
        assertEquals(board.numMoves(), 1);
        board.makeMove(Move.mv(blackone, Square.sq(64), Square.sq(65)));
        assertEquals(board.turn(), WHITE);
        assertEquals(board.numMoves(), 2);
        board.makeMove(Move.mv(whitefour, Square.sq(49), Square.sq(59)));
        assertEquals(board.turn(), BLACK);
        assertEquals(board.numMoves(), 3);
        board.makeMove(Move.mv(Square.sq(64), Square.sq(54), Square.sq(55)));
        assertEquals(board.turn(), WHITE);
        assertEquals(board.numMoves(), 4);
        board.undo();
        assertEquals(board.turn(), BLACK);
        assertEquals(board.numMoves(), 3);
        board.undo();
        assertEquals(board.turn(), WHITE);
        assertEquals(board.numMoves(), 2);
        board.undo();
        assertEquals(board.turn(), BLACK);
        assertEquals(board.numMoves(), 1);
        board.undo();
        assertEquals(board.turn(), WHITE);
        assertEquals(board.numMoves(), 0);
        board.undo();
        assertEquals(board.turn(), WHITE);
        assertEquals(board.numMoves(), 0);
        board.undo();
        assertEquals(board.turn(), WHITE);
        assertEquals(board.numMoves(), 0);
    }
}
