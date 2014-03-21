package main;

import controller.Juego;
import controller.Partida;
import controller.Partida.Estado;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import model.disparador.Invasor;
import model.disparador.Nave;

/**
 *
 * @author chema
 */
public class Decorador extends JPanel{
    //Fondo
    private Image imgInJe=null,
            imgInFu=null,imgInRe=null,
            imgInJeDis=null,imgInFuDis=null,imgInReDis=null;
   
    private Main main;    
    private Partida partida;
    //Imagen de fondo del decorador
    private Image fondoDecorador=null;
    
    private enum Teclas{
        PRINCIPAL(KeyEvent.VK_ESCAPE),
        TIENDA(KeyEvent.VK_F1),
        SALIR(KeyEvent.VK_F10),
        COMENZAR(KeyEvent.VK_ENTER),
        PAUSAR(KeyEvent.VK_P); 
        
        private final int value;
        private Teclas(int code){ value = code; }        
        public int getValue(){ return value; }
    }
    
    private static Decorador decorador;
    public static Decorador getInstance(Main main){
        if(decorador==null) decorador= new Decorador(main);        
        return decorador;
    }
    
    private Decorador(Main main){  
//        System.out.println("Decorador.Decorador()");
        setSize(new Dimension(1200, 700)); //tamaño inicial               
        this.main = main;
        this.partida = new Partida();
        initComponent(); 
        setFocusable(true);
        setVisible(true);
    }
    
