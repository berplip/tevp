package com.example.Microservice_Producto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.example.Microservice_Producto.model.Producto;
import com.example.Microservice_Producto.repository.ProductoRepository;
import com.example.Microservice_Producto.service.ProductoService;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Tests de Integración del Microservicio")
class MicroserviceProductoApplicationTests {

	@Autowired
	private ProductoService productoService;

	@Autowired
	private ProductoRepository productoRepository;

	@Test
	@DisplayName("El contexto de Spring Boot debería cargar correctamente")
	void contextLoads() {
		// Test básico que verifica que Spring Boot puede arrancar
		assertThat(productoService).isNotNull();
		assertThat(productoRepository).isNotNull();
	}

	@Test
	@DisplayName("Debería crear y obtener un producto en la base de datos real")
	void deberiaCrearYObtenerProductoEnBaseDatos() {
		// Given - Crear un producto de prueba
		Producto producto = new Producto();
		producto.setNombre("Test iPhone");
		producto.setDescripcion("Producto de prueba");
		producto.setPrecio(1000000.0);
		producto.setStock(5);

		// When - Guardar el producto
		Producto productoGuardado = productoService.crear(producto);

		// Then - Verificar que se guardó correctamente
		assertThat(productoGuardado).isNotNull();
		assertThat(productoGuardado.getId()).isNotNull();
		assertThat(productoGuardado.getNombre()).isEqualTo("Test iPhone");

		// Verificar que se puede obtener por ID
		Producto productoObtenido = productoService.obtenerPorId(productoGuardado.getId());
		assertThat(productoObtenido).isNotNull();
		assertThat(productoObtenido.getNombre()).isEqualTo("Test iPhone");
	}

	@Test
	@DisplayName("Debería obtener la lista de productos incluyendo los del DataLoader")
	void deberiaObtenerListaDeProductos() {
		// When - Obtener todos los productos
		List<Producto> productos = productoService.obtenerTodos();

		// Then - Verificar que existen productos (del DataLoader)
		assertThat(productos).isNotEmpty();
		assertThat(productos.size()).isGreaterThan(0);
		
		// Verificar que los productos tienen datos válidos
		Producto primerProducto = productos.get(0);
		assertThat(primerProducto.getId()).isNotNull();
		assertThat(primerProducto.getNombre()).isNotBlank();
		assertThat(primerProducto.getPrecio()).isPositive();
	}

	@Test
	@DisplayName("Debería actualizar un producto existente")
	void deberiaActualizarProductoExistente() {
		// Given - Crear un producto inicial
		Producto producto = new Producto();
		producto.setNombre("Producto Original");
		producto.setDescripcion("Descripción original");
		producto.setPrecio(500000.0);
		producto.setStock(10);

		Producto productoGuardado = productoService.crear(producto);

		// When - Actualizar el producto
		productoGuardado.setNombre("Producto Actualizado");
		productoGuardado.setPrecio(600000.0);
		Producto productoActualizado = productoService.actualizar(productoGuardado.getId(), productoGuardado);

		// Then - Verificar la actualización
		assertThat(productoActualizado).isNotNull();
		assertThat(productoActualizado.getNombre()).isEqualTo("Producto Actualizado");
		assertThat(productoActualizado.getPrecio()).isEqualTo(600000.0);
	}

	@Test
	@DisplayName("Debería eliminar un producto existente")
	void deberiaEliminarProductoExistente() {
		// Given - Crear un producto para eliminar
		Producto producto = new Producto();
		producto.setNombre("Producto a Eliminar");
		producto.setDescripcion("Producto que será eliminado");
		producto.setPrecio(300000.0);
		producto.setStock(7);

		Producto productoGuardado = productoService.crear(producto);
		Long idProducto = productoGuardado.getId();

		// Verificar que el producto existe
		assertThat(productoService.obtenerPorId(idProducto)).isNotNull();

		// When - Eliminar el producto
		productoService.eliminar(idProducto);

		// Then - Verificar que el producto fue eliminado
		Producto productoEliminado = productoService.obtenerPorId(idProducto);
		assertThat(productoEliminado).isNull();
	}

