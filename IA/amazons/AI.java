package amazons;

import java.util.Iterator;

import static java.lang.Math.*;

import static amazons.Piece.*;

/** A Player that automatically generates moves.
 *  @author Ziyue Meng
 */
class AI extends Player {

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        if (depth == 0 || board.winner() != null) {
            return staticScore(board);
        }
        if (sense == 1) {
            Iterator dummy = board.legalMoves(WHITE);
            while (dummy.hasNext()) {
                Move option = (Move) dummy.next();
                board.makeMove(option);
                int response = findMove(board, depth - 1,
                        false, -1, alpha, beta);
                if (response > alpha) {
                    if (saveMove) {
                        _lastFoundMove = option;
                    }
                    alpha = max(alpha, response);
                }
                board.undo();
            }
        } else if (sense == -1) {
            Iterator dummy = board.legalMoves(BLACK);
            while (dummy.hasNext()) {
                Move option = (Move) dummy.next();
                board.makeMove(option);
                int response = findMove(board, depth - 1,
                        false, 1, alpha, beta);
                if (response < beta) {
                    beta = min(beta, response);
                    if (saveMove) {
                        _lastFoundMove = option;
                    }
                }
                board.undo();
            }
        }
        return staticScore(board);
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {
        int N = board.numMoves();

        if (N > J) {
            return 10;
        } else if (N > B) {
            return 5;
        } else if (N > D) {
            return 3;
        } else {
            return 1;
        }
    }

    /** Return a value for BOARD. */
    private int staticScore(Board board) {
        Piece winner = board.winner();
        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        }
        int score = 0;
        Iterator blackmoves = board.legalMoves(BLACK);
        Iterator whitemoves = board.legalMoves(WHITE);
        while (blackmoves.hasNext()) {
            Move tmp = (Move) blackmoves.next();
            score--;
        }
        while (whitemoves.hasNext()) {
            Move tmp = (Move) whitemoves.next();
            score++;
        }
        return score;
    }
    /** jb. */
    static final int J = 50;
    /** jb. */
    static final int B = 40;
    /** jb. */
    static final int D = 20;

}
