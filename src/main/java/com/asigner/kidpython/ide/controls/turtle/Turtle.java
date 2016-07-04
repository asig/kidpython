package com.asigner.kidpython.ide.controls.turtle;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import static java.awt.Color.BLACK;

/**
 * This class has been automatically generated using
 * <a href="http://ebourg.github.io/flamingo-svg-transcoder/">Flamingo SVG transcoder</a>.
 */
public class Turtle {

    /**
     * Paints the transcoded SVG image on the specified graphics context. You
     * can install a custom transformation on the graphics context to scale the
     * image.
     *
     * @param g Graphics context.
     */
    public static void paint(Graphics2D g) {
        Shape shape = null;

        float origAlpha = 1.0f;
        Composite origComposite = ((Graphics2D)g).getComposite();
        if (origComposite instanceof AlphaComposite) {
            AlphaComposite origAlphaComposite = (AlphaComposite)origComposite;
            if (origAlphaComposite.getRule() == AlphaComposite.SRC_OVER) {
                origAlpha = origAlphaComposite.getAlpha();
            }
        }

        java.util.LinkedList<AffineTransform> transformations = new java.util.LinkedList<AffineTransform>();


        //
        transformations.offer(g.getTransform());
        g.transform(new AffineTransform(1.0666667f, 0, 0, 1.0666667f, 0, 0));

        // _0

        // _0_0

        // _0_0_0
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(308.85416, 680.6261);
        ((GeneralPath) shape).lineTo(248.75009, 784.16675);
        ((GeneralPath) shape).lineTo(194.70692, 719.0119);
        ((GeneralPath) shape).lineTo(183.59525, 669.5144);
        ((GeneralPath) shape).lineTo(179.55464, 645.7758);
        ((GeneralPath) shape).lineTo(176.0191, 619.51184);
        ((GeneralPath) shape).lineTo(276.52927, 621.53217);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xD4595B));
        g.fill(shape);

        // _0_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(267.69043, 617.4915);
        ((GeneralPath) shape).lineTo(178.29193, 616.98645);
        ((GeneralPath) shape).lineTo(181.32239, 562.9433);
        ((GeneralPath) shape).lineTo(206.5762, 494.758);
        ((GeneralPath) shape).lineTo(239.40616, 453.8468);
        ((GeneralPath) shape).lineTo(250.01276, 448.29095);
        ((GeneralPath) shape).lineTo(305.06607, 552.3367);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xC94447));
        g.fill(shape);

        // _0_0_2
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(308.39285, 556.6479);
        ((GeneralPath) shape).lineTo(383.75, 555.9336);
        ((GeneralPath) shape).lineTo(417.67856, 618.4336);
        ((GeneralPath) shape).lineTo(379.8214, 678.255);
        ((GeneralPath) shape).lineTo(310.5357, 677.3621);
        ((GeneralPath) shape).lineTo(273.0357, 616.6478);
        ((GeneralPath) shape).closePath();

        g.fill(shape);

        // _0_0_3
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(424.01154, 620.77454);
        ((GeneralPath) shape).lineTo(513.41003, 620.2695);
        ((GeneralPath) shape).lineTo(510.37958, 566.2263);
        ((GeneralPath) shape).lineTo(485.12576, 498.04102);
        ((GeneralPath) shape).lineTo(452.2958, 457.12982);
        ((GeneralPath) shape).lineTo(441.6892, 451.57397);
        ((GeneralPath) shape).lineTo(386.6359, 555.6197);
        ((GeneralPath) shape).closePath();

        g.fill(shape);

        // _0_0_4
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(304.2857, 417.36218);
        ((GeneralPath) shape).lineTo(268.92856, 354.50504);
        ((GeneralPath) shape).lineTo(272.85712, 292.71933);
        ((GeneralPath) shape).lineTo(296.42856, 259.1479);
        ((GeneralPath) shape).lineTo(341.0714, 248.07646);
        ((GeneralPath) shape).lineTo(388.92856, 254.14789);
        ((GeneralPath) shape).lineTo(421.7857, 295.57645);
        ((GeneralPath) shape).lineTo(421.42856, 342.36215);
        ((GeneralPath) shape).lineTo(405.0, 382.005);
        ((GeneralPath) shape).lineTo(384.64285, 415.57645);
        ((GeneralPath) shape).lineTo(323.9286, 406.6479);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);

        // _0_0_5
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(308.60162, 551.32654);
        ((GeneralPath) shape).lineTo(252.03308, 446.2707);
        ((GeneralPath) shape).lineTo(306.07626, 412.93564);
        ((GeneralPath) shape).lineTo(348.50266, 407.37982);
        ((GeneralPath) shape).lineTo(379.31232, 413.44073);
        ((GeneralPath) shape).lineTo(421.7387, 430.6133);
        ((GeneralPath) shape).lineTo(443.96207, 448.79605);
        ((GeneralPath) shape).lineTo(378.30215, 553.8519);
        ((GeneralPath) shape).lineTo(308.60162, 553.34686);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xD4595B));
        g.fill(shape);

        // _0_0_6
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(316.4286, 824.14795);
        ((GeneralPath) shape).lineTo(350.7143, 825.57654);
        ((GeneralPath) shape).lineTo(357.14285, 878.43365);
        ((GeneralPath) shape).lineTo(332.5, 867.3622);
        ((GeneralPath) shape).lineTo(312.5, 832.71936);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);
        g.setPaint(BLACK);
        g.setStroke(new BasicStroke(1, 0, 0, 4));
        g.draw(shape);

        // _0_0_7
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(383.3561, 682.64856);
        ((GeneralPath) shape).lineTo(439.92465, 787.7044);
        ((GeneralPath) shape).lineTo(385.88147, 821.0394);
        ((GeneralPath) shape).lineTo(343.45508, 826.5953);
        ((GeneralPath) shape).lineTo(312.64542, 820.53436);
        ((GeneralPath) shape).lineTo(270.21902, 803.36176);
        ((GeneralPath) shape).lineTo(247.99567, 785.179);
        ((GeneralPath) shape).lineTo(313.65558, 680.12317);
        ((GeneralPath) shape).lineTo(383.3561, 680.62823);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xC94447));
        g.fill(shape);

        // _0_0_8
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(383.60544, 682.8989);
        ((GeneralPath) shape).lineTo(443.7095, 786.4396);
        ((GeneralPath) shape).lineTo(497.7527, 721.2847);
        ((GeneralPath) shape).lineTo(508.86438, 671.78723);
        ((GeneralPath) shape).lineTo(512.90497, 648.04865);
        ((GeneralPath) shape).lineTo(516.4405, 621.78467);
        ((GeneralPath) shape).lineTo(415.93033, 623.805);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xD4595B));
        g.fill(shape);

        // _0_0_9
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(422.37698, 617.6091);
        ((GeneralPath) shape).lineTo(383.6999, 684.5998);
        ((GeneralPath) shape).lineTo(306.34567, 684.5998);
        ((GeneralPath) shape).lineTo(267.66858, 617.6091);
        ((GeneralPath) shape).lineTo(306.34567, 550.61835);
        ((GeneralPath) shape).lineTo(383.6999, 550.61835);
        ((GeneralPath) shape).closePath();

        g.setPaint(BLACK);
        g.setStroke(new BasicStroke(20, 1, 1, 4));
        g.draw(shape);

        // _0_0_10
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(308.5942, 553.32336);
        ((GeneralPath) shape).lineTo(306.23386, 553.62195);

        g.setStroke(new BasicStroke(1, 0, 0, 4));
        g.draw(shape);

        // _0_0_11
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(241.5841, 450.5864);
        ((GeneralPath) shape).lineTo(240.57396, 451.59656);

        g.draw(shape);

        // _0_0_12
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(223.2143, 758.43365);
        ((GeneralPath) shape).lineTo(205.35715, 799.50507);
        ((GeneralPath) shape).lineTo(182.5, 817.00507);
        ((GeneralPath) shape).lineTo(151.07143, 813.7908);
        ((GeneralPath) shape).lineTo(130.35713, 792.71936);
        ((GeneralPath) shape).lineTo(125.35713, 755.93365);
        ((GeneralPath) shape).lineTo(157.5, 728.43365);
        ((GeneralPath) shape).lineTo(198.92857, 719.50507);
        ((GeneralPath) shape).lineTo(222.5, 756.6479);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);

        // _0_0_13
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(225.7143, 468.43362);
        ((GeneralPath) shape).lineTo(199.28572, 513.4336);
        ((GeneralPath) shape).lineTo(148.92857, 512.3622);
        ((GeneralPath) shape).lineTo(120.714294, 489.50504);
        ((GeneralPath) shape).lineTo(120.714294, 459.1479);
        ((GeneralPath) shape).lineTo(140.7143, 425.21933);
        ((GeneralPath) shape).lineTo(176.07144, 420.93362);
        ((GeneralPath) shape).lineTo(201.07144, 439.8622);
        ((GeneralPath) shape).lineTo(216.07144, 463.07648);
        ((GeneralPath) shape).closePath();

        g.fill(shape);

        // _0_0_14
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(310.27448, 553.62195);
        ((GeneralPath) shape).lineTo(247.64502, 446.54578);

        g.setPaint(BLACK);
        g.setStroke(new BasicStroke(20, 0, 0, 4));
        g.draw(shape);

        // _0_0_15
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(274.68558, 617.6979);
        ((GeneralPath) shape).lineTo(178.9357, 617.5202);

        g.draw(shape);

        // _0_0_16
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(306.23386, 686.9621);
        ((GeneralPath) shape).lineTo(250.67546, 784.9469);

        g.draw(shape);

        // _0_0_17
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(468.2143, 479.14792);
        ((GeneralPath) shape).lineTo(493.57144, 432.71936);
        ((GeneralPath) shape).lineTo(532.8572, 424.14792);
        ((GeneralPath) shape).lineTo(559.6429, 437.3622);
        ((GeneralPath) shape).lineTo(571.7857, 469.8622);
        ((GeneralPath) shape).lineTo(557.5, 507.00507);
        ((GeneralPath) shape).lineTo(505.0, 518.43365);
        ((GeneralPath) shape).lineTo(491.42856, 515.57654);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);

        // _0_0_18
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(465.35715, 770.5765);
        ((GeneralPath) shape).lineTo(491.07144, 719.1479);
        ((GeneralPath) shape).lineTo(548.2143, 733.7908);
        ((GeneralPath) shape).lineTo(565.0, 765.21936);
        ((GeneralPath) shape).lineTo(554.6429, 800.93365);
        ((GeneralPath) shape).lineTo(531.0715, 817.00507);
        ((GeneralPath) shape).lineTo(493.92862, 814.8622);
        ((GeneralPath) shape).lineTo(472.85718, 784.50507);
        ((GeneralPath) shape).closePath();

        g.fill(shape);

        // _0_0_19
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(379.77106, 553.62195);
        ((GeneralPath) shape).lineTo(442.4005, 446.54578);

        g.setPaint(BLACK);
        g.draw(shape);

        // _0_0_20
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(415.35995, 617.6979);
        ((GeneralPath) shape).lineTo(511.10983, 617.5202);

        g.draw(shape);

        // _0_0_21
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(383.81168, 686.9621);
        ((GeneralPath) shape).lineTo(439.37006, 784.9469);

        g.draw(shape);

        // _0_0_22
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(468.88232, 471.5328);
        ((GeneralPath) shape).curveTo(534.30133, 335.0551, 644.7285, 527.0688, 493.37454, 514.56757);

        g.setStroke(new BasicStroke(13.616275f, 0, 0, 4));
        g.draw(shape);

        // _0_0_23
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(304.35477, 417.22058);
        ((GeneralPath) shape).curveTo(149.84132, 191.79163, 540.4957, 189.12134, 384.0451, 416.2775);

        g.setStroke(new BasicStroke(20, 0, 0, 4));
        g.draw(shape);
        transformations.offer(g.getTransform());
        g.transform(new AffineTransform(-1, 0, 0, 1, 0, 0));

        // _0_0_24
        shape = new Ellipse2D.Double(-511.7604064941406, 409.03765869140625, 334.28570556640625, 417.1428527832031);
        g.setStroke(new BasicStroke(20, 1, 1, 4));
        g.draw(shape);

        g.setTransform(transformations.poll()); // _0_0_24

        // _0_0_25
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(468.88226, 471.5328);
        ((GeneralPath) shape).curveTo(534.3013, 335.0551, 644.72845, 527.0688, 493.37448, 514.56757);

        g.setStroke(new BasicStroke(20, 0, 0, 4));
        g.draw(shape);

        // _0_0_26
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(495.22333, 721.95);
        ((GeneralPath) shape).curveTo(646.0082, 734.97815, 505.3396, 906.07983, 463.60547, 760.0574);

        g.draw(shape);

        // _0_0_27
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(220.35281, 471.5328);
        ((GeneralPath) shape).curveTo(154.93382, 335.0551, 44.506638, 527.0688, 195.86058, 514.56757);

        g.draw(shape);

        // _0_0_28
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(194.01175, 721.95);
        ((GeneralPath) shape).curveTo(43.22691, 734.97815, 183.89548, 906.0798, 225.62962, 760.0573);

        g.draw(shape);

        // _0_0_29
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(351.43976, 833.1112);
        ((GeneralPath) shape).curveTo(353.1276, 857.686, 355.88113, 864.2503, 372.0902, 884.3071);
        ((GeneralPath) shape).moveTo(316.5921, 832.2508);
        ((GeneralPath) shape).curveTo(322.16397, 874.0282, 358.95822, 883.6228, 372.52042, 884.3071);

        g.setStroke(new BasicStroke(20, 1, 0, 4));
        g.draw(shape);
        transformations.offer(g.getTransform());
        g.transform(new AffineTransform(1, 0, 0, 1, -0.852551f, 0));

        // _0_0_30

        // _0_0_30_0
        shape = new Ellipse2D.Double(303.0457458496094, 285.6564025878906, 34.34518814086914, 34.34518814086914);
        g.fill(shape);

        // _0_0_30_1
        shape = new Ellipse2D.Double(353.5533752441406, 285.6564025878906, 34.34518814086914, 34.34518814086914);
        g.fill(shape);

        g.setTransform(transformations.poll()); // _0_0_30

        g.setTransform(transformations.poll()); // _0

    }

    /**
     * Returns the X of the bounding box of the original SVG image.
     *
     * @return The X of the bounding box of the original SVG image.
     */
    public static int getOrigX() {
        return 114;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     *
     * @return The Y of the bounding box of the original SVG image.
     */
    public static int getOrigY() {
        return 253;
    }

    /**
     * Returns the width of the bounding box of the original SVG image.
     *
     * @return The width of the bounding box of the original SVG image.
     */
    public static int getOrigWidth() {
        return 509;
    }

    /**
     * Returns the height of the bounding box of the original SVG image.
     *
     * @return The height of the bounding box of the original SVG image.
     */
    public static int getOrigHeight() {
        return 703;
    }
}

