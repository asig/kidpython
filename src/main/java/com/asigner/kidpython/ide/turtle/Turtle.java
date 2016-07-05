package com.asigner.kidpython.ide.turtle;

import java.awt.*;
import java.awt.geom.*;
import static java.awt.Color.*;

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
        g.transform(new AffineTransform(1.0666667f, 0, 0, 1.0666667f, 0, 8.138021E-6f));

        // _0
        transformations.offer(g.getTransform());
        g.transform(new AffineTransform(1, 0, 0, 1, -182.26279f, -259.51443f));

        // _0_0
        transformations.offer(g.getTransform());
        g.transform(new AffineTransform(0.9999982f, 0, 0, 1, -81.99967f, -132.91075f));

        // _0_0_0

        // _0_0_0_0
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(251.61917, 414.09927);
        ((GeneralPath) shape).lineTo(230.67422, 450.1809);
        ((GeneralPath) shape).lineTo(211.84135, 427.47586);
        ((GeneralPath) shape).lineTo(207.96918, 410.22708);
        ((GeneralPath) shape).lineTo(206.56111, 401.9547);
        ((GeneralPath) shape).lineTo(205.32906, 392.8023);
        ((GeneralPath) shape).lineTo(240.35468, 393.50635);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xD4595B));
        g.fill(shape);

        // _0_0_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(237.27452, 392.09827);
        ((GeneralPath) shape).lineTo(206.1211, 391.92227);
        ((GeneralPath) shape).lineTo(207.17714, 373.08942);
        ((GeneralPath) shape).lineTo(215.97754, 349.32834);
        ((GeneralPath) shape).lineTo(227.41808, 335.0717);
        ((GeneralPath) shape).lineTo(231.11424, 333.1356);
        ((GeneralPath) shape).lineTo(250.29912, 369.39325);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xC94447));
        g.fill(shape);

        // _0_0_0_2
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(251.45842, 370.8956);
        ((GeneralPath) shape).lineTo(277.71875, 370.6467);
        ((GeneralPath) shape).lineTo(289.5421, 392.42657);
        ((GeneralPath) shape).lineTo(276.34973, 413.27304);
        ((GeneralPath) shape).lineTo(252.20517, 412.9619);
        ((GeneralPath) shape).lineTo(239.13724, 391.80432);
        ((GeneralPath) shape).closePath();

        g.fill(shape);

        // _0_0_0_3
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(307.1527, 343.88852);
        ((GeneralPath) shape).lineTo(315.9891, 327.70917);
        ((GeneralPath) shape).lineTo(329.67932, 324.72223);
        ((GeneralPath) shape).lineTo(339.01355, 329.32712);
        ((GeneralPath) shape).lineTo(343.24506, 340.65265);
        ((GeneralPath) shape).lineTo(338.26678, 353.59613);
        ((GeneralPath) shape).lineTo(319.97168, 357.57874);
        ((GeneralPath) shape).lineTo(315.24234, 356.5831);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);

        // _0_0_0_4
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(307.38547, 341.23483);
        ((GeneralPath) shape).curveTo(330.18256, 293.67535, 368.664, 360.58792, 315.92047, 356.2315);

        g.setPaint(BLACK);
        g.setStroke(new BasicStroke(6.9695635f, 0, 0, 4));
        g.draw(shape);

        // _0_0_0_5
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(291.74902, 393.2423);
        ((GeneralPath) shape).lineTo(322.90247, 393.0663);
        ((GeneralPath) shape).lineTo(321.8464, 374.23346);
        ((GeneralPath) shape).lineTo(313.04602, 350.47238);
        ((GeneralPath) shape).lineTo(301.6055, 336.21573);
        ((GeneralPath) shape).lineTo(297.90933, 334.27963);
        ((GeneralPath) shape).lineTo(278.72446, 370.5373);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xC94447));
        g.fill(shape);

        // _0_0_0_6
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(250.02718, 322.35754);
        ((GeneralPath) shape).lineTo(237.70598, 300.45322);
        ((GeneralPath) shape).lineTo(239.07501, 278.92224);
        ((GeneralPath) shape).lineTo(247.28914, 267.22333);
        ((GeneralPath) shape).lineTo(262.8462, 263.36517);
        ((GeneralPath) shape).lineTo(279.52338, 265.48093);
        ((GeneralPath) shape).lineTo(290.97336, 279.91788);
        ((GeneralPath) shape).lineTo(290.8489, 296.22168);
        ((GeneralPath) shape).lineTo(285.1239, 310.03635);
        ((GeneralPath) shape).lineTo(278.02988, 321.73526);
        ((GeneralPath) shape).lineTo(256.87225, 318.62384);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);

        // _0_0_0_7
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(251.53117, 369.0412);
        ((GeneralPath) shape).lineTo(231.81827, 332.43152);
        ((GeneralPath) shape).lineTo(250.65114, 320.81497);
        ((GeneralPath) shape).lineTo(265.43582, 318.8789);
        ((GeneralPath) shape).lineTo(276.17233, 320.991);
        ((GeneralPath) shape).lineTo(290.957, 326.97528);
        ((GeneralPath) shape).lineTo(298.70135, 333.31158);
        ((GeneralPath) shape).lineTo(275.8203, 369.92126);
        ((GeneralPath) shape).lineTo(251.5312, 369.74527);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xD4595B));
        g.fill(shape);

        // _0_0_0_8
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(254.2587, 464.1135);
        ((GeneralPath) shape).lineTo(266.2065, 464.61133);
        ((GeneralPath) shape).lineTo(268.44675, 483.03088);
        ((GeneralPath) shape).lineTo(259.85925, 479.17273);
        ((GeneralPath) shape).lineTo(252.88968, 467.10046);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);

        // _0_0_0_9
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(277.58148, 414.80408);
        ((GeneralPath) shape).lineTo(297.29437, 451.41376);
        ((GeneralPath) shape).lineTo(278.46152, 463.0303);
        ((GeneralPath) shape).lineTo(263.67685, 464.96637);
        ((GeneralPath) shape).lineTo(252.94035, 462.85428);
        ((GeneralPath) shape).lineTo(238.15567, 456.87);
        ((GeneralPath) shape).lineTo(230.41132, 450.5337);
        ((GeneralPath) shape).lineTo(253.29236, 413.924);
        ((GeneralPath) shape).lineTo(277.58148, 414.1);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xC94447));
        g.fill(shape);

        // _0_0_0_10
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(277.66837, 414.8913);
        ((GeneralPath) shape).lineTo(298.6133, 450.97296);
        ((GeneralPath) shape).lineTo(317.44617, 428.2679);
        ((GeneralPath) shape).lineTo(321.31833, 411.01913);
        ((GeneralPath) shape).lineTo(322.7264, 402.74677);
        ((GeneralPath) shape).lineTo(323.95847, 393.59436);
        ((GeneralPath) shape).lineTo(288.93286, 394.2984);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xD4595B));
        g.fill(shape);

        // _0_0_0_11
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(291.1794, 392.13922);
        ((GeneralPath) shape).lineTo(277.7013, 415.484);
        ((GeneralPath) shape).lineTo(250.74504, 415.484);
        ((GeneralPath) shape).lineTo(237.2669, 392.13922);
        ((GeneralPath) shape).lineTo(250.74504, 368.79443);
        ((GeneralPath) shape).lineTo(277.7013, 368.79443);
        ((GeneralPath) shape).closePath();

        g.setPaint(BLACK);
        g.setStroke(new BasicStroke(6.9695635f, 1, 1, 4));
        g.draw(shape);

        // _0_0_0_12
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(251.5286, 369.73706);
        ((GeneralPath) shape).lineTo(250.70607, 369.84113);

        g.setStroke(new BasicStroke(0.34847817f, 0, 0, 4));
        g.draw(shape);

        // _0_0_0_13
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(228.17703, 333.93546);
        ((GeneralPath) shape).lineTo(227.82503, 334.28748);

        g.draw(shape);

        // _0_0_0_14
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(221.77554, 441.2135);
        ((GeneralPath) shape).lineTo(215.55272, 455.526);
        ((GeneralPath) shape).lineTo(207.5875, 461.62436);
        ((GeneralPath) shape).lineTo(196.63533, 460.50424);
        ((GeneralPath) shape).lineTo(189.41685, 453.16132);
        ((GeneralPath) shape).lineTo(187.67447, 440.3423);
        ((GeneralPath) shape).lineTo(198.87556, 430.75912);
        ((GeneralPath) shape).lineTo(213.31252, 427.6477);
        ((GeneralPath) shape).lineTo(221.52664, 440.5912);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);

        // _0_0_0_15
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(222.64674, 340.15482);
        ((GeneralPath) shape).lineTo(213.43697, 355.83633);
        ((GeneralPath) shape).lineTo(195.8886, 355.46295);
        ((GeneralPath) shape).lineTo(186.05653, 347.49774);
        ((GeneralPath) shape).lineTo(186.05653, 336.91895);
        ((GeneralPath) shape).lineTo(193.0261, 325.09558);
        ((GeneralPath) shape).lineTo(205.34729, 323.6021);
        ((GeneralPath) shape).lineTo(214.05925, 330.1983);
        ((GeneralPath) shape).lineTo(219.28642, 338.28796);
        ((GeneralPath) shape).closePath();

        g.fill(shape);

        // _0_0_0_16
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(252.11414, 369.8411);
        ((GeneralPath) shape).lineTo(230.28914, 332.52737);

        g.setPaint(BLACK);
        g.setStroke(new BasicStroke(6.9695635f, 0, 0, 4));
        g.draw(shape);

        // _0_0_0_17
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(239.71217, 392.17017);
        ((GeneralPath) shape).lineTo(206.34543, 392.10828);

        g.draw(shape);

        // _0_0_0_18
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(250.70605, 416.30722);
        ((GeneralPath) shape).lineTo(231.34518, 450.4528);

        g.draw(shape);

        // _0_0_0_19
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(306.15707, 445.44504);
        ((GeneralPath) shape).lineTo(315.11792, 427.5233);
        ((GeneralPath) shape).lineTo(335.03094, 432.626);
        ((GeneralPath) shape).lineTo(340.8804, 443.5782);
        ((GeneralPath) shape).lineTo(337.27115, 456.02383);
        ((GeneralPath) shape).lineTo(329.057, 461.6244);
        ((GeneralPath) shape).lineTo(316.11353, 460.87766);
        ((GeneralPath) shape).lineTo(308.7706, 450.29886);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);

        // _0_0_0_20
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(276.33218, 369.8411);
        ((GeneralPath) shape).lineTo(298.1572, 332.52737);

        g.setPaint(BLACK);
        g.draw(shape);

        // _0_0_0_21
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(288.73413, 392.17017);
        ((GeneralPath) shape).lineTo(322.10086, 392.10828);

        g.draw(shape);

        // _0_0_0_22
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(277.74023, 416.30722);
        ((GeneralPath) shape).lineTo(297.10114, 450.4528);

        g.draw(shape);

        // _0_0_0_23
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(250.05124, 322.3082);
        ((GeneralPath) shape).curveTo(196.20668, 243.75113, 332.34122, 242.82059, 277.8216, 321.97955);

        g.setStroke(new BasicStroke(6.97f, 0, 0, 4));
        g.draw(shape);
        transformations.offer(g.getTransform());
        g.transform(new AffineTransform(-1, 0, 0, 1, 0, 0));

        // _0_0_0_24
        shape = new Ellipse2D.Double(-322.32757568359375, 319.4566345214844, 116.49127197265625, 145.3651885986328);
        g.setStroke(new BasicStroke(6.9695635f, 1, 1, 4));
        g.draw(shape);

        g.setTransform(transformations.pollLast()); // _0_0_0_24

        // _0_0_0_25
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(316.56476, 428.49976);
        ((GeneralPath) shape).curveTo(369.11, 433.0398, 320.09006, 492.66498, 305.54663, 441.77933);

        g.setStroke(new BasicStroke(6.9695635f, 0, 0, 4));
        g.draw(shape);

        // _0_0_0_26
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(220.7784, 341.23483);
        ((GeneralPath) shape).curveTo(197.98131, 293.67535, 159.49985, 360.58792, 212.2434, 356.2315);

        g.draw(shape);

        // _0_0_0_27
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(211.5991, 428.49976);
        ((GeneralPath) shape).curveTo(159.0539, 433.0398, 208.0738, 492.66498, 222.61725, 441.77933);

        g.draw(shape);

        // _0_0_0_28
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(266.45932, 467.237);
        ((GeneralPath) shape).curveTo(267.0475, 475.80078, 268.00705, 478.0883, 273.65555, 485.07767);
        ((GeneralPath) shape).moveTo(254.31567, 466.93716);
        ((GeneralPath) shape).curveTo(256.25735, 481.4957, 269.07935, 484.8392, 273.80548, 485.07767);

        g.setStroke(new BasicStroke(6.9695635f, 1, 0, 4));
        g.draw(shape);
        transformations.offer(g.getTransform());
        g.transform(new AffineTransform(0.34847817f, 0, 0, 0.34847817f, 143.69315f, 176.91594f));

        // _0_0_0_29

        // _0_0_0_29_0
        shape = new Ellipse2D.Double(303.0457458496094, 285.6564025878906, 34.34518814086914, 34.34518814086914);
        g.fill(shape);

        // _0_0_0_29_1
        shape = new Ellipse2D.Double(353.5533752441406, 285.6564025878906, 34.34518814086914, 34.34518814086914);
        g.fill(shape);

        g.setTransform(transformations.pollLast()); // _0_0_0_29

        g.setTransform(transformations.pollLast()); // _0_0_0

        g.setTransform(transformations.pollLast()); // _0_0

        g.setTransform(transformations.pollLast()); // _0

    }

    /**
     * Returns the X of the bounding box of the original SVG image.
     *
     * @return The X of the bounding box of the original SVG image.
     */
    public static int getOrigX() {
        return 0;
    }

    /**
     * Returns the Y of the bounding box of the original SVG image.
     *
     * @return The Y of the bounding box of the original SVG image.
     */
    public static int getOrigY() {
        return 0;
    }

    /**
     * Returns the width of the bounding box of the original SVG image.
     *
     * @return The width of the bounding box of the original SVG image.
     */
    public static int getOrigWidth() {
        return 89;
    }

    /**
     * Returns the height of the bounding box of the original SVG image.
     *
     * @return The height of the bounding box of the original SVG image.
     */
    public static int getOrigHeight() {
        return 104;
    }
}

