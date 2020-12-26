package informatica.estrategiasCL4P_TP;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;
import java.lang.Math;
import robocode.util.*;
import robocode.*;

public class Orbitar extends Estrategias
{



	public Orbitar(int movimiento){
		super(movimiento);
	}	

///////////////////////////////////////////////////////////////////////////
	public void movimiento(AdvancedRobot c,ScannedRobotEvent e){
		
		if(e.getDistance()>170){//Si está lejos, el robot se acercará al enemigo
			c.setTurnRightRadians(Utils.normalRelativeAngle(c.getRadarHeadingRadians()-c.getHeadingRadians()));
			//El robot debe mantenerse directo con el enemigo
			c.setAhead(e.getDistance()-170);//Controla distancia con el enemigo
			
		}
		else if(e.getDistance()<=170){//Cuenado está cerca del enemigo, el robot girará en torno al enemigo
			//Perpendicular al enemigo
			c.setTurnRightRadians(Utils.normalRelativeAngle(e.getBearingRadians()-Math.toRadians(90)));
			
			c.setAhead((e.getDistance()-170)*getMovimiento());//Controla distancia con el enemigo
			c.setMaxVelocity(8);
		}
		
		c.execute();
		if(e.getDistance()>100) c.setFire(2);
		else c.setFire(3);//Mayor potencia al estar más cerca del enemigo
	}

///////////////////////////////////////////////////////////////////////////
	public double centro_mapa(AdvancedRobot c){
		double ladoX=(c.getBattleFieldWidth()/2)-c.getX();//Distancia X del robot al centro
		double ladoY=(c.getBattleFieldHeight()/2)-c.getY();//Distancia Y del robot al centro
		double alpha = Math.toDegrees(Math.atan(ladoY/ladoX));//Angulo del triangulo equivalente
		
		//Según el sector del mapa, tendrá que girar un ángulo u otro
		if (ladoY < 0 && ladoX < 0) 	 alpha = -90 -c.getHeading()- alpha;
		else if (ladoY < 0 && ladoX > 0) alpha = -270 -c.getHeading()- alpha;
		else if (ladoY > 0 && ladoX < 0) alpha = -90 -c.getHeading()- alpha;
		else 							 alpha = 90 -c.getHeading()- alpha;
		
		if(alpha<=180) alpha=alpha;//Busca el ángulo y sentido de giro más óptimo
		else alpha-=360;
		if(alpha>=-180) alpha=alpha;
		else alpha +=360;

		return alpha;
	}

///////////////////////////////////////////////////////////////////////////
	public void hitWall(AdvancedRobot c){
		double angulo_giro = centro_mapa(c);
			c.setMaxTurnRate(Rules.MAX_TURN_RATE);//Máxima velocidad de giro
			c.setTurnRight(angulo_giro);
			c.setMaxVelocity(8);//Se incrementa la avelocidad
			c.setAhead(100);
			c.execute();
	}
	

///////////////////////////////////////////////////////////////////////////
	public void disparo_predictivo(AdvancedRobot c,ScannedRobotEvent e){
		double cantidad_velocidad;//Cantidad de velocidad segun la distancia
		double absBearing=e.getBearingRadians()+c.getHeadingRadians();//bearing absoulto del enemigo
		double velocidad=e.getVelocity() * Math.sin(e.getHeadingRadians() -absBearing);//Velocidad del enemigo

		double angulo_radar =c.getHeadingRadians() + e.getBearingRadians()-c.getRadarHeadingRadians();
		
		if(angulo_radar<=180) angulo_radar=angulo_radar;
		else angulo_radar-=360;//Busca el ángulo y sentido de giro más óptimo
		if(angulo_radar>=-180) angulo_radar=angulo_radar;
		else angulo_radar +=360;
		
		if(e.getDistance()>=200) cantidad_velocidad=velocidad/22;//Numeros obtenidos experimentalmente
		else cantidad_velocidad=velocidad/13;

		c.setTurnRadarRightRadians(Utils.normalRelativeAngle(angulo_radar));//Radar sigue al enemigo
		//Torreta a posición futura
		c.setTurnGunRightRadians(c.getRadarHeadingRadians()-c.getGunHeadingRadians()+cantidad_velocidad);
	}
///////////////////////////////////////////////////////////////////////////
}
