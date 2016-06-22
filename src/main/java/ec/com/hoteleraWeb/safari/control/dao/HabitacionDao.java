package ec.com.hoteleraWeb.safari.control.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import ec.com.hoteleraWeb.safari.control.entity.Habitacion;
import ec.com.hoteleraWeb.safari.utils.dao.GenericDao;

public interface HabitacionDao extends GenericDao<Habitacion, Integer> {

	@Transactional
	public List<Habitacion> obtenerTodos();

	@Transactional
	public Habitacion obtenerPorRuc(String ruc);

	@Transactional
	public Habitacion obtenerPorRuc_Codigo(String ruc, Integer codigo);

}