    /**
     * Inicializar todos los miembros
     */
    private void initComponent(){        
        setLayout(null); 
        try {
            fondoDecorador= ImageIO.read(getClass().getClassLoader().getResource("images/juego/fondo.png")); 
            imgInJe = ImageIO.read(getClass().getClassLoader().getResource("images/juego/jefeT.png")); 
            imgInFu = ImageIO.read(getClass().getClassLoader().getResource("images/juego/fuerteT.png")); 
            imgInRe = ImageIO.read(getClass().getClassLoader().getResource("images/juego/resistenteT.png")); 
            imgInJeDis = ImageIO.read(getClass().getClassLoader().getResource("images/juego/laVet.png"));
            imgInFuDis= ImageIO.read(getClass().getClassLoader().getResource("images/juego/laAmt.png"));
            imgInReDis= ImageIO.read(getClass().getClassLoader().getResource("images/juego/laAzt.png")); 
        } catch (java.io.IOException | java.lang.IllegalArgumentException ex) { 
        } 
        //
        addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {
                for (Nave n : partida.getNaves()) n.keyReleased(e);                
            }
            @Override
            public void keyPressed(KeyEvent e) {
                
                for (Nave n : partida.getNaves()) n.keyPressed(e);
                
                if(e.getKeyCode() == Teclas.PRINCIPAL.getValue()){
                    partida.setEstado(controller.Partida.Estado.PAUSADO);
                    main.getPrincipal().setVisible(true);
                }
                //Abrir tienda
                if (e.getKeyCode() == Teclas.TIENDA.getValue()) {
                    partida.setEstado(controller.Partida.Estado.PAUSADO);
                    main.getTienda().setVisible(true);
                }
                //Salir del juego
                if (e.getKeyCode() == Teclas.SALIR.getValue()) 
                    System.exit(0); 
                //Pausar el juego
                if (e.getKeyCode() == Teclas.PAUSAR.getValue()) 
                   partida.setEstado(controller.Partida.Estado.PAUSADO);
                //Comenzar el juego
                if (e.getKeyCode() == Teclas.COMENZAR.getValue()) 
                   partida.setEstado(controller.Partida.Estado.JUGANDO);
            }
        });        
    } 

    @Override
    public void paint(Graphics g){
        super.paint(g); 
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);     
        //g2d.drawImage(fondo, 0, 0, (int)getSize().getWidth(), (int)getSize().getHeight(),null);
        int x = (getWidth() - fondoDecorador.getWidth(null)) / 2;
        int y = (getHeight() - fondoDecorador.getHeight(null)) / 2;   
        
        //pintamos el fondo (centrado)
        g2d.drawImage(fondoDecorador, x, y, null); 
        //ajustamos el ancho y el alto de la pantalla
        Partida.Pantalla pantalla = Partida.getPantalla();
        pantalla.setWidth(getWidth()-pantalla.getX()*2);
        pantalla.setHeigth(getHeight()-70);
        
        Partida.Estado estadoPartida = partida.getEstado();
        
        // JUGANDO
        if(estadoPartida.equals(Partida.Estado.JUGANDO)){            
//            System.out.println("Decorador.paint()> estadoJuego=>JUGANDO");
            
            partida.paint(g2d); //pintamos la pantalla
            
        }
        // PAUSADO
        else if(estadoPartida.equals(Partida.Estado.PAUSADO)){
//            System.out.println("Decorador.paint()> estadoJuego=>PAUSADO");
            g2d.setFont(new Font("Arial", Font.PLAIN, 35));
            g2d.setColor(Color.CYAN);
            String s = "PULSA 'ENTER' PARA CONTINUAR";
            g2d.drawString(s, pantalla.getWidth()/2-125, pantalla.getHeigth()/2);

            partida.paint(g2d);
            
        }
        // PREPARADO
        else if(estadoPartida.equals(Partida.Estado.PREPARADO)){
//            System.out.println("Decorador.paint()> estadoJuego=>PREPARADO");
            g2d.setFont(new Font("Arial", Font.PLAIN, 35));                            
            g2d.setColor(Color.BLACK);
            String s = "PREPARADO...";
            g2d.drawString(s, pantalla.getWidth()/2+32, pantalla.getHeigth()/2);
            
        } 
        //CARGANDO
        else if(estadoPartida.equals(Partida.Estado.CARGANDO)){
//            System.out.println("Decorador.paint()> estadoJuego=>CARGANDO");
            g2d.setFont(new Font("Arial", Font.PLAIN, 35));                            
            g2d.setColor(Color.LIGHT_GRAY);
            String s = "CARGANDO...";
            g2d.drawString(s, pantalla.getWidth()/2+32, pantalla.getHeigth()/2);
            
        } 
        //PERDIDO
        else if(estadoPartida.equals(Partida.Estado.PERDIDO)){
//            System.out.println("Decorador.paint()> estadoJuego=>PERDIDO");
            partida.gameOver();
            
            g2d.setFont(new Font("Arial", Font.PLAIN, 35));                            
            g2d.setColor(Color.LIGHT_GRAY);
            String s = "YOU LOSE";
            g2d.drawString(s, pantalla.getWidth()/2+32, pantalla.getHeigth()/2);
            
            new Thread(new Runnable(){ 
                @Override
                public void run() {
                    main.getPrincipal().setVisible(true);
                } 
            }).start(); 
        } 
        //CAGADO
        else if(estadoPartida.equals(Partida.Estado.GANADO)){
            
            partida.win(); 
            
        }
        if(estadoPartida.equals(Partida.Estado.PREPARADO)){
            
        }else if(estadoPartida.equals(Partida.Estado.CARGANDO)){
            paintInfoInvasores(g2d);
            paintInfoNaves(g2d);
        } else {
            pintarRecursos(g2d);
        }
    }
    
    private void pintarRecursos(final Graphics2D g2d){
        
        //PINTAMOS LOS RECURSOS
        //Información de las Naves
        paintInfoNaves(g2d);
        //Información de los invasores
        paintInfoInvasores(g2d);   
        //otra información
        paintInfo(g2d);
        //bordes
        //paintBordes(g2d);        
    }
    
    /**
     * Imprimir la información de las naves
     * y notificar si han muerto
     * @param g2d 
     */
    private void paintInfoNaves(Graphics2D g2d){
        List<Nave> naves = partida.getNaves();
        List<Nave> list;        
        list = (partida.getJuego().getnNaves()==2)
                ? naves : naves.subList(0, 1);
        
        int h = (int)getSize().getHeight();
        for(int i =0; i< list.size();i++){ 
            int separaX, separaY = 150;
            Nave n = list.get(i); 
            Color color;
            if(i==0) { // NAVE 1
                separaX = 10+(i*200);
                color = Color.BLACK;
            } else {   // NAVE 2
                separaX = 10+(int)getSize().getWidth()-Partida.getPantalla().getX();
                color = Color.WHITE;
            } 
            if(!n.isVivo()){ //Si no está viva
                g2d.setColor(Color.RED); //lo tachamos con una línea en rojo
                g2d.setFont(new Font("Arial",Font.PLAIN,22)); 
                g2d.drawLine(separaX, h-separaY, separaX+separaY,  h-30);
                g2d.drawLine(separaX+separaY, h -separaY, separaX, h-30);
                g2d.drawString("Caído",separaX+20, h-separaY);
            }
            g2d.setFont(new Font("Arial",Font.PLAIN,18)); 
            g2d.setColor(color);            
            g2d.drawString("Nave : "+n.getNombre(), separaX, h-(separaY-=20));            
            g2d.drawString("Munición:"+n.getMunicion(), separaX, h-(separaY-=20));
            g2d.drawString("Fuerza:"+n.getFuerza(), separaX, h-(separaY-=20));
            g2d.drawString("Salud:"+n.getSalud(), separaX, h-(separaY-=20));            
            g2d.drawString("Puntos:"+n.getPuntos(), separaX, h-(separaY-=20));
            g2d.setColor(Color.RED);
            g2d.drawString("Vidas:"+n.getVidas(), separaX, h-(separaY-=20));
        }
    }
    
    //PAINT INVASORES ANTIGUO
    /** PAINT INVASORES
     * 
     * @param g2d 
     **
    private void paintInfoInvasores(Graphics2D g2d){
        Pantalla pantalla = Juego.getPantalla();
        g2d.setFont(new Font("Arial",Font.PLAIN,18));
        g2d.setColor(Color.WHITE);
        int nJ=Jefe.getnTotalVivos(),fJ=Jefe.getFuer(),rJ=Jefe.getResis(),//número de Jefes
            nF=Fuerte.getnTotalVivos(),fF=Fuerte.getFuer(),rF=Fuerte.getResis(),//número de Fuertes
            nR=Resistente.getnTotalVivos(),fR=Resistente.getFuer(),rR=Resistente.getResis(),//número de Resistentes
            nT=Invasor.getnTotalVivos(); //número de Totales
              
        int separaX = pantalla.getWidth() + pantalla.getX() + 10, 
            separaY = pantalla.getY() + 20,
            wIn = 40, hIn = 40, //tamaño img_invasor
            wDi = 20, hDi = 35; //tamaño img_disparo        
        
        g2d.drawString("Jefes: "+nJ, separaX, separaY );
        g2d.drawImage(imgInJeDis, separaX, separaY+10, wDi, hDi, null);
        g2d.drawImage(imgInJe, separaX+20, separaY+10 , wIn, hIn, null);        
        g2d.drawString("F: "+fJ, separaX+70, separaY+20);
        g2d.drawString("R: "+rJ, separaX+70, separaY+40);
        
        g2d.drawString("Resistentes: "+nR, separaX, separaY+=100);
        g2d.drawImage(imgInReDis, separaX,separaY+10 , wDi, hDi, null);
        g2d.drawImage(imgInRe, separaX+20,separaY+10, wIn,hIn, null);         
        g2d.drawString("F: "+fR, separaX+70, separaY+20);
        g2d.drawString("R: "+rR, separaX+70, separaY+40);
        
        g2d.drawString("Fuertes: "+nF, separaX,separaY+=100);
        g2d.drawImage(imgInFuDis, separaX,separaY+10, wDi, hDi, null);
        g2d.drawImage(imgInFu, separaX+20,separaY+10, wIn,hIn, null); 
        g2d.drawString("F: "+fF, separaX+70, separaY+20);
        g2d.drawString("R: "+rF, separaX+70, separaY+40);
        
        g2d.drawString("Totales: "+nT, separaX, separaY+=100);
    }   
    /* */
    
    private void paintInfoInvasores(Graphics2D g2d){
        Partida.Pantalla pantalla = Partida.getPantalla();
        g2d.setFont(new Font("Arial",Font.PLAIN,20));
        g2d.setColor(Color.WHITE);
        int  nR=Invasor.getnTotalVivos()
            ,fR=Invasor.getFuer()
            ,rR=Invasor.getResis();
//        int nT=Invasor.getnTotalVivos(); //número de Totales
              
        int separaX = pantalla.getWidth() + pantalla.getX() + 10, 
            separaY = pantalla.getY() + 20,
            wIn = 40, hIn = 40, //tamaño img_invasor
            wDi = 20, hDi = 35; //tamaño img_disparo        
        
        
        g2d.drawString("Invasores: "+nR, separaX, separaY+=100);
        g2d.drawImage(imgInReDis, separaX,separaY+10 , wDi, hDi, null);
        g2d.drawImage(imgInRe, separaX+20,separaY+10, wIn,hIn, null);         
        g2d.drawString("F: "+fR, separaX+70, separaY+20);
        g2d.drawString("R: "+rR, separaX+70, separaY+40);        
        
//        g2d.drawString("Totales: "+nT, separaX, separaY+=100);
    } 
    
    private void paintInfo(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        Juego juego = partida.getJuego();        
        String dificultad = juego.getDificultad().name().toUpperCase();
        g2d.drawString("Dificultad: "+dificultad,20, 40);
        g2d.drawString("Puntuación:",20, 60); 
        g2d.drawString("->"+juego.getPuntos(),20, 80); 
        
        g2d.drawString("Nivel: "+juego.getNivel(),getWidth()-400, 20);        
        g2d.drawString("Menú: ESC", getWidth()-270, 20);
        g2d.drawString("Tienda: F1", getWidth()-120,20);
    }
    
    private void paintBordes(Graphics2D g2d){
        Partida.Pantalla pantalla = Partida.getPantalla();
        int ax = pantalla.getX(),
            ay = pantalla.getY() + pantalla.getHeigth(),
            bx = pantalla.getX() + pantalla.getWidth(),        
            by = pantalla.getY() + pantalla.getHeigth();
        g2d.drawLine(ax,ay,bx,by); //ABAJO
            ax = pantalla.getX();
            ay = pantalla.getY();
            bx = pantalla.getX() + pantalla.getWidth(); 
            by = pantalla.getY();
        g2d.drawLine(ax,ay,bx,by);//ARRIBA
            ax = pantalla.getX();
            ay = pantalla.getY(); 
            bx = pantalla.getX(); 
            by = pantalla.getY() + pantalla.getHeigth();
        g2d.drawLine(ax,ay,bx,by);//IZQUIERDA
            ax = pantalla.getX() + pantalla.getWidth();
            ay = pantalla.getY(); 
            bx = pantalla.getX() + pantalla.getWidth(); 
            by = pantalla.getY() + pantalla.getHeigth();
        g2d.drawLine(ax,ay,bx,by);//DERECHA
    }    
    
    public void begin(){
        while(true){ 
            partida.mover();
            repaint();
            try{
                Thread.sleep(18);
            }catch(InterruptedException e){}
        }
    }

    public Partida getPartida(){
        return partida;
    }
    
    public void setPartida(Partida juego){
        this.partida= juego;
        this.partida.setEstado(Estado.JUGANDO);
    }
}
