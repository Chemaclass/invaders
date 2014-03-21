package controller;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Escudo;
import model.Solido;
import model.disparador.Invasor;
import model.Movimientos;
import model.disparador.Nave;

/**
 *
 * @author chema
 */
public final class Partida {
    
    /**
     * Clase encargada de controlar el tamaño de la pantalla.
     * Se ajusta automáticamente según las dimensiones de la ventana
     * gracias al Decorador
     */
    public static class Pantalla{  

        private int id;
        private int x = 160;
        private int y = 50;
        private int width = 0;
        private int heigth=0; 

        public Pantalla() {}

        //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
        public int getId() {
            return id;
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

        public void setWidth(int ancho) {
            this.width = ancho;
        }

        public void setHeigth(int alto) {
            this.heigth = alto;
        }    

        public int getWidth() {
            return width;
        }

        public int getHeigth() {
            return heigth;
        }    

        @Override
        public String toString() {
            return "Pantalla{" + "x=" + x + ", y=" + y + ", width=" + width + ", heigth=" + heigth + '}';
        }
        //</editor-fold>   

    }
    
    public enum Estado {
        PREPARADO(1),CARGANDO(2), JUGANDO(3), PAUSADO(4), PERDIDO(5),GANADO(6); 
        public final int ID;
        private Estado(int i){ID=i;}
        public static Estado getById(int id){
            if(PREPARADO.ID==id)return PREPARADO;
            else if(CARGANDO.ID==id)return CARGANDO;
            else if(JUGANDO.ID==id)return JUGANDO;
            else if(PAUSADO.ID==id)return PAUSADO;
            else if(PERDIDO.ID==id)return PERDIDO;
            else return GANADO;
        }
    }
    
    private Juego juego;
    private List<Nave> naves;
    private List<Escudo> escudos;
    private List<Invasor> invasores;    
    private Estado estado;
    private static Pantalla pantalla = new Pantalla();
    
    /**
     * Partida por default
     */
    public Partida(){  
        estado = Estado.PREPARADO;
        juego = new Juego("Nuevo juego preparado");
        crearNaves();
    }
    
    /**
     * Crear una nueva partida 
     * @param juego crear una nueva partida con las propiedades de el juego pasado
     * por parámetro
     */
    public Partida(Juego juego){
        estado = Estado.CARGANDO;
        // crear una partida por defecto o más bien con los parámetros que se estimen        
        this.juego=juego;
        initComponents();
    }
    