	@Test
	@DisplayName("Debería obtener un producto por su código único")
	void deberiaObtenerProductoPorCodigo() {
		// Given - Crear un producto con código específico
		Producto producto = new Producto();
		producto.setCodigo("PTEST123");
		producto.setNombre("Producto con Código");
		producto.setDescripcion("Producto para buscar por código");
		producto.setPrecio(450000.0);
		producto.setStock(12);

		Producto productoGuardado = productoService.crear(producto);

		// When - Buscar por código
		Producto productoEncontrado = productoService.obtenerPorCodigo("PTEST123");

		// Then - Verificar que se encontró correctamente
		assertThat(productoEncontrado).isNotNull();
		assertThat(productoEncontrado.getCodigo()).isEqualTo("PTEST123");
		assertThat(productoEncontrado.getNombre()).isEqualTo("Producto con Código");
		assertThat(productoEncontrado.getId()).isEqualTo(productoGuardado.getId());
	}

	@Test
	@DisplayName("Debería generar código automáticamente cuando no se proporciona")
	void deberiaGenerarCodigoAutomaticamente() {
		// Given - Crear producto sin código
		Producto producto = new Producto();
		producto.setNombre("Producto Sin Código");
		producto.setDescripcion("Producto para verificar generación automática");
		producto.setPrecio(250000.0);
		producto.setStock(15);

		// When - Guardar el producto
		Producto productoGuardado = productoService.crear(producto);

		// Then - Verificar que se generó un código automáticamente
		assertThat(productoGuardado).isNotNull();
		assertThat(productoGuardado.getCodigo()).isNotNull();
		assertThat(productoGuardado.getCodigo()).isNotBlank();
		assertThat(productoGuardado.getCodigo()).startsWith("P");
		assertThat(productoGuardado.getCodigo()).hasSize(7); // P + 6 caracteres
	}

