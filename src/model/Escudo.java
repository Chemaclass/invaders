package model;

import com.j256.ormlite.table.DatabaseTable;
import controller.Juego;
import controller.Partida;

/**
 *
 * @author chema
 */
@DatabaseTable(tableName = "escudos")
public class Escudo extends Solido{ 
        
    public static final byte NUM_MAX_ESCUDOS = 14;
    
    public Escudo(){ }
    
    public Escudo(Juego juego,String nombre, int x, int y) {
        super(juego,"juego/escudo");
        this.nombre = nombre;
        this.x = x;
        this.y = y;
        width = 90;
        height = 45;
        salud=100;
        saludTotal = salud;
        fuerza = 10;
        xv = 4;
        yv = 0;
        vidas = 1;
        rellenoTotal = (width*salud)/saludTotal;
        super.cargarImagenes();
    }    
        
    @Override
    public int mover() { 
        Partida.Pantalla pantalla = Partida.getPantalla();
        
        if (x + xv < pantalla.getX()){//izq
            xv = Math.abs(xv);
        }
        if (x + xv + width > pantalla.getX() + pantalla.getWidth()){//derecha            
            xv = -Math.abs(xv);
        }
        x += xv; 
        y = pantalla.getHeigth() - 100;
                
        return salud;
    }
    
    
}
