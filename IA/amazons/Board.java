package amazons;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import static amazons.Piece.*;
/** The state of an Amazons Game.
 *  @author Ziyue Meng
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        _numMoves = model.numMoves();
        _turn = model._turn;
        lastmove = model.lastmove;
        _board = model._board;
    }

    /** Clears the board to the initial position. */
    void init() {
        _turn = WHITE;
        _winner = EMPTY;
        _board = new Square[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                _board[i][j] = Square.sq(j, i);
                _board[i][j].setContent(EMPTY);
            }
        }
        _board[0][3].setContent(WHITE);
        _board[0][6].setContent(WHITE);
        _board[3][0].setContent(WHITE);
        _board[3][9].setContent(WHITE);
        _board[6][0].setContent(BLACK);
        _board[6][9].setContent(BLACK);
        _board[9][3].setContent(BLACK);
        _board[9][6].setContent(BLACK);
        _numMoves = 0;
        lastmove = new ArrayList<>();
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return _numMoves;
    }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        return _winner;
    }

    /** Return the contents the square at S. */
    final Piece get(Square s) {
        return get(s.col(), s.row());
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        if (col < 0 || col > 9 || row < 0 || row > 9) {
            return SPEAR;
        }
        return _board[row][col].getContent();
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        put(p, s.col(), s.row());
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        _board[row][col].setContent(p);
        _winner = EMPTY;
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (from.isQueenMove(to)) {
            int d = from.direction(to);
            if (d == -1) {
                return false;
            } else {
                int steps = Math.max(Math.abs(from.row() - to.row()),
                        Math.abs(from.col() - to.col()));
                for (int i = 1; i <= steps; i++) {
                    Square x = from.queenMove(d, i);
                    if (get(x) != EMPTY && !x.equals(asEmpty)) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return get(from).equals(_turn)
                && ((get(from.col() + 1, from.row() + 1) == EMPTY)
                || (get(from.col() + 1, from.row()) == EMPTY)
                || (get(from.col(), from.row() + 1) == EMPTY)
                || (get(from.col() + 1, from.row() - 1) == EMPTY)
                || (get(from.col() - 1, from.row() + 1) == EMPTY)
                || (get(from.col(), from.row() - 1) == EMPTY)
                || (get(from.col() - 1, from.row()) == EMPTY)
                || (get(from.col() - 1, from.row() - 1) == EMPTY));
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        return (get(from).equals(_turn))
                && from.isQueenMove(to) && isUnblockedMove(from, to, null);
    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        return isLegal(from, to) && to.isQueenMove(spear)
                && isUnblockedMove(to, spear, from);

    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return isLegal(move.from(), move.to(), move.spear());
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        assert isLegal(from, to, spear);
        lastmove.add(Move.mv(from, to, spear));
        put(get(from), to);
        put(EMPTY, from);
        put(SPEAR, spear);
        _numMoves++;
        if (_turn == WHITE) {
            _turn = BLACK;
            if (!legalMoves(BLACK).hasNext()) {
                _winner = WHITE;
            }
        } else {
            _turn = WHITE;
            if (!legalMoves(WHITE).hasNext()) {
                _winner = BLACK;
            }
        }

    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        makeMove(move.from(), move.to(), move.spear());
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        if (lastmove.size() > 0) {
            Move x = lastmove.remove(lastmove.size() - 1);
            put(EMPTY, x.spear());
            put(get(x.to()), x.from());
            put(EMPTY, x.to());
            _numMoves--;
            if (_turn == WHITE) {
                _turn = BLACK;
            } else {
                _turn = WHITE;
            }
        }
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = -1;
            _steps = 0;
            _asEmpty = asEmpty;
            _squares = new ArrayList<>();
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _squares.size() != 0;
        }

        @Override
        public Square next() {
            assert (hasNext());
            return _squares.remove(0);
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            if (_dir == -1) {
                _dir++;
                _steps++;
            }
            while (_dir < 8) {
                Square tmp = _from.queenMove(_dir, _steps);
                if (tmp == null
                        || tmp.getContent() != EMPTY
                        && tmp != _asEmpty) {
                    _dir++;
                    _steps = 1;
                } else {
                    _squares.add(_from.queenMove(_dir, _steps));
                    _steps++;
                }
            }
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;
        /** Storage of all reacheable squares. */
        private ArrayList<Square> _squares;
    }

    /** An iterator used by legalMoves. */
    public class LegalMoveIterator implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            _queens = new ArrayList<>();
            _hasNext = true;
            toNext();
        }

        @Override
        public boolean hasNext() {
            if (!_spearThrows.hasNext()
                    && !_pieceMoves.hasNext() && _queens.size() > 0) {
                advance();
                return hasNext();
            }
            return _hasNext
                    && (_spearThrows.hasNext() || _pieceMoves.hasNext());
        }

        @Override
        public Move next() {
            toNext();
            return Move.mv(_start, _nextSquare, _spearThrows.next());
        }

        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {
            if (_pieceMoves == NO_SQUARES) {
                while (_startingSquares.hasNext()) {
                    Square tmp = _startingSquares.next();
                    if (get(tmp) == _fromPiece) {
                        _queens.add(tmp);
                    }
                }
                advance();
            } else if (!_spearThrows.hasNext()) {
                if (_pieceMoves.hasNext()) {
                    _nextSquare = _pieceMoves.next();
                    _spearThrows =
                            new ReachableFromIterator(_nextSquare, _start);
                } else {
                    advance();
                }
            }
        }
        /** Call the method
         *  whenever I want to move on to the next queen. */
        private void advance() {
            _start = _queens.remove(0);
            _pieceMoves = new ReachableFromIterator(_start, null);
            if (((ReachableFromIterator) _pieceMoves)._squares.size() > 0) {
                _nextSquare = _pieceMoves.next();
                _spearThrows = new ReachableFromIterator(_nextSquare, _start);
            } else if (_queens.size() > 0) {
                advance();
            } else {
                _hasNext = false;
            }
        }

        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
        /** Store all my queens of a side. */
        private ArrayList<Square> _queens;
        /** hasNext? */
        private boolean _hasNext;

    }

    @Override
    public String toString() {
        String result = "";
        for (int i = 9; i > -1; i--) {
            for (int j = 0; j < 10; j++) {
                if (j == 0) {
                    result += String.format("   %s",
                            _board[i][j].getContent());
                } else if (j == 9) {
                    result += String.format(" %s%n",
                            _board[i][j].getContent());
                } else {
                    result += String.format(" %s",
                            _board[i][j].getContent());
                }
            }
        }
        return result;
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
        Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;
    /** Storage of the board. */
    private Square[][] _board;
    /** Get the board. */
    /** Storage of the lastmove. */
    private ArrayList<Move> lastmove;

    /** The number of all Moves created. */
    private static int _numMoves;
}
