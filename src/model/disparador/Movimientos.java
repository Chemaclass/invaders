package model.disparador;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.awt.event.KeyEvent;
import java.util.Objects;

/**
 * Encargada de darle movimiento a las naves a través de los eventos    
 * @author chema
 */
@DatabaseTable(tableName = "movimientos")
public class Movimientos{
        
    //<editor-fold defaultstate="collapsed" desc="fields">
    public static final String ID = "id";
    public static final String IZQUIERDA = "izquierda";
    public static final String DERECHA = "derecha";
    public static final String ARRIBA = "arriba";
    public static final String ABAJO = "abajo";
    public static final String DISPARAR = "disparar";
    public static final String RECARGAR = "recargar";
    public static final String CORRER = "correr";    
    //</editor-fold>    
    @DatabaseField(id = true, columnName = ID)
    private int id; 
    @DatabaseField(columnName = IZQUIERDA, foreign = true, foreignAutoCreate = true,foreignAutoRefresh = true)
    private Movimiento izq;
    @DatabaseField(columnName = ARRIBA, foreign = true, foreignAutoCreate = true,foreignAutoRefresh = true)
    private Movimiento arr;
    @DatabaseField(columnName = DERECHA, foreign = true, foreignAutoCreate = true,foreignAutoRefresh = true)
    private Movimiento der;
    @DatabaseField(columnName = ABAJO, foreign = true, foreignAutoCreate = true,foreignAutoRefresh = true)
    private Movimiento aba;
    @DatabaseField(columnName = CORRER, foreign = true, foreignAutoCreate = true,foreignAutoRefresh = true)
    private Movimiento corre;
    @DatabaseField(columnName = DISPARAR, foreign = true, foreignAutoCreate = true,foreignAutoRefresh = true)
    private Movimiento disparar;
    @DatabaseField(columnName = RECARGAR, foreign = true, foreignAutoCreate = true,foreignAutoRefresh = true)
    private Movimiento recargar;
    
    private static Movimientos movimientos1;
    private static Movimientos movimientos2;
    private static Movimientos movimientos3;
    private static Movimientos movimientos4;
    static{
        int izq =  KeyEvent.VK_A, arr = KeyEvent.VK_W, 
            der = KeyEvent.VK_D, aba = KeyEvent.VK_S,
            corre = KeyEvent.VK_SHIFT,disparar = KeyEvent.VK_F, 
            recargar = KeyEvent.VK_G;
        movimientos1 = new Movimientos(1, izq, arr, der, aba, disparar, recargar, corre);
                izq = KeyEvent.VK_H; arr = KeyEvent.VK_U;
            der = KeyEvent.VK_K; aba = KeyEvent.VK_J;
            corre = KeyEvent.VK_B; disparar = KeyEvent.VK_I; 
            recargar = KeyEvent.VK_O;
        movimientos2 = new Movimientos(2, izq, arr, der, aba, disparar, recargar, corre);
        izq = KeyEvent.VK_LEFT; arr = KeyEvent.VK_UP;
            der = KeyEvent.VK_RIGHT; aba = KeyEvent.VK_DOWN;
            corre = KeyEvent.VK_CONTROL; disparar = KeyEvent.VK_NUMPAD1; 
            recargar = KeyEvent.VK_NUMPAD2;            
        movimientos3 = new Movimientos(3, izq, arr, der, aba, disparar, recargar, corre);
        izq = KeyEvent.VK_A; arr = KeyEvent.VK_W;
            der = KeyEvent.VK_D; aba = KeyEvent.VK_S;
            corre = KeyEvent.VK_SPACE; disparar = KeyEvent.VK_F; 
            recargar = KeyEvent.VK_R;            
        movimientos4 = new Movimientos(4, izq, arr, der, aba, disparar, recargar, corre);
    }
    
    /**
     * Obtener todos los movimientos que hay
     * @return todos los movimientos en un array Movimientos [] 
     */
    public static Movimientos[] getMovimientos(){        
        Movimientos [] m = {movimientos1,movimientos2,movimientos3,movimientos4};
        return m;
    }
    
