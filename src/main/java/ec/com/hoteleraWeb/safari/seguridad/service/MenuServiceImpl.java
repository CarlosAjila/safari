package ec.com.hoteleraWeb.safari.seguridad.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ec.com.hoteleraWeb.safari.seguridad.dao.MenuDao;
import ec.com.hoteleraWeb.safari.seguridad.entity.Menu;

@Service
public class MenuServiceImpl implements MenuService, Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private MenuDao menuDao;

	public String actualizar(Menu menu) {
		menuDao.actualizar(menu);
		return "SAVE";
	}

	public void eliminar(int menuId) {
		menuDao.eliminarPorId(Menu.class, menuId);
	}

	public String insertar(Menu menu) {
		menuDao.insertar(menu);
		return "SAVE";
	}

	public List<Menu> obtener() {
		return menuDao.obtener(Menu.class, "orden", null);
	}

	public List<Menu> obtenerPorUsuario(String nick) {
		return menuDao.obtenerPorHql(
				"select distinct m from Menu m " + "inner join m.rolMenus rm inner join rm.rol r "
						+ "inner join r.rolUsuarios ru inner join ru.usuario u "
						+ "where ru.rolUsulAtivo=true and m.menVisible=true and u.usuNick=?1 order by m.menId",
				new Object[] { nick });
	}
}