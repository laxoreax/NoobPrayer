import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.osbot.rs07.api.Chatbox.MessageType;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
   
   
   
    @ScriptManifest(name = "DuckysNoobPrayer", author = "Ducky", version = 1.0, info = "", logo = "https://i.imgur.com/k0PQ4QE.png")
    public class NoobPrayer extends Script {
        BufferedImage background;
    public Area lumbridge = new Area(3200, 3300, 3268, 3197);
   
      Area CowPen = new Area(
            new int[][]{
                { 3243, 3296 },
                { 3243, 3284 },
                { 3254, 3274 },
                { 3254, 3256 },
                { 3264, 3256 },
                { 3263, 3295 },
                { 3258, 3297 },
                { 3247, 3297 }
            }
        );

     // Optional<NPC> cow = this.getNpcs().getAll().stream().filter(o -> o.getName().equals("Cow")).findFirst();
 
        @Override
        public void onStart() throws InterruptedException {
        	Global.startExp = skills.getExperience(Skill.PRAYER);
        	Global.currentStatus = Global.Status.COLLECTING;
        	getExperienceTracker().start(Skill.PRAYER);
        }

        @Override 
        public int onLoop() {
            RS2Object gate = objects.closest(true, "Gate");
	        GroundItem items = getGroundItems().closest("Bones");
            int rn = (int )(Math.random() * 4000 + 1);
            
            if (Global.currentStatus == Global.Status.BURRYING) {
            	rn = (int )(Math.random() * 1250 + 1);
            } else {
            if (rn <= 800) {
            	rn = rn + (int )(Math.random() * 3200 + 1);
            		}
            }

            if (dialogues.isPendingContinuation()) {
        		dialogues.clickContinue();
            }	
            
            if (chatbox.contains(MessageType.GAME, "You need to wait another")) {
                warn("Start in Lumbridge / Wait till home teleport works again.");
                warn("Stopping Script.");
                stop(false);
            }
            
            
            if (chatbox.contains(MessageType.GAME, "I can't reach that!") && !CowPen.contains(myPosition())) {
                    gate.interact("Open");
            }
            
            if (inventory.contains("Raw beef", "Cowhide")) {
            	inventory.dropAll("Raw beef", "Cowhide");
            }
            
            if (getExperienceTracker().getGainedXP(Skill.PRAYER) == 4) {
            	Global.bones = Global.bones + 1;
            }
            
            
            
            if (CowPen.contains(myPosition())){
            	
            	if (!inventory.contains("Bones")) {
            		Global.burrying = false;
            	}
            	
            	if (inventory.contains("Bones") && inventory.isFull() && Global.burrying == false) {
            		inventory.interact("Bury", "Bones");
            		Global.burrying = true;
            		Global.currentStatus = Global.Status.BURRYING;
            	} else {
            		if (Global.burrying == true) {
            			inventory.interact("Bury", "Bones");
            			Global.currentStatus = Global.Status.BURRYING;
            		} else {
            	if (items != null) items.interact("Take");
            	Global.currentStatus = Global.Status.COLLECTING;
            } }
           } else {
        	   walking.walk(CowPen);
           }
            
            return rn; //The amount of time in milliseconds before the loop starts over
           
        }
 
       
       
        @Override 
        public void onPaint(Graphics2D g) {
        	int xp = skills.getExperience(Skill.PRAYER) - Global.startExp;
        String[] status = {"Collecting Bones", "Burrying Bones"};
        String update = null;
        	if (Global.currentStatus == Global.Status.COLLECTING) {
        		update = status[0]; } else { update = status[1];}
        	
            //This is where you will put your code for paint(s)
        	g.drawString("Prayer Xp: " + xp, 15, 290);
        	g.drawString("Bones Burried: " + Global.bones, 15, 310);
        	g.drawString("Status: " + update, 15, 330);
        }
 
    }