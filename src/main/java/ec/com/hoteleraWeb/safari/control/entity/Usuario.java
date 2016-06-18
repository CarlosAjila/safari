package ec.com.hoteleraWeb.safari.control.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ec.com.hoteleraWeb.safari.seguridad.entity.Bitacora;
import ec.com.hoteleraWeb.safari.seguridad.entity.RolUsuario;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	@Id
	@SequenceGenerator(allocationSize = 1, name = "usuario_usu_id_seq", sequenceName = "usuario_usu_id_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_usu_id_seq")
	@Column(name = "usu_id")
	private Integer usuId;

	@Column(name = "usu_activo")
	private Boolean usuActivo;

	@Column(name = "usu_nick")
	private String usuNick;

	@Column(name = "usu_nombre")
	private String usuNombre;

	@Column(name = "usu_password")
	private String usuPassword;

	// bi-directional many-to-one association to RolUsuario
	@OneToMany(mappedBy = "usuario")
	private List<RolUsuario> rolUsuarios;

	// bi-directional many-to-one association to Bitacora
	@OneToMany(mappedBy = "bitUsuario")
	private List<Bitacora> bitacoras;

	// bi-directional many-to-one association to Hotel
	@ManyToOne
	@JoinColumn(name = "hot_codigo")
	private Hotel hotel;

	public Usuario() {
	}
	
	

	public Usuario(Integer usuId, Boolean usuActivo, String usuNick, String usuNombre, String usuPassword,
			List<RolUsuario> rolUsuarios, List<Bitacora> bitacoras, Hotel hotel) {
		this.usuId = usuId;
		this.usuActivo = usuActivo;
		this.usuNick = usuNick;
		this.usuNombre = usuNombre;
		this.usuPassword = usuPassword;
		this.rolUsuarios = rolUsuarios;
		this.bitacoras = bitacoras;
		this.hotel = hotel;
	}



	public Integer getUsuId() {
		return this.usuId;
	}

	public void setUsuId(Integer usuId) {
		this.usuId = usuId;
	}

	public Boolean getUsuActivo() {
		return this.usuActivo;
	}

	public void setUsuActivo(Boolean usuActivo) {
		this.usuActivo = usuActivo;
	}

	public String getUsuNick() {
		return this.usuNick;
	}

	public void setUsuNick(String usuNick) {
		this.usuNick = usuNick;
	}

	public String getUsuNombre() {
		return this.usuNombre;
	}

	public void setUsuNombre(String usuNombre) {
		this.usuNombre = usuNombre;
	}

	public String getUsuPassword() {
		return this.usuPassword;
	}

	public void setUsuPassword(String usuPassword) {
		this.usuPassword = usuPassword;
	}

	public List<RolUsuario> getRolUsuarios() {
		return this.rolUsuarios;
	}

	public void setRolUsuarios(List<RolUsuario> rolUsuarios) {
		this.rolUsuarios = rolUsuarios;
	}

	public RolUsuario addRolUsuario(RolUsuario rolUsuario) {
		getRolUsuarios().add(rolUsuario);
		rolUsuario.setUsuario(this);
		return rolUsuario;
	}

	public RolUsuario removeRolUsuario(RolUsuario rolUsuario) {
		getRolUsuarios().remove(rolUsuario);
		rolUsuario.setUsuario(null);
		return rolUsuario;
	}

	public Hotel getHotel() {
		return this.hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public List<Bitacora> getBitacoras() {
		return bitacoras;
	}

	public void setBitacoras(List<Bitacora> bitacoras) {
		this.bitacoras = bitacoras;
	}

	public Bitacora removeBitacora(Bitacora bitacora) {
		getBitacoras().remove(bitacora);
		bitacora.setBitUsuario(null);

		return bitacora;
	}

	public Bitacora addBitacora(Bitacora bitacora) {
		getBitacoras().add(bitacora);
		bitacora.setBitUsuario(this);
		return bitacora;
	}

}