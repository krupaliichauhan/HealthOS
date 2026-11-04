package com.healthos.ui;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

/**
 * HealthOS Vector Illustration Factory.
 * Constructs clean, resolution-independent cute pastel characters
 * and Apple Health-inspired vector graphics natively in JavaFX.
 */
public final class IllustrationFactory {

    private IllustrationFactory() {}

    /**
     * Cute smiling heart character with blush cheeks and ECG pulse line.
     */
    public static Node createHeart(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Heart shape
        SVGPath heart = new SVGPath();
        heart.setContent("M 50 85 C 25 70 8 52 8 32 C 8 18 19 8 34 8 C 41 8 47 11 50 16 C 53 11 59 8 66 8 C 81 8 92 18 92 32 C 92 52 75 70 50 85 Z");
        LinearGradient grad = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#FFA4B0")),
                new Stop(0.6, Color.web("#FF6B8B")),
                new Stop(1, Color.web("#F85175")));
        heart.setFill(grad);
        heart.setStroke(Color.WHITE);
        heart.setStrokeWidth(2);
        heart.setEffect(new DropShadow(8, 0, 4, Color.web("#FF6B8B", 0.35)));

        // Eyes
        Circle leftEye = new Circle(38, 35, 3, Color.web("#34433E"));
        Circle rightEye = new Circle(62, 35, 3, Color.web("#34433E"));
        Circle leftGlint = new Circle(39, 34, 1, Color.WHITE);
        Circle rightGlint = new Circle(63, 34, 1, Color.WHITE);

        // Blushing cheeks
        Ellipse leftCheek = new Ellipse(32, 41, 4, 2.5);
        leftCheek.setFill(Color.web("#FF3366", 0.45));
        Ellipse rightCheek = new Ellipse(68, 41, 4, 2.5);
        rightCheek.setFill(Color.web("#FF3366", 0.45));

        // Cute smile
        QuadCurve smile = new QuadCurve(45, 41, 50, 46, 55, 41);
        smile.setFill(null);
        smile.setStroke(Color.web("#34433E"));
        smile.setStrokeWidth(2);

        // Little ECG line
        SVGPath ecg = new SVGPath();
        ecg.setContent("M 35 56 L 43 56 L 46 51 L 51 61 L 55 54 L 58 56 L 65 56");
        ecg.setFill(null);
        ecg.setStroke(Color.web("#FFFFFF", 0.9));
        ecg.setStrokeWidth(1.8);

