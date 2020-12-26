//Autor:Javier Galán Méndez
//Robot:Robot bievento CL4P_TP
//Asignatura: Informática Industrial II
//Fecha:30/10/2019


package informatica;
import robocode.*;
import robocode.util.*;
import java.lang.Math;
import java.awt.Color;
import informatica.estrategiasCL4P_TP.Orbitar;
import informatica.estrategiasCL4P_TP.Estrategias;
 
public class CL4P_TP extends AdvancedRobot{
	double velamount=0;//Controlador de giro predictivo del cañón
	int t=1;           //Variable para el modo wall. Ejecuta una vez por batalla el proceso
	double energia_actual=100;
	double energia_enemigo;
	int movimiento=1 ;//Dirección del movimiento
	
	
	Orbitar orbitar=new Orbitar(movimiento);//Declaración de objetos
	


///////////////////////////////////////////////////////////////////////////	
    public void run() {
	    
        setColors(Color.yellow,Color.black,Color.red,Color.red,Color.blue);
        this.setAdjustGunForRobotTurn(true);//La partes del robot se manejan independientemente
		this.setAdjustRadarForGunTurn(true);
		this.setAdjustRadarForRobotTurn(true);
		
		Condition triggerHitCondition = new Condition("esquivar") {//Evento para esquivar balas
       		public boolean test() {
          		 return (energia_actual-energia_enemigo<10);
       		}
   		};
		addCustomEvent(triggerHitCondition);
		
		while(true){
			turnRadarRightRadians(2);//Barrido
			scan();
		}
		
    }
///////////////////////////////////////////////////////////////////////////	
	 public void onScannedRobot(ScannedRobotEvent e) {
		
		orbitar.disparo_predictivo(this,e);
		energia_enemigo=e.getEnergy();
		
		orbitar.movimiento(this,e);//LLamada al movimiento de orbitar
	
		
		scan();
	}
///////////////////////////////////////////////////////////////////////////	


	public void onHitWall(HitWallEvent e){//En caso de chocar contra las paredes, alejarse hacia el centro
	
		orbitar.hitWall(this);
			
	}

///////////////////////////////////////////////////////////////////////////
	public void onCustomEvent(CustomEvent e){
		if(e.getCondition().getName().equals("esquivar")){//Evento custom para esquivar balas
			movimiento=-movimiento;//Cambio de dirección
			energia_actual=energia_enemigo;
			orbitar.setMovimiento(movimiento);
		}
	}

///////////////////////////////////////////////////////////////////////////
	public void onWin(WinEvent e){//Baile de la victoria
		setTurnGunRightRadians(Double.POSITIVE_INFINITY);
		setTurnLeftRadians(Double.POSITIVE_INFINITY);
		setAhead(10000);
		execute();
	}
} 
///////////////////////////////////////////////////////////////////////////
