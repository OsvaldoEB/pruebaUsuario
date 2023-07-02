package com.example.prueba.controller;

import com.example.prueba.usuario.controller.UsuarioController;
import com.example.prueba.usuario.model.Usuario;
import com.example.prueba.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void crearUsuario_DebeRetornarUsuarioCreado() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNombre("Osvaldo");
        usuario.setApellido("Escamilla");
        usuario.setCorreoElectronico("oescamilla@gmail.com");
        usuario.setFechaNacimiento(LocalDate.of(1997, 11, 1));

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        ResponseEntity<Usuario> response = usuarioController.crearUsuario(usuario);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    public void obtenerUsuario_Existente_DebeRetornarUsuario() {
        // Arrange
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Osvaldo");
        usuario.setApellido("Escamilla");
        usuario.setCorreoElectronico("oescamilla@gmail.com");
        usuario.setFechaNacimiento(LocalDate.of(1997, 11, 1));

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // Act
        ResponseEntity<Usuario> response = usuarioController.obtenerUsuario(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuario, response.getBody());
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    public void obtenerUsuario_NoExistente_DebeRetornarNotFound() {
        // Arrange
        Long id = 1L;

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Usuario> response = usuarioController.obtenerUsuario(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    public void obtenerUsuarios_DebeRetornarListaDeUsuarios() {
        // Arrange
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario(1L, "Osvaldo", "Escamilla", "oescamilla@gmail.com", LocalDate.of(1997, 11, 1)));
        usuarios.add(new Usuario(2L, "Jael", "Barrera", "jbarrera@gmail.com", LocalDate.of(1995, 11, 1)));
        Page<Usuario> paginaUsuarios = new PageImpl<>(usuarios);

        Pageable pageable = mock(Pageable.class);

        when(usuarioRepository.findAll(pageable)).thenReturn(paginaUsuarios);

        // Act
        ResponseEntity<Page<Usuario>> response = usuarioController.obtenerUsuarios(pageable);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paginaUsuarios, response.getBody());
        verify(usuarioRepository, times(1)).findAll(pageable);
    }

    @Test
    public void actualizarUsuario_Existente_DebeRetornarUsuarioActualizado() {
        // Arrange
        Long id = 1L;
        Usuario usuarioExistente = new Usuario(id, "Osvaldo", "Escamilla", "oescamilla@gmail.com", LocalDate.of(1997, 11, 1));
        Usuario usuarioActualizado = new Usuario(id, "Jael", "Barrera", "jbarrera@gmail.com", LocalDate.of(1995, 11, 1));

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // Act
        ResponseEntity<Usuario> response = usuarioController.actualizarUsuario(id, usuarioActualizado);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioActualizado, response.getBody());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).save(usuarioExistente);
    }

    @Test
    public void actualizarUsuario_NoExistente_DebeRetornarNotFound() {
        // Arrange
        Long id = 1L;
        Usuario usuarioActualizado = new Usuario(id, "Jael", "Sanchez", "jsanchez@gmail.com", LocalDate.of(1997, 1, 1));

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Usuario> response = usuarioController.actualizarUsuario(id, usuarioActualizado);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    public void eliminarUsuario_Existente_DebeRetornarOk() {
        // Arrange
        Long id = 1L;
        Usuario usuarioExistente = new Usuario(id, "Jael", "Sanchez", "jsanchez@gmail.com", LocalDate.of(1997, 1, 1));

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuarioExistente));

        // Act
        ResponseEntity<Object> response = usuarioController.eliminarUsuario(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(usuarioRepository, times(1)).findById(id);
        verify(usuarioRepository, times(1)).delete(usuarioExistente);
    }


}
