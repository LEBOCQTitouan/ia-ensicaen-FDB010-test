package fr.ensicaen.lv223.teams.jamesbond.robot.robots;

import fr.ensicaen.lv223.model.agent.command.CommandFactory;
import fr.ensicaen.lv223.model.agent.robot.RobotType;
import fr.ensicaen.lv223.model.agent.robot.message.Message;
import fr.ensicaen.lv223.model.agent.robot.specials.FoodRetriever;

public class FoodRetrieverJB extends FoodRetriever {
    public FoodRetrieverJB(RobotType type, CommandFactory commandFactory) {
        super(type, commandFactory);
    }

    @Override
    public boolean isAvailable( Message m ) {
        return false;
    }
}