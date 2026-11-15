package lab2.gem.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lab2.gem.model.ColorType;
import lab2.gem.model.GemType;
import lab2.gem.model.PreciousnessType;
import lab2.gem.model.VisualParametersType;
import org.junit.jupiter.api.Test;

class GemComparatorTest {

    @Test
    void ordersByPreciousnessWeightAndName() {
        List<GemType> gems = new ArrayList<>(List.of(
                gem("Amethyst", PreciousnessType.SEMIPRECIOUS, "5.50"),
                gem("Ruby", PreciousnessType.PRECIOUS, "2.10"),
                gem("Diamond", PreciousnessType.PRECIOUS, "1.25"),
                gem("Zircon", PreciousnessType.SEMIPRECIOUS, "1.00"),
                gem("Agate", PreciousnessType.SEMIPRECIOUS, "1.00"),
                gem("Sapphire", PreciousnessType.PRECIOUS, "3.40")));

        gems.sort(new GemComparator());

        assertEquals(
                List.of("Sapphire", "Ruby", "Diamond", "Amethyst", "Agate", "Zircon"),
                gems.stream().map(GemType::getName).toList());
    }

    private static GemType gem(String name, PreciousnessType preciousness, String weight) {
        VisualParametersType visual = new VisualParametersType();
        visual.setColor(ColorType.WHITE);
        visual.setTransparency(50);
        visual.setFacetCount(8);

        GemType gem = new GemType();
        gem.setId(name.toLowerCase());
        gem.setName(name);
        gem.setPreciousness(preciousness);
        gem.setOrigin("Test");
        gem.setVisualParameters(visual);
        gem.setValue(new BigDecimal(weight));
        return gem;
    }
}
