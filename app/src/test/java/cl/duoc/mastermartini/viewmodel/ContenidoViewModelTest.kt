package cl.duoc.mastermartini.viewmodel

import cl.duoc.mastermartini.model.TipoContenido
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ContenidoViewModelTest {
    @Test
    fun filtrarMuestraSoloElTipoElegidoYTodosLoRestaura() {
        val vm = ContenidoViewModel()
        val total = vm.estado.value.lista.size

        vm.filtrar(TipoContenido.Video)
        assertTrue(vm.estado.value.lista.all { it.tipo == TipoContenido.Video })
        assertEquals(2, vm.estado.value.lista.size)

        vm.filtrar(null)
        assertEquals(total, vm.estado.value.lista.size)
    }
}
