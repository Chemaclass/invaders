package controller;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import model.Escudo;
import model.disparador.Invasor;
import model.disparador.Nave;

/**
 *
 * @author chema
 */
@DatabaseTable(tableName = "juegos")
public class Juego {
   
    
    /**
     * Dificultad del Juego.
     * Fácil, Medio, Pro 
     */
    public enum Dificultad{
        FACIL(1),MEDIO(2), PRO(3);        
        public final int VALUE;
        private Dificultad(int value){ 
            this.VALUE=value;
        }
        public static Dificultad getDificultadById(int d) {
            if(d == FACIL.VALUE)return FACIL;
            if(d == PRO.VALUE)return PRO;
            return MEDIO;
        }
    }
    //<editor-fold defaultstate="collapsed" desc="fields">    
    public static final String NOMBRE = "nombre";
    public static final String PUNTOS = "puntos";
    public static final String NIVEL = "nivel";
    public static final String N_NAVES = "n_naves";
    public static final String N_FILAS = "n_filas";
    public static final String N_COLUM = "n_colum";
    public static final String DIFICULTAD_ID = "dificultad_id";
    public static final String IS_SONIDO_FONDO = "is_sonido_fondo";
    public static final String IS_SONIDO_EFECTOS = "is_sonido_efectos";
    public static final String SOUND_FONDO_URL = "sound_fondo_url";
    public static final String SOUND_EFECTOS_URL = "sound_efectos_url";
    
    //</editor-fold>
    @DatabaseField(generatedId = true)
    private int id=0;
    @DatabaseField(columnName = NOMBRE, canBeNull = false)
    private String nombre;  
    @DatabaseField(columnName = PUNTOS)
    private int puntos;
    @DatabaseField(columnName = NIVEL)
    private int nivel;
    @DatabaseField(columnName = N_NAVES)
    private int nNaves;
    @DatabaseField(columnName = N_FILAS)
    private int nFilas;
    @DatabaseField(columnName = N_COLUM)
    private int  nColum;
    @DatabaseField(columnName = DIFICULTAD_ID)
    private int dificultad = Dificultad.MEDIO.VALUE;    
    @ForeignCollectionField
    private ForeignCollection<Escudo> escudos;
    @ForeignCollectionField
    private ForeignCollection<Nave> naves;
    @ForeignCollectionField
    private ForeignCollection<Invasor> invasores;    
    
    
//    @DatabaseField(columnName = IS_SONIDO_FONDO)
//    private boolean sonidoFondo;
//    @DatabaseField(columnName = IS_SONIDO_EFECTOS)
//    private boolean sonidoEfectos;
//    @DatabaseField(columnName = SOUND_FONDO_URL)
//    private String audioClipFondoUrl;    
//    private AudioClip audioClipFondo;
    
    public Juego(){}

    /**
     * Crear un nuevo juego con los valores por default
     * @param nombre 
     */
    public Juego(String nombre) {
        this.nombre = nombre;
        this.puntos = 0;
        this.nivel = 1;
        this.nNaves = 0;
        this.nFilas = 2;
        this.nColum = 7;
//        this.sonidoFondo = false;
//        this.sonidoEfectos = false;
//        audioClipFondoUrl = "fondoC.wav";
    }
    
//    public void cargarSonidos(){
//         try { 
//             this.audioClipFondo = Applet.newAudioClip(Sound.class.getResource(audioClipFondoUrl));
//        } catch (Exception ex) { 
//            ex.printStackTrace();
//        } 
//    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters & toString">
       
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.id;
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
        final Juego other = (Juego) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }   
        
    @Override
    public String toString() {
        return "id: " + id + ", nombre: " + nombre + ", puntos: " + puntos 
                + ", nivel: " + nivel + ", naves: " + nNaves 
                + ", invasores(" + nFilas + "F, " + nColum + "C)"
                + ", dificultad: " + dificultad ;
    }
    
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getnNaves() {
        return nNaves;
    }

    public void setnNaves(int nNaves) {
        this.nNaves = nNaves;
    }

    public int getnFilas() {
        return nFilas;
    }

    public void setnFilas(int nFilas) {
        this.nFilas = nFilas;
    }

    public int getnColum() {
        return nColum;
    }

    public void setnColum(int nColum) {
        this.nColum = nColum;
    }

    public Dificultad getDificultad() {
        return Dificultad.getDificultadById(dificultad);
    }
    
    public void setDificultadId(int newDificultad) {
        this.dificultad = newDificultad;
    }

    public ForeignCollection<Escudo> getEscudos() {
        return escudos;
    }

    public void setEscudos(ForeignCollection<Escudo> escudos) {
        this.escudos = escudos;
    }

    public ForeignCollection<Nave> getNaves() {
        return naves;
    }

    public void setNaves(ForeignCollection<Nave> naves) {
        this.naves = naves;
    }        

    public ForeignCollection<Invasor> getInvasores() {
        return invasores;
    }

    public void setInvasores(ForeignCollection<Invasor> invasores) {
        this.invasores = invasores;
    }
    
    //</editor-fold>  
}
//</editor-fold 