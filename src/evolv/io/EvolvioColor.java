package evolv.io;

import evolv.io.creature.Creature;
import evolv.io.environment.Camera;
import evolv.io.environment.Environment;
import evolv.io.environment.Terrain;
import evolv.io.peripherals.MouseAction;
import evolv.io.peripherals.MouseButton;
import evolv.io.peripherals.Peripherals;
import evolv.io.ui.UI;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import processing.core.PApplet;
import processing.core.PFont;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class EvolvioColor extends PApplet {

    private enum DragMode {
        NONE,
        SCREEN,
        MIN_TEMPERATURE,
        MAX_TEMPERATURE,
    }
    private static final List<BoardAction> BOARD_ACTIONS = Arrays.asList(new BoardAction.ToggleUserControl(),
            new BoardAction.ChangeSpawnChance(), new BoardAction.PrepareForFileSave(0),
            new BoardAction.ChangeImageSaveInterval(), new BoardAction.PrepareForFileSave(2),
            new BoardAction.ChangeTextSaveInterval(), new BoardAction.ChangePlaySpeed(),
            new BoardAction.ToggleRender());

    private final int seed = parseInt(random(1000000));
    private final Peripherals peripherals = new Peripherals();
    private final UI ui = new UI();

    private Environment evoBoard;
    private int windowWidth;
    private int windowHeight;

    private DragMode dragMode;
    private float prevMouseX;
    private float prevMouseY;
    private boolean draggedFar = false;

    public static void main(String[] passedArgs) {
        String[] appletArgs = new String[]{"evolv.io.EvolvioColor"};
        if (passedArgs != null) {
            PApplet.main(concat(appletArgs, passedArgs));
        } else {
            PApplet.main(appletArgs);
        }
    }

    @Override
    public void settings() {
        // Get users window size
        this.windowWidth = displayWidth;
        this.windowHeight = displayHeight;

        // Set scaling to be custom to current users screen size
        // Allow window to be docked and resized the UI still needs to be
        // changed to make UI look good after resize
        size(windowWidth, windowHeight);
    }

    @Override
    public void setup() {
        surface.setResizable(true);
        ui.setLocation(windowWidth / 2, 0);
        ui.setWidth(windowWidth / 2);
        ui.setHeight(height);
        colorMode(HSB, 1.0f);
        PFont font = createDefaultFont(12);
        textFont(font);
        this.evoBoard = new Environment(this);
        Camera cam = evoBoard.getCam();
        cam.setScaleFactor(windowHeight / Configuration.BOARD_HEIGHT / Configuration.SCALE_TO_FIXBUG);
        cam.resetZoom();
        peripherals.onMouse(MouseButton.NONE, MouseAction.WHEEL, evoBoard.getCam()::zoom);
        peripherals.onMouse(MouseButton.LEFT, MouseAction.PRESS, this::mouseDown);
        peripherals.onMouse(MouseButton.LEFT, MouseAction.RELEASE, this::mouseUp);
    }

    @Override
    public void draw() {
        update();
        respondToUser();
        render();
    }

    private void update() {
//        for (int iteration = 0; iteration < evoBoard.getPlaySpeed(); iteration++) {
        evoBoard.update(Configuration.TIME_STEP);
        ui.update(evoBoard, Configuration.TIME_STEP);
//        }
    }

    private void respondToUser() {
        Camera cam = evoBoard.getCam();
        if (dist(prevMouseX, prevMouseY, mouseX, mouseY) > 5) {
            draggedFar = true;
        }
        if (null != dragMode) {
            switch (dragMode) {
                case SCREEN:
                    Terrain ground = evoBoard.getGround();
                    cam.moveX(-(ground.toWorldXCoordinate(mouseX, mouseY) - ground.toWorldXCoordinate(prevMouseX, prevMouseY)));
                    cam.moveY(-(ground.toWorldYCoordinate(mouseX, mouseY) - ground.toWorldYCoordinate(prevMouseX, prevMouseY)));
                    break;
//                case MIN_TEMPERATURE:
//                    if (evoBoard.setMinTemperature(1.0f - (mouseY - 30) / 660.0f)) {
//                        dragMode = DragMode.MAX_TEMPERATURE;
//                    }
//                    break;
//                case MAX_TEMPERATURE:
//                    if (evoBoard.setMaxTemperature(1.0f - (mouseY - 30) / 660.0f)) {
//                        dragMode = DragMode.MIN_TEMPERATURE;
//                    }
//                    break;
                default:
                    break;
            }
        }
        if (evoBoard.getPopulation().getSelectedCreature() != null) {
            double cameraX = evoBoard.getPopulation().getSelectedCreature().getxLocation();
            double cameraY = evoBoard.getPopulation().getSelectedCreature().getyLocation();
            cam.setLocation(cameraX, cameraY);
        } else {
            cam.setRotation(0);
        }
    }

    private void render() {
        Camera cam = evoBoard.getCam();
        pushMatrix();
        scale((float) cam.getScaleFactor());
        fill(Color.BLACK.getRGB());
        rect(0, 0, Configuration.BOARD_WIDTH * Configuration.SCALE_TO_FIXBUG, Configuration.BOARD_HEIGHT * Configuration.SCALE_TO_FIXBUG);
        translate(Configuration.BOARD_WIDTH * 0.5f * Configuration.SCALE_TO_FIXBUG,
                Configuration.BOARD_HEIGHT * 0.5f * Configuration.SCALE_TO_FIXBUG);
        float zoom = (float) cam.getZoom();
        scale(zoom);
//        if (evoBoard.isUserControl() && evoBoard.getSelectedCreature() != null) {
//            rotate(cameraR);
//        }
        translate((float) -cam.getxLocation() * Configuration.SCALE_TO_FIXBUG, (float) -cam.getyLocation() * Configuration.SCALE_TO_FIXBUG);
        evoBoard.render(this, Configuration.SCALE_TO_FIXBUG, zoom);
        popMatrix();
        ui.render(this, Configuration.SCALE_TO_FIXBUG, zoom);
        prevMouseX = mouseX;
        prevMouseY = mouseY;
    }

    @Override
    protected void handleMouseEvent(MouseEvent mouseEvent) {
        super.handleMouseEvent(mouseEvent);
        peripherals.handleMouseEvent(mouseEvent);
    }

    @Override
    protected void handleKeyEvent(KeyEvent keyEvent) {
        super.handleKeyEvent(keyEvent);
        peripherals.handleKeyEvent(keyEvent);
    }

    private void mouseDown(MouseEvent event) {
        if (mouseX < windowHeight) {
            dragMode = DragMode.SCREEN;
        } else {
            Camera cam = evoBoard.getCam();
            Creature selectedCreature = evoBoard.getPopulation().getSelectedCreature();
            if (abs(mouseX - (windowHeight + 65)) <= 60 && abs(mouseY - 147) <= 60
                    && selectedCreature != null) {
                cam.setxLocation(selectedCreature.getxLocation());
                cam.setyLocation(selectedCreature.getyLocation());
                cam.setZoom(16);
            } else if (mouseY >= 95 && mouseY < 135 && selectedCreature == null) {
                if (mouseX >= windowHeight + 10 && mouseX < windowHeight + 230) {
                    cam.resetZoom();
                } else if (mouseX >= windowHeight + 240 && mouseX < windowHeight + 460) {
                    if (mouseButton == LEFT) {
//                        evoBoard.incrementSortMetric();
                    } else if (mouseButton == RIGHT) {
//                        evoBoard.decrementSortMetric();
                    }
                }
            } else if (mouseY >= 570) {
                float x = (mouseX - (windowHeight + 10));
                float y = (mouseY - 570);
                boolean clickedOnLeft = (x % 230 < 110);
                if (x >= 0 && x < 460 && y >= 0 && y < 200 && x % 230 < 220 && y % 50 < 40) {
                    // 460 = 2 * 230 and 200 = 4 * 50
                    int mX = (int) (x / 230);
                    int mY = (int) (y / 50);
                    int buttonNum = mX + mY * 2;
                    BOARD_ACTIONS.get(buttonNum).doAction(evoBoard, clickedOnLeft);
                }
            } else if (mouseX >= height + 10 && mouseX < width - 50 && evoBoard.getPopulation().getSelectedCreature() == null) {
//                int listIndex = (mouseY - 150) / 70;
//                if (evoBoard.getCreatureInList(listIndex) != null) {
//                    evoBoard.setSelectedCreature(evoBoard.getCreatureInList(listIndex));
//                    cameraX = (float) evoBoard.getSelectedCreature().getPx();
//                    cameraY = (float) evoBoard.getSelectedCreature().getPy();
//                    zoom = 16;
//                }
            }
            if (mouseX >= width - 50) {
//                float toClickTemp = (mouseY - 30) / 660.0f;
//                float lowTemp = 1.0f - evoBoardgetLowTempProportion();
//                float highTemp = 1.0f - evoBoard.getHighTempProportion();
//                if (abs(toClickTemp - lowTemp) < abs(toClickTemp - highTemp)) {
//                    dragMode = DragMode.MIN_TEMPERATURE;
//                } else {
//                    dragMode = DragMode.MAX_TEMPERATURE;
//                }
            }
        }
        draggedFar = false;
    }

    private void mouseUp(MouseEvent event) {
        if (!draggedFar) {
            if (mouseX < windowHeight) {
                // DO NOT LOOK AT THIS CODE EITHER it is bad
                dragMode = DragMode.SCREEN;
                double mX = evoBoard.getGround().toWorldXCoordinate(mouseX, mouseY);
                double mY = evoBoard.getGround().toWorldYCoordinate(mouseX, mouseY);
                int x = (int) Math.floor(mX);
                int y = (int) Math.floor(mY);
//                evoBoard.unselect();
                Camera cam = evoBoard.getCam();
                cam.setRotation(0);
                if (x >= 0 && x < Configuration.BOARD_WIDTH && y >= 0 && y < Configuration.BOARD_HEIGHT) {
                    evoBoard.getGround().getObjectsNear(x, y, 1).stream().filter((body) -> (body instanceof Creature)).forEach((body) -> {
                        double distance = body.distance(mX, mY, body.getxLocation(), body.getyLocation());
                        if (distance <= body.getBodySize()) {
                            evoBoard.getPopulation().setSelectedCreature((Creature) body);
                            cam.setZoom(16);
                        }
                    });
                }
            }
        }
        dragMode = DragMode.NONE;
    }
}
