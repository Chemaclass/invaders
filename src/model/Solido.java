package model;

import com.j256.ormlite.field.DatabaseField;
import controller.Juego;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author chema
 */
public abstract class Solido{
    
    //<editor-fold defaultstate="collapsed" desc="fields">
    public static final String JUEGO_ID = "juego_id";   
    public static final String PROPIEDADES_ID = "propiedades_solido_id"; 
    public static final String NOMBRE = "nombre";
    public static final String IMG_ACT_URL = "img_act_url";
    public static final String SOUND_BREAK_URL = "sound_break_url";
    public static final String X = "x";
    public static final String Y = "y";
    public static final String XV = "xv";
    public static final String YV = "yv";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String SALUD = "salud";
    public static final String SALUD_TOTAL = "salud_total";
    public static final String FUERZA = "fuerza";
    public static final String VELOCIDAD = "velocidad";
    public static final String VIDAS = "vidas";
    public static final String MUNICION = "muncion";
    public static final String PUNTOS = "puntos";
    public static final String MUNICION_MAX = "municion_max";
    public static final String RELLENO_TOTAL = "relleno_total";
    public static final String ESTADO = "estado";
    public static final String SOLIDO_ID = "solido_id";  
    //</editor-fold>
    @DatabaseField(generatedId = true, columnName = SOLIDO_ID)
    protected int id;
    @DatabaseField(foreign = true, columnName = JUEGO_ID)
    protected Juego juego;
    @DatabaseField(columnName = NOMBRE, canBeNull = false)
    protected String nombre;    
    @DatabaseField(columnName = IMG_ACT_URL)
    protected String imgActUrl;
    // La imagen la cargaremos una vez que estemos jugando.
    // En la DB guardaremos la ruta donde se encuentra.
    protected ImageIcon imgAct;
    @DatabaseField(columnName = X)
    protected int x;
    @DatabaseField(columnName = Y)
    protected int y; 
    @DatabaseField(columnName = XV)
    protected int xv;
    @DatabaseField(columnName = YV)
    protected int yv;
    @DatabaseField(columnName = WIDTH)
    protected int width;
    @DatabaseField(columnName = HEIGHT)
    protected int height; 
    @DatabaseField(columnName = SALUD)
    protected int salud;
    @DatabaseField(columnName = SALUD_TOTAL)
    protected int saludTotal;
    @DatabaseField(columnName = FUERZA)
    protected int fuerza;
    @DatabaseField(columnName = VELOCIDAD)
    protected int velocidad;
    @DatabaseField(columnName = VIDAS)
    protected int vidas;   
    @DatabaseField(columnName = RELLENO_TOTAL)
    protected int rellenoTotal;    
    @DatabaseField(columnName = MUNICION)
    protected int municion;
    @DatabaseField(columnName = MUNICION_MAX)
    protected int municionMax;
    @DatabaseField(columnName = PUNTOS)
    protected int puntos;
    
    public Solido() {}

    /**
     * Creamos un nuevo sólido
     * @param juego Juego al que pertenece el sólido
     * @param imgUrlAct URL de la imagen del sólido
     */
    public Solido(Juego juego,String imgUrlAct) {
        this.juego = juego;
        this.imgActUrl = imgUrlAct;
    }
    
    /**
     * Cargamos las imágenes del sólido
     */
    public void cargarImagenes(){
        try {
             this.imgAct = new ImageIcon(ImageIO.read(getClass()
                     .getClassLoader().getResource("images/"+imgActUrl+".png")));
        } catch (Exception ex) { 
            ex.printStackTrace();
        } 
    }
        
    /**
     * Mover sólido
     * @return boolean estado (true:vivo || false:muerto)
     */
    public abstract int mover();
        
    /**
     * Obtener tamaño
     * @return Obtenemos el rectándulo que formaría el sólido 
     * a través de sus coordenadas
     */
    public Rectangle getBounds() {   
        return new Rectangle(x, y, width, height);
    }
    