	@Test
	@DisplayName("No debería permitir crear productos con código duplicado")
	void noDeberiaPermitirCodigosDuplicados() {
		// Given - Crear primer producto
		Producto producto1 = new Producto();
		producto1.setCodigo("PDUP001");
		producto1.setNombre("Producto Original");
		producto1.setPrecio(400000.0);
		producto1.setStock(5);

		productoService.crear(producto1);

		// When - Intentar crear producto con mismo código
		Producto producto2 = new Producto();
		producto2.setCodigo("PDUP001");
		producto2.setNombre("Producto Duplicado");
		producto2.setPrecio(500000.0);
		producto2.setStock(10);

		// Then - Debería lanzar excepción
		assertThat(org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
			productoService.crear(producto2);
		})).hasMessageContaining("Ya existe un producto con el código: PDUP001");
	}

	@Test
	@DisplayName("Debería retornar null al buscar producto inexistente por ID")
	void deberiaRetornarNullParaProductoInexistentePorId() {
		// When - Buscar producto con ID que no existe
		Producto productoInexistente = productoService.obtenerPorId(99999L);

		// Then - Debería retornar null
		assertThat(productoInexistente).isNull();
	}

	@Test
	@DisplayName("Debería retornar null al buscar producto inexistente por código")
	void deberiaRetornarNullParaProductoInexistentePorCodigo() {
		// When - Buscar producto con código que no existe
		Producto productoInexistente = productoService.obtenerPorCodigo("PNOEXISTE");

		// Then - Debería retornar null
		assertThat(productoInexistente).isNull();
	}

	@Test
	@DisplayName("Debería actualizar producto usando código en lugar de ID")
	void deberiaActualizarProductoPorCodigo() {
		// Given - Crear un producto inicial
		Producto producto = new Producto();
		producto.setCodigo("PUPDATE1");
		producto.setNombre("Producto Original");
		producto.setDescripcion("Descripción original");
		producto.setPrecio(350000.0);
		producto.setStock(8);

		Producto productoGuardado = productoService.crear(producto);

		// When - Actualizar usando código
		Producto productoParaActualizar = new Producto();
		productoParaActualizar.setNombre("Producto Actualizado por Código");
		productoParaActualizar.setDescripcion("Descripción actualizada");
		productoParaActualizar.setPrecio(450000.0);
		productoParaActualizar.setStock(12);

		Producto productoActualizado = productoService.actualizar("PUPDATE1", productoParaActualizar);

		// Then - Verificar la actualización
		assertThat(productoActualizado).isNotNull();
		assertThat(productoActualizado.getId()).isEqualTo(productoGuardado.getId());
		assertThat(productoActualizado.getCodigo()).isEqualTo("PUPDATE1");
		assertThat(productoActualizado.getNombre()).isEqualTo("Producto Actualizado por Código");
		assertThat(productoActualizado.getPrecio()).isEqualTo(450000.0);
	}

	@Test
	@DisplayName("Debería eliminar producto usando código en lugar de ID")
	void deberiaEliminarProductoPorCodigo() {
		// Given - Crear un producto para eliminar
		Producto producto = new Producto();
		producto.setCodigo("PDELETE1");
		producto.setNombre("Producto a Eliminar por Código");
		producto.setPrecio(200000.0);
		producto.setStock(3);

		productoService.crear(producto);

		// Verificar que el producto existe
		assertThat(productoService.obtenerPorCodigo("PDELETE1")).isNotNull();

		// When - Eliminar usando código
		productoService.eliminar("PDELETE1");

		// Then - Verificar que fue eliminado
		assertThat(productoService.obtenerPorCodigo("PDELETE1")).isNull();
	}

	@Test
	@DisplayName("CRUD Completo: Crear, Leer, Actualizar y Eliminar en secuencia")
	void deberiaCumplirCrudCompleto() {
		// CREATE - Crear producto
		Producto nuevoProducto = new Producto();
		nuevoProducto.setCodigo("PCRUD001");
		nuevoProducto.setNombre("Producto CRUD Test");
		nuevoProducto.setDescripcion("Producto para prueba CRUD completa");
		nuevoProducto.setPrecio(750000.0);
		nuevoProducto.setStock(20);

		Producto productoCreado = productoService.crear(nuevoProducto);
		assertThat(productoCreado).isNotNull();
		assertThat(productoCreado.getId()).isNotNull();
		assertThat(productoCreado.getCodigo()).isEqualTo("PCRUD001");

		// READ - Leer producto por ID
		Producto productoPorId = productoService.obtenerPorId(productoCreado.getId());
		assertThat(productoPorId).isNotNull();
		assertThat(productoPorId.getNombre()).isEqualTo("Producto CRUD Test");

		// READ - Leer producto por código
		Producto productoPorCodigo = productoService.obtenerPorCodigo("PCRUD001");
		assertThat(productoPorCodigo).isNotNull();
		assertThat(productoPorCodigo.getId()).isEqualTo(productoCreado.getId());

		// READ - Verificar que aparece en la lista de todos
		List<Producto> todosLosProductos = productoService.obtenerTodos();
		assertThat(todosLosProductos).anyMatch(p -> "PCRUD001".equals(p.getCodigo()));

		// UPDATE - Actualizar producto
		productoPorId.setNombre("Producto CRUD Actualizado");
		productoPorId.setPrecio(850000.0);
		productoPorId.setStock(25);

		Producto productoActualizado = productoService.actualizar(productoPorId.getId(), productoPorId);
		assertThat(productoActualizado).isNotNull();
		assertThat(productoActualizado.getNombre()).isEqualTo("Producto CRUD Actualizado");
		assertThat(productoActualizado.getPrecio()).isEqualTo(850000.0);

		// DELETE - Eliminar producto
		productoService.eliminar(productoActualizado.getId());

		// Verificar que fue eliminado
		assertThat(productoService.obtenerPorId(productoActualizado.getId())).isNull();
		assertThat(productoService.obtenerPorCodigo("PCRUD001")).isNull();
	}

}
