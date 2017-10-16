/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolv.io.environment.rendering;

/**
 *
 * @author quentin
 */
public class Terrainrenderer {
//
//    public void drawUI(float scaleUp, float camZoom, double timeStep, int x1, int y1, int x2, int y2) {
//        this.evolvioColor.fill(0, 0, 0);
//        this.evolvioColor.noStroke();
//        this.evolvioColor.rect(x1, y1, x2 - x1, y2 - y1);
//
//        this.evolvioColor.pushMatrix();
//        this.evolvioColor.translate(x1, y1);
//
//        this.evolvioColor.fill(0, 0, 1);
//        this.evolvioColor.textAlign(EvolvioColor.RIGHT);
//        this.evolvioColor.text(EvolvioColor.nfs(camZoom * 100, 0, 3) + " %", 0, y2 - y1 - 30);
//        this.evolvioColor.textAlign(EvolvioColor.LEFT);
//        this.evolvioColor.textSize(48);
//        String yearText = "Year " + EvolvioColor.nf((float) year, 0, 2);
//        this.evolvioColor.text(yearText, 10, 48);
//        float seasonTextXCoor = this.evolvioColor.textWidth(yearText) + 50;
//        this.evolvioColor.textSize(20);
//        this.evolvioColor.text("Population: " + creatures.size(), 10, 80);
//        String[] seasons = {"Winter", "Spring", "Summer", "Autumn"};
//        this.evolvioColor.text(seasons[(int) (getSeason() * 4)] + "\nSeed: " + randomSeed, seasonTextXCoor, 30);
//
//        if (selectedCreature == null) {
//        } else {
//            float energyUsage = (float) selectedCreature.getEnergyUsage(timeStep);
//            this.evolvioColor.noStroke();
//            if (energyUsage <= 0) {
//                this.evolvioColor.fill(0, 1, 0.5f);
//            } else {
//                this.evolvioColor.fill(0.33f, 1, 0.4f);
//            }
//            float EUbar = 20 * energyUsage;
//            this.evolvioColor.rect(110, 280, EvolvioColor.min(EvolvioColor.max(EUbar, -110), 110), 25);
//            if (EUbar < -110) {
//                this.evolvioColor.rect(0, 280, 25, (-110 - EUbar) * 20 + 25);
//            } else if (EUbar > 110) {
//                float h = (EUbar - 110) * 20 + 25;
//                this.evolvioColor.rect(185, 280 - h, 25, h);
//            }
//            this.evolvioColor.fill(0, 0, 1);
//            this.evolvioColor.text("Name: " + selectedCreature.getName(), 10, 225);
//            this.evolvioColor.text(
//                    "Energy: " + EvolvioColor.nf(100 * (float) selectedCreature.getEnergy(), 0, 2) + " yums", 10, 250);
//            this.evolvioColor.text("" + EvolvioColor.nf(100 * energyUsage, 0, 2) + " yums/year", 10, 275);
//
//            this.evolvioColor.text("ID: " + selectedCreature.getId(), 10, 325);
//            this.evolvioColor.text("X: " + EvolvioColor.nf((float) selectedCreature.getxLocation(), 0, 2), 10, 350);
//            this.evolvioColor.text("Y: " + EvolvioColor.nf((float) selectedCreature.getyLocation(), 0, 2), 10, 375);
//            this.evolvioColor.text("Rotation: " + EvolvioColor.nf((float) selectedCreature.getRotation(), 0, 2), 10,
//                    400);
//            this.evolvioColor.text("Birthday: " + toDate(selectedCreature.getBirthTime()), 10, 425);
//            this.evolvioColor.text("(" + toAge(selectedCreature.getAge()) + ")", 10, 450);
//            this.evolvioColor.text("Generation: " + selectedCreature.getGen(), 10, 475);
//            this.evolvioColor.text("Parents: " + selectedCreature.getParents(), 10, 500, 210, 255);
//            this.evolvioColor.text("Hue: " + EvolvioColor.nf((float) (selectedCreature.getBodyHue()), 0, 2), 10, 550, 210,
//                    255);
//            this.evolvioColor.text("Mouth Hue: " + EvolvioColor.nf((float) (selectedCreature.getMouthHue()), 0, 2), 10,
//                    575, 210, 255);
//
//            if (userControl) {
//                this.evolvioColor.text(
//                        "Controls:\nUp/Down: Move\nLeft/Right: Rotate\nSpace: Eat\nF: Fight\nV: Vomit\nU, J: Change color"
//                        + "\nI, K: Change mouth color\nB: Give birth (Not possible if under "
//                        + Math.round((Configuration.MANUAL_BIRTH_SIZE + 1) * 100) + " yums)",
//                        10, 625, 250, 400);
//            }
//            this.evolvioColor.pushMatrix();
//            this.evolvioColor.translate(400, 80);
//            float apX = EvolvioColor
//                    .round(((this.evolvioColor.mouseX) - 400 - Brain.NEURON_OFFSET_X - x1) / 50.0f / 1.2f);
//            float apY = EvolvioColor.round((this.evolvioColor.mouseY - 80 - Brain.NEURON_OFFSET_Y - y1) / 50.0f);
//            selectedCreature.drawBrain(50, (int) apX, (int) apY);
//            this.evolvioColor.popMatrix();
//        }
//
//        drawPopulationGraph(x1, x2, y1, y2);
//        this.evolvioColor.fill(0, 0, 0);
//        this.evolvioColor.textAlign(EvolvioColor.RIGHT);
//        this.evolvioColor.textSize(24);
//        this.evolvioColor.text("Population: " + creatures.size(), x2 - x1 - 10, y2 - y1 - 10);
//        this.evolvioColor.popMatrix();
//
//        this.evolvioColor.pushMatrix();
//        this.evolvioColor.translate(x2, y1);
//        if (selectedCreature == null) {
//            this.evolvioColor.textAlign(EvolvioColor.RIGHT);
//            this.evolvioColor.textSize(24);
//            this.evolvioColor.text("Temperature", -10, 24);
//            drawThermometer(-45, 30, 20, 660, temperature, Configuration.THERMOMETER_MINIMUM,
//                    Configuration.THERMOMETER_MAXIMUM, Color.RED.getRGB());
//        }
//        this.evolvioColor.popMatrix();
//
//        if (selectedCreature != null) {
//            drawCreature(selectedCreature, x1 + 65, y1 + 147, 2.3f, scaleUp);
//        }
//    }
//
//    private void drawVerticalSlider(float x1, float y1, float w, float h, double prog, int fillColor, int antiColor) {
//        this.evolvioColor.noStroke();
//        this.evolvioColor.fill(0, 0, 0.2f);
//        this.evolvioColor.rect(x1, y1, w, h);
//        if (prog >= 0) {
//            this.evolvioColor.fill(fillColor);
//        } else {
//            this.evolvioColor.fill(antiColor);
//        }
//        this.evolvioColor.rect(x1, (float) (y1 + h * (1 - prog)), w, (float) (prog * h));
//    }
}
