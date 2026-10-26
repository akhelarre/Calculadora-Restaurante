package cl.marenas.restaurantecalculadora // Tu paquete principal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cl.marenas.restaurantecalculadora.modelo.CuentaMesa
import cl.marenas.restaurantecalculadora.modelo.ItemMenu
import cl.marenas.restaurantecalculadora.modelo.ItemMesa
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // 1. Declaración de objetos del modelo
    private lateinit var pastelDeChoclo: ItemMenu
    private lateinit var cazuela: ItemMenu
    private lateinit var itemMesaPastel: ItemMesa
    private lateinit var itemMesaCazuela: ItemMesa
    private lateinit var cuentaMesa: CuentaMesa

    // 2. Declaración de Vistas (Views)
    private lateinit var etCantidadPlato1: EditText
    private lateinit var etCantidadPlato2: EditText
    private lateinit var tvSubtotalPlato1: TextView
    private lateinit var tvSubtotalPlato2: TextView
    private lateinit var tvTotalSinPropina: TextView
    private lateinit var swPropina: Switch
    private lateinit var tvMontoPropina: TextView
    private lateinit var tvTotalConPropina: TextView

    // 3. Formateador de moneda para Pesos Chilenos (CLP)
    private val formatoCLP: NumberFormat = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarModelo()
        inicializarVistas()
        configurarEventos()
        actualizarTotalesEnPantalla() // Muestra todo en $0 al iniciar
    }

    private fun inicializarModelo() {
        // Definimos el menú con sus precios como String según el UML
        pastelDeChoclo = ItemMenu("Pastel de Choclo", "12000")
        cazuela = ItemMenu("Cazuela", "10000")

        // Instanciamos los ítems de la mesa con cantidad inicial 0
        itemMesaPastel = ItemMesa(pastelDeChoclo, 0)
        itemMesaCazuela = ItemMesa(cazuela, 0)

        // Creamos la cuenta de la mesa 1 y agregamos los ítems
        cuentaMesa = CuentaMesa(1)
        cuentaMesa.agregarItem(itemMesaPastel)
        cuentaMesa.agregarItem(itemMesaCazuela)
    }

    private fun inicializarVistas() {
        etCantidadPlato1 = findViewById(R.id.etCantidadPlato1)
        etCantidadPlato2 = findViewById(R.id.etCantidadPlato2)
        tvSubtotalPlato1 = findViewById(R.id.tvSubtotalPlato1)
        tvSubtotalPlato2 = findViewById(R.id.tvSubtotalPlato2)
        tvTotalSinPropina = findViewById(R.id.tvTotalSinPropina)
        swPropina = findViewById(R.id.swPropina)
        tvMontoPropina = findViewById(R.id.tvMontoPropina)
        tvTotalConPropina = findViewById(R.id.tvTotalConPropina)
    }

    private fun configurarEventos() {
        // Evento TextChanged para el Platillo 1 (Pastel de Choclo)
        etCantidadPlato1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // Si el usuario borra el texto, asumimos 0 para que no se caiga la app
                val cantidad = s.toString().toIntOrNull() ?: 0
                itemMesaPastel.cantidad = cantidad
                actualizarTotalesEnPantalla()
            }
        })

        // Evento TextChanged para el Platillo 2 (Cazuela)
        etCantidadPlato2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val cantidad = s.toString().toIntOrNull() ?: 0
                itemMesaCazuela.cantidad = cantidad
                actualizarTotalesEnPantalla()
            }
        })

        // Evento CheckedChange para el Switch de Propina
        swPropina.setOnCheckedChangeListener { _, isChecked ->
            cuentaMesa.aceptaPropina = isChecked
            actualizarTotalesEnPantalla()
        }
    }

    private fun actualizarTotalesEnPantalla() {
        // Formateamos y mostramos los subtotales de cada plato
        tvSubtotalPlato1.text = formatoCLP.format(itemMesaPastel.calcularSubtotal())
        tvSubtotalPlato2.text = formatoCLP.format(itemMesaCazuela.calcularSubtotal())

        // Formateamos y mostramos los totales de la cuenta
        tvTotalSinPropina.text = formatoCLP.format(cuentaMesa.calcularTotalSinPropina())
        tvMontoPropina.text = formatoCLP.format(cuentaMesa.calcularPropina())
        tvTotalConPropina.text = formatoCLP.format(cuentaMesa.calcularTotalConPropina())
    }
}