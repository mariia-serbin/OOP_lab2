package lab2.gem.parser;

import java.math.BigDecimal;
import lab2.gem.model.ColorType;
import lab2.gem.model.GemType;
import lab2.gem.model.PreciousnessType;
import lab2.gem.model.VisualParametersType;

final class GemBuilder {

    private String id;
    private String name;
    private PreciousnessType preciousness;
    private String origin;
    private ColorType color;
    private Integer transparency;
    private Integer facetCount;
    private BigDecimal value;

    void reset(String id) {
        this.id = clean(id);
        this.name = null;
        this.preciousness = null;
        this.origin = null;
        this.color = null;
        this.transparency = null;
        this.facetCount = null;
        this.value = null;
    }

    void setId(String id) {
        this.id = clean(id);
    }

    void setName(String text) {
        this.name = clean(text);
    }

    void setPreciousness(String text) {
        this.preciousness = PreciousnessType.fromValue(clean(text));
    }

    void setOrigin(String text) {
        this.origin = clean(text);
    }

    void setColor(String text) {
        this.color = ColorType.fromValue(clean(text));
    }

    void setTransparency(String text) {
        this.transparency = Integer.valueOf(clean(text));
    }

    void setFacetCount(String text) {
        this.facetCount = Integer.valueOf(clean(text));
    }

    void setValue(String text) {
        this.value = new BigDecimal(clean(text));
    }

    GemType build() {
        checkComplete();

        VisualParametersType visualParameters = new VisualParametersType();
        visualParameters.setColor(color);
        visualParameters.setTransparency(transparency);
        visualParameters.setFacetCount(facetCount);

        GemType gem = new GemType();
        gem.setId(id);
        gem.setName(name);
        gem.setPreciousness(preciousness);
        gem.setOrigin(origin);
        gem.setVisualParameters(visualParameters);
        gem.setValue(value);
        return gem;
    }

    private void checkComplete() {
        if (id == null || id.isBlank() || name == null || preciousness == null || origin == null
                || color == null || transparency == null || facetCount == null || value == null) {
            throw new IllegalStateException("Gem element does not contain all required fields");
        }
    }

    private static String clean(String text) {
        return text == null ? null : text.trim();
    }
}
