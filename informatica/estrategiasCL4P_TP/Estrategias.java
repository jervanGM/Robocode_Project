package informatica.estrategiasCL4P_TP;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import robocode.util.*;

//Clase padre de la estrategia orbitador, pensada para añadir más estrategias de movimiento al robot
public class Estrategias
{	
	private int _movimiento;
	
	public Estrategias(int movimiento){
		this._movimiento=movimiento;
	}
	
	public void setMovimiento(int movimiento){//Sentido del movimiento
		this._movimiento=movimiento;
	}
	
	public int getMovimiento(){
		return _movimiento;
	}
	
	

}
