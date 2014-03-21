package model;

import controller.Juego;
import controller.Partida;
import java.awt.Graphics2D;
import model.disparador.Disparador;


/**
 * Disparo
 * @author chema
 */
public class Disparo extends Solido{
    
    private static int numTotalDisparos = 0;
    
    public Disparo(Juego juego, Disparador disparador,int xv, int yv,String imgNombre) {
        super(juego,"juego/"+imgNombre);  
        this.xv=xv;
        this.yv =yv;
        x = disparador.getX() + (disparador.getWidth()/2);
        y = disparador.getY() + (disparador.getHeight()/2);        
        width = 22;
        height = 33;
        nombre = "d: "+numTotalDisparos++;
        fuerza = disparador.getFuerza();
        salud = 1;
        saludTotal = 1;
        super.cargarImagenes();
    }

    public boolean collision(Solido solido) {        
        return getBounds().intersects(solido.getBounds());
    }

    @Override
    public int mover() {
        
        Partida.Pantalla pantalla = Partida.getPantalla();
        //cambiamos el estado del disparo si saliera del panel del Juego        
        if(x < -width) 
            return -1;
        if(x > pantalla.getX()*2 + pantalla.getWidth()) 
            return -1;//sale por la derecha
        if(y < -height) 
            return -1; //sale por arriba
        if(y > pantalla.getY()*2 + pantalla.getHeigth()) 
            return -1;//sale por abajo
        y += yv;
        x += xv;
        return salud;
    }
    
    @Override
    public void paint(Graphics2D g2d) {
        
        g2d.drawImage(imgAct.getImage(), x,y,width,height, null); 
    } 
    
}