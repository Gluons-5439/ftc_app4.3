package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;
import java.util.Objects;
import java.lang.String;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "Depot", group = "Autonomous")
public class DepotAuto extends LinearOpMode {
    Hardware hulk = new Hardware();
    AutonomousTools t = new AutonomousTools();

    final double MAX_WHEEL_VELOCITY = 0.77203; // Good to keep track of

    public void runOpMode() throws InterruptedException {
        hulk.init(hardwareMap);
        t.initVuforia();
        t.initTfod(hardwareMap);

        String p = "";

        waitForStart();
        t.land(hulk);
        t.tfod.activate();

        if (t.tfod != null) {
            boolean foundMinerals = false;

            while (!foundMinerals) {
                List<Recognition> updatedRecognitions = t.tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Objects Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() == 2) {
                        foundMinerals = true;
                        int goldMineralY = -1;
                        int silverMineral1Y = -1;
                        int silverMineral2Y = -1;

                        // This just records values, and is unchanged

                        for (Recognition recognition : updatedRecognitions) {
                            if (recognition.getLabel().equals(t.LABEL_GOLD_MINERAL)) {
                                goldMineralY = (int) recognition.getTop();
                            }
                            else if (silverMineral1Y == -1) {
                                silverMineral1Y = (int) recognition.getTop();
                            }
                            else {
                                silverMineral2Y = (int) recognition.getTop();
                            }
                        }

                        // If there is no gold (-1) and there two silvers (not -1) the gold
                        // is not visible, and must be on the right
                        if (goldMineralY == -1 && silverMineral1Y != -1 && silverMineral2Y != -1) {
                            p = "Right";
                        }
                        else if (goldMineralY != -1 && silverMineral1Y != -1) {   // If you can see one gold and one silver ...
                            // ... if the gold is to the left of the silver, the gold is in the center ...
                            if (goldMineralY > silverMineral1Y) {
                                p = "Center";
                            }   // ... otherwise it is on the left
                            else {
                                p = "Left";
                            }
                        }
                    }

                }
            }
        }
        telemetry.addData("Gold Mineral Position: ", p);
        telemetry.update();



        t.strafe(750,'r',hulk);
        t.turn(80, 'r', hulk);
        if (p.equals("Center")) {
            t.strafe(300,'l',hulk);
            t.moveForward(3000,-.25, hulk);
            t.lowerMarker(hulk);
            t.turn(65, 'r', hulk);
            t.moveForward(3000,-.15, hulk);
            t.turn(30,'l',hulk);
            t.moveForward(3600,-.25, hulk);
            t.moveForward(300,-.8,hulk);

        }
        else if (p.equals("Right")) {
            t.strafe(1200,'l',hulk);
            t.turn(35,'r',hulk);
            t.moveForward(2000,-.25,hulk);
            t.lowerMarker(hulk);
            t.turn(55, 'l', hulk);
            t.moveForward(725,.15, hulk);
            t.turn(90, 'l', hulk);
            t.moveForward(3600,.25, hulk);
            t.moveForward(300,.8,hulk);

        }
        else { // "Left"
            t.strafe(1000, 'r', hulk);
            t.turn(40,'l', hulk);
            t.moveForward(2500,-.25,hulk);
            t.lowerMarker(hulk);
            t.turn(40, 'r', hulk);
            t.moveForward(725,.15, hulk);
            t.turn(85, 'r', hulk);
            t.moveForward(3600,.25, hulk);
            t.moveForward(300,.8, hulk);
        }
    }
}












