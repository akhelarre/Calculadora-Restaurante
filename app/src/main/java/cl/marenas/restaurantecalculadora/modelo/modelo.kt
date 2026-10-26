package cl.marenas.restaurantecalculadora.modelo

// Necesario para función de calculo.
import kotlin.math.roundToInt

class ItemMenu(
    val nombre: String,
    var precio: String
)

// Se da la relación entre ItemMesa e itemMenu usando atributos.
class ItemMesa(
    val itemMenu: ItemMenu,
    var cantidad: Int
) {
    fun calcularSubtotal(): Int {
        val precioEntero = itemMenu.precio.toIntOrNull() ?: 0
        return precioEntero * cantidad
    }
}

class CuentaMesa(
    val mesa: Int
) { // <-- La clase CuentaMesa abre aquí

    private val _items: MutableList<ItemMesa> = mutableListOf()
    var aceptaPropina: Boolean = true

    fun agregarItem(itemMenu: ItemMenu, cantidad: Int) {
        val nuevoItem = ItemMesa(itemMenu, cantidad)
        _items.add(nuevoItem)
    }

    fun agregarItem(itemMesa: ItemMesa) {
        _items.add(itemMesa)
    }

    fun calcularTotalSinPropina(): Int {
        var total = 0
        for (item in _items) {
            total += item.calcularSubtotal()
        }
        return total
    }

    fun calcularPropina(): Int {
        return if (aceptaPropina) {
            (calcularTotalSinPropina() * 0.10).roundToInt()
        } else {
            0
        }
    }

    fun calcularTotalConPropina(): Int {
        return calcularTotalSinPropina() + calcularPropina()
    }

}