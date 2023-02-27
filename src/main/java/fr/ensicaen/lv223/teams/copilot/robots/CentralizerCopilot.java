package fr.ensicaen.lv223.teams.copilot.robots;

import fr.ensicaen.lv223.model.agent.command.CommandFactory;
import fr.ensicaen.lv223.model.agent.robot.RobotType;
import fr.ensicaen.lv223.model.agent.robot.message.Message;
import fr.ensicaen.lv223.model.agent.robot.specials.Centralizer;
import fr.ensicaen.lv223.model.logic.agentInterface.PlanetInterface;

public class CentralizerCopilot extends Centralizer {
    public CentralizerCopilot(RobotType type, CommandFactory commandFactory, PlanetInterface captors) {
        super(type, commandFactory, captors);
    }

    @Override
    public boolean isAvailable(Message m) {
        return false;
    }
}