    /**
     * Iniciar simultáneamente la creación de las naves, los escudos y los invasores
     * mediante hilos concurrentes
     */
    private void initComponents(){
        //CREAMOS TODO EN PARALELO CON HILOS
        Thread tNaves = new Thread(new Runnable(){
            @Override
            public void run() {
                crearNaves();
            }
        });
        Thread tEscudos = new Thread(new Runnable(){
            @Override
            public void run() {
                crearEscudos();
            }
        });
        Thread tInvasores = new Thread(new Runnable(){
            @Override
            public void run() {
                crearInvasores();
            }
        });       
        tNaves.start();
        tEscudos.start();
        tInvasores.start(); 
        try {
            tNaves.join();
            tEscudos.join();
            tInvasores.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Partida.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Crear una partida con los datos establecidos
     * @param juego Juego
     * @param naves Lista de naves
     * @param escudos Lista de escudos
     * @param invasores Lista de invasores
     */
    public Partida(Juego juego, List<Nave> naves, List<Escudo> escudos
            , List<Invasor> invasores) {
        this.estado = Estado.PREPARADO;
        this.juego = juego;
        this.naves = naves;
        this.escudos = escudos;
        this.invasores = invasores;        
        cargarImagenes();
    }
       
    private void cargarImagenes(){
        for(Nave n: naves) n.cargarImagenes();
        for(Escudo e: escudos) e.cargarImagenes();
        for(Invasor i: invasores) i.cargarImagenes();
    }
    
    
    /**
     * Crear nuevas naves para la partida
     */
    private void crearNaves(){
        naves = new ArrayList<>(); 
        //Creamos la primera nave
        Nave nave1 = new Nave(juego, "nave_1",Movimientos.getInstance(1));  
        //Añadimos la nave creada a la lista de nuestras naves
        naves.add(nave1);
        //Preparamos la segunda nave        
        Nave nave2= new Nave(juego,"nave_2",Movimientos.getInstance(2));
        naves.add(nave2);
    }    
    
    /**
     * Crear nuevos escudos para la partida
     */
    private void crearEscudos(){
        escudos = new ArrayList<>();
        Escudo escudo;
        String nombre;
        int cant = 6;
        
        for(int i = 0;i<cant;i++){            
            int x = 10 + (i * 120), y = 450;
            nombre = "e:"+i;
            escudo = new Escudo(juego,nombre, x, y);
            escudos.add(escudo);
        }
    }    
    
    /**
     * Crear nuevos invasores para la partida
     */
    public void crearInvasores(){
        Invasor.setnTotalVivos(0);
        
        int colum = juego.getnColum();
        int filas = juego.getnFilas();
        int nivel = juego.getNivel();
        
        //Aquí controlaremos que la velocidad nunca sea mayor a 20 <-~->
        while(nivel >= 20){ nivel -= 20; } 
        Invasor.setIncremento(nivel);
        //<editor-fold defaultstate="collapsed" desc="Crear todos los invasores">
        invasores = new ArrayList<>(); 
        int x,y;
        Invasor invasor;
        String nombre;
        for (int i = 0; i < colum; i++) {
            for (int j = 0; j < filas; j++) {
                x = pantalla.getX() + (i * (Invasor.WIDTH + 20)); 
                y = pantalla.getY() + (j * (Invasor.HEIGHT + 20)); 
                nombre = "i:"+i+"-"+j;
                invasor = new Invasor(juego, nombre, x, y);
                invasores.add(invasor);
            }
        }       
        //</editor-fold>
    } 
    
    /**
     * Pintar la partida
     * @param g2d Graphics2D
     */
    public void paint(Graphics2D g2d){
        paintNaves(g2d);
        paintInvasores(g2d);
        paintEscudos(g2d);
    }
    
    /**
     * Pintar las naves de la partida
     * @param g2d 
     */
    private void paintNaves(Graphics2D g2d){
        int nNaves = juego.getnNaves();
        List<Nave> nav=naves;
        List<Nave> list = (nNaves==2) ? nav : nav.subList(0, 1);        
        for (Nave n : list) {
            if(n.isVivo()) n.paint(g2d);
        }
    }
    
    /**
     * Pintar todos los invasores de la partida
     * @param g2d 
     */
    private void paintInvasores(Graphics2D g2d){
         for (Invasor i : invasores) {
            if(i.isVivo()) i.paint(g2d);
        }
    }    
    
    /**
     * Pintar todos los escudos de la partida
     * @param g2d 
     */
    private void paintEscudos(Graphics2D g2d){
        for(Escudo e: escudos) 
            if(e.isVivo()) e.paint(g2d);
    }    
    
    /**
     * Mover todas las naves, los escudos y los invasores de la partida
     */
    public void mover() {
        if (estado.equals(Estado.JUGANDO)) {
            moverNaves();
            moverInvasores();
            moverEscudos();
            colisiones();
        }
    }
    
    /**
     * Comprobar las colisiones que pueden llegar a producirse de la partida
     */
    private void colisiones() {
        colisionesNaves();
        colisionesInvasores();
    }

    /**
     * Comprueba las colisiones de los disparos de las naves con los invasores.
     * Si se produjo una colisión mortal se le sumará adicionalmente un punto
     * en el juego
     */
    private void colisionesNaves() {
        for (Nave n : naves) {   
            if(n.isVivo()){
                int colisionesMortales = n.colisionesMisDisparosConList(invasores);
                if(colisionesMortales>0){
                    juego.setPuntos(juego.getPuntos() + colisionesMortales);
                }
            }
        }
    }

    /**
     * Comprobar las colisiones que se llevaron acabo entre los disparos de los
     * invasores con las naves y los escudos
     */
    private void colisionesInvasores() {
        for (Invasor i : invasores) {   
            if(i.isVivo()){
                 i.colisionesMisDisparosConList(naves);
                 i.colisionesMisDisparosConList(escudos);
            }
        }
    }    
    
    /**
     * Mover las naves de la partida. Si no queda ninguna nave viva entonces 
     * se habrá perdido el juego.
     */
    private void moverNaves() {
        int nNaves = juego.getnNaves();
        List<Nave> nav = naves;
        List<Nave> list;
        try{
        list = (nNaves == 2) ? nav : nav.subList(0, 1);
        
        if(moverListSolido(list)<=0)
            gameOver(); 
        }catch(java.lang.IndexOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    /**
     * Mover los invasores de la partida. Si no queda ningún invaosr vivo entonces 
     * se habrá ganado la partida.
     */
    private void moverInvasores() {        
        if(moverListSolido(invasores) <= 0) 
            win();
    }

    /**
     * Mover los escudos de la partida
     */
    private void moverEscudos() {
        moverListSolido(escudos);
    }
    
    /**
     * Mueve todos los componentes que están en la lista pasada por parámetro.
     * @param listMovibles lista a mover
     * @return int número de vivos que quedan en la lista
     */
    private int moverListSolido(List listMovibles){
        int vivos = 0;
        
        Iterator<Solido> itNave = listMovibles.iterator();
        
        while(itNave.hasNext()){
            Solido solido = itNave.next();
            //lo movemos y retornamos su salud actual                
            if(solido.mover() > 0) vivos++; 
        }        
        return vivos;
    }
    
    /**
     * Obtiene un número aleatorio
     * @param inf Rango inferior
     * @param sup Rango superior
     * @return número aleatorio comprendido entre el rango inf y sup
     */
    public static int random(int inf, int sup) {
        int posibilidades = sup - inf;
        double a = Math.random() * posibilidades;
        return (int) Math.round(a) + inf;
    }
    
    /**
     * Es llamada cuando se ha perdido la partida
     */
    public void gameOver() {
        estado = Estado.PERDIDO;
    }  
    
    /**
     * Es llamada cuando se ha ganado la partida.
     * Seguidamente se incrementará en uno el nivel del juego y 
     * se procede a crear de nuevo los invasores
     */
    public void win() {
        estado = Estado.CARGANDO;        
        new Thread(new Runnable(){
            @Override
            public void run() {
                juego.setNivel(juego.getNivel()+1);
                //creamos los invasores de nuevo
                crearInvasores();
                //vaciamos todos los disparos de las naves
                for(Nave n: naves) n.vaciarDisparos(); 
                //cambiamos el estado de la partida a JUGANDO
                estado = Estado.JUGANDO; 
            }            
        }).start();        
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getters and Setters & toString">
    
    public static Pantalla getPantalla() {
        return pantalla;
    }
     
    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
    
    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public List<Nave> getNaves() {
        return naves;
    }

    public void setNaves(List<Nave> naves) {
        this.naves = naves;
    }

    public List<Escudo> getEscudos() {
        return escudos;
    }

    public void setEscudos(List<Escudo> escudos) {
        this.escudos = escudos;
    }

    public List<Invasor> getInvasores() {
        return invasores;
    }

    public void setInvasores(List<Invasor> invasores) {
        this.invasores = invasores;
    }   
    
    @Override
    public String toString() {
        return "Partida{" 
                + "\njuego=" + juego + ","
                + "\nnaves=" + naves + ","
//                + "\nescudos=" + escudos + ","
//                + "\ninvasores=" + invasores + ","
                + "\nestado=" + estado + ""
                + "\n}";
    }
    
    //</editor-fold>

    
    /**
     * Restaurar una cantidad de escudos
     * @param cant 
     */
    public void restaurarEscudos(int cant){
        for(Escudo escudo : escudos)
            if(!escudo.isVivo()){//si está muerto
                escudo.restaurar();
                if(--cant <= 0)break;
            }
    }
    
    
}
//<editor-fold>