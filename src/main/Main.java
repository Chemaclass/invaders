package main;

import view.Tienda;
import view.Principal;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 
 * @author Chema_class
 */
public class Main extends JFrame{
    
    private Principal principal;
    private Tienda tienda;
    private Decorador decorador;
    
    
    private Main(String s){
        super(s);
        setSize(new Dimension(1100, 600)); 
        setMinimumSize(new Dimension(950, 550));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        initComponent(); 
        setVisible(true);
    } 
    
    private void initComponent(){
        this.principal = new Principal(this);
        this.tienda = new Tienda(this);
        this.decorador = Decorador.getInstance(this);        
        getContentPane().add(decorador); 
    }
    
    public Principal getPrincipal(){
        return principal;       
    }
    
    public Tienda getTienda(){
        return tienda;
    }
    
    public Decorador getDecorador(){
        return decorador;
    }    
        
    private void begin(){
        principal.setVisible(true);
        decorador.begin();
    }
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            //java.util.logging.Logger.getLogger(Opciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        new Main("ChemaInvaders").begin();
    }
    
}