    /**
     * Actualizar su salud actual.
     * @param cant int Cantidad a incrementar/decrementar de salud
     * @return Si la salud es positiva true  (vivo)
     *         Si la salud es negativa false (muerto)
     */
    public void actualizarResistencia(int cant){        
        //nos aseguramos que la salud no sea superior a su salud actual
        if(salud+cant<=saludTotal){
            
            salud += cant; //actualizamos su salud
            
            if( salud < 0){ //si su salud es positiva               
               
                //si su salud es negativa
                if(vidas > 1){//si tiene aún vidas
                    restaurar(); //se regenera
                    vidas--; //y pierde una vida
                } 
            }
        }else salud = saludTotal;        
    } 
    
    /**
     * Restaurar Solido. 
     * Pone su estado a true (vivo) 
     * y completa su salud al máximo (reistenciaTotal)
     */
    public void restaurar(){
        salud = saludTotal;
    }
    
    /**
     * Pinta el Solido
     * @param g2d 
     */
    public void paint(Graphics2D g2d){
        //si no está muerto
        if(salud > 0){ 
            Color color;
            if(salud < 10 && salud!=saludTotal) color = Color.CYAN;
            else if(salud < 5) color = Color.RED;
            else color = Color.GREEN;
            //pintamos la imagen y su salud  
            if(imgAct != null)
                g2d.drawImage(imgAct.getImage(), x,y,width,height, null);
            
            g2d.setColor(Color.BLACK);//resistencia contorno
            g2d.drawRect(x,y-10,rellenoTotal,2); 
            g2d.setColor(color);//resistencia total
            int relleno = (width*salud)/saludTotal;
            g2d.fillRect(x, y-10, relleno, 2);//vida total
        } 
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters & toString">
    public boolean isVivo(){
        return salud > 0;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getXv() {
        return xv;
    }

    public void setXv(int xv) {
        this.xv = xv;
    }

    public int getYv() {
        return yv;
    }

    public void setYv(int yv) {
        this.yv = yv;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSalud() {
        return salud;
    }

    public void setSalud(int salud) {
        this.salud = salud;
    }

    public int getSaludTotal() {
        return saludTotal;
    }

    public void setSaludTotal(int saludTotal) {
        this.saludTotal = saludTotal;
    }

    public int getFuerza() {
        return fuerza;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public int getMunicion() {
        return municion;
    }

    public void setMunicion(int municion) {
        this.municion = municion;
    }

    public int getMunicionMax() {
        return municionMax;
    }

    public void setMunicionMax(int municionMax) {
        this.municionMax = municionMax;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getRellenoTotal() {
        return rellenoTotal;
    }

    public void setRellenoTotal(int rellenoTotal) {
        this.rellenoTotal = rellenoTotal;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    } 

    public String getImgActUrl() {
        return imgActUrl;
    }

    public void setImgActUrl(String imgActUrl) {
        this.imgActUrl = imgActUrl;
    }        
    
    @Override
    public String toString() {
        return "Solido{" + "id=" + id + ", juego=" + juego 
                + ", nombre=" + nombre + ", x=" + x + ", y=" + y + ", xv=" + xv 
                + ", yv=" + yv + ", width=" + width + ", height=" + height 
                + ", resistencia=" + salud + ", resistenciaTotal=" + saludTotal 
                +", velocidad="+velocidad+ ", fuerza=" + fuerza + ", vidas=" + vidas 
                + ", rellenoTotal=" + rellenoTotal 
                + ", municion=" + municion + ", municionMax=" + municionMax 
                + ", puntos=" + puntos + '}';
    } 
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        hash = 67 * hash + Objects.hashCode(this.juego);
        hash = 67 * hash + Objects.hashCode(this.nombre);
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
        final Solido other = (Solido) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.juego, other.juego)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }
    //</editor-fold>
}
 //</editor-fold