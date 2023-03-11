package fr.ensicaen.lv223.teams.jamesbond.robots;

import fr.ensicaen.lv223.model.agent.command.CommandFactory;
import fr.ensicaen.lv223.model.agent.robot.Robot;
import fr.ensicaen.lv223.model.agent.robot.RobotType;
import fr.ensicaen.lv223.model.agent.robot.message.Message;
import fr.ensicaen.lv223.model.agent.robot.specials.Centralizer;
import fr.ensicaen.lv223.model.environment.cells.CellType;
import fr.ensicaen.lv223.model.logic.agentInterface.PlanetInterface;
import fr.ensicaen.lv223.model.logic.localisation.Coordinate;
import fr.ensicaen.lv223.model.logic.localisation.Direction;
import fr.ensicaen.lv223.teams.jamesbond.UnknownCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CentralizerJB extends Centralizer implements RobotInterfaceJB{
    private final List<List<UnknownCell>> cells;

    private static CentralizerJB instance;

    private static final int WIDTH_MAP = 21;
    private static final int HEIGHT_MAP = 21;

    private int oreQttToExtract;
    private final int foodQttToExtract;

    private CentralizerJB(RobotType type, CommandFactory commandFactory, PlanetInterface captors) {
        super(type, commandFactory, captors);
        this.oreQttToExtract = 200;
        this.foodQttToExtract = 200;
        cells = new ArrayList<>();
        for(int i = 0; i< WIDTH_MAP; i++){
            cells.add(new ArrayList<>());
            for(int j = 0; j< HEIGHT_MAP; j++){
                cells.get(i).add(new UnknownCell(i,j));
            }
        }
    }

    public static CentralizerJB getInstance(RobotType type, CommandFactory commandFactory, PlanetInterface captors){
        if(instance == null){
            instance = new CentralizerJB(type, commandFactory, captors);
        }
        return instance;
    }

    public void takeHitOnOre() {
        this.oreQttToExtract = (int) (this.oreQttToExtract*0.90);
    }
    @Override
    public boolean isAvailable(Message m) {
        return false;
    }

    public List<List<UnknownCell>> getCells(){
        return cells;
    }

    public void updateMap(HashMap<Direction, CellType> map, Robot robot){
        int x = robot.getPos_x();
        int y = robot.getPos_y();
        for(Direction d : map.keySet()){
            switch (d) {
                case NORTH -> cells.get(x - 1).get(y).update(map.get(d));
                case SOUTH -> cells.get(x + 1).get(y).update(map.get(d));
                case EAST -> cells.get(x).get(y + 1).update(map.get(d));
                case WEST -> cells.get(x).get(y - 1).update(map.get(d));
                case NORTH_EAST -> cells.get(x - 1).get(y + 1).update(map.get(d));
                case NORTH_WEST -> cells.get(x - 1).get(y - 1).update(map.get(d));
                case SOUTH_EAST -> cells.get(x + 1).get(y + 1).update(map.get(d));
                case SOUTH_WEST -> cells.get(x + 1).get(y - 1).update(map.get(d));
            }
        }
    }

    @Override
    public CentralizerJB getCentralizer() {
        return this;
    }

    @Override
    public void updateCentralizerMap() {
        this.updateMap(captors.getSurrounding(this), this);
    }

    @Override
    public Coordinate getPosition() {
        return new Coordinate(WIDTH_MAP/2,HEIGHT_MAP/2);
    }

    public int getQttToExtract(CellType type){
        if(type == CellType.ORE){
            return oreQttToExtract;
        }
        else{
            return foodQttToExtract;
        }
    }

    public UnknownCell findCellToExtract(CellType type) {
        UnknownCell closestCell = null;
        int distance = Integer.MAX_VALUE;
        for (List<UnknownCell> list : cells) {
            for (UnknownCell cell : list) {
                //todo : check if cell is not already extracted
                if (cell.getType() == type && !cell.isFocused()) {
                    if(closestCell == null || Math.max(Math.abs(getPos_x() - cell.getX()), Math.abs(getPos_y() - cell.getY())) < distance){
                        closestCell = cell;
                        distance = Math.max(Math.abs(closestCell.getX() - cell.getX()), Math.abs(closestCell.getY() - cell.getY()));
                    }
                }
            }
        }
        return closestCell;
    }
}
