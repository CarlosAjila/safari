package ec.com.hoteleraWeb.safari.control.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "suplemento")
public class Suplemento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(allocationSize = 1, name = "suplemento_sup_codigo_seq", sequenceName = "suplemento_sup_codigo_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suplemento_sup_codigo_seq")
	@Column(name = "sup_codigo")
	private Integer supCodigo;

	@Column(name = "sup_activo")
	private Boolean supActivo;

	@Column(name = "sup_detalle")
	private String supDetalle;

	@Column(name = "sup_temporada")
	private Boolean supTemporada;

	@Column(name = "sup_valor")
	private BigDecimal supValor;

	public Suplemento() {
	}

	public Integer getSupCodigo() {
		return this.supCodigo;
	}

	public void setSupCodigo(Integer supCodigo) {
		this.supCodigo = supCodigo;
	}

	public Boolean getSupActivo() {
		return this.supActivo;
	}

	public void setSupActivo(Boolean supActivo) {
		this.supActivo = supActivo;
	}

	public String getSupDetalle() {
		return this.supDetalle;
	}

	public void setSupDetalle(String supDetalle) {
		this.supDetalle = supDetalle;
	}

	public Boolean getSupTemporada() {
		return this.supTemporada;
	}

	public void setSupTemporada(Boolean supTemporada) {
		this.supTemporada = supTemporada;
	}

	public BigDecimal getSupValor() {
		return this.supValor;
	}

	public void setSupValor(BigDecimal supValor) {
		this.supValor = supValor;
	}

}