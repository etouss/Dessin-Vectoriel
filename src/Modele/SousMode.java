package Modele;

/**
 * Created by ByTeK on 21/11/2014.
 */
public enum SousMode {
    //Sous mode Vide
    vide(0),
    //Droite Sous Mode
    droite(18),
    droiteParalele(1),
    droitePerpendiculaire(2),
    droiteIntersection(3),

    //Polygone SousMode
    poly(17),
    polygoneGrav(4),
    polygoneTriangleRectangle(5),
    polygoneCarre(6),
    polygoneRectangle(7),
    polygoneLosange(8),
    polygoneParalelograme(9),
    polygoneTriangleIsocele(10),
    polygoneTriangleEqui(11),

    //Circle
    cercle(12),
    arcCercle(13),
    cercleoval(14),

    //Point,
    entre2point(17),
    avec2point(18);

    //select
    //rotation(15),
    //scale(16);


    SousMode(int i){
    }
}
