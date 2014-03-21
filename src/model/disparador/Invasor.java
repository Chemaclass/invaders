package model.disparador;

import com.j256.ormlite.table.DatabaseTable;
import controller.Juego;
import controller.Partida;
import static controller.Partida.random;
import java.awt.Color;
import java.awt.Graphics2D;
/**
 * Funciona como clase abstracta de la que heredarán los tipos específicos 
 * de invasores además de como la factoría de invasores que nos proveerá de  
 * @author chema
 */
@DatabaseTable(tableName = "invasores")
public class Invasor extends Disparador{
        
    public static final int WIDTH = 100;
    public static final int HEIGHT = 60;
    private static int nTotalVivos, nTotalVivosMax;
    private static int fuer,resis;
    private static boolean ganador = false;     
    private static int incremento = 1;  
    
    public Invasor(){}
    
    public Invasor(Juego juego,String nombre, int x, int y) {
        super(juego,"naves/resistente"); 
        int nivel = juego.getNivel();
        this.nombre = nombre;            
        this.x = x;
        this.y = y;
        int var = nivel/2;
        var = (var <= 5)? 5 : var;
        salud = var;
        var = (nivel/4 <= 5)? 5 : (nivel/4);
        fuerza = var;
        width = WIDTH;
        height = HEIGHT;    
        velocidad = 4 + incremento;
        xv = velocidad;
        yv = 8;  
        vidas = juego.getDificultad().VALUE; //1-FÁCIL,2-MEDIO,3-PRO        
        saludTotal = salud;
        rellenoTotal = (width*salud)/saludTotal;
        fuer = fuerza;
        resis = salud;
        nTotalVivos++;
        nTotalVivosMax = nTotalVivos; 
        super.cargarImagenes();
    }
        
    @Override
    public void fire(){
        if(salud > 0){
            int value = Velocidad.MEDIA.VALUE;    
            disparar(value,"laserAzul");
        }   
    } 
     
    @Override
    public void actualizarResistencia(int cant){
       super.actualizarResistencia(cant);
        //si muere restaremos 1 al total de vivos
        if(salud < 0) nTotalVivos--;     
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters & toString">
    
    public static int getIncremento(){
        return incremento;
    }
    
    public static void setIncremento(int i){
        incremento = i;
    }
    
    public static int getnTotalVivos() {
        return nTotalVivos;
    }

    public static void setnTotalVivos(int nTotalVivos) {
        Invasor.nTotalVivos = nTotalVivos;
    }

    public static int getnTotalVivosMax() {
        return nTotalVivosMax;
    }

    public static void setnTotalVivosMax(int nTotalVivosMax) {
        Invasor.nTotalVivosMax = nTotalVivosMax;
    }

    public static int getFuer() {
        return fuer;
    }

    public static void setFuer(int fuer) {
        Invasor.fuer = fuer;
    }

    public static int getResis() {
        return resis;
    }

    public static void setResis(int resis) {
        Invasor.resis = resis;
    }

    public static boolean isGanador() {
        return ganador;
    }

    public static void setGanador(boolean ganador) {
        Invasor.ganador = ganador;
    }
    
    
    
    //</editor-fold>    

    @Override
    public int mover() {
        if(salud > 0){
            Partida.Pantalla pantalla = Partida.getPantalla();
            int yvv = (pantalla.getWidth() + pantalla.getHeigth()) / 50;
            
            if (x + velocidad < pantalla.getX()){ // izquierda
                imgAct = imgDer;
                xv = velocidad;
                y += yvv;
            } 
            if (x + velocidad + width > pantalla.getX()+ pantalla.getWidth()){ //derecha
                imgAct = imgIzq;
                xv = -(velocidad);
            } 
            if (y + yvv > pantalla.getY() + pantalla.getHeigth()){
               ganador = true;
            } 
            x += xv; 
            //de forma aleatoria vemos si dispara o no           
            if(random(0, nTotalVivos * 10) == 4)fire();             
            actualizarDisparos();//movemos sus disparos            
        }
        return salud;
    }
    
    @Override
    public void paint(Graphics2D g2d) {
        super.paint(g2d); 
        g2d.setColor(Color.CYAN);
        g2d.drawString("R:"+salud, x, y+50);
        if(salud <= 10 && vidas > 0){
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawString("v:"+vidas, x, y+10);            
        }
    }

}
