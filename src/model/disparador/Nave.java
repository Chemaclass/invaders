package model.disparador;

import model.Precios;
import model.Movimientos;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import controller.Juego;
import controller.Partida;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import model.Precios.Precio;

/**
 *
 * @author chema
 */
@DatabaseTable(tableName = "naves")
public class Nave extends Disparador  {
    
    @DatabaseField(columnName = "movimientos_id", foreign = true, canBeNull = false)
    private Movimientos movimientos; 
    @DatabaseField(columnName = "precios_id", foreign = true, canBeNull = false
            , foreignAutoCreate = true
            , foreignAutoRefresh = true
            )
    private Precios precios;
    
    private boolean isCorre = false;
    private final int MUNICION_PARA_RECARGAR = 5;
    
    public Nave(){}
    
    public Nave(Juego j,String nombre,Movimientos mov){
        super(j,"naves/nave");
        this.nombre= nombre; 
        this.movimientos = mov; 
        precios = new Precios();
        width = 80;
        height = 65;
        x = 250;
        y = 300;
        xv = 10;
        yv = 10;
        velocidad = 3;
        fuerza = 3; 
        vidas = 3;
        municion = 15;
        municionMax = municion;
        puntos = 750;
        salud = 25;
        saludTotal = salud;
        rellenoTotal = (width*salud)/saludTotal;
        super.cargarImagenes();
    } 
    
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters & toString">
    public void setMov(Movimientos mov) {
        this.movimientos = mov;
    }
        
    public Precios getPrecios(){
        return precios;
    } 

    public Movimientos getMov() {
        return movimientos;
    }
       
    public boolean isCorre() {
        return isCorre;
    }

    @Override
    public String toString() {
        return "Nave{"+"id:"+id+", mov:" +movimientos + '}';
    } 
   
    //</editor-fold >

    @Override
    public void fire() {        
         if(salud > 0){
            int value = Velocidad.MEDIA.VALUE;
            if(municion > 0){
                disparar(-value,"laserRojo");  
            }
            municion = (municion--<=0)? 0 : municion;
        }
    }

    @Override
    public int mover() {       
         if(salud > 0){
            Partida.Pantalla pantalla = Partida.getPantalla();
            //Si se sale por los bordes rebotará
            short aux = 1;
            if (x + xv < pantalla.getX()){ //izquierda
                xv = aux;
            }
            else if (x + xv+ width > pantalla.getX()+pantalla.getWidth()){// //derecha
                xv = -aux;
            }
            else if (y + yv < pantalla.getY()){ //arriba
                yv =  aux;
            }
            else if (y + yv+ height > pantalla.getY()+pantalla.getHeigth()){// abajo
                yv = -aux;
            }
            //movemos la nave 
            x += xv;
            y += yv;
            //movemos sus disparos 
            actualizarDisparos();
        }
        return salud;
    }
   
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == movimientos.getIzq().getCode()
                || e.getKeyCode() == movimientos.getDer().getCode())
            xv = 0;
        else if (e.getKeyCode() == movimientos.getArr().getCode()
                || e.getKeyCode() == movimientos.getAba().getCode())
            yv = 0;
        else if(e.getKeyCode() == movimientos.getCorre().getCode()) 
            isCorre = false;        
    }
    
    public void keyPressed(KeyEvent e) {
                
        if (e.getKeyCode() == movimientos.getCorre().getCode()) 
            isCorre = true;
        if (e.getKeyCode() == movimientos.getIzq().getCode()){
            imgAct = imgIzq;
            xv = (isCorre)?(-velocidad*2):-velocidad;
        }else        
        if (e.getKeyCode() == movimientos.getDer().getCode()){
            imgAct = imgDer;
            xv = (isCorre)?(velocidad*2):velocidad;
        }
        if (e.getKeyCode() == movimientos.getArr().getCode()){
            imgAct = imgArr;
            yv = (isCorre)?(-velocidad*2):-velocidad;
        }else
        if (e.getKeyCode() == movimientos.getAba().getCode()){
            imgAct = imgAba;
            yv = (isCorre)?(velocidad*2):velocidad;
        }
        
        if (e.getKeyCode() == movimientos.getRecargar().getCode()){       
            recargar(); 
        }
        
        if (e.getKeyCode() == movimientos.getDisparar().getCode()){
            imgAct = imgArr;
            fire();     
        } 
    }
    
    private void recargar(){
        if(municion <= MUNICION_PARA_RECARGAR)
            municion = municionMax;
    }   
    
    /**
     * Compra una mejora de las características de la nave.
     * Si tiene puntos suficientes se realiza la mejora.
     * @param precioAMejorar 
     */
    public boolean comprar(final Precio precioAMejorar){
        //comprobamos que tengamos suficientes puntos
        int aGastar = puntos - precioAMejorar.getActual();        
        if(aGastar >= 0){
            //incrementar nos devolverá la cantidad total que costó 
            // dicha incrementación
            int aRestar = precioAMejorar.incrementarActual();
            puntos -= aRestar;
            String mejorar = precioAMejorar.getClave();
            switch(mejorar){
                case Precios.FUERZA:     fuerza++;break;
                case Precios.MUNICION:   municionMax++;break;
                case Precios.RESISTENCIA:saludTotal++;break;
                case Precios.VIDAS:      vidas++;break;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void paint(Graphics2D g2d) {
        super.paint(g2d);   
        if(municion <= MUNICION_PARA_RECARGAR)           
            g2d.drawString("Reload! ("+movimientos.getRecargar().getNombreTecla()+")", x, y+6);
        if(salud<=5 && vidas>0) g2d.drawString("Reviviendo en "+salud, x, y-10);        
        g2d.setColor(Color.WHITE);
        g2d.drawString(nombre, x, y+15);
    }
}