    /**
     * Devuelve el tipo de movimientos que se quiere. 
     * Hay 3 tipos de movimientos
     * @param tipo número que indicará el tipo de movimientos
     * @return movimientos especificado por el parámetro
     */
    public static Movimientos getInstance(int tipo){
        switch(tipo){
            case 1: return movimientos1;
            case 2: return movimientos2;
            case 3: return movimientos3;
            case 4: return movimientos4;
        }
        return null;
    }
    
    public Movimientos(){}
    
    private Movimientos(int id,int izq, int arr,int der,int aba,int disparar,int recargar,int corre){
        this.id = id;
        this.izq = new Movimiento(izq);
        this.der = new Movimiento(der);
        this.arr = new Movimiento(arr);
        this.aba = new Movimiento(aba);
        this.corre = new Movimiento(corre);
        this.disparar = new Movimiento(disparar);
        this.recargar = new Movimiento(recargar);      
    }
        
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters & toString">
        @Override
        public String toString() {
            return "Movimiento " + id;
        }
//    @Override
//    public String toString() {
//        return "Movimientos{" + "id=" + id + ", izq=" + izq + ", arr=" + arr + ", der=" + der + ", aba=" + aba + ", corre=" + corre + ", disparar=" + disparar + ", recargar=" + recargar + '}';
//    }

    
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + this.id;
        hash = 23 * hash + Objects.hashCode(this.izq);
        hash = 23 * hash + Objects.hashCode(this.arr);
        hash = 23 * hash + Objects.hashCode(this.der);
        hash = 23 * hash + Objects.hashCode(this.aba);
        hash = 23 * hash + Objects.hashCode(this.corre);
        hash = 23 * hash + Objects.hashCode(this.disparar);
        hash = 23 * hash + Objects.hashCode(this.recargar);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Movimientos other = (Movimientos) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
    
    
    public int getId() {
        return id;
    }

    public Movimiento getIzq() {
        return izq;
    }

    public Movimiento getArr() {
        return arr;
    }

    public Movimiento getDer() {
        return der;
    }

    public Movimiento getAba() {
        return aba;
    }

    public Movimiento getCorre() {
        return corre;
    }

    public Movimiento getDisparar() {
        return disparar;
    }

    public Movimiento getRecargar() {
        return recargar;
    }   

    public void setIzq(Movimiento izq) {
        this.izq = izq;
    }

    public void setArr(Movimiento arr) {
        this.arr = arr;
    }

    public void setDer(Movimiento der) {
        this.der = der;
    }

    public void setAba(Movimiento aba) {
        this.aba = aba;
    }

    public void setCorre(Movimiento corre) {
        this.corre = corre;
    }

    public void setDisparar(Movimiento disparar) {
        this.disparar = disparar;
    }

    public void setRecargar(Movimiento recargar) {
        this.recargar = recargar;
    }
    
    //</editor-fold> 
    
    @DatabaseTable(tableName = "movimiento")
    public static class Movimiento{ 
        
        public static final String MOV_CODE = "mov_code";
        public static final String NOM_TECLA = "nom_tecla";
        @DatabaseField(columnName = ID, generatedId = true)
        private int id; 
        @DatabaseField(columnName = MOV_CODE)
        private int code;
        @DatabaseField(columnName = NOM_TECLA)
        private String nombreTecla;
        
        public Movimiento(){}
        
        private Movimiento(int mov_code){
            this.code =mov_code;
            this.nombreTecla = KeyEvent.getKeyText(mov_code);
        }
        public int getId() {
            return id;
        }

        public int getCode() {
            return code;
        }
        public String getNombreTecla() {
            return nombreTecla;
        }        
        
        @Override
        public String toString() {
            return  nombreTecla;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 53 * hash + this.id;
            hash = 53 * hash + this.code;
            hash = 53 * hash + Objects.hashCode(this.nombreTecla);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Movimiento other = (Movimiento) obj;
            if (this.id != other.id) {
                return false;
            }
            return true;
        }        
        
    }
    
}