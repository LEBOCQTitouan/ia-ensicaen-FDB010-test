package fr.ensicaen.lv223.presenter.planet;

import fr.ensicaen.lv223.model.environment.cells.Cell;
import fr.ensicaen.lv223.model.environment.construction.WaterPipe;
import fr.ensicaen.lv223.model.environment.planet.Planet;
import fr.ensicaen.lv223.presenter.IPresenter;
import fr.ensicaen.lv223.view.CellView;

import java.util.ArrayList;
import java.util.List;

import static fr.ensicaen.lv223.util.Map.MAP_HEIGHT;
import static fr.ensicaen.lv223.util.Map.MAP_WIDTH;

public class PlanetPresenter {
    private IPresenter view;
    private final Planet planet;

    public PlanetPresenter(Planet planet) {
        this.planet = planet;
    }

    public void drawPlanet() {
        List<List<Cell>> cells = planet.getCells();
        List<List<CellView>> cellsView = new ArrayList<>();

        for (int i = 0; i < cells.size(); i++) {
            cellsView.add(new ArrayList<>(cells.get(0).size()));
            for (int j = 0; j < cells.get(i).size(); j++) {
                cellsView.get(i).add(
                        new CellView(
                                view.getSceneWidth() / (double)MAP_WIDTH,
                                view.getSceneHeight() / (double)MAP_HEIGHT,
                                cells.get(i).get(j).getType(),
                                WaterPipe.hasPipe(i, j) != null,
                                i,
                                j
                        )
                );
            }
        }

        view.setCellsView(cellsView);
        view.draw();
    }

    public void setView(IPresenter view) {
        this.view = view;
    }

    public void updateStatus(){
        view.updateStock(planet.getStockFood(), planet.getStockWater(), planet.getStockMineral());
    }
}
