<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

    <xsl:key name="gemsByPreciousness" match="Gem/gem" use="preciousness"/>

    <xsl:template match="/">
        <GemGroups>
            <xsl:for-each select="Gem/gem[generate-id() = generate-id(key('gemsByPreciousness', preciousness)[1])]">
                <xsl:variable name="preciousness" select="preciousness"/>
                <Group preciousness="{$preciousness}" count="{count(key('gemsByPreciousness', $preciousness))}">
                    <xsl:for-each select="key('gemsByPreciousness', $preciousness)">
                        <xsl:copy-of select="."/>
                    </xsl:for-each>
                </Group>
            </xsl:for-each>
        </GemGroups>
    </xsl:template>

</xsl:stylesheet>
