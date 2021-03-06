package com.example.a4;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class ShipView extends StackPane implements ShipModelSubscriber {
    Canvas myCanvas;
    GraphicsContext gc;
    ShipModel model;
    InteractionModel iModel;

    public ShipView() {
        myCanvas = new Canvas(1000,700);
        gc = myCanvas.getGraphicsContext2D();
        this.getChildren().add(myCanvas);
        this.setStyle("-fx-background-color: black");
    }

    public void setModel(ShipModel newModel) {
        model = newModel;
    }

    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    public void setController(ShipController controller) {
        myCanvas.setOnMousePressed(e -> controller.handlePressed(e.getX(),e.getY(), e));
        myCanvas.setOnMouseDragged(e -> controller.handleDragged(e.getX(),e.getY(), e));
        myCanvas.setOnMouseReleased(e -> controller.handleReleased(e.getX(),e.getY(), e));
    }

    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        if (model.rect != null) {
            gc.setStroke(Color.WHITE);
            gc.setFill(Color.rgb(183, 133, 37));
            gc.fillRect(model.rect.left, model.rect.top,
                    model.rect.right-model.rect.left, model.rect.bottom-model.rect.top);
        }
        model.ships.forEach(ship -> {
            if (!ship.hasChildren()) {
                drawShip(ship.getShip());
            }
            else {
                if (iModel.getSelected() != null && iModel.getSelected().contains(ship)) {
                    gc.setStroke(Color.WHITE);
                    gc.strokeRect(ship.getLeft(), ship.getTop(),
                            ship.getRight() - ship.getLeft(), ship.getBottom() - ship.getTop());
                }
                drawGroup(ship);
            }
        });
        iModel.getSelectedShips().forEach(this::drawSelectedShip);
    }

    private void drawShip(Ship ship) {
        gc.setStroke(Color.YELLOW);
        gc.setFill(Color.CORAL);
        gc.fillPolygon(ship.displayXs, ship.displayYs, ship.displayXs.length);
        gc.strokePolygon(ship.displayXs, ship.displayYs, ship.displayXs.length);
    }

    private void drawSelectedShip(Ship ship) {
        gc.setFill(Color.YELLOW);
        gc.setStroke(Color.CORAL);
        gc.fillPolygon(ship.displayXs, ship.displayYs, ship.displayXs.length);
        gc.strokePolygon(ship.displayXs, ship.displayYs, ship.displayXs.length);
    }

    private void drawGroup(Groupable group) {
        if (!group.hasChildren()) {
            drawShip(group.getShip());
        }
        else {
            group.getChildren().forEach(this::drawGroup);
        }
    }

    @Override
    public void modelChanged() {
        draw();
    }
}
