package tmsva.org.free.planificate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BipRs {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("estadoContrato")
    @Expose
    private String estadoContrato;
    @SerializedName("saldoTarjeta")
    @Expose
    private String saldoTarjeta;
    @SerializedName("fechaSaldo")
    @Expose
    private String fechaSaldo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstadoContrato() {
        return estadoContrato;
    }

    public void setEstadoContrato(String estadoContrato) {
        this.estadoContrato = estadoContrato;
    }

    public String getSaldoTarjeta() {
        return saldoTarjeta;
    }

    public void setSaldoTarjeta(String saldoTarjeta) {
        this.saldoTarjeta = saldoTarjeta;
    }

    public String getFechaSaldo() {
        return fechaSaldo;
    }

    public void setFechaSaldo(String fechaSaldo) {
        this.fechaSaldo = fechaSaldo;
    }

    @Override
    public String toString() {
        return "BipRs{" +
                "id='" + id + '\'' +
                ", estadoContrato='" + estadoContrato + '\'' +
                ", saldoTarjeta='" + saldoTarjeta + '\'' +
                ", fechaSaldo='" + fechaSaldo + '\'' +
                '}';
    }
}
