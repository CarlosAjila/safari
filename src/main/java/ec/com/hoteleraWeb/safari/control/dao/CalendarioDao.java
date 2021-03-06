package ec.com.hoteleraWeb.safari.control.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.hoteleraWeb.safari.control.entity.Calendario;
import ec.com.hoteleraWeb.safari.utils.dao.GenericDao;

public interface CalendarioDao extends GenericDao<Calendario, Integer> {
	@Transactional
	public List<Calendario> obtenerTodos();

}
