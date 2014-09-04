package house;

import javax.swing.JPanel;

import util.Loc;
import cityGui.CityHouse;
import cityGui.CityHouseCard;
import house.gui.HouseControlPanel;
import house.gui.InhabitantGui;
import interfaces.Person;

public class House {
	
	
	CityHouseCard animation;
	public HouseControlPanel controlPanel;
	
	public LivingUnit room;
	public Loc address;
	public House(){
		room=new LivingUnit();
		
	}
	
	
	public House(Loc loc){
		this.address = loc;
	}
	
	
	public void msgImHome(InhabitantRole i){ //called by Person
		room.inhabitant=i;
		InhabitantGui tempGui=new InhabitantGui(room.inhabitant);
		room.inhabitant.setGui(tempGui );
		
		//hack for getting the unit test to work
		if (animation!=null)
			animation.addGui(room.inhabitant.gui);



		
	}
	
	
	
	public void setAnimationPanel(CityHouseCard chc){
		animation=chc;
		
	}
	
	
	
	

}
