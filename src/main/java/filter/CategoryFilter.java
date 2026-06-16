package filter;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryFilter extends FilterDecorator {
    private List<Integer> categoryIds;

    public CategoryFilter(EbookQueryFilter wrapped, List<Integer> categoryIds) {
        super(wrapped);
        this.categoryIds = categoryIds;
    }

    @Override
    public QueryBase apply(QueryBase queryData) {
        QueryBase qb = super.apply(queryData);

        if(categoryIds != null && !categoryIds.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Integer id : categoryIds) {
                sb .append("?, ");
            }

            String placeholders = !sb.isEmpty() ?
                    sb.substring(0, sb.length() - 2) : "";

            qb.append("AND c.id IN (" + placeholders + ")", categoryIds.toArray());
        }

        return qb;
    }
}
