package ec.com.hoteleraWeb.safari.control.service;

import static ec.com.hoteleraWeb.safari.utils.UtilsAplicacion.presentaMensaje;
import static ec.com.hoteleraWeb.safari.utils.UtilsAplicacion.redireccionar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ec.com.hoteleraWeb.safari.control.dao.UsuarioDao;
import ec.com.hoteleraWeb.safari.seguridad.entity.Rol;
import ec.com.hoteleraWeb.safari.seguridad.entity.RolUsuario;
import ec.com.hoteleraWeb.safari.seguridad.entity.Usuario;
import ec.com.hoteleraWeb.safari.seguridad.service.RolService;

@Service
public class UsuarioServiceImpl implements UsuarioService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private RolService rolService;

	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();

	public boolean actualizar(Usuario usuario) {
		boolean retorno = false;
		Set<ConstraintViolation<Usuario>> violationsUsuario = validator.validate(usuario);
		if (violationsUsuario.size() > 0)
			for (ConstraintViolation<Usuario> cv : violationsUsuario)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else if (usuarioDao.comprobarIndices(Usuario.class, "usu_nick", usuario.getUsuNick(),
				String.valueOf(usuario.getUsuId())))
			presentaMensaje(FacesMessage.SEVERITY_INFO, "LA CÉDULA YA EXISTE", "cerrar", false);
		else {
			usuario.setUsuPassword(generarClave(usuario.getUsuNick()));
			usuarioDao.actualizar(usuario);
			presentaMensaje(FacesMessage.SEVERITY_INFO, "CHOFER ACTUALIZADO", "cerrar", true);
			retorno = true;
		}
		return retorno;
	}

	public void cambiarClave(String claveActual, String clave1, String clave2) {
		Usuario usuario = obtenerActivoPorCedula(SecurityContextHolder.getContext().getAuthentication().getName());
		if (claveActual.length() == 0 || clave1.length() == 0 || clave2.length() == 0) {
			presentaMensaje(FacesMessage.SEVERITY_INFO, "INGRESE TODOS LOS DATOS REQUERIDOS");
		} else if (clave1.length() < 8 || clave2.length() < 8) {
			presentaMensaje(FacesMessage.SEVERITY_INFO,
					"LA NUEVA CONTRASEÑA DEBE TENER UNA LONGITUD MINIMA DE 8 CARACTERES");
		} else if (!compararClave(usuario.getUsuPassword(), generarClave(claveActual))) {
			presentaMensaje(FacesMessage.SEVERITY_INFO, "LA CONTRASEÑA ACTUAL ES INCORRECTA");
		} else if (!compararClave(clave1, clave2)) {
			presentaMensaje(FacesMessage.SEVERITY_INFO, "LAS CONTRASEÑAS NUEVAS NO COINCIDEN");
		} else if (compararClave(clave1, usuario.getUsuNick())) {
			presentaMensaje(FacesMessage.SEVERITY_INFO, "LA CONTRASEÑA NO PUEDE SER IGUAL AL USUARIO");
		} else {
			usuario.setUsuPassword(generarClave(clave1));
			usuarioDao.actualizar(usuario);
			presentaMensaje(FacesMessage.SEVERITY_INFO, "CAMBIO DE PASSWORD EXITOSO");
			redireccionar("../../logout.jsf");
		}
	}

	public boolean compararClave(String clave1, String clave2) {
		return clave1.compareTo(clave2) == 0 ? true : false;
	}

	public boolean comprobarRol(String cedula, String rol) {
		List<Usuario> usuario = null;
		usuario = usuarioDao.obtenerPorHql("select distinct c from Usuario c " + "left join fetch c.rolUsuarios ru "
				+ "left join fetch ru.rol r " + "where c.cedula=?1 and r.nombre=?2", new Object[] { cedula, rol });

		if (usuario.size() == 0)
			return false;
		else
			return true;
	}

	public Long contar() {
		return (Long) usuarioDao.contar(Usuario.class);
	}

	public void eliminar(Usuario usuario) {
		usuario.setUsuActivo(usuario.getUsuActivo() ? false : true);
		usuarioDao.actualizar(usuario);

		if (usuario.getUsuActivo())
			presentaMensaje(FacesMessage.SEVERITY_INFO, "SE ACTIVO AL USUARIO: " + usuario.getUsuNombre());
		else
			presentaMensaje(FacesMessage.SEVERITY_INFO, "SE DESACTIVO AL USUARIO: " + usuario.getUsuNombre());
	}

	public String generarClave(String clave) {
		ShaPasswordEncoder shaPasswordEncoder = new ShaPasswordEncoder(256);
		return shaPasswordEncoder.encodePassword(clave, null);
	}

	public Usuario insertarActualizar(Usuario usuario) {
		Set<ConstraintViolation<Usuario>> violationsUsuario = validator.validate(usuario);
		if (violationsUsuario.size() > 0)
			for (ConstraintViolation<Usuario> cv : violationsUsuario)
				presentaMensaje(FacesMessage.SEVERITY_INFO, cv.getMessage());
		else {
			boolean retorno = false;
			if (usuario.getUsuId() == null)
				retorno = insertar(usuario);
			else
				retorno = actualizar(usuario);

			if (retorno) {
				List<String> roles = new ArrayList<String>();
				roles.add("INVITADO");
				insertarRoles(usuario, roles);
				presentaMensaje(FacesMessage.SEVERITY_INFO, "CHOFER INSERTADO CORRECTAMENTE", "cerrar", true);
			}
		}
		return usuario;

	}

	public boolean insertar(Usuario usuario) {
		boolean retorno = false;
		if (usuarioDao.comprobarIndices(Usuario.class, "cedula", usuario.getUsuNick(),
				String.valueOf(usuario.getUsuId())))
			presentaMensaje(FacesMessage.SEVERITY_INFO, "LA CÉDULA YA EXISTE", "cerrar", false);
		else {
			usuario.setUsuActivo(true);
			usuario.setUsuPassword(generarClave(usuario.getUsuPassword()));
			usuarioDao.insertar(usuario);
			retorno = true;
		}
		return retorno;
	}

	public String insertarRoles(Usuario usuario, List<String> roles) {
		if (usuario.getRolUsuarios() == null)
			usuario.setRolUsuarios(new ArrayList<RolUsuario>());

		for (String r : roles) {
			RolUsuario rolUsuario = new RolUsuario();
			Rol rol = rolService.obtenerPorNombre(r);
			rolUsuario.setRol(rol);
			rolUsuario.setRolUsulAtivo(true);
			usuario.addRolUsuario(rolUsuario);
		}
		usuarioDao.actualizar(usuario);
		// }
		return "SAVE";
	}

	public List<Usuario> obtener(Boolean activo) {
		List<Usuario> lista = usuarioDao.obtenerPorHql("select c from Usuario c order by c.apellido, c.nombre",
				new Object[] {});
		return lista;
	}

	public Usuario obtenerActivoPorCedula(String cedula) {
		List<Usuario> usuario = usuarioDao.obtenerPorHql("select c from Usuario c where c.cedula=?1 and c.activo=true",
				new Object[] { cedula });
		if (usuario != null && usuario.size() == 1)
			return usuario.get(0);
		return null;
	}

	public Usuario obtenerPorCedula(String cedula) {
		List<Usuario> usuario = usuarioDao.obtenerPorHql("select c from Usuario c where c.cedula=?1 and c.activo=true",
				new Object[] { cedula });
		if (usuario != null)
			if (usuario.size() != 0)
				return usuario.get(0);

		return null;
	}

	public Usuario obtenerPorUsuarioId(Integer usuarioId) {
		Usuario usuario = usuarioDao.obtenerPorHql("select c from Usuario c " + "where c.id=?1 and c.activo=true",
				new Object[] { usuarioId }).get(0);
		return usuario;
	}

	public List<Usuario> obtenerTodosPorBusqueda(String criterioBusquedaUsuario) {
		List<Usuario> lista = null;
		criterioBusquedaUsuario = criterioBusquedaUsuario.toUpperCase();

		if (criterioBusquedaUsuario.compareToIgnoreCase("") == 0)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE UN CRITERIO DE BUSQUEDA");
		else {
			if (criterioBusquedaUsuario.length() >= 3) {
				if (criterioBusquedaUsuario.compareToIgnoreCase("") != 0)
					lista = usuarioDao.obtenerPorHql(
							"select distinct c from Usuario c "
									+ "where (c.cedula like ?1 or c.nombre like ?1 or c.apellido like ?1 or c.licencia like ?1 ) "
									+ "order by c.apellido, c.nombre",
							new Object[] { "%" + criterioBusquedaUsuario + "%" });
				if (lista.isEmpty())
					presentaMensaje(FacesMessage.SEVERITY_INFO, "NO SE ENCONTRO NINGUNA COINCIDENCIA");
			} else
				presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE MINIMO 3 CARACTERES");
		}
		return lista;
	}

	public List<String> obtenerListaUsuarioAutoComplete(String criterioUsuarioBusqueda) {
		List<String> list = new ArrayList<String>();
		List<Usuario> lista = obtenerUsuarios(criterioUsuarioBusqueda);
		if (!lista.isEmpty())
			for (Usuario c : lista)
				list.add(c.getUsuNick() + " - " + c.getUsuNombre());
		return list;
	}

	public List<Usuario> obtenerUsuarios(String criterioUsuarioBusqueda) {
		criterioUsuarioBusqueda = criterioUsuarioBusqueda.toUpperCase();
		List<Usuario> lista = null;
		if (criterioUsuarioBusqueda.length() >= 0 && criterioUsuarioBusqueda.length() < 3)
			presentaMensaje(FacesMessage.SEVERITY_ERROR, "INGRESE MAS DE 3 CARACTERES");
		else {
			lista = usuarioDao.obtenerPorHql(
					"select distinct p from Usuario p " + "where "
							+ "(p.cedula like ?1 or p.nombre like ?1 or p.apellido like ?1 ) " + "and p.activo=true",
					new Object[] { "%" + criterioUsuarioBusqueda + "%" });
			if (lista.isEmpty())
				presentaMensaje(FacesMessage.SEVERITY_WARN, "NO SE ENCONTRO NINGUNA COINCIDENCIA");
		}
		return lista;
	}

	public Usuario cargarUsuario(String usuario) {
		return obtenerPorCedula(usuario.split(" - ")[0]);
	}

}