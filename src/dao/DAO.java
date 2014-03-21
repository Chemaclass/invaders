package dao;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import controller.Juego;
import controller.Partida;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import model.Escudo;
import model.disparador.Invasor;
import model.Movimientos;
import model.disparador.Nave;
import model.Precios;
import model.Precios.Precio;

/**
 *
 * @author chema
 */
public class DAO {
    
    private static JLabel label;
    private final static String db_name = "db.db";
    private final static String DATABASE_URL = "jdbc:sqlite:"+db_name;

    private Dao<Escudo, Integer> escudoDao;
    private Dao<Juego, Integer> juegoDao;
    private Dao<Nave, Integer> naveDao;
    private Dao<Invasor, Integer> invasorDao;
    private Dao<Movimientos, Integer> movimientosDao;
//    private Dao<Movimiento, Integer> movimientoDao;
//    private Dao<Precios, Integer> preciosDao;
    private Dao<Precio, Integer> precioDao;
    
    private DAO() {  
        JdbcConnectionSource connectionSource = null;
        try {
            File f = new File(db_name);            
//            if(f.exists()) f.delete();
//             create our data source            
            connectionSource = new JdbcConnectionSource(DATABASE_URL);             
            setupDatabase(connectionSource);   
            
            if(f.length()<=0){ //si la db no existía, creamos las tablas
                createsTables(connectionSource);
                createsAllMovimientos();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // destroy the data source which should close underlying connections
            if (connectionSource != null) try {
                connectionSource.close();
            } catch (SQLException ex) {
                Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
    }
    
    private static DAO dao = new DAO();
    
    public static DAO getInstance(JLabel label){
        DAO.label = label;
        return dao;
    }
    
    private void setupDatabase(ConnectionSource connectionSource) throws Exception {

        escudoDao = DaoManager.createDao(connectionSource, Escudo.class);
        juegoDao = DaoManager.createDao(connectionSource, Juego.class);
        naveDao = DaoManager.createDao(connectionSource, Nave.class);
        invasorDao = DaoManager.createDao(connectionSource, Invasor.class); 
        movimientosDao = DaoManager.createDao(connectionSource, Movimientos.class); 
//        preciosDao = DaoManager.createDao(connectionSource, Precios.class); 
        precioDao = DaoManager.createDao(connectionSource, Precio.class); 
        
    }
    
    private void createsTables(ConnectionSource connectionSource) throws Exception {
        // if you need to create the table
        TableUtils.createTable(connectionSource, Escudo.class);
        TableUtils.createTable(connectionSource, Juego.class);            
//        TableUtils.createTable(connectionSource, Pantalla.class);
        
        TableUtils.createTable(connectionSource, Nave.class);
        TableUtils.createTable(connectionSource, Invasor.class);
        TableUtils.createTable(connectionSource, Movimientos.class);
        TableUtils.createTable(connectionSource, Movimientos.Movimiento.class);
        TableUtils.createTable(connectionSource, Precios.class);
        TableUtils.createTable(connectionSource, Precios.Precio.class);
    }
    
    private void createsAllMovimientos() throws Exception{       
        for(Movimientos m : Movimientos.getMovimientos()){
            movimientosDao.create(m);
        }
    }
    
    public void insert(Partida partida){
        Juego juego = partida.getJuego();
        List<Nave> naves = partida.getNaves();
        List<Escudo> escudos = partida.getEscudos();
        List<Invasor> invasores = partida.getInvasores();
        
        try {
            label.setText(format("Guardando juego ..."));
            juegoDao.create(juego);
            label.setText(format("Guardando naves ..."));
            for(Nave n: naves){ //n.setJuego(juego);
                naveDao.create(n);
            }
            label.setText(format("Guardando escudos ..."));
            for(Escudo e: escudos){// e.setJuego(juego);
                escudoDao.create(e);
            }
            label.setText(format("Guardando invasores ..."));
            for(Invasor i:invasores){//   i.setJuego(juego);
                invasorDao.create(i);
            } 
            label.setText(format("Partida guardada con éxito."));
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(Partida partida){
        Juego juego = partida.getJuego();
        List<Nave> naves = partida.getNaves();
        List<Escudo> escudos = partida.getEscudos();
        List<Invasor> invasores = partida.getInvasores();
        try {
            label.setText(format("Actualizando juego ..."));
            juegoDao.update(juego);
            label.setText(format("Actualizando naves ..."));
            for(Nave n: naves) update(n);
            label.setText(format("Actualizando escudos ..."));
            for(Escudo e: escudos) update(e);
            label.setText(format("Actualizando invasores ..."));
            for(Invasor i:invasores) update(i);
            label.setText(format("Partida actualizada con éxito."));
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(Juego juego){
        try {
            label.setText(format("Actualizando partida ..."));
            juegoDao.update(juego);           
            label.setText(format("Partida actualizada con éxito."));
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(Nave nave){
         try {
            label.setText(format("Actualizando nave ..."));
            naveDao.update(nave);
            label.setText(format("Nave actualizada con éxito."));
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(Precio precio){
        try {
            label.setText(format("Actualizando precio ..."));
            precioDao.update(precio);
            label.setText(format("Precio actualizado con éxito."));
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(Escudo escudo){
        try {
            label.setText(format("Actualizando escudo ..."));
            escudoDao.update(escudo);           
            label.setText(format("Escudo actualizado con éxito."));
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update(Invasor invasor){
        try {
            label.setText(format("Actualizando invasor ..."));
            invasorDao.update(invasor);           
            label.setText(format("Invasor actualizado con éxito."));
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void delete(Juego juego){
        Partida partida = selectPartida(juego);
        List<Nave> naves = partida.getNaves();
        List<Escudo> escudos = partida.getEscudos();
        List<Invasor> invasores = partida.getInvasores();
        try {
            label.setText(format("Borrando juego ..."));
            juegoDao.delete(juego);
            label.setText(format("Borrando naves ..."));
            for(Nave n: naves) naveDao.delete(n);
            label.setText(format("Borrando escudos ..."));
            for(Escudo e: escudos) escudoDao.delete(e);
            label.setText(format("Borrando invasores ..."));
            for(Invasor i: invasores) invasorDao.delete(i);
            label.setText(format("Partida borrada con éxito."));
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Partida selectPartida(Juego juego){
        
        List<Nave> listNaves = selectNaves(juego);
        List<Escudo> listEscudos = selectEscudos(juego);
        List<Invasor> listInvasores = selectInvasores(juego);         
                
        return new Partida(juego, listNaves, listEscudos, listInvasores);
    }
    
    private List<Nave> selectNaves(Juego juego){
        List<Nave> listNaves = new ArrayList<>(); 
        ForeignCollection<Nave> naves = juego.getNaves();
        CloseableIterator<Nave> itNaves = naves.closeableIterator();
//        Map<String,Object> map = new HashMap<>(); 
//        map.put(Nave.JUEGO_ID, juego.getId());// esto es menos pro
//        listNaves = naveDao.queryForFieldValues(map);
        try { 
            try{
                while(itNaves.hasNext()){
                    //obtenemos la nave
                    Nave n = itNaves.next();
                    //obtenemos su movimiento por su id
                    Movimientos m = movimientosDao.queryForId(n.getMov().getId());
                    //le añadimos sus movimientos correspondientes
                    n.setMov(m);
                    //añadimos a la lista de naves nuestra nave ya formada
                    listNaves.add(n);
                }
                    
            } finally {
                itNaves.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return listNaves;
    }
    
    private List<Escudo> selectEscudos(Juego juego){
        List<Escudo> listEscudos = new ArrayList<>(); 
        ForeignCollection<Escudo> escudos = juego.getEscudos();
        CloseableIterator<Escudo> itEscudos = escudos.closeableIterator();
         try {            
            try{
                while(itEscudos.hasNext())
                    listEscudos.add(itEscudos.next());                
            } finally {
                itEscudos.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return listEscudos;
    }
    
    private List<Invasor> selectInvasores(Juego juego){
        List<Invasor> listInvasores = new ArrayList<>(); 
        ForeignCollection<Invasor> invasores = juego.getInvasores();
         CloseableIterator<Invasor> itInvasores = invasores.closeableIterator();
         try {            
            try{
                while(itInvasores.hasNext())
                    listInvasores.add(itInvasores.next());                
            } finally {
                itInvasores.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }  
        return listInvasores;
    }
    
    public DefaultListModel<Juego> selectAllJuegos(){
        DefaultListModel<Juego> model = new DefaultListModel<>();
        try {            
            List<Juego> juegos = juegoDao.queryForAll();
            for(Juego j: juegos) model.addElement(j); 
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }        
        return model;
    }
    
    private String format(String s){
        return ">> "+s;
    }

    public ComboBoxModel selectAllMovimientos() {
        DefaultComboBoxModel<Movimientos> model = new DefaultComboBoxModel();
        try {
            List<Movimientos> movimientos = movimientosDao.queryForAll();
            for(Movimientos m: movimientos) {
                model.addElement(m);
            }            
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return model;
    }
}
