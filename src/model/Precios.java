package model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author chema
 */
@DatabaseTable(tableName = "precios")
public class Precios{
    
    public static final String ID = "id";
//    public static final String NAVE_ID = "nave_id";
    public static final String FUERZA = "fuerza";
    public static final String RESISTENCIA = "resistencia";
    public static final String VIDAS = "vidas";
    public static final String MUNICION = "municion";
    
    @DatabaseField(columnName = ID, generatedId = true)
    private int id;      
//    @DatabaseField(columnName = NAVE_ID, foreign = true, index = true)
//    private Nave nave;
    @DatabaseField(columnName = FUERZA, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Precio fuerza;
    @DatabaseField(columnName = RESISTENCIA, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Precio resistencia;
    @DatabaseField(columnName = VIDAS, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Precio vidas;
    @DatabaseField(columnName = MUNICION, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true)
    private Precio municion;
    
//    public Precios(){}
    
    public Precios(){
//        this.nave = nave;
        fuerza = new Precio(Precios.FUERZA, 100, 5);
        resistencia = new Precio(Precios.RESISTENCIA, 150,7);
        vidas = new Precio(Precios.VIDAS, 200,10);
        municion = new Precio(Precios.MUNICION, 80,20);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters & toString">
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Precio getFuerza() {
        return fuerza;
    }

    public void setFuerza(Precio fuerza) {
        this.fuerza = fuerza;
    }

    public Precio getResistencia() {
        return resistencia;
    }

    public void setResistencia(Precio resistencia) {
        this.resistencia = resistencia;
    }

    public Precio getVidas() {
        return vidas;
    }

    public void setVidas(Precio vidas) {
        this.vidas = vidas;
    }

    public Precio getMunicion() {
        return municion;
    }

    public void setMunicion(Precio municion) {
        this.municion = municion;
    }    
    //</editor-fold>
    
    @DatabaseTable(tableName = "precio")
    public static class Precio{ 
        
        public static final String CLAVE = "clave";
        public static final String ACTUAL = "actual";
        public static final String INCREMENTO = "incremento";
        
        @DatabaseField(columnName = ID, generatedId = true)
        private int id; 
        @DatabaseField(columnName = CLAVE)
        private String clave;
        @DatabaseField(columnName = ACTUAL)
        private int actual;
        @DatabaseField(columnName = INCREMENTO)
        private int incremento;
        
        public Precio(){}
        
        public Precio(String clave, int actual, int incremento){
            this.clave = clave;
            this.actual =actual;
            this.incremento =incremento;
        }
        public int getId() {
            return id;
        }
        public int getActual() {
            return actual;
        }
        public int getIncremento(){
            return incremento;
        }   
        public String getClave() {
            return clave;
        }        
        public int incrementarActual() {
            int anterior = actual;
            actual += incremento;
            return anterior;
        }
    }
}