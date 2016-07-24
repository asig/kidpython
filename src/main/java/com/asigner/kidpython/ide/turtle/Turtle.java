package com.asigner.kidpython.ide.turtle;

import java.awt.*;
import java.awt.geom.*;
import static java.awt.Color.*;
import static java.awt.MultipleGradientPaint.CycleMethod.*;
import static java.awt.MultipleGradientPaint.ColorSpaceType.*;

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

        // _0_0_0
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(225.1525, 210.97778);
        ((GeneralPath) shape).lineTo(233.98889, 194.79843);
        ((GeneralPath) shape).lineTo(247.67908, 191.81148);
        ((GeneralPath) shape).lineTo(257.0133, 196.41637);
        ((GeneralPath) shape).lineTo(261.2448, 207.74191);
        ((GeneralPath) shape).lineTo(256.26657, 220.68538);
        ((GeneralPath) shape).lineTo(237.9715, 224.66798);
        ((GeneralPath) shape).lineTo(233.24216, 223.67233);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);

        // _0_0_1
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(225.38525, 208.32408);
        ((GeneralPath) shape).curveTo(248.18231, 160.76459, 286.6637, 227.67717, 233.92024, 223.32076);

        g.setPaint(BLACK);
        g.setStroke(new BasicStroke(6.9695635f, 0, 0, 4));
        g.draw(shape);

        // _0_0_2
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(168.02705, 189.4468);
        ((GeneralPath) shape).lineTo(155.70587, 167.54245);
        ((GeneralPath) shape).lineTo(157.0749, 146.01147);
        ((GeneralPath) shape).lineTo(165.289, 134.31256);
        ((GeneralPath) shape).lineTo(180.84604, 130.4544);
        ((GeneralPath) shape).lineTo(197.5232, 132.57016);
        ((GeneralPath) shape).lineTo(208.97316, 147.00711);
        ((GeneralPath) shape).lineTo(208.84871, 163.31091);
        ((GeneralPath) shape).lineTo(203.12372, 177.12558);
        ((GeneralPath) shape).lineTo(196.02972, 188.8245);
        ((GeneralPath) shape).lineTo(174.87216, 185.71309);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);

        // _0_0_3
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(172.25858, 331.20276);
        ((GeneralPath) shape).lineTo(184.20638, 331.7006);
        ((GeneralPath) shape).lineTo(186.4466, 350.12015);
        ((GeneralPath) shape).lineTo(177.8591, 346.262);
        ((GeneralPath) shape).lineTo(170.88954, 334.18973);
        ((GeneralPath) shape).closePath();

        g.fill(shape);

        // _0_0_4
        shape = new Ellipse2D.Double(121.86993408203125, 185.208251953125, 119.82144165039062, 148.03570556640625);
        g.setPaint(new Color(0xC3383C));
        g.fill(shape);

        // _0_0_5
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(168.63818, 236.66615);
        ((GeneralPath) shape).lineTo(147.13959, 199.87791);
        ((GeneralPath) shape).lineTo(168.47243, 186.65424);
        ((GeneralPath) shape).lineTo(183.25706, 184.71815);
        ((GeneralPath) shape).lineTo(193.99355, 186.83025);
        ((GeneralPath) shape).lineTo(208.77818, 192.81451);
        ((GeneralPath) shape).lineTo(217.77254, 199.8651);
        ((GeneralPath) shape).lineTo(196.49867, 237.01047);
        ((GeneralPath) shape).lineTo(168.63818, 237.1916);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0xD4595B));
        g.fill(shape);

        // _0_0_6
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(216.2824, 319.53696);
        ((GeneralPath) shape).curveTo(227.58737, 303.0604, 238.33475, 296.39432, 240.5476, 275.3717);
        ((GeneralPath) shape).lineTo(242.6725, 258.18356);
        ((GeneralPath) shape).lineTo(209.07555, 257.99472);
        ((GeneralPath) shape).lineTo(194.9539, 281.80197);

        g.fill(shape);

        // _0_0_7
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(170.51192, 282.43854);
        ((GeneralPath) shape).lineTo(147.95985, 319.7702);
        ((GeneralPath) shape).lineTo(129.66273, 293.49374);
        ((GeneralPath) shape).lineTo(125.79056, 276.24496);
        ((GeneralPath) shape).lineTo(124.38251, 267.9726);
        ((GeneralPath) shape).lineTo(123.150444, 258.8202);
        ((GeneralPath) shape).lineTo(158.176, 259.52423);
        ((GeneralPath) shape).closePath();

        g.fill(shape);
        transformations.offer(g.getTransform());
        g.transform(new AffineTransform(1, 0, 0, 1.0000018f, 0, 0));

        // _0_0_8
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(209.17921, 259.228);
        ((GeneralPath) shape).lineTo(195.70111, 282.57275);
        ((GeneralPath) shape).lineTo(168.7449, 282.57275);
        ((GeneralPath) shape).lineTo(155.2668, 259.228);
        ((GeneralPath) shape).lineTo(168.7449, 235.88324);
        ((GeneralPath) shape).lineTo(195.70111, 235.88324);
        ((GeneralPath) shape).closePath();

        g.setPaint(BLACK);
        g.setStroke(new BasicStroke(6.9695635f, 1, 1, 4));
        g.draw(shape);

        g.setTransform(transformations.pollLast()); // _0_0_8

        // _0_0_9
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(169.52847, 236.8263);
        ((GeneralPath) shape).lineTo(168.70595, 236.93036);

        g.setStroke(new BasicStroke(0.34847817f, 0, 0, 4));
        g.draw(shape);

        // _0_0_10
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(146.17696, 201.0247);
        ((GeneralPath) shape).lineTo(145.82495, 201.37672);

        g.draw(shape);

        // _0_0_11
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(139.77548, 308.30276);
        ((GeneralPath) shape).lineTo(133.55267, 322.61526);
        ((GeneralPath) shape).lineTo(125.58747, 328.71362);
        ((GeneralPath) shape).lineTo(114.63532, 327.5935);
        ((GeneralPath) shape).lineTo(107.416855, 320.25058);
        ((GeneralPath) shape).lineTo(105.67446, 307.43155);
        ((GeneralPath) shape).lineTo(116.875534, 297.8484);
        ((GeneralPath) shape).lineTo(131.31245, 294.73697);
        ((GeneralPath) shape).lineTo(139.52658, 307.68045);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);

        // _0_0_12
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(140.64668, 207.24408);
        ((GeneralPath) shape).lineTo(131.43692, 222.9256);
        ((GeneralPath) shape).lineTo(113.88859, 222.55223);
        ((GeneralPath) shape).lineTo(104.05653, 214.587);
        ((GeneralPath) shape).lineTo(104.05653, 204.00821);
        ((GeneralPath) shape).lineTo(111.026085, 192.18484);
        ((GeneralPath) shape).lineTo(123.34725, 190.69136);
        ((GeneralPath) shape).lineTo(132.05919, 197.28755);
        ((GeneralPath) shape).lineTo(137.28635, 205.37723);
        ((GeneralPath) shape).closePath();

        g.fill(shape);

        // _0_0_13
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(170.114, 236.93036);
        ((GeneralPath) shape).lineTo(148.28905, 199.61665);

        g.setPaint(BLACK);
        g.setStroke(new BasicStroke(6.9695635f, 0, 0, 4));
        g.draw(shape);

        // _0_0_14
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(157.71207, 259.25943);
        ((GeneralPath) shape).lineTo(124.34538, 259.19754);

        g.draw(shape);

        // _0_0_15
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(168.70593, 283.39648);
        ((GeneralPath) shape).lineTo(149.34508, 317.54205);

        g.draw(shape);

        // _0_0_16
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(224.15685, 312.53427);
        ((GeneralPath) shape).lineTo(233.11769, 294.61252);
        ((GeneralPath) shape).lineTo(253.03069, 299.71524);
        ((GeneralPath) shape).lineTo(258.88013, 310.66742);
        ((GeneralPath) shape).lineTo(255.2709, 323.11307);
        ((GeneralPath) shape).lineTo(247.0568, 328.71362);
        ((GeneralPath) shape).lineTo(234.11333, 327.9669);
        ((GeneralPath) shape).lineTo(226.7704, 317.3881);
        ((GeneralPath) shape).closePath();

        g.setPaint(new Color(0x00B200));
        g.fill(shape);

        // _0_0_17
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(194.332, 236.93036);
        ((GeneralPath) shape).lineTo(216.15697, 199.61665);

        g.setPaint(BLACK);
        g.draw(shape);

        // _0_0_18
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(206.73393, 259.25943);
        ((GeneralPath) shape).lineTo(240.10062, 259.19754);

        g.draw(shape);

        // _0_0_19
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(195.74007, 283.39648);
        ((GeneralPath) shape).lineTo(215.10092, 317.54205);

        g.draw(shape);

        // _0_0_20
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(168.05112, 189.39745);
        ((GeneralPath) shape).curveTo(114.20666, 110.84038, 250.34094, 109.90984, 195.82141, 189.0688);

        g.setStroke(new BasicStroke(6.97f, 0, 0, 4));
        g.draw(shape);
        transformations.offer(g.getTransform());
        g.transform(new AffineTransform(-1, 0, 0, 1, 0, 0));

        // _0_0_21
        shape = new Ellipse2D.Double(-240.3273162841797, 186.5458984375, 116.49105834960938, 145.3651885986328);
        g.setStroke(new BasicStroke(6.9695635f, 1, 1, 4));
        g.draw(shape);

        g.setTransform(transformations.pollLast()); // _0_0_21

        // _0_0_22
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(234.56451, 295.589);
        ((GeneralPath) shape).curveTo(287.10965, 300.12903, 238.0898, 359.7542, 223.5464, 308.86856);

        g.setStroke(new BasicStroke(6.9695635f, 0, 0, 4));
        g.draw(shape);

        // _0_0_23
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(138.77832, 208.32408);
        ((GeneralPath) shape).curveTo(115.98127, 160.76459, 77.49988, 227.67717, 130.24333, 223.32076);

        g.draw(shape);

        // _0_0_24
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(129.59904, 295.589);
        ((GeneralPath) shape).curveTo(77.05392, 300.12903, 126.073746, 359.7542, 140.61716, 308.86856);

        g.draw(shape);

        // _0_0_25
        shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(184.45918, 334.32626);
        ((GeneralPath) shape).curveTo(185.04736, 342.89005, 186.0069, 345.17755, 191.65541, 352.16693);
        ((GeneralPath) shape).moveTo(172.31557, 334.02643);
        ((GeneralPath) shape).curveTo(174.25725, 348.58496, 187.0792, 351.92847, 191.80534, 352.16693);

        g.setStroke(new BasicStroke(6.9695635f, 1, 0, 4));
        g.draw(shape);
        transformations.offer(g.getTransform());
        g.transform(new AffineTransform(0.34847754f, 0, 0, 0.34847817f, 61.693222f, 44.00519f));

        // _0_0_26

        // _0_0_26_0
        shape = new Ellipse2D.Double(303.0457458496094, 285.6564025878906, 34.34518814086914, 34.34518814086914);
        g.fill(shape);

        // _0_0_26_1
        shape = new Ellipse2D.Double(353.5533752441406, 285.6564025878906, 34.34518814086914, 34.34518814086914);
        g.fill(shape);

        g.setTransform(transformations.pollLast()); // _0_0_26

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

