package filter;

public class SortDecorator extends FilterDecorator {

    private final String sortBy;
    private final String sortDir;

    public SortDecorator(EbookQueryFilter wrapped, String sortBy, String sortDir) {
        super(wrapped);
        this.sortBy = sortBy;
        this.sortDir = sortDir;
    }

    @Override
    public QueryBase apply(QueryBase queryBase) {
        QueryBase q = super.apply(queryBase);
        String safeSortBy = switch (sortBy) {
            case "price" -> "e.price";
            case "title" -> "e.title";
            default -> "e.id";
        };

        String safeDir = "asc".equalsIgnoreCase(sortDir) ? "ASC" : "DESC";
        q.append(" GROUP BY e.id, e.title, e.price ");
        q.append(" ORDER BY " + safeSortBy + " " + safeDir);

        return q;
    }
}
