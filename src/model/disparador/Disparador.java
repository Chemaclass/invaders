package model.disparador;

import controller.Juego;
import controller.Partida;
import static controller.Partida.random;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import main.Decorador;
import model.Disparo;
import model.Solido;

/**
 * Disparador será todo aquel que dispare: las naves y los invasores
 * @author chema
 */
public abstract class Disparador extends Solido {
    
    public static final String AUDIO_DISPARO_URL = "audio_disparo_url";  
        
    protected enum Velocidad{
        BAJA(2),MEDIA(3),ALTA(4);
        public final int VALUE;
        private Velocidad(int v){
            VALUE = v;
        }
    }
    
    protected final List<Disparo> disparos = new ArrayList<>(); 
            
    protected ImageIcon imgIzq;
    protected ImageIcon imgDer;
    protected ImageIcon imgArr;
    protected ImageIcon imgAba;
    
    @Override
    public void cargarImagenes(){
        super.cargarImagenes();        
        try {
             imgIzq = new ImageIcon(ImageIO.read(getClass()
                     .getClassLoader().getResource("images/"+imgActUrl+"Iz.png")));
             imgDer = new ImageIcon(ImageIO.read(getClass()
                     .getClassLoader().getResource("images/"+imgActUrl+"De.png")));
             imgArr = new ImageIcon(ImageIO.read(getClass()
                     .getClassLoader().getResource("images/"+imgActUrl+"Ar.png")));
             imgAba = new ImageIcon(ImageIO.read(getClass()
                     .getClassLoader().getResource("images/"+imgActUrl+"Ab.png")));             
        } catch (Exception ex) {  ex.printStackTrace(); } 
    }
        
    public Disparador(){}
    
    public Disparador(Juego j, String imageUrl){
        super(j,imageUrl);       
    }
        
    public abstract void fire();
    
    protected void disparar(int yv,String imgNombre){
        disparar(yv,0,imgNombre);
    }
    
    protected void disparar(int yv,int xv,String imgNombre){        
        disparos.add(new Disparo(getJuego(),this, xv, yv, imgNombre));                        
    }
    
    public int colisionesMisDisparosConList(List list) {
        int colisionesMortales = 0;
        Iterator<Disparo> itDisparo = disparos.iterator();
        try{
            while(itDisparo.hasNext()){
                Disparo disparo = itDisparo.next();
                Iterator<Solido> itSolido = list.iterator();            
                while (itSolido.hasNext()) {
                    Solido solido = itSolido.next();
                    //¿Disparo del disparador colisiona con algún sólido de iterator?
                    //y el sólido (y el disparo) no tienen estado 'muerto'                    
                    if (disparo.collision(solido) 
                            && solido.isVivo() && disparo.isVivo()){                                        
                        
                        //el sólido recibe el impacto del disparador
                        //restándole así a su salud la fuerza del disparo
                        int fuerzaDisparo = disparo.getFuerza();
                        int fuerzaSolido = solido.getFuerza();

                        solido.actualizarResistencia(-fuerzaDisparo);                            
                        disparo.actualizarResistencia(-fuerzaSolido);

                        if(solido instanceof Invasor 
                                && !solido.isVivo()){
                            Invasor invasor = (Invasor)solido;
                            bonus(invasor);
                            //incrementamos el número de colisiones mortales
                            colisionesMortales++;
                        }
                        if(!disparo.isVivo())itDisparo.remove();                    
                    }
                }
            }
        }catch(java.util.ConcurrentModificationException ex){
            System.err.println("Disparador.colisionesMisDisparosConList()> java.util.ConcurrentModificationException");
        }
        return colisionesMortales;
    }
     
    private void bonus(Invasor invasor){
        //obtenemos el valor de la dificultad
        int value = juego.getDificultad().VALUE; //1:fácil, 2:medio, 3:difícil
        
        //habrá 1/value+1 posibilidades de que se recuperen de 1 a 3 escudos 
        if (random(0, value+1) == 1){ //probamos suerte            
            Partida partida = Decorador.getInstance(null).getPartida();
            partida.restaurarEscudos(1);
        }
        
        value *= 3; //lo multiplicaremos por tres para darle más gracia al asunto       
        //-> 3:fácil, -> 6:medio, -> 9:difícil
        
        boolean suerte = (random(0, value) == 1) ? true : false;
        //incrementará la salud de la nave
        actualizarResistencia((suerte) ? random(5, 10) : random(1, 2));
        //e incrementarán sus puntos
        actualizarPuntos((suerte) ? random(1, 10) : random(1, 2));               
    }
    
    private void actualizarPuntos(int cant){
        puntos += cant;
    }
     
    protected void actualizarDisparos(){
        if(!disparos.isEmpty()){
            Iterator<Disparo> itDisparo = disparos.iterator();
            try{
            while(itDisparo.hasNext()){
                Disparo d = itDisparo.next();
                //Movemos el disparo y nos retorna su salud
                //eliminamos el disparo o salido de la pantalla
                if(d.mover() < 0)  itDisparo.remove();
            }
            }catch(java.util.ConcurrentModificationException e){
                System.err.println("Disparador.actualizarDisparos()>ConcurrentModificationException");
            }
        }
    }
    
    public void vaciarDisparos(){
        disparos.clear();
    }
    
    public List<Disparo> getDisparos(){
        return disparos;
    }
    
    @Override
    public void paint(Graphics2D g2d) {
        //lo pintamos como sólido
        super.paint(g2d); 
        try{
            if(!disparos.isEmpty())//pintamos sus disparos si los hubiera
                for(Disparo d: disparos) d.paint(g2d);             
        }catch(java.util.ConcurrentModificationException ex){
            System.err.println("Disparador.paint()> java.util.ConcurrentModificationException");
        }
    } 

    @Override
    public String toString() {
        return "Disparador{" + super.toString() + '}';
    }    
    
}
