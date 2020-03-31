package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

data class DataCell(val i: Int, val j: Int)
val res = hashMapOf<DataCell, Cell>()

open class SquareBoardImpl(override val width: Int): SquareBoard {

    init {
        (1..width)
                .forEach { i ->
                    (1..width)
                            .forEach { j ->
                                res[DataCell(i,j)] = Cell(i,j)
                            }
                }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if(i < 1 || i > width || j< 1 || j > width)
            return null
        return res.getValue(DataCell(i,j))
    }

    override fun getCell(i: Int, j: Int): Cell {
        val cell = getCellOrNull(i,j)
        requireNotNull(cell) { "Incorrect Values of i and j" }
        return cell
    }

    override fun getAllCells(): Collection<Cell> {
        val re = mutableListOf<Cell>()
        (1..width)
                .forEach { i ->
                    (1..width)
                            .forEach { j ->
                                re.add(res.getValue(DataCell(i,j)))
                            }
                }
        return re
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val re = mutableListOf<Cell>()
        for (j in jRange)
            if (getCellOrNull(i,j) != null)
                re.add(getCell(i,j))
        return re
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val re = mutableListOf<Cell>()
        for (i in iRange)
            if (getCellOrNull(i,j) != null)
                re.add(getCell(i,j))
        return re
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when(direction) {
            UP -> getCellOrNull(i-1, j)
            DOWN -> getCellOrNull(i+1, j)
            RIGHT -> getCellOrNull(i, j+1)
            LEFT -> getCellOrNull(i, j-1)
        }
    }

}

class GameBoardImpl<T>(override val width: Int): SquareBoardImpl(width), GameBoard<T> {

    private val fin = hashMapOf<DataCell,T?>()

    init {
        for (i in 1..width)
            for (j in 1..width) {
                res[DataCell(i, j)] = Cell(i, j)
                fin[DataCell(i,j)] = null
            }
    }

    override fun get(cell: Cell): T? {
        return fin[DataCell(cell.i,cell.j)]
    }

    override fun set(cell: Cell, value: T?) {
        fin[DataCell(cell.i, cell.j)] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        val re = mutableListOf<Cell?>()
        for ((key,value) in fin) {
            if (predicate(value)) {
                re.add(res[key])
            }
        }
        return re.filterNotNull()
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        for ((key,value) in fin) {
            if (predicate(value))
                return res[key]
        }
        return null
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        (1..width)
                .forEach { i ->
                    (1..width).forEach { j ->
                        val cell = getCell(i, j)
                        if (predicate(fin[DataCell(cell.i, cell.j)]))
                            return true
                    }
                }
        return false
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        (1..width)
                .forEach { i ->
                    (1..width).forEach { j ->
                        val cell = getCell(i, j)
                        if (!predicate(fin[(DataCell(cell.i, cell.j))]))
                            return false
                    }
                }
        return true
    }

}

