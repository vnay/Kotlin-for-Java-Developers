package games.gameOfFifteen

import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer): Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        board.setNewValues(initializer)
    }

    override fun canMove(): Boolean = board.any { it == null }

    override fun hasWon(): Boolean {
        val allSet = board.getAllCells()
        for (i in 1..15) {
            if (board[allSet.elementAt(i - 1)] != i)
                return false
        }
        return true
    }

    override fun processMove(direction: Direction) {
        val nCell = board.filter { it == null }.first()
        when (direction) {
            Direction.UP -> {
                if (board.getCellOrNull(nCell.i + 1, nCell.j) != null) {
                    val t = board[board.getCell(nCell.i + 1, nCell.j)]
                    board[nCell] = t
                    board[board.getCell(nCell.i + 1, nCell.j)] = null
                }
            }
            Direction.DOWN -> {
                if (board.getCellOrNull(nCell.i - 1, nCell.j) != null) {
                    val t = board[board.getCell(nCell.i - 1, nCell.j)]
                    board[nCell] = t
                    board[board.getCell(nCell.i - 1, nCell.j)] = null
                }
            }
            Direction.RIGHT -> {
                if (board.getCellOrNull(nCell.i, nCell.j - 1) != null) {
                    val t = board[board.getCell(nCell.i, nCell.j - 1)]
                    board[nCell] = t
                    board[board.getCell(nCell.i, nCell.j - 1)] = null
                }
            }
            Direction.LEFT -> {
                if (board.getCellOrNull(nCell.i, nCell.j + 1) != null) {
                    val t = board.get(board.getCell(nCell.i, nCell.j + 1))
                    board[nCell] = t
                    board[board.getCell(nCell.i, nCell.j + 1)] = null
                }
            }
        }
    }

    override fun get(i: Int, j: Int): Int? = board.get(board.getCell(i, j))

    private fun GameBoard<Int?>.setNewValues(initializer: GameOfFifteenInitializer) {
        val allSet = getAllCells()
        val perm = initializer.initialPermutation
        for (i in allSet.indices) {
            try {
                this[allSet.elementAt(i)] = perm[i]
            } catch (e: Exception) {
                this[allSet.elementAt(i)] = null
            }
        }
    }
}

