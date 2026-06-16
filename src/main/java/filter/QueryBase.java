package filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryBase {
    private StringBuilder baseSQL;
    private List<Object> params;

    public QueryBase() {
        baseSQL = new StringBuilder("""
                SELECT
                        e.id,
                        e.title,
                        e.price,
                        GROUP_CONCAT(
                        DISTINCT a.authorName
                        SEPARATOR ', '
                        ) AS authorName,
                        COALESCE(
                            MIN(CASE
                                WHEN i.migration_status = 'MIGRATED'
                                     AND i.cloudinary_url IS NOT NULL
                                     AND i.cloudinary_url <> ''
                                THEN i.cloudinary_url
                            END),
                            MIN(i.imgLink),
                            '/assets/img/no-image.png'
                        ) AS thumbnail
                    FROM ebook e
                    LEFT JOIN category c ON e.categoryID = c.id
                    LEFT JOIN ebookimage ei ON e.id = ei.ebookID
                    LEFT JOIN images i ON ei.imgID = i.id
                    LEFT JOIN ebookauthor ea ON e.id = ea.ebookID
                    LEFT JOIN author a ON ea.authorID = a.id
                    LEFT JOIN files f ON f.id = e.id
                    WHERE i.imgStatus = 'ACTIVE'
                    AND e.status = 'ACTIVE'
                """);
        params = new ArrayList<>();
    }

    public void append(String condition, Object... values) {
        baseSQL.append(condition);

        if(values != null) {
            params.addAll(Arrays.asList(values));
        }
    }

    public String getBaseSql() {
        return this.baseSQL.toString();
    }

    public List<Object> getParams() {
        return this.params;
    }
}