        group.getChildren().addAll(heart, leftEye, rightEye, leftGlint, rightGlint, leftCheek, rightCheek, smile, ecg);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);

        return pane;
    }

    /**
     * Sleepy crescent moon with cozy nightcap, closed lashes, and floating stars.
     */
    public static Node createSleepyMoon(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Crescent moon
        SVGPath moon = new SVGPath();
        moon.setContent("M 42 12 C 65 12 85 30 85 54 C 85 78 65 96 42 96 C 35 96 28 94 22 90 C 40 85 54 69 54 50 C 54 31 40 15 22 10 C 28 8 35 12 42 12 Z");
        LinearGradient moonGrad = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#FFF6CE")),
                new Stop(1, Color.web("#FCD271")));
        moon.setFill(moonGrad);
        moon.setStroke(Color.web("#E5B94E"));
        moon.setStrokeWidth(1.5);
        moon.setEffect(new DropShadow(6, 0, 3, Color.web("#FCD271", 0.4)));

        // Closed eye with gentle lashes
        QuadCurve eye = new QuadCurve(48, 46, 54, 52, 60, 46);
        eye.setFill(null);
        eye.setStroke(Color.web("#5E4F3E"));
        eye.setStrokeWidth(2);

        Line lash1 = new Line(50, 49, 48, 52);
        lash1.setStroke(Color.web("#5E4F3E"));
        Line lash2 = new Line(54, 50, 54, 54);
        lash2.setStroke(Color.web("#5E4F3E"));
        Line lash3 = new Line(58, 49, 60, 52);
        lash3.setStroke(Color.web("#5E4F3E"));

        // Rosy cheek
        Ellipse cheek = new Ellipse(56, 56, 3.5, 2);
        cheek.setFill(Color.web("#FFAAA6", 0.6));

        // Cozy Nightcap
        SVGPath cap = new SVGPath();
        cap.setContent("M 36 14 Q 44 4 55 8 Q 48 20 36 21 Z");
        cap.setFill(Color.web("#B9A8E8"));
        Circle pompom = new Circle(56, 7, 3.8, Color.WHITE);

        // Floating little stars
        Polygon star1 = new Polygon(88, 20, 90, 25, 95, 25, 91, 28, 93, 33, 88, 30, 83, 33, 85, 28, 81, 25, 86, 25);
        star1.setFill(Color.web("#FFDA79"));

        Polygon star2 = new Polygon(16, 34, 17, 38, 21, 38, 18, 41, 19, 45, 16, 42, 13, 45, 14, 41, 11, 38, 15, 38);
        star2.setFill(Color.web("#FFDA79"));

        group.getChildren().addAll(moon, eye, lash1, lash2, lash3, cheek, cap, pompom, star1, star2);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);

        return pane;
    }

    /**
     * Cute smiling water droplet character with shine highlight.
     */
    public static Node createWaterDrop(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Water drop shape
        SVGPath drop = new SVGPath();
        drop.setContent("M 50 10 C 50 10 18 48 18 70 C 18 88 32 99 50 99 C 68 99 82 88 82 70 C 82 48 50 10 50 10 Z");
        LinearGradient dropGrad = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#A4EEFC")),
                new Stop(0.5, Color.web("#58CBF8")),
                new Stop(1, Color.web("#3B9FE7")));
        drop.setFill(dropGrad);
        drop.setStroke(Color.WHITE);
        drop.setStrokeWidth(2);
        drop.setEffect(new DropShadow(8, 0, 4, Color.web("#4FB3EE", 0.35)));

        // Shine reflection
        SVGPath shine = new SVGPath();
        shine.setContent("M 32 58 C 28 65 28 76 33 82");
        shine.setFill(null);
        shine.setStroke(Color.WHITE);
        shine.setStrokeWidth(2.8);
        shine.setOpacity(0.8);

        Circle shineDot = new Circle(36, 50, 2, Color.WHITE);
        shineDot.setOpacity(0.85);

        // Happy eyes
        Circle leftEye = new Circle(40, 65, 2.8, Color.web("#1C3F5E"));
        Circle rightEye = new Circle(60, 65, 2.8, Color.web("#1C3F5E"));
        Circle leftGlint = new Circle(41, 64, 1, Color.WHITE);
        Circle rightGlint = new Circle(61, 64, 1, Color.WHITE);

        // Blush cheeks
        Ellipse leftCheek = new Ellipse(35, 71, 3.5, 2);
        leftCheek.setFill(Color.web("#FF84A8", 0.5));
        Ellipse rightCheek = new Ellipse(65, 71, 3.5, 2);
        rightCheek.setFill(Color.web("#FF84A8", 0.5));

        // Open cute smile
        SVGPath mouth = new SVGPath();
        mouth.setContent("M 46 69 Q 50 76 54 69 Z");
        mouth.setFill(Color.web("#1C3F5E"));

        group.getChildren().addAll(drop, shine, shineDot, leftEye, rightEye, leftGlint, rightGlint, leftCheek, rightCheek, mouth);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);

        return pane;
    }

    /**
     * Cute pastel runner sneaker with movement sparkles.
     */
    public static Node createSneaker(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Sneaker upper body
        SVGPath upper = new SVGPath();
        upper.setContent("M 25 36 C 25 36 30 28 40 28 C 48 28 53 38 62 44 L 80 47 C 88 49 94 57 92 68 L 24 68 C 20 56 22 42 25 36 Z");
        upper.setFill(Color.web("#A8D5BE"));
        upper.setStroke(Color.web("#4A8F76"));
        upper.setStrokeWidth(1.8);

        // Toe cap
        SVGPath toe = new SVGPath();
        toe.setContent("M 77 47 C 86 49 92 57 92 68 L 81 68 C 81 57 79 52 77 47 Z");
        toe.setFill(Color.web("#FCE8DD"));

        // Sole
        Rectangle sole = new Rectangle(20, 68, 74, 13);
        sole.setArcWidth(12);
        sole.setArcHeight(12);
        sole.setFill(Color.web("#FFFFFF"));
        sole.setStroke(Color.web("#34433E"));
        sole.setStrokeWidth(1.6);
        sole.setEffect(new DropShadow(4, 0, 2, Color.web("#34433E", 0.1)));

        // Cute face on shoe body
        Circle eye1 = new Circle(48, 56, 2.2, Color.web("#34433E"));
        Circle eye2 = new Circle(58, 56, 2.2, Color.web("#34433E"));
        Ellipse blush = new Ellipse(44, 60, 2.2, 1.2);
        blush.setFill(Color.web("#FFAAA6", 0.6));
        QuadCurve smile = new QuadCurve(51, 60, 53, 62, 55, 60);
        smile.setFill(null);
        smile.setStroke(Color.web("#34433E"));
        smile.setStrokeWidth(1.5);

        // Speed lines
        Line speed1 = new Line(10, 60, 18, 60);
        speed1.setStroke(Color.web("#78BDB1"));
        speed1.setStrokeWidth(2);
        Line speed2 = new Line(6, 68, 16, 68);
        speed2.setStroke(Color.web("#78BDB1"));
        speed2.setStrokeWidth(2);

        group.getChildren().addAll(upper, toe, sole, eye1, eye2, blush, smile, speed1, speed2);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);

        return pane;
    }

    /**
     * Cute fruit & avocado nutrition character.
     */
    public static Node createNutrition(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Smiling Avocado on left
        SVGPath avocado = new SVGPath();
        avocado.setContent("M 35 30 C 46 30 50 42 50 57 C 50 74 40 82 28 82 C 17 82 10 74 10 57 C 10 42 20 30 35 30 Z");
        avocado.setFill(Color.web("#BDE09C"));
        avocado.setStroke(Color.web("#6B8E42"));
        avocado.setStrokeWidth(1.6);

        Circle pit = new Circle(28, 62, 11, Color.web("#A07248"));

        // Avocado face
        Circle aEye1 = new Circle(25, 44, 2, Color.web("#34433E"));
        Circle aEye2 = new Circle(33, 44, 2, Color.web("#34433E"));
        QuadCurve aSmile = new QuadCurve(27, 47, 29, 49, 31, 47);
        aSmile.setFill(null);
        aSmile.setStroke(Color.web("#34433E"));
        aSmile.setStrokeWidth(1.2);

        // Smiling Apple on right
        SVGPath apple = new SVGPath();
        apple.setContent("M 70 38 C 65 38 62 42 60 42 C 58 42 55 38 49 38 C 40 38 35 46 35 59 C 35 74 47 84 60 84 C 73 84 85 74 85 59 C 85 46 80 38 70 38 Z");
        apple.setFill(Color.web("#FF8DA1"));
        apple.setStroke(Color.web("#C94860"));
        apple.setStrokeWidth(1.6);

        // Stem & Leaf
        Line stem = new Line(60, 40, 62, 30);
        stem.setStroke(Color.web("#5C4033"));
        stem.setStrokeWidth(2);

        SVGPath leaf = new SVGPath();
        leaf.setContent("M 62 32 Q 68 28 72 34 Q 67 37 62 32 Z");
        leaf.setFill(Color.web("#80C97F"));

        // Apple face
        Circle pEye1 = new Circle(54, 57, 2.2, Color.web("#34433E"));
        Circle pEye2 = new Circle(66, 57, 2.2, Color.web("#34433E"));
        Ellipse pBlush = new Ellipse(49, 61, 2.2, 1.2);
        pBlush.setFill(Color.web("#FF5A78", 0.5));
        QuadCurve pSmile = new QuadCurve(58, 61, 60, 64, 62, 61);
        pSmile.setFill(null);
        pSmile.setStroke(Color.web("#34433E"));
        pSmile.setStrokeWidth(1.4);

        group.getChildren().addAll(avocado, pit, aEye1, aEye2, aSmile, apple, stem, leaf, pEye1, pEye2, pBlush, pSmile);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);

        return pane;
    }

    /**
     * Cute growing plant in pot for wellness progress.
     */
    public static Node createWellnessPlant(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Stem
        QuadCurve stem = new QuadCurve(50, 65, 50, 45, 50, 26);
        stem.setFill(null);
        stem.setStroke(Color.web("#5DA786"));
        stem.setStrokeWidth(2.8);

        // Leaves
        SVGPath leftLeaf = new SVGPath();
        leftLeaf.setContent("M 50 48 C 38 45 30 35 32 28 C 39 28 48 39 50 48 Z");
        leftLeaf.setFill(Color.web("#A8D5BE"));
        leftLeaf.setStroke(Color.web("#4A8A6D"));
        leftLeaf.setStrokeWidth(1.4);

        SVGPath rightLeaf = new SVGPath();
        rightLeaf.setContent("M 50 40 C 62 37 70 27 68 20 C 61 20 52 31 50 40 Z");
        rightLeaf.setFill(Color.web("#A8D5BE"));
        rightLeaf.setStroke(Color.web("#4A8A6D"));
        rightLeaf.setStrokeWidth(1.4);

        // Blooming flower bud
        Circle bud = new Circle(50, 22, 7.5, Color.web("#FFB5C5"));
        bud.setStroke(Color.web("#E87A90"));
        bud.setStrokeWidth(1.2);
        Circle center = new Circle(50, 22, 4, Color.web("#FFF2A3"));

        // Terracotta pot
        Polygon pot = new Polygon(30, 60, 70, 60, 64, 90, 36, 90);
        pot.setFill(Color.web("#FCD2B8"));
        pot.setStroke(Color.web("#BA734F"));
        pot.setStrokeWidth(1.8);

        Rectangle rim = new Rectangle(27, 56, 46, 7);
        rim.setArcWidth(4);
        rim.setArcHeight(4);
        rim.setFill(Color.web("#F8BC9E"));
        rim.setStroke(Color.web("#BA734F"));
        rim.setStrokeWidth(1.8);

        // Cute smiling face on pot
        Circle pEye1 = new Circle(44, 73, 2.2, Color.web("#34433E"));
        Circle pEye2 = new Circle(56, 73, 2.2, Color.web("#34433E"));
        Ellipse pBlush1 = new Ellipse(39, 76, 2.5, 1.3);
        pBlush1.setFill(Color.web("#FFAAA6", 0.65));
        Ellipse pBlush2 = new Ellipse(61, 76, 2.5, 1.3);
        pBlush2.setFill(Color.web("#FFAAA6", 0.65));
        QuadCurve pSmile = new QuadCurve(48, 76, 50, 79, 52, 76);
        pSmile.setFill(null);
        pSmile.setStroke(Color.web("#34433E"));
        pSmile.setStrokeWidth(1.5);

        group.getChildren().addAll(stem, leftLeaf, rightLeaf, bud, center, pot, rim, pEye1, pEye2, pBlush1, pBlush2, pSmile);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);

        return pane;
    }

    /**
     * HealthOS Brand Logo Badge (Pastel Mint Pill with Heart & Leaf).
     */
    public static Node createAppLogo(double width, double height) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);

        StackPane iconPane = new StackPane();
        iconPane.setPrefSize(height, height);
        iconPane.setStyle("-fx-background-color: #DDF3E8; -fx-background-radius: 12px; -fx-border-color: #A8D5BE; -fx-border-radius: 12px;");

        Node heart = createHeart(height * 0.75);
        iconPane.getChildren().add(heart);

        Label title = new Label("HealthOS");
        title.setStyle("-fx-font-size: 19px; -fx-font-weight: 800; -fx-text-fill: #34433E;");

        box.getChildren().addAll(iconPane, title);
        return box;
    }

    /**
     * Cute Golden Trophy Character for Achievements & Reports.
     */
    public static Node createTrophy(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Cup
        SVGPath cup = new SVGPath();
        cup.setContent("M 30 20 L 70 20 C 70 45 62 58 50 60 C 38 58 30 45 30 20 Z");
        cup.setFill(Color.web("#FFE58F"));
        cup.setStroke(Color.web("#E5A630"));
        cup.setStrokeWidth(1.8);

        // Handles
        SVGPath leftH = new SVGPath();
        leftH.setContent("M 30 25 C 16 25 15 42 30 46");
        leftH.setFill(null);
        leftH.setStroke(Color.web("#E5A630"));
        leftH.setStrokeWidth(3);

        SVGPath rightH = new SVGPath();
        rightH.setContent("M 70 25 C 84 25 85 42 70 46");
        rightH.setFill(null);
        rightH.setStroke(Color.web("#E5A630"));
        rightH.setStrokeWidth(3);

        // Stem & Base
        Rectangle stem = new Rectangle(46, 60, 8, 14);
        stem.setFill(Color.web("#E5A630"));

        Rectangle base = new Rectangle(34, 74, 32, 10);
        base.setArcWidth(4);
        base.setArcHeight(4);
        base.setFill(Color.web("#A8D5BE"));
        base.setStroke(Color.web("#4A8F76"));
        base.setStrokeWidth(1.4);

        // Cute face
        Circle e1 = new Circle(44, 36, 2, Color.web("#34433E"));
        Circle e2 = new Circle(56, 36, 2, Color.web("#34433E"));
        Ellipse b1 = new Ellipse(40, 40, 2, 1);
        b1.setFill(Color.web("#FFAAA6", 0.6));
        Ellipse b2 = new Ellipse(60, 40, 2, 1);
        b2.setFill(Color.web("#FFAAA6", 0.6));
        QuadCurve smile = new QuadCurve(47, 40, 50, 43, 53, 40);
        smile.setFill(null);
        smile.setStroke(Color.web("#34433E"));
        smile.setStrokeWidth(1.3);

        group.getChildren().addAll(cup, leftH, rightH, stem, base, e1, e2, b1, b2, smile);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }

    /**
     * Cute Sleeping Cloud Character for Empty States & Ingestion.
     */
    public static Node createEmptyState(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        SVGPath cloud = new SVGPath();
        cloud.setContent("M 32 70 C 24 70 18 62 20 53 C 20 45 28 40 36 42 C 40 32 52 27 62 32 C 70 27 82 30 86 40 C 94 42 100 48 98 58 C 98 66 91 70 83 70 Z");
        cloud.setFill(Color.web("#E2EEFA"));
        cloud.setStroke(Color.web("#90BAE2"));
        cloud.setStrokeWidth(1.8);

        // Sleepy eyes
        QuadCurve eye1 = new QuadCurve(42, 52, 46, 55, 50, 52);
        eye1.setFill(null);
        eye1.setStroke(Color.web("#34433E"));
        eye1.setStrokeWidth(1.6);

        QuadCurve eye2 = new QuadCurve(62, 52, 66, 55, 70, 52);
        eye2.setFill(null);
        eye2.setStroke(Color.web("#34433E"));
        eye2.setStrokeWidth(1.6);

        Ellipse b1 = new Ellipse(38, 56, 2.5, 1.4);
        b1.setFill(Color.web("#FFAAA6", 0.6));
        Ellipse b2 = new Ellipse(74, 56, 2.5, 1.4);
        b2.setFill(Color.web("#FFAAA6", 0.6));

        group.getChildren().addAll(cloud, eye1, eye2, b1, b2);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }

    /**
     * Cute Weighing Scale Mascot with smiling dial and little feet for Weight & BMI.
     */
    public static Node createWeighingScale(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Feet
        Circle foot1 = new Circle(28, 86, 6, Color.web("#D1DFD7"));
        Circle foot2 = new Circle(72, 86, 6, Color.web("#D1DFD7"));

        // Main Scale Body
        Rectangle body = new Rectangle(16, 22, 68, 62);
        body.setArcWidth(22);
        body.setArcHeight(22);
        body.setFill(Color.web("#EDF9F2"));
        body.setStroke(Color.web("#78BDB1"));
        body.setStrokeWidth(2);
        body.setEffect(new DropShadow(6, 0, 3, Color.web("#78BDB1", 0.25)));

        // Dial window at top
        Rectangle dial = new Rectangle(32, 28, 36, 20);
        dial.setArcWidth(10);
        dial.setArcHeight(10);
        dial.setFill(Color.web("#FFFFFF"));
        dial.setStroke(Color.web("#A8D5BE"));
        dial.setStrokeWidth(1.4);

        // Dial needle / reading
        Line needle = new Line(50, 44, 50, 32);
        needle.setStroke(Color.web("#FF6B8B"));
        needle.setStrokeWidth(2);
        Circle needlePin = new Circle(50, 44, 2, Color.web("#34433E"));

        // Cute face on scale body
        Circle eye1 = new Circle(38, 62, 2.8, Color.web("#34433E"));
        Circle eye2 = new Circle(62, 62, 2.8, Color.web("#34433E"));
        Circle glint1 = new Circle(39, 61, 1, Color.WHITE);
        Circle glint2 = new Circle(63, 61, 1, Color.WHITE);

        Ellipse b1 = new Ellipse(33, 67, 3, 1.8);
        b1.setFill(Color.web("#FFAAA6", 0.6));
        Ellipse b2 = new Ellipse(67, 67, 3, 1.8);
        b2.setFill(Color.web("#FFAAA6", 0.6));

        QuadCurve smile = new QuadCurve(46, 67, 50, 71, 54, 67);
        smile.setFill(null);
        smile.setStroke(Color.web("#34433E"));
        smile.setStrokeWidth(1.8);

        group.getChildren().addAll(foot1, foot2, body, dial, needle, needlePin, eye1, eye2, glint1, glint2, b1, b2, smile);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }

    /**
     * Cute Blushing Thermometer with heart mercury for Body Temperature.
     */
    public static Node createThermometer(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Stem tube
        Rectangle stem = new Rectangle(42, 12, 16, 54);
        stem.setArcWidth(16);
        stem.setArcHeight(16);
        stem.setFill(Color.web("#FFFFFF"));
        stem.setStroke(Color.web("#A8D5BE"));
        stem.setStrokeWidth(1.8);

        // Bottom bulb
        Circle bulb = new Circle(50, 72, 15, Color.web("#FF9EAA"));
        bulb.setStroke(Color.web("#FA5A7D"));
        bulb.setStrokeWidth(1.8);

        // Mercury column
        Rectangle mercury = new Rectangle(46, 34, 8, 30);
        mercury.setArcWidth(6);
        mercury.setArcHeight(6);
        mercury.setFill(Color.web("#FF6B8B"));

        // Measurement ticks
        Line t1 = new Line(58, 22, 63, 22);
        t1.setStroke(Color.web("#A8D5BE"));
        t1.setStrokeWidth(1.5);
        Line t2 = new Line(58, 30, 65, 30);
        t2.setStroke(Color.web("#A8D5BE"));
        t2.setStrokeWidth(1.5);
        Line t3 = new Line(58, 38, 63, 38);
        t3.setStroke(Color.web("#A8D5BE"));
        t3.setStrokeWidth(1.5);

        // Cute face on the bulb
        Circle e1 = new Circle(45, 70, 2, Color.web("#34433E"));
        Circle e2 = new Circle(55, 70, 2, Color.web("#34433E"));
        Ellipse b1 = new Ellipse(41, 74, 2, 1.2);
        b1.setFill(Color.web("#FA5A7D", 0.5));
        Ellipse b2 = new Ellipse(59, 74, 2, 1.2);
        b2.setFill(Color.web("#FA5A7D", 0.5));
        QuadCurve smile = new QuadCurve(48, 73, 50, 76, 52, 73);
        smile.setFill(null);
        smile.setStroke(Color.web("#34433E"));
        smile.setStrokeWidth(1.4);

        group.getChildren().addAll(stem, mercury, bulb, t1, t2, t3, e1, e2, b1, b2, smile);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }

    /**
     * Cute Fluffy Lavender Pillow with dreamy sleeping lashes for Sleep Tracker.
     */
    public static Node createPillow(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Pillow shape
        SVGPath pillow = new SVGPath();
        pillow.setContent("M 18 34 C 18 20 82 20 82 34 C 92 48 92 56 82 70 C 82 84 18 84 18 70 C 8 56 8 48 18 34 Z");
        pillow.setFill(Color.web("#F1ECFA"));
        pillow.setStroke(Color.web("#B9A8E8"));
        pillow.setStrokeWidth(2);
        pillow.setEffect(new DropShadow(8, 0, 4, Color.web("#B9A8E8", 0.3)));

        // Corner tufts
        Circle c1 = new Circle(14, 28, 3.5, Color.web("#DCD3F4"));
        Circle c2 = new Circle(86, 28, 3.5, Color.web("#DCD3F4"));
        Circle c3 = new Circle(14, 76, 3.5, Color.web("#DCD3F4"));
        Circle c4 = new Circle(86, 76, 3.5, Color.web("#DCD3F4"));

        // Closed dreamy lashes
        QuadCurve eye1 = new QuadCurve(36, 50, 42, 56, 48, 50);
        eye1.setFill(null);
        eye1.setStroke(Color.web("#5E4694"));
        eye1.setStrokeWidth(2.2);

        QuadCurve eye2 = new QuadCurve(54, 50, 60, 56, 66, 50);
        eye2.setFill(null);
        eye2.setStroke(Color.web("#5E4694"));
        eye2.setStrokeWidth(2.2);

        Ellipse b1 = new Ellipse(32, 54, 3, 2);
        b1.setFill(Color.web("#FFAAA6", 0.65));
        Ellipse b2 = new Ellipse(70, 54, 3, 2);
        b2.setFill(Color.web("#FFAAA6", 0.65));

        // Sleepy little mouth
        QuadCurve smile = new QuadCurve(48, 57, 51, 60, 54, 57);
        smile.setFill(null);
        smile.setStroke(Color.web("#5E4694"));
        smile.setStrokeWidth(1.6);

        // Little floating Zzz
        Label z1 = new Label("z");
        z1.setStyle("-fx-font-size: 11px; -fx-font-weight: bold; -fx-text-fill: #A89ACF;");
        z1.setLayoutX(72);
        z1.setLayoutY(22);

        Label z2 = new Label("Z");
        z2.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #7C68B0;");
        z2.setLayoutX(80);
        z2.setLayoutY(10);

        group.getChildren().addAll(pillow, c1, c2, c3, c4, eye1, eye2, b1, b2, smile, z1, z2);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }

    /**
     * Cute Retro Mint Alarm Clock with happy bells.
     */
    public static Node createAlarmClock(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Twin bells
        Circle bell1 = new Circle(26, 26, 12, Color.web("#FFD166"));
        bell1.setStroke(Color.web("#E5B94E"));
        bell1.setStrokeWidth(1.8);

        Circle bell2 = new Circle(74, 26, 12, Color.web("#FFD166"));
        bell2.setStroke(Color.web("#E5B94E"));
        bell2.setStrokeWidth(1.8);

        // Feet
        Line foot1 = new Line(30, 78, 20, 88);
        foot1.setStroke(Color.web("#78BDB1"));
        foot1.setStrokeWidth(4);
        foot1.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);

        Line foot2 = new Line(70, 78, 80, 88);
        foot2.setStroke(Color.web("#78BDB1"));
        foot2.setStrokeWidth(4);
        foot2.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);

        // Clock Body
        Circle body = new Circle(50, 54, 30, Color.web("#DDF3E8"));
        body.setStroke(Color.web("#78BDB1"));
        body.setStrokeWidth(2.5);
        body.setEffect(new DropShadow(6, 0, 3, Color.web("#78BDB1", 0.3)));

        // Clock face inner circle
        Circle face = new Circle(50, 54, 24, Color.WHITE);

        // Hands pointing to 7:00 AM
        Line hourHand = new Line(50, 54, 40, 62);
        hourHand.setStroke(Color.web("#34433E"));
        hourHand.setStrokeWidth(2.2);

        Line minHand = new Line(50, 54, 50, 36);
        minHand.setStroke(Color.web("#34433E"));
        minHand.setStrokeWidth(1.8);

        // Face eyes
        Circle e1 = new Circle(44, 48, 1.8, Color.web("#34433E"));
        Circle e2 = new Circle(56, 48, 1.8, Color.web("#34433E"));
        Ellipse b1 = new Ellipse(41, 52, 2, 1.2);
        b1.setFill(Color.web("#FFAAA6", 0.7));
        Ellipse b2 = new Ellipse(59, 52, 2, 1.2);
        b2.setFill(Color.web("#FFAAA6", 0.7));

        group.getChildren().addAll(foot1, foot2, bell1, bell2, body, face, hourHand, minHand, e1, e2, b1, b2);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }

    /**
     * Cute Pastel Water Bottle with measurement markings and cheerful smile.
     */
    public static Node createWaterBottle(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Bottle Cap & Handle
        Rectangle capHandle = new Rectangle(40, 6, 20, 10);
        capHandle.setArcWidth(6);
        capHandle.setArcHeight(6);
        capHandle.setFill(Color.web("#A8D5BE"));

        Rectangle cap = new Rectangle(36, 14, 28, 8);
        cap.setArcWidth(4);
        cap.setArcHeight(4);
        cap.setFill(Color.web("#78BDB1"));

        // Bottle Body
        Rectangle bottle = new Rectangle(32, 22, 36, 68);
        bottle.setArcWidth(18);
        bottle.setArcHeight(18);
        bottle.setFill(Color.web("#E2EEFA"));
        bottle.setStroke(Color.web("#55C7F7"));
        bottle.setStrokeWidth(1.8);
        bottle.setEffect(new DropShadow(6, 0, 3, Color.web("#55C7F7", 0.25)));

        // Water level inside
        SVGPath waterLevel = new SVGPath();
        waterLevel.setContent("M 33 55 Q 41 52 50 55 Q 59 58 67 55 L 67 80 C 67 86 59 89 50 89 C 41 89 33 86 33 80 Z");
        waterLevel.setFill(Color.web("#55C7F7", 0.5));

        // Measurement lines
        Line m1 = new Line(34, 38, 40, 38);
        m1.setStroke(Color.web("#3FA8EB"));
        Line m2 = new Line(34, 46, 42, 46);
        m2.setStroke(Color.web("#3FA8EB"));
        Line m3 = new Line(34, 54, 40, 54);
        m3.setStroke(Color.web("#3FA8EB"));

        // Cute face
        Circle e1 = new Circle(44, 66, 2.2, Color.web("#1C3F5E"));
        Circle e2 = new Circle(56, 66, 2.2, Color.web("#1C3F5E"));
        Ellipse b1 = new Ellipse(40, 70, 2.2, 1.3);
        b1.setFill(Color.web("#FF84A8", 0.6));
        Ellipse b2 = new Ellipse(60, 70, 2.2, 1.3);
        b2.setFill(Color.web("#FF84A8", 0.6));
        QuadCurve smile = new QuadCurve(47, 70, 50, 73, 53, 70);
        smile.setFill(null);
        smile.setStroke(Color.web("#1C3F5E"));
        smile.setStrokeWidth(1.4);

        group.getChildren().addAll(capHandle, cap, bottle, waterLevel, m1, m2, m3, e1, e2, b1, b2, smile);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }

    /**
     * Cute Strawberry character for Nutrition tracker.
     */
    public static Node createStrawberry(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Strawberry body
        SVGPath berry = new SVGPath();
        berry.setContent("M 50 92 C 30 78 20 62 20 44 C 20 30 35 24 50 24 C 65 24 80 30 80 44 C 80 62 70 78 50 92 Z");
        berry.setFill(Color.web("#FF708A"));
        berry.setStroke(Color.web("#E04362"));
        berry.setStrokeWidth(1.8);
        berry.setEffect(new DropShadow(6, 0, 3, Color.web("#FF708A", 0.3)));

        // Green leafy cap
        SVGPath cap = new SVGPath();
        cap.setContent("M 50 24 C 42 16 34 20 30 22 C 38 28 44 26 44 28 C 42 22 50 14 50 14 C 50 14 58 22 56 28 C 56 26 62 28 70 22 C 66 20 58 16 50 24 Z");
        cap.setFill(Color.web("#78C274"));

        // Seed dots
        Circle s1 = new Circle(32, 38, 1, Color.web("#FFF2A3"));
        Circle s2 = new Circle(44, 34, 1, Color.web("#FFF2A3"));
        Circle s3 = new Circle(56, 34, 1, Color.web("#FFF2A3"));
        Circle s4 = new Circle(68, 38, 1, Color.web("#FFF2A3"));
        Circle s5 = new Circle(38, 70, 1, Color.web("#FFF2A3"));
        Circle s6 = new Circle(62, 70, 1, Color.web("#FFF2A3"));

        // Cute face
        Circle e1 = new Circle(42, 50, 2.4, Color.web("#34433E"));
        Circle e2 = new Circle(58, 50, 2.4, Color.web("#34433E"));
        Circle glint1 = new Circle(43, 49, 0.8, Color.WHITE);
        Circle glint2 = new Circle(59, 49, 0.8, Color.WHITE);
        Ellipse b1 = new Ellipse(36, 55, 2.5, 1.4);
        b1.setFill(Color.web("#FA4B6B", 0.5));
        Ellipse b2 = new Ellipse(64, 55, 2.5, 1.4);
        b2.setFill(Color.web("#FA4B6B", 0.5));
        QuadCurve smile = new QuadCurve(47, 54, 50, 58, 53, 54);
        smile.setFill(null);
        smile.setStroke(Color.web("#34433E"));
        smile.setStrokeWidth(1.6);

        group.getChildren().addAll(berry, cap, s1, s2, s3, s4, s5, s6, e1, e2, glint1, glint2, b1, b2, smile);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }

    /**
     * Cute Cheerful Carrot with green ponytail for Nutrition.
     */
    public static Node createCarrot(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Carrot leafy greens
        SVGPath leaves = new SVGPath();
        leaves.setContent("M 50 26 C 42 12 36 10 32 14 C 40 18 45 22 47 26 C 48 10 52 8 56 12 C 54 18 52 22 51 26 C 58 14 66 12 70 18 C 62 22 56 24 53 26 Z");
        leaves.setFill(Color.web("#6BBF72"));

        // Carrot body
        SVGPath body = new SVGPath();
        body.setContent("M 35 26 C 30 26 30 36 36 46 L 47 88 C 48 92 52 92 53 88 L 64 46 C 70 36 70 26 65 26 Z");
        body.setFill(Color.web("#FFA05C"));
        body.setStroke(Color.web("#E8752A"));
        body.setStrokeWidth(1.8);

        // Texture ridges
        Line r1 = new Line(38, 38, 44, 38);
        r1.setStroke(Color.web("#E8752A", 0.6));
        Line r2 = new Line(56, 48, 62, 48);
        r2.setStroke(Color.web("#E8752A", 0.6));
        Line r3 = new Line(44, 62, 50, 62);
        r3.setStroke(Color.web("#E8752A", 0.6));

        // Cute winking face
        QuadCurve wink = new QuadCurve(40, 42, 43, 40, 46, 42);
        wink.setFill(null);
        wink.setStroke(Color.web("#34433E"));
        wink.setStrokeWidth(1.8);

        Circle eye = new Circle(56, 42, 2.2, Color.web("#34433E"));
        Ellipse b1 = new Ellipse(37, 46, 2, 1.2);
        b1.setFill(Color.web("#FF6B6B", 0.6));
        Ellipse b2 = new Ellipse(61, 46, 2, 1.2);
        b2.setFill(Color.web("#FF6B6B", 0.6));
        QuadCurve smile = new QuadCurve(46, 46, 49, 49, 52, 46);
        smile.setFill(null);
        smile.setStroke(Color.web("#34433E"));
        smile.setStrokeWidth(1.4);

        group.getChildren().addAll(leaves, body, r1, r2, r3, wink, eye, b1, b2, smile);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }

    /**
     * Cute Analytics Owl Mascot for Data & Insights.
     */
    public static Node createAnalyticsOwl(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Body
        Ellipse body = new Ellipse(50, 56, 26, 32);
        body.setFill(Color.web("#E9E3F7"));
        body.setStroke(Color.web("#B9A8E8"));
        body.setStrokeWidth(2);

        // Ear tufts
        Polygon leftEar = new Polygon(30, 28, 24, 14, 38, 24);
        leftEar.setFill(Color.web("#B9A8E8"));
        Polygon rightEar = new Polygon(70, 28, 76, 14, 62, 24);
        rightEar.setFill(Color.web("#B9A8E8"));

        // Big cute eyes / glasses
        Circle eyeFrame1 = new Circle(38, 46, 11, Color.WHITE);
        eyeFrame1.setStroke(Color.web("#78BDB1"));
        eyeFrame1.setStrokeWidth(2);

        Circle eyeFrame2 = new Circle(62, 46, 11, Color.WHITE);
        eyeFrame2.setStroke(Color.web("#78BDB1"));
        eyeFrame2.setStrokeWidth(2);

        Line glassesBridge = new Line(49, 46, 51, 46);
        glassesBridge.setStroke(Color.web("#78BDB1"));
        glassesBridge.setStrokeWidth(2);

        Circle pupil1 = new Circle(40, 46, 4.5, Color.web("#34433E"));
        Circle pupil2 = new Circle(60, 46, 4.5, Color.web("#34433E"));
        Circle shine1 = new Circle(41, 44, 1.5, Color.WHITE);
        Circle shine2 = new Circle(61, 44, 1.5, Color.WHITE);

        // Cute yellow beak
        Polygon beak = new Polygon(47, 54, 53, 54, 50, 60);
        beak.setFill(Color.web("#FFD166"));

        // Blush cheeks
        Ellipse b1 = new Ellipse(28, 56, 3, 1.8);
        b1.setFill(Color.web("#FFAAA6", 0.65));
        Ellipse b2 = new Ellipse(72, 56, 3, 1.8);
        b2.setFill(Color.web("#FFAAA6", 0.65));

        // Tummy feathers / mini graph symbol
        SVGPath bellyGraph = new SVGPath();
        bellyGraph.setContent("M 38 74 L 44 68 L 50 72 L 58 64 L 62 66");
        bellyGraph.setFill(null);
        bellyGraph.setStroke(Color.web("#78BDB1"));
        bellyGraph.setStrokeWidth(2);

        group.getChildren().addAll(leftEar, rightEar, body, eyeFrame1, eyeFrame2, glassesBridge, pupil1, pupil2, shine1, shine2, beak, b1, b2, bellyGraph);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }

    /**
     * Cute Document / Certificate with Heart Stamp for PDF Reports.
     */
    public static Node createDocumentHeart(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Sheet of paper
        Rectangle page = new Rectangle(24, 14, 52, 72);
        page.setArcWidth(10);
        page.setArcHeight(10);
        page.setFill(Color.web("#FFFFFF"));
        page.setStroke(Color.web("#A8D5BE"));
        page.setStrokeWidth(2);
        page.setEffect(new DropShadow(6, 0, 3, Color.web("#A8D5BE", 0.25)));

        // Folded top-right corner
        Polygon corner = new Polygon(64, 14, 76, 26, 64, 26);
        corner.setFill(Color.web("#DDF3E8"));

        // Document text lines
        Line l1 = new Line(32, 28, 54, 28);
        l1.setStroke(Color.web("#78BDB1"));
        l1.setStrokeWidth(2.5);
        l1.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.ROUND);

        Line l2 = new Line(32, 38, 66, 38);
        l2.setStroke(Color.web("#E6EFE9"));
        l2.setStrokeWidth(2);

        Line l3 = new Line(32, 46, 60, 46);
        l3.setStroke(Color.web("#E6EFE9"));
        l3.setStrokeWidth(2);

        Line l4 = new Line(32, 54, 66, 54);
        l4.setStroke(Color.web("#E6EFE9"));
        l4.setStrokeWidth(2);

        // Heart seal at bottom
        Circle seal = new Circle(50, 68, 10, Color.web("#FF9EAA"));
        SVGPath heart = new SVGPath();
        heart.setContent("M 50 72 C 45 68 42 64 42 60 C 42 57 44 55 47 55 C 49 55 50 56 50 57 C 50 56 51 55 53 55 C 56 55 58 57 58 60 C 58 64 55 68 50 72 Z");
        heart.setFill(Color.WHITE);

        group.getChildren().addAll(page, corner, l1, l2, l3, l4, seal, heart);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }

    /**
     * Cheerful Warm Calorie Flame Mascot.
     */
    public static Node createFlameCalorie(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Outer Flame
        SVGPath flame = new SVGPath();
        flame.setContent("M 50 14 C 50 14 68 34 68 56 C 68 74 58 86 50 86 C 42 86 32 74 32 56 C 32 40 44 26 50 14 Z");
        flame.setFill(Color.web("#FFA767"));
        flame.setStroke(Color.web("#F58231"));
        flame.setStrokeWidth(1.8);
        flame.setEffect(new DropShadow(6, 0, 3, Color.web("#FFA767", 0.35)));

        // Inner Yellow Core
        SVGPath inner = new SVGPath();
        inner.setContent("M 50 36 C 50 36 60 50 60 64 C 60 76 54 82 50 82 C 46 82 40 76 40 64 C 40 54 48 44 50 36 Z");
        inner.setFill(Color.web("#FFE58F"));

        // Cute face
        Circle e1 = new Circle(46, 62, 1.8, Color.web("#34433E"));
        Circle e2 = new Circle(54, 62, 1.8, Color.web("#34433E"));
        Ellipse b1 = new Ellipse(43, 66, 1.8, 1);
        b1.setFill(Color.web("#FF5A78", 0.6));
        Ellipse b2 = new Ellipse(57, 66, 1.8, 1);
        b2.setFill(Color.web("#FF5A78", 0.6));
        QuadCurve smile = new QuadCurve(48, 65, 50, 68, 52, 65);
        smile.setFill(null);
        smile.setStroke(Color.web("#34433E"));
        smile.setStrokeWidth(1.2);

        group.getChildren().addAll(flame, inner, e1, e2, b1, b2, smile);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }

    /**
     * Cute Database Server Rack Mascot with green LED eyes and smile.
     */
    public static Node createDatabaseServer(double size) {
        Pane pane = new Pane();
        pane.setPrefSize(size, size);
        pane.setMinSize(size, size);
        pane.setMaxSize(size, size);

        Group group = new Group();
        double scale = size / 100.0;
        group.setScaleX(scale);
        group.setScaleY(scale);

        // Rack 1 (Top)
        Rectangle r1 = new Rectangle(24, 20, 52, 18);
        r1.setArcWidth(8);
        r1.setArcHeight(8);
        r1.setFill(Color.web("#FFFFFF"));
        r1.setStroke(Color.web("#78BDB1"));
        r1.setStrokeWidth(1.8);
        Circle led1 = new Circle(68, 29, 2.5, Color.web("#69B89E"));

        // Rack 2 (Middle - with cute face)
        Rectangle r2 = new Rectangle(24, 42, 52, 18);
        r2.setArcWidth(8);
        r2.setArcHeight(8);
        r2.setFill(Color.web("#EDF9F2"));
        r2.setStroke(Color.web("#78BDB1"));
        r2.setStrokeWidth(1.8);
        Circle e1 = new Circle(44, 50, 2, Color.web("#34433E"));
        Circle e2 = new Circle(56, 50, 2, Color.web("#34433E"));
        Ellipse b1 = new Ellipse(39, 53, 1.8, 1);
        b1.setFill(Color.web("#FFAAA6", 0.6));
        Ellipse b2 = new Ellipse(61, 53, 1.8, 1);
        b2.setFill(Color.web("#FFAAA6", 0.6));
        QuadCurve smile = new QuadCurve(48, 53, 50, 56, 52, 53);
        smile.setFill(null);
        smile.setStroke(Color.web("#34433E"));
        smile.setStrokeWidth(1.3);
        Circle led2 = new Circle(68, 51, 2.5, Color.web("#69B89E"));

        // Rack 3 (Bottom)
        Rectangle r3 = new Rectangle(24, 64, 52, 18);
        r3.setArcWidth(8);
        r3.setArcHeight(8);
        r3.setFill(Color.web("#FFFFFF"));
        r3.setStroke(Color.web("#78BDB1"));
        r3.setStrokeWidth(1.8);
        Circle led3 = new Circle(68, 73, 2.5, Color.web("#69B89E"));

        group.getChildren().addAll(r1, led1, r3, led3, r2, led2, e1, e2, b1, b2, smile);
        group.setLayoutX((size - 100) / 2.0);
        group.setLayoutY((size - 100) / 2.0);
        pane.getChildren().add(group);
        return pane;
    }
}
