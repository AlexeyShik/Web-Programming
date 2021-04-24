package ru.itmo.wp.web.page;

import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@SuppressWarnings({"unused", "RedundantSuppression"})
public class TicTacToePage {

    private boolean isNotInitialized(State state, Map<String, Object> view, HttpServletRequest request) {
        if (state == null) {
            action(view, request);
            return true;
        }
        return false;
    }

    private int keyToIndex(String key, int i) {
        return Integer.parseInt(String.valueOf(key.charAt(key.length() - 1 - i)));
    }

    private void onMove(Map<String, Object> view, HttpServletRequest request) {
        State state = (State) request.getSession().getAttribute("state");
        if (isNotInitialized(state, view, request)) {
            return;
        }
        for (var entry : request.getParameterMap().entrySet()) {
            String key = entry.getKey();
            if (key.startsWith("cell")) {
                int col = keyToIndex(key, 0);
                int row = keyToIndex(key, 1);
                if (state.isCorrectIndex(row) && state.isCorrectIndex(col) && state.makeMove(view, row, col)) {
                    state.checkEndGame();
                    state.crossesMove ^= true;
                }
                break;
            }
        }
        view.put("state", state);
        throw new RedirectException("/ticTacToe");
    }

    private void newGame(Map<String, Object> view, HttpServletRequest request) {
        State state = (State) request.getSession().getAttribute("state");
        if (isNotInitialized(state, view, request)) {
            return;
        }
        state.clear();
        view.put("state", state);
    }

    private void action(Map<String, Object> view, HttpServletRequest request) {
        HttpSession session = request.getSession();
        State state = (State) session.getAttribute("state");
        if (state == null) {
            state = new State();
            session.setAttribute("state", state);
        }
        view.put("state", state);
    }

    private enum Phase {
        RUNNING,
        WON_X,
        WON_O,
        DRAW
    }

    public static class State {
        private final int SIZE = 3;
        private Phase phase;
        private boolean crossesMove;
        private final String[][] cells;
        private int moves;

        State() {
            phase = Phase.RUNNING;
            crossesMove = true;
            moves = 0;
            cells = new String[SIZE][SIZE];
        }

        public int getSize() {
            return SIZE;
        }

        public String getPhase() {
            return phase.name();
        }

        public boolean isCrossesMove() {
            return crossesMove;
        }

        public String[][] getCells() {
            return cells.clone();
        }

        private boolean isCorrectIndex(int index) {
            return index >= 0 && index < SIZE;
        }

        private void checkEndGame() {
            if (checkWin()) {
                phase = (crossesMove ? Phase.WON_X : Phase.WON_O);
            } else if (moves == SIZE * SIZE) {
                phase = Phase.DRAW;
            }
        }

        private boolean checkWin(int i0, int j0, int di, int dj) {
            if (cells[i0][j0] == null) {
                return false;
            }
            for (int t = 1; t < SIZE; ++t) {
                int i1 = i0 + t * di;
                int j1 = j0 + t * dj;
                if (!cells[i0][j0].equals(cells[i1][j1])) {
                    return false;
                }
            }
            return true;
        }

        private boolean checkWin() {
            boolean win = false;
            for (int i = 0; i < SIZE; ++i) {
                win |= checkWin(i, 0, 0, 1);
                win |= checkWin(0, i, 1, 0);
            }
            win |= checkWin(0, 0, 1, 1);
            win |= checkWin(2, 0, -1, 1);
            return win;
        }

        private boolean makeMove(Map<String, Object> view, int row, int col) {
            if (phase != Phase.RUNNING) {
                return false;
            }
            if (cells[row][col] != null) {
                return false;
            }
            cells[row][col] = crossesMove ? "X" : "O";
            ++moves;
            return true;
        }

        private void clear() {
            for (int i = 0; i < SIZE; ++i) {
                for (int j = 0; j < SIZE; ++j) {
                    cells[i][j] = null;
                }
            }
            phase = Phase.RUNNING;
            crossesMove = true;
            moves = 0;
        }
    }
}