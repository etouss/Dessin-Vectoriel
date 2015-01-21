package Modele;

import controlleur.action.SegmentAction;
import vue.forme.Point.PointVector;
import vue.forme.SegmentVector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by ByTeK on 23/11/2014.
 *
 * Classe sotckant la map des element du drawPanel itérant sur les points d'abord
 * de facon a pouvoir les bouger et les selectionner
 */
public class HashMapComponent extends HashMap<String,FormeVue> implements Iterable<FormeVue> {
    public HashMapComponent(){
        super();
    }
    public void add(FormeVue q){
        q.add(this);
    }
    public void delete(FormeVue q){
        //this.remove(q);
        this.remove(""+q.hashCode());
    }

    public Iterator<FormeVue> iterator() {
        return new MyIterator();
    }

    class MyIterator implements Iterator<FormeVue> {

        private HashSet<FormeVue> pasPoints = new HashSet<FormeVue>();
        private HashSet<FormeVue> segment = new HashSet<FormeVue>();
        private Iterator<FormeVue> iter = values().iterator();
        private Iterator<FormeVue> iterPasPoints = pasPoints.iterator();
        private Iterator<FormeVue> iterSegment = segment.iterator();
        boolean creerPasPoint = false;
        boolean creerSegment = false;

        public boolean hasNext() {
            return iter.hasNext() || iterSegment.hasNext() || iterPasPoints.hasNext();
        }

        public FormeVue next() {
            FormeVue q;
            if (iter.hasNext()) q = iter.next();
            else if(iterSegment.hasNext()) q =iterSegment.next();
            else q = iterPasPoints.next();
            if (iter.hasNext()) {
                if (q instanceof PointVector) return q;
                else if (q instanceof SegmentVector)segment.add(q);
                else pasPoints.add(q);
                return this.next();
            }
            else
            {
                if(!creerSegment){
                    creerSegment = true;
                    iterSegment = segment.iterator();
                }
                if(iterSegment.hasNext()){
                    return q;
                }
                else{
                    if(!creerPasPoint){
                        creerPasPoint = true;
                        iterPasPoints = pasPoints.iterator();
                    }
                    return q;
                }
            }
        }
    }
}
