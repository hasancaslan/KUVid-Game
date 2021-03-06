package dmme.kuvid.domain.Controllers;

import dmme.kuvid.domain.Collusion.AtomMoleculeCollision;
import dmme.kuvid.domain.Collusion.PowerReactionCollision;
import dmme.kuvid.domain.Collusion.ReactionSurfaceCollision;
import dmme.kuvid.domain.GameObjects.GameObject;
import dmme.kuvid.domain.GameObjects.Molecules.Molecule;
import dmme.kuvid.domain.GameObjects.Position;
import dmme.kuvid.domain.GameObjects.Powerup.PowerUp;
import dmme.kuvid.domain.GameObjects.ReactionBlocker.ReactionBlocker;
import dmme.kuvid.domain.KUVidGame;
import dmme.kuvid.lib.types.AtomType;
import dmme.kuvid.lib.types.Key;
import dmme.kuvid.lib.types.MoleculeType;
import dmme.kuvid.lib.types.ObjectType;
import dmme.kuvid.lib.types.PowerType;
import dmme.kuvid.lib.types.ReactionType;
import dmme.kuvid.utils.observer.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class movementHandler extends Observable{
	
	private int MOLNO = (int) Math.ceil((KUVidGame.getInstance().getNumMolecules()/4.0));
    private int POWERNO = (int) Math.ceil((KUVidGame.getInstance().getNumPowerUp()/4.0));
    private int BLOCKNO = (int) Math.ceil((KUVidGame.getInstance().getNumBlocker()/4.0));
    
    private int alphaPowerNo=1;
    private int betaPowerNo=1;
    private int gammaPowerNo=1;
    private int sigmaPowerNo=1;
    
    private int alphaBlockNo=1;
    private int betaBlockNo=1;
    private int gammaBlockNo=1;
    private int sigmaBlockNo=1;

    private int alphaNo=1;
    private int betaNo=1;
    private int gammaNo=1;
    private int sigmaNo=1;
    
    private int L = KUVidGame.getInstance().getL();
    private int Width = KUVidGame.getInstance().getPlayableArea().width;
    private transient Random rand = new Random();
    private List<GameObject> garbage = new ArrayList<GameObject>();
    private List<GameObject> collidedMol = new ArrayList<GameObject>();
    private List<GameObject> collidedAtom = new ArrayList<GameObject>();
    private List<GameObject> collidedBlocker = new ArrayList<GameObject>();
    private List<GameObject> collidedPower = new ArrayList<GameObject>();
    private List<GameObject> eliminateBlocker = new ArrayList<GameObject>();

    private static movementHandler instance = null;
    

    private movementHandler() {
    }

    public static movementHandler getInstance() {
        if (instance == null)
            instance = new movementHandler();
        return instance;
    }

    public GameObject getRandomAtom() {
        List<GameObject> list = KUVidGame.getGameObjectMap().get(new Key(ObjectType.ATOM, AtomType.randomAtomType()));
        while (list.size() == 0) {
            list = KUVidGame.getGameObjectMap().get(new Key(ObjectType.ATOM, AtomType.randomAtomType()));
        }
        GameObject atom = list.get(this.rand.nextInt(list.size()));
        while (atom.isActive()) {
            atom = list.get(this.rand.nextInt(list.size()));
        }
        return atom;
    }

    public void throwMolecule() {
    	//@requires: gameObject map and alpha,beta,gamma, sigma molecule lists to be created
    	//@modifies: molecule instance fields from random list from gameObject map
    	//           makes active boolean true and gives a random position in x since y is fixed top of screen.
    	//@effects:  This procedure selects a random molecule type gets that types list and activates the first molecule
    	//			 that is not active in that list also sets a random x position for it.
    	
    	MoleculeType t=MoleculeType.randomMoleculeType();
    	List<GameObject> list=KUVidGame.getGameObjectMap().get(new Key(ObjectType.MOLECULE,t));	
    	while(list.size()==0) {
    		t=MoleculeType.randomMoleculeType();
    		list=KUVidGame.getGameObjectMap().get(new Key(ObjectType.MOLECULE,t));
    	}
    	Molecule molecule=null;
    	while(molecule==null) {	
	    	if(t.equals(MoleculeType.ALPHA) && this.alphaNo<=this.MOLNO) {
	    		molecule=(Molecule)list.get(this.MOLNO-this.alphaNo);
	    		this.alphaNo++;
	    	}else if(t.equals(MoleculeType.BETA) && this.betaNo<=this.MOLNO) {
	    		molecule=(Molecule)list.get(this.MOLNO-this.betaNo);
	    		this.betaNo++;
	    	}else if(t.equals(MoleculeType.GAMMA) && this.gammaNo<=this.MOLNO) {
	    		molecule=(Molecule)list.get(this.MOLNO-this.gammaNo);
	    		this.gammaNo++;
	    	}else if(t.equals(MoleculeType.SIGMA) && this.sigmaNo<=this.MOLNO) {
	    		molecule=(Molecule)list.get(this.MOLNO-this.sigmaNo);
	    		this.sigmaNo++;
	    	}
        	t=MoleculeType.randomMoleculeType();
    		list=KUVidGame.getGameObjectMap().get(new Key(ObjectType.MOLECULE,t)); 		
        }      
        molecule.setPosition(new Position(this.rand.nextInt(KUVidGame.getInstance().getPlayableArea().width - 30*L) + 10 * L ,0));
        molecule.setActive(true);
    }

    public void throwBlocker() {

        ReactionType t = ReactionType.randomReactionType();
        List<GameObject> list = KUVidGame.getGameObjectMap().get(new Key(ObjectType.REACTION_BLOCKER, t));
        while (list.size() == 0) {
            t = ReactionType.randomReactionType();
            list = KUVidGame.getGameObjectMap().get(new Key(ObjectType.REACTION_BLOCKER, t));
        }
        ReactionBlocker blocker = null;
        while (blocker == null) {
            if (t.equals(ReactionType.ALPHA_R) && this.alphaBlockNo <= this.BLOCKNO) {
                blocker = (ReactionBlocker) list.get(this.BLOCKNO - this.alphaBlockNo);
                this.alphaBlockNo++;
            } else if (t.equals(ReactionType.BETA_R) && this.betaBlockNo <= this.BLOCKNO) {
                blocker = (ReactionBlocker) list.get(this.BLOCKNO - this.betaBlockNo);
                this.betaBlockNo++;
            } else if (t.equals(ReactionType.GAMMA_R) && this.gammaBlockNo <= this.BLOCKNO) {
                blocker = (ReactionBlocker) list.get(this.BLOCKNO - this.gammaBlockNo);
                this.gammaBlockNo++;
            } else if (t.equals(ReactionType.SIGMA_R) && this.sigmaBlockNo <= this.BLOCKNO) {
                blocker = (ReactionBlocker) list.get(this.BLOCKNO - this.sigmaBlockNo);
                this.sigmaBlockNo++;
            }
            t = ReactionType.randomReactionType();
            list = KUVidGame.getGameObjectMap().get(new Key(ObjectType.REACTION_BLOCKER, t));
        }
        blocker.setPosition(new Position(this.rand.nextInt(Width - 30 * L) + 10 * L, 0));
        blocker.setActive(true);
    }

    public void throwPower() {

        PowerType t = PowerType.randomPowerType();
        List<GameObject> list = KUVidGame.getGameObjectMap().get(new Key(ObjectType.POWER_UP, t));
        while (list.size() == 0) {
            t = PowerType.randomPowerType();
            list = KUVidGame.getGameObjectMap().get(new Key(ObjectType.POWER_UP, t));
        }
        PowerUp power = null;
        while (power == null) {
            if (t.equals(PowerType.ALPHA_B) && this.alphaPowerNo <= this.POWERNO) {
                power = (PowerUp) list.get(this.POWERNO - this.alphaPowerNo);
                this.alphaPowerNo++;
            } else if (t.equals(PowerType.BETA_B) && this.betaPowerNo <= this.POWERNO) {
                power = (PowerUp) list.get(this.POWERNO - this.betaPowerNo);
                this.betaPowerNo++;
            } else if (t.equals(PowerType.GAMMA_B) && this.gammaPowerNo <= this.POWERNO) {
                power = (PowerUp) list.get(this.POWERNO - this.gammaPowerNo);
                this.gammaPowerNo++;
            } else if (t.equals(PowerType.SIGMA_B) && this.sigmaPowerNo <= this.POWERNO) {
                power = (PowerUp) list.get(this.POWERNO - this.sigmaPowerNo);
                this.sigmaPowerNo++;
            }
            t = PowerType.randomPowerType();
            list = KUVidGame.getGameObjectMap().get(new Key(ObjectType.POWER_UP, t));

        }
        power.setPosition(new Position(this.rand.nextInt(Width - 30 * L) + 10 * L, 0));
        power.setDirection(new Position(0, L));
        power.setActive(true);
    }

    public void search() {
       HashMap<Key,List<GameObject>> map= KUVidGame.getGameObjectMap();
       
       for(GameObject atom : KUVidGame.getShootedAtom()) {
    	   int x1 = atom.getPosition().getX();
           int y1 = atom.getPosition().getY();
    	   for(MoleculeType Mtype: MoleculeType.values()) {
    		   List<GameObject> MolList = map.get(new Key(ObjectType.MOLECULE,Mtype));
    		   List<GameObject> BlockList = map.get(new Key(ObjectType.REACTION_BLOCKER,ReactionType.valueOf(Mtype.toString()+"_R")));
    		   for(GameObject mol : MolList) {
    			   if (!mol.isActive()) continue;
    			   int x2 = mol.getPosition().getX();
    	           int y2 = mol.getPosition().getY();
    	           if (Math.abs(x1 - x2) < 10*L && Math.abs(y1 - y2) < 10*L) { 
                      
                      this.collidedMol.add(mol);
                      this.collidedAtom.add(atom);
                      
                      boolean added=false;
                      for(GameObject block : BlockList) {
                    	  if (!block.isActive()) continue;
           			      int xb = block.getPosition().getX();
           	              int yb = block.getPosition().getY();
           	              if (Math.abs(x1 - xb) < 15*L && Math.abs(y1 - yb) < 15*L) { 
           	            	  this.collidedBlocker.add(block);
                              added=true;
           	              }
                      }
                      if(!added) {
                    	  this.collidedBlocker.add(null);
                      }
    	           }
    		   }
    	   }
       }
       
       
       for(int i=0; i<this.collidedMol.size();i++) {
    	   
    	   if(this.collidedBlocker.get(i)==null) {
    		   new AtomMoleculeCollision(this.collidedAtom.get(i), this.collidedMol.get(i),false);
    	   }else {
    		   if((this.collidedMol.get(i).getSubType().toString()+"_R").equals(this.collidedBlocker.get(i).getSubType().toString())) {
    			   new AtomMoleculeCollision(this.collidedAtom.get(i), this.collidedMol.get(i),true);
    			   System.out.println("BLOCKED");
    			   destroyHandler.getInstance().destroyObject(this.collidedBlocker.get(i));
    		   }else {
    			   new AtomMoleculeCollision(this.collidedAtom.get(i), this.collidedMol.get(i),false);
    			   System.out.println("TYPE MISS");
    		   }
    	   }
       }
       this.collidedAtom.clear();
       this.collidedMol.clear();
       this.collidedBlocker.clear();
       
       for(GameObject power : KUVidGame.getShootedPower()) {
    	   int x1 = power.getPosition().getX();
           int y1 = power.getPosition().getY();
    	   for(MoleculeType Mtype: MoleculeType.values()) {
    		   List<GameObject> BlockList = map.get(new Key(ObjectType.REACTION_BLOCKER,ReactionType.valueOf(Mtype.toString()+"_R")));
    		   for(GameObject block : BlockList) {
    			   if (!block.isActive()) continue;
    			   int xb = block.getPosition().getX();
       	           int yb = block.getPosition().getY();
    	           if (Math.abs(x1 - xb) < 10*L && Math.abs(y1 - yb) < 10*L) {  
                      this.collidedPower.add(power);
                      this.eliminateBlocker.add(block);
    	           }
    		   }
    	   }
       }
       
       for(int i=0; i<this.eliminateBlocker.size();i++) {
    	   new PowerReactionCollision(this.collidedPower.get(i),this.eliminateBlocker.get(i));
       }
       this.collidedPower.clear();
       this.eliminateBlocker.clear();
    }

    public void move() {
    	int L=KUVidGame.getInstance().getL();
    	HashMap<Key,List<GameObject>> map= KUVidGame.getGameObjectMap();
        for (Key k: map.keySet()) {
        	 List<GameObject> gameObjectList = map.get(k);
	        for (GameObject gameObject : gameObjectList) {
	        	if(gameObject.isActive()) {
	        		gameObject.move();
	        		
	        		if(gameObject.getType().equals(ObjectType.REACTION_BLOCKER)) {
	        			int shooterPosition= KUVidGame.getInstance().getShooter().getPosition();
	        			int objectPosition= gameObject.getPosition().getX();
	        			if((Math.abs(shooterPosition-objectPosition)<5*L)&& gameObject.isActive()&&gameObject.getPosition().getY()>(KUVidGame.getInstance().getPlayableArea().height-20*L)) {
	        				this.garbage.add(gameObject);
	        			}else if(gameObject.getPosition().getY()>(KUVidGame.getInstance().getPlayableArea().height-10*L) && gameObject.isActive()) {
			            	this.garbage.add(gameObject);
			            	this.searchBlocker(gameObject);
			        	}
	        		}else if(gameObject.getType().equals(ObjectType.POWER_UP)) {
	        			int shooterPosition= KUVidGame.getInstance().getShooter().getPosition();
	        			int objectPosition= gameObject.getPosition().getX();
	        			if((Math.abs(shooterPosition-objectPosition)<5*L)&& gameObject.isActive()&&gameObject.getPosition().getY()>(KUVidGame.getInstance().getPlayableArea().height-20*L)&&(!KUVidGame.getPowerArsenal().get(gameObject.getSubType()).contains(gameObject))) {
	        				this.garbage.add(gameObject);
	        				KUVidGame.getPowerArsenal().get(gameObject.getSubType()).add((PowerUp) gameObject);
	        				publishPropertyEvent("updatePower",null,null);
	        			}else if(gameObject.getPosition().getY()>(KUVidGame.getInstance().getPlayableArea().height-10*L) && gameObject.isActive()) {
			            	this.garbage.add(gameObject);
			        	}
	        		}else {
	        			if(gameObject.getPosition().getY()>KUVidGame.getInstance().getPlayableArea().height && gameObject.isActive()) {
			            	this.garbage.add(gameObject);
			        	}
	        		}
	        	}
	        }
        }

        for (GameObject atom : KUVidGame.getShootedAtom()) {
            atom.move();
            if (atom.getPosition().getY() < 0) {
                this.garbage.add(atom);
            }
        }

        for (GameObject power : KUVidGame.getShootedPower()) {
            power.move();
            if (power.getPosition().getY() < 0) {
                this.garbage.add(power);
            }
        }
        
        for(GameObject gameObject: garbage) {
        	if(gameObject.getType().equals(ObjectType.REACTION_BLOCKER)) {
        		//this.searchBlocker(gameObject);
        		new ReactionSurfaceCollision(gameObject);
        	}else {
        		destroyHandler.getInstance().destroyObject(gameObject);
        	}
        }
        garbage.clear();
        this.search();
    }
    
    public void searchBlocker(GameObject blocker) {
    	double x=blocker.getPosition().getX();
    	double y=blocker.getPosition().getY();
    	HashMap<Key,List<GameObject>> map= KUVidGame.getGameObjectMap();
        for (Key k: map.keySet()) {
        	 List<GameObject> gameObjectList = map.get(k);
	        for (GameObject gameObject : gameObjectList) {
	        	if(gameObject.isActive() && !gameObject.getType().equals(ObjectType.REACTION_BLOCKER)) {
	        		double objectX=gameObject.getPosition().getX();
	        		double objectY=gameObject.getPosition().getY();
	        		double dist=Math.hypot(Math.abs(x-objectX),Math.abs(y-objectY));
	        		if(dist<=20*L) {
	        			this.garbage.add(gameObject);
	        		}
	        	}
	        }
        }
    }

    public void run() {
        search();
        move();
    }
    
    public int numMolToThrow() {
    	return (this.MOLNO*4)-(this.alphaNo+this.betaNo+this.sigmaNo+this.gammaNo-4);
    }
    
    public int numBlockToThrow() {
    	return (this.BLOCKNO*4)-(this.alphaBlockNo+this.betaBlockNo+this.sigmaBlockNo+this.gammaBlockNo-4);
    }
    
    public int numPowerToThrow() {
    	return (this.POWERNO*4)-(this.alphaPowerNo+this.betaPowerNo+this.sigmaPowerNo+this.gammaPowerNo-4);
    }
    
    public static void setInstance(movementHandler init) {
    	movementHandler.instance=init;
    }
}