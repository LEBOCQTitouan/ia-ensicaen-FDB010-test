package fr.ensicaen.lv223.teams.jamesbond.objectifs;

import fr.ensicaen.lv223.model.agent.command.Command;
import fr.ensicaen.lv223.model.agent.command.CommandType;
import fr.ensicaen.lv223.model.agent.robot.Robot;
import fr.ensicaen.lv223.model.agent.robot.objectif.Objectif;
import fr.ensicaen.lv223.model.agent.robot.specials.FoodRetriever;
import fr.ensicaen.lv223.model.agent.robot.specials.OreExtractor;
import fr.ensicaen.lv223.model.environment.cells.Cell;
import fr.ensicaen.lv223.model.environment.cells.CellType;
import fr.ensicaen.lv223.model.logic.localisation.Coordinate;
import fr.ensicaen.lv223.teams.ProjectTeam;
import fr.ensicaen.lv223.teams.jamesbond.UnknownCell;
import fr.ensicaen.lv223.teams.jamesbond.robots.CentralizerJB;
import fr.ensicaen.lv223.teams.jamesbond.robots.FoodRetrieverJB;
import fr.ensicaen.lv223.teams.jamesbond.robots.OreExtractorJB;
import fr.ensicaen.lv223.teams.jamesbond.robots.RobotInterfaceJB;
import fr.ensicaen.lv223.util.astar.Astar;

import java.util.*;

import static fr.ensicaen.lv223.util.Util.cellListToCommandList;

public class CollectObjectif implements Objectif {
    private Robot robot;
    private CentralizerJB centralizer;

    private UnknownCell focusedCell;
    private CellType focusedCellType;

    public CollectObjectif(Robot currentRobot, CentralizerJB centralizer) {
        this.robot = currentRobot;
        this.centralizer = centralizer;
        focusedCell = null;
        if(robot instanceof OreExtractor) {
            focusedCellType = CellType.ORE;
        } else if(robot instanceof FoodRetriever) {
            focusedCellType = CellType.FOOD;
        }
    }

    @Override
    public Queue<Command> generateCommmandList() {
        LinkedList<Command> commandes = new LinkedList<>();
        // Si le robot est plein, retourner au centralizer
        if((robot instanceof OreExtractorJB && ((OreExtractorJB) robot).isFull()) || (robot instanceof FoodRetrieverJB && ((FoodRetrieverJB) robot).isFull())) {
            System.out.println("Je suis foooooooooul");
            List<List<UnknownCell>> map = centralizer.getCells();
            UnknownCell start = map.get(robot.getPos_x()).get(robot.getPos_y());
            UnknownCell end = map.get(centralizer.getPos_x()).get(centralizer.getPos_y());
            Astar astar = new Astar(map, start, end);

            astar.compute();
            ArrayList<Cell> cellPath = (ArrayList<Cell>) astar.getPath();
            return cellListToCommandList(cellPath, ProjectTeam.JAMES_BOND, robot);
        }

        // Si pas de cellule de minerais ciblée, en trouver une
        if (this.focusedCell == null) {
            this.focusedCell = this.centralizer.findCellToExtract(this.focusedCellType);
            if(this.focusedCell != null) {
                this.focusedCell.setFocusedRobot(this.robot);
            }
        }
        // Si on a une cellule de minerais ciblée :
        if(this.focusedCell != null){
            // Si on est déjà dessus, on extrait
            if(this.focusedCell.getX() == robot.getPos_x() && this.focusedCell.getY() == robot.getPos_y()) {
                System.out.println("Je suis sur la cellule MAMAAAAAAAAAAAAAAAAAAAAAN !");
                System.out.println("Position : " + robot.getPos_x() + " " + robot.getPos_y());
                System.out.println("Cellule : " + this.focusedCell.getX() + " " + this.focusedCell.getY());
                System.out.println("Type : " + this.focusedCell.getType());
                int amount = this.centralizer.getQttToExtract(this.focusedCellType);
                if(robot instanceof OreExtractorJB) {
                    amount = Math.min(amount, ((OreExtractorJB) robot).getMaxQttToExtract());
                } else if(robot instanceof FoodRetrieverJB) {
                    amount = Math.min(amount, ((FoodRetrieverJB) robot).getMaxQttToExtract());
                }
                commandes.add(this.robot.getCommandFactory().createCommand(this.robot, CommandType.EXTRACT, amount));
                return commandes;
            }
            // Sinon, on se rend sur la cellule
            else{
                List<List<UnknownCell>> map = centralizer.getCells();
                UnknownCell start = map.get(robot.getPos_x()).get(robot.getPos_y());
                UnknownCell end = map.get(this.focusedCell.getX()).get(this.focusedCell.getY());
                Astar astar = new Astar(map, start, end);

                astar.compute();
                ArrayList<Cell> cellPath = (ArrayList<Cell>) astar.getPath();
                return cellListToCommandList(cellPath, ProjectTeam.JAMES_BOND, robot);
            }
        }
        // si pas de cellules trouvé ???????????? Pour l'instant on attend
        else {
            commandes.add(this.robot.getCommandFactory().createCommand(this.robot, CommandType.IDLE, 0));
            return commandes;
        }
    }

    private void oreExtractorAction() {
    }

    private void foodRetrieverAction() {
    }
}
