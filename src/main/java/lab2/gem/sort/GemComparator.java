package lab2.gem.sort;

import java.util.Comparator;
import lab2.gem.model.GemType;
import lab2.gem.model.PreciousnessType;

public class GemComparator implements Comparator<GemType> {

    @Override
    public int compare(GemType first, GemType second) {
        int byPreciousness = Integer.compare(rank(first), rank(second));
        if (byPreciousness != 0) {
            return byPreciousness;
        }

        int byValue = second.getValue().compareTo(first.getValue());
        if (byValue != 0) {
            return byValue;
        }

        return first.getName().compareToIgnoreCase(second.getName());
    }

    private static int rank(GemType gem) {
        return gem.getPreciousness() == PreciousnessType.PRECIOUS ? 0 : 1;
    }
}
